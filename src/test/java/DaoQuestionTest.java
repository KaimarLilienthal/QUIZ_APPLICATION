import org.example.DaoQuestion;
import org.example.Question;
import org.example.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The DaoQuestionTest class contains unit tests for the DaoQuestion class.
 */
public class DaoQuestionTest {

    private DaoQuestion daoQuestion;
    private PGSimpleDataSource dataSource;

    /**
     * Sets up the test environment before each test method is executed.
     */
    @BeforeEach
    public void setUp() {

        dataSource = new PGSimpleDataSource();
        dataSource.setServerName("localhost");
        dataSource.setPortNumber(5432);
        dataSource.setDatabaseName("postgres");
        dataSource.setUser("postgres");
        dataSource.setPassword("student123");

        daoQuestion = new DaoQuestion(dataSource);
    }

    /**
     * Tests the saveQuestion method with responses.
     * @throws SQLException If a database error occurs.
     */
    @Test
    public void testSaveQuestionWithResponses() throws SQLException {
        // Create and save a question with responses.
        Question question = new Question();
        question.setContent("Sample Question");
        question.setQuizId(1); // Assuming a quiz with ID 1 exists
        Response response1 = new Response("Response 1", true); // Provide non-null text value
        Response response2 = new Response("Response 2", false); // Provide non-null text value
        question.addResponse(response1);
        question.addResponse(response2);

        // Save the question and responses in the database.
        boolean result = daoQuestion.saveQuestion(question);
        assertTrue(result);

        // Verify that the saved question and responses exist in the database.
        try (Connection connection = dataSource.getConnection()) {
            // Check if the question with the specified ID exists and has the expected content and quiz ID.
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM question WHERE id = ?");
            statement.setInt(1, question.getId());
            ResultSet resultSet = statement.executeQuery();

            // Assert that the result set has at least one row, indicating the saved question with the specified ID exists.
            assertTrue(resultSet.next());

            // Assert that the content of the retrieved question matches the expected content.
            assertEquals(question.getContent(), resultSet.getString("content"));

            // Assert that the quiz ID of the retrieved question matches the expected quiz ID.
            assertEquals(question.getQuizId(), resultSet.getInt("quiz_id"));

            // Check if responses associated with the saved question exist in the database.
            PreparedStatement responseStatement = connection.prepareStatement("SELECT * FROM response WHERE question_id = ?");
            responseStatement.setInt(1, question.getId());
            ResultSet responseResultSet = responseStatement.executeQuery();

            // Assert that responses include the provided ones with their expected attributes.
            assertTrue(responseResultSet.next());
            assertEquals(response1.getText(), responseResultSet.getString("text"));
            assertEquals(response1.isCorrect(), responseResultSet.getBoolean("correct"));
            assertTrue(responseResultSet.next());
            assertEquals(response2.getText(), responseResultSet.getString("text"));
            assertEquals(response2.isCorrect(), responseResultSet.getBoolean("correct"));
        }
    }

    /**
     * Tests the updateQuestion method with responses.
     * @throws SQLException If a database error occurs.
     */
    @Test
    public void testUpdateQuestionWithResponses() throws SQLException {
        // Create and save a question with responses.
        Question question = new Question();
        question.setContent("Sample Question");
        question.setQuizId(1);
        Response response1 = new Response("Response 1", true);
        Response response2 = new Response("Response 2", false);
        question.addResponse(response1);
        question.addResponse(response2);
        daoQuestion.saveQuestion(question);

        // Update the question content and add a new response.
        question.setContent("Updated Question");
        Response updatedResponse = new Response("Updated Response", true);
        question.addResponse(updatedResponse);
        daoQuestion.updateQuestion(question);

        // Verify that the question and its responses are updated in the database.
        try (Connection connection = dataSource.getConnection()) {
            // Check if the question with the specified ID exists and has updated content.
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM question WHERE id = ?");
            statement.setInt(1, question.getId());
            ResultSet resultSet = statement.executeQuery();

            // Assert that the result set has at least one row, indicating the question with the specified ID exists.
            assertTrue(resultSet.next());

            // Assert that the content of the retrieved question matches the updated content.
            assertEquals(question.getContent(), resultSet.getString("content"));

            // Check if responses associated with the updated question exist in the database.
            PreparedStatement responseStatement = connection.prepareStatement("SELECT * FROM response WHERE question_id = ?");
            responseStatement.setInt(1, question.getId());
            ResultSet responseResultSet = responseStatement.executeQuery();

            // Assert that responses include the original ones and the newly added response.
            assertTrue(responseResultSet.next());
            assertEquals(response1.getText(), responseResultSet.getString("text"));
            assertEquals(response1.isCorrect(), responseResultSet.getBoolean("correct"));
            assertTrue(responseResultSet.next());
            assertEquals(response2.getText(), responseResultSet.getString("text"));
            assertEquals(response2.isCorrect(), responseResultSet.getBoolean("correct"));
            assertTrue(responseResultSet.next());
            assertEquals(updatedResponse.getText(), responseResultSet.getString("text"));
            assertEquals(updatedResponse.isCorrect(), responseResultSet.getBoolean("correct"));
        }
    }

