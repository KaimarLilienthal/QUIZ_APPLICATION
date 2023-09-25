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

public class DaoQuestionTest {
    private DaoQuestion daoQuestion;
    private PGSimpleDataSource dataSource;

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

    @Test
    public void testSaveQuestionWithResponses() throws SQLException {
        System.out.println("testSaveQuestionWithResponses executed");
        Question question = new Question();
        question.setContent("Sample Question");
        question.setQuizId(1); // Assuming a quiz with ID 1 exists
        Response response1 = new Response("Response 1", true); // Provide non-null text value
        Response response2 = new Response("Response 2", false); // Provide non-null text value
        question.addResponse(response1);
        question.addResponse(response2);

        boolean result = daoQuestion.saveQuestion(question);
        assertTrue(result);

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM question WHERE id = ?");
            statement.setInt(1, question.getId());
            ResultSet resultSet = statement.executeQuery();

            assertTrue(resultSet.next());

            assertEquals(question.getContent(), resultSet.getString("content"));
            assertEquals(question.getQuizId(), resultSet.getInt("quiz_id"));

            PreparedStatement responseStatement = connection.prepareStatement("SELECT * FROM response WHERE question_id = ?");
            responseStatement.setInt(1, question.getId());
            ResultSet responseResultSet = responseStatement.executeQuery();

            assertTrue(responseResultSet.next());
            assertEquals(response1.getText(), responseResultSet.getString("text"));
            assertEquals(response1.isCorrect(), responseResultSet.getBoolean("correct"));
            assertTrue(responseResultSet.next());
            assertEquals(response2.getText(), responseResultSet.getString("text"));
            assertEquals(response2.isCorrect(), responseResultSet.getBoolean("correct"));
        }
    }

    @Test
    public void testUpdateQuestionWithResponses() throws SQLException {
        Question question = new Question();
        question.setContent("Sample Question");
        question.setQuizId(1);
        Response response1 = new Response("Response 1", true);
        Response response2 = new Response("Response 2", false);
        question.addResponse(response1);
        question.addResponse(response2);
        daoQuestion.saveQuestion(question);

        question.setContent("Updated Question");
        Response updatedResponse = new Response("Updated Response", true);
        question.addResponse(updatedResponse);
        daoQuestion.updateQuestion(question);

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM question WHERE id = ?");
            statement.setInt(1, question.getId());
            ResultSet resultSet = statement.executeQuery();
            assertTrue(resultSet.next());

            assertEquals(question.getContent(), resultSet.getString("content"));

            PreparedStatement responseStatement = connection.prepareStatement("SELECT * FROM response WHERE question_id = ?");
            responseStatement.setInt(1, question.getId());
            ResultSet responseResultSet = responseStatement.executeQuery();

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

    @Test
    public void testDeleteQuestionWithResponses() throws SQLException {
        Question question = new Question();
        question.setContent("Sample Question");
        question.setQuizId(1);
        Response response1 = new Response("Response 1", true);
        Response response2 = new Response("Response 2", false);
        question.addResponse(response1);
        question.addResponse(response2);
        daoQuestion.saveQuestion(question);

        daoQuestion.deleteQuestion(question.getId());

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM question WHERE id = ?");
            statement.setInt(1, question.getId());
            ResultSet resultSet = statement.executeQuery();

            assertFalse(resultSet.next());

            PreparedStatement responseStatement = connection.prepareStatement("SELECT * FROM response WHERE question_id = ?");
            responseStatement.setInt(1, question.getId());
            ResultSet responseResultSet = responseStatement.executeQuery();
            assertFalse(responseResultSet.next());
        }
    }

    @Test
    public void testSearchQuestionByTopicWithResponses() throws SQLException {
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

        List<Question> result = daoQuestion.searchQuestionByTopic("1");

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(q -> q.getQuizId() == 1));

        for (Question question : result) {
            assertFalse(question.getResponses().isEmpty());
        }
    }
    @AfterEach
    public void cleanupTestData() {
        try (Connection connection = dataSource.getConnection()) {
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
