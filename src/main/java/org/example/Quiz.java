package org.example;

import java.util.List;
/**
 * The Quiz class represents a quiz within the application.
 * It contains information about the quiz, such as its unique identifier, topic, difficulty level,
 * and a list of questions associated with the quiz.
 */
public class Quiz {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    private int id;
    private String topic;
    private int difficulty;
    private List<Question> questions;
}
