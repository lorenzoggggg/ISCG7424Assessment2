package com.example.iscg7424assessment2part1;

import java.util.List;

public class Quiz {
    public String id;
    public String name;
    public String category;
    public String difficulty;
    public String startDate;
    public String endDate;
    public int likes;
    public List<Question> questions;

    public Quiz() {

    }

    public Quiz(String name, String category, String difficulty, String startDate, String endDate, int likes) {
        this.name = name;
        this.category = category;
        this.difficulty = difficulty;
        this.startDate = startDate;
        this.endDate = endDate;
        this.likes = likes;
    }

    public Quiz(String id, String name, String difficulty, String category, String startDate, String endDate, List<Question> questions) {
        this.id = id;
        this.name = name;
        this.difficulty = difficulty;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.questions = questions;
        this.likes = 0; // default value
    }
}