    /**
     * Tests the deleteQuestion method with responses.
     * @throws SQLException If a database error occurs.
     */
    @Test
    public void testDeleteQuestionWithResponses() throws SQLException {
        // Create and save a question with responses.
        Question question = new Question();
        question.setContent("Sample Question");
        question.setQuizId(1);
        Response response1 = new Response("Response 1", true);
        Response response2 = new Response("Response 2", false);
        question.addResponse(response1);
        question.addResponse(response2);
        daoQuestion.saveQuestion(question);

        // Delete the question.
        daoQuestion.deleteQuestion(question.getId());

        // Verify that the question and its associated responses are deleted from the database.
        try (Connection connection = dataSource.getConnection()) {
            // Check if the question with the specified ID exists.
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM question WHERE id = ?");
            statement.setInt(1, question.getId());
            ResultSet resultSet = statement.executeQuery();

            // Assert that there are no rows in the result, indicating the question is deleted.
            assertFalse(resultSet.next());

            // Check if responses associated with the deleted question exist.
            PreparedStatement responseStatement = connection.prepareStatement("SELECT * FROM response WHERE question_id = ?");
            responseStatement.setInt(1, question.getId());
            ResultSet responseResultSet = responseStatement.executeQuery();

            // Assert that there are no rows in the result, indicating the responses are deleted.
            assertFalse(responseResultSet.next());
        }
    }

    /**
     * Tests the searchQuestionByTopic method with responses.
     * @throws SQLException If a database error occurs.
     */
    @Test
    public void testSearchQuestionByTopicWithResponses() throws SQLException {
        // Create and save three questions with different quiz IDs and responses.
        Question question1 = new Question();
        question1.setContent("Question 1");
        question1.setQuizId(1);
        Response response1 = new Response("Response 1", true);
        question1.addResponse(response1);
        daoQuestion.saveQuestion(question1);

        Question question2 = new Question();
        question2.setContent("Question 2");
        question2.setQuizId(2);
        Response response2 = new Response("Response 2", false);
        question2.addResponse(response2);
        daoQuestion.saveQuestion(question2);

        Question question3 = new Question();
        question3.setContent("Question 3");
        question3.setQuizId(1);
        Response response3 = new Response("Response 3", true);
        question3.addResponse(response3);
        daoQuestion.saveQuestion(question3);

        // Perform a search for questions with quiz ID 1.
        List<Question> result = daoQuestion.searchQuestionByTopic("1");

        // Assert that there are two questions in the result with quiz ID 1.
        assertEquals(2, result.size());

        // Assert that all questions in the result have quiz ID 1.
        assertTrue(result.stream().allMatch(q -> q.getQuizId() == 1));

        // Assert that each question in the result has non-empty responses.
        for (Question question : result) {
            assertFalse(question.getResponses().isEmpty());
        }
    }

    /**
     * Cleans up test data after each test method is executed.
     */
    @AfterEach
    public void cleanupTestData() {
        try (Connection connection = dataSource.getConnection()) {
            // Delete rows from the response table where question_id references questions with quiz_id 1 and 2.
            try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM response WHERE question_id IN (SELECT id FROM question WHERE quiz_id IN (?, ?))")) {
                preparedStatement.setInt(1, 1);
                preparedStatement.setInt(2, 2);
                preparedStatement.executeUpdate();
            }

            // Then, delete rows from the question table where quiz_id = 1 and 2.
            try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM question WHERE quiz_id IN (?, ?)")) {
                preparedStatement.setInt(1, 1);
                preparedStatement.setInt(2, 2);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
