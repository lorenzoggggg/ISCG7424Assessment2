package com.example.iscg7424assessment2part1;

import java.util.List;

public class Question {
    public String question;
    public List<String> options;
    public String correctAnswer;

    public Question() {}

    public Question(String question, List<String> options, String correctAnswer) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }
}
