package org.example;

import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DaoQuestion {
    private PGSimpleDataSource dataSource;

    public DaoQuestion(PGSimpleDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean saveQuestion(Question question) {
        try (Connection connection = dataSource.getConnection()) {
            String insertQuestionQuery = "INSERT INTO question (content, quiz_id) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuestionQuery, new String[]{"id"})) {
                preparedStatement.setString(1, question.getContent());
                preparedStatement.setInt(2, question.getQuizId());
                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int questionId = generatedKeys.getInt(1);
                        question.setId(questionId);

                        if (question.getResponses() != null && !question.getResponses().isEmpty()) {
                            for (Response response : question.getResponses()) {
                                saveResponse(response, questionId, connection);
                            }
                        }
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void saveResponse(Response response, int questionId, Connection connection) throws SQLException {
        String insertResponseQuery = "INSERT INTO response (text, correct, question_id) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertResponseQuery)) {
            preparedStatement.setString(1, response.getText());
            preparedStatement.setBoolean(2, response.isCorrect());
            preparedStatement.setInt(3, questionId);
            preparedStatement.executeUpdate();
        }
    }

    public void updateQuestion(Question question) {
        try (Connection connection = dataSource.getConnection()) {
            String updateQuestionQuery = "UPDATE question SET content = ?, quiz_id = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuestionQuery)) {
                preparedStatement.setString(1, question.getContent());
                preparedStatement.setInt(2, question.getQuizId());
                preparedStatement.setInt(3, question.getId());
                preparedStatement.executeUpdate();

                updateResponses(question, connection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateResponses(Question question, Connection connection) throws SQLException {
        String deleteResponsesQuery = "DELETE FROM response WHERE question_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteResponsesQuery)) {
            preparedStatement.setInt(1, question.getId());
            preparedStatement.executeUpdate();

            if (question.getResponses() != null && !question.getResponses().isEmpty()) {
                for (Response response : question.getResponses()) {
                    saveResponse(response, question.getId(), connection);
                }
            }
        }
    }

    public void deleteQuestion(int questionId) {
        try (Connection connection = dataSource.getConnection()) {
            // Delete the question and its associated responses
            String deleteQuestionQuery = "DELETE FROM question WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuestionQuery)) {
                preparedStatement.setInt(1, questionId);
                preparedStatement.executeUpdate();

                String deleteResponsesQuery = "DELETE FROM response WHERE question_id = ?";
                try (PreparedStatement responseStatement = connection.prepareStatement(deleteResponsesQuery)) {
                    responseStatement.setInt(1, questionId);
                    responseStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Question> searchQuestionByTopic(String topic) {
        List<Question> questions = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            String searchQuery = "SELECT * FROM question WHERE quiz_id IN (SELECT id FROM quiz WHERE topic = ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(searchQuery)) {
                preparedStatement.setString(1, topic);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Question question = new Question();
                        question.setId(resultSet.getInt("id"));
                        question.setContent(resultSet.getString("content"));
                        question.setQuizId(resultSet.getInt("quiz_id"));

                        List<Response> responses = getResponsesForQuestion(question.getId(), connection);
                        question.setResponses(responses);

                        questions.add(question);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    private List<Response> getResponsesForQuestion(int questionId, Connection connection) throws SQLException {
        List<Response> responses = new ArrayList<>();
        String getResponsesQuery = "SELECT * FROM response WHERE question_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(getResponsesQuery)) {
            preparedStatement.setInt(1, questionId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Response response = new Response("Sample Text", true);
                    response.setId(resultSet.getInt("id"));
                    response.setText(resultSet.getString("text"));
                    response.setCorrect(resultSet.getBoolean("correct"));
                    responses.add(response);
                }
            }
        }
        return responses;
    }

    public PGSimpleDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(PGSimpleDataSource dataSource) {
        this.dataSource = dataSource;
    }
}
