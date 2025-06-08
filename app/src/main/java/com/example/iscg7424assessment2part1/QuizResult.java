package com.example.iscg7424assessment2part1;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuizResult {
    @SerializedName("question")
    public String question;
    @SerializedName("correct_answer")
    public String correctAnswer;
    @SerializedName("incorrect_answers")
    public List<String> wrongAnswers;
}
