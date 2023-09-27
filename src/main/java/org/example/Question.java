package org.example;

import java.util.ArrayList;
import java.util.List;
/**
 *The Question class represents a question within the application.
 * It contains information about the question, such as its unique identifier,
 * content, associated quiz ID, and a list of possible responses to the question.
 */
public class Question {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Response> getResponses() {
        return responses;
    }

    public void setResponses(List<Response> responses) {
        this.responses = responses;
    }

    private int id;
    private String content;
    private int quizId;
    private List<Response> responses = new ArrayList<>();

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }
    public void addResponse(Response response) {
        responses.add(response);
    }

}
