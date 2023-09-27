package org.example;
/**
 * The Response class represents a possible response or answer
 * to a question within the application. It contains information
 * about the response, including its unique identifier,
 * text content, and whether it is a correct answer.
 */
public class Response {
    public Response(String text, boolean correct) {
        this.text = text;
        this.correct = correct;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    private int id;
    private String text;
    private boolean correct;
}
