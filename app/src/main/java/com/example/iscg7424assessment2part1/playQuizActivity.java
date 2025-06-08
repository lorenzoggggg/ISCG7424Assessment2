package com.example.iscg7424assessment2part1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class playQuizActivity extends AppCompatActivity {

    private TextView qNumTxt, questionTxt;
    private Spinner ansSpin;
    private Button confBtn;
    private List<Question> questions = new ArrayList<>();
    private int currentIndex = 0;
    private int score = 0;
    private String quizId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_play_quiz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        qNumTxt = findViewById(R.id.qNumTxt);
        questionTxt = findViewById(R.id.questionTxt);
        ansSpin = findViewById(R.id.ansSpin);
        confBtn = findViewById(R.id.confBtn);

        quizId = getIntent().getStringExtra("quizId");
        loadQuestions();
    }

    private void loadQuestions() {
        FirebaseDatabase.getInstance().getReference("quizzes")
                .child(quizId)
                .child("questions")
                .get().addOnSuccessListener(snapshot -> {
                    questions.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Question q = ds.getValue(Question.class);
                        questions.add(q);
                    }
                    if (!questions.isEmpty()) {
                        showQuestion();
                    }
                });
    }

    private void showQuestion() {
        Question q = questions.get(currentIndex);
        qNumTxt.setText("Question " + (currentIndex + 1));
        questionTxt.setText(q.question);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, q.options);
        ansSpin.setAdapter(adapter);

        confBtn.setOnClickListener(v -> checkAnswer());
    }

    private void checkAnswer() {
        Question q = questions.get(currentIndex);
        String selectedAnswer = ansSpin.getSelectedItem().toString();

        if (selectedAnswer.equals(q.correctAnswer)) {
            Toast.makeText(this, "Correct!!!", Toast.LENGTH_SHORT).show();
            score++;
        } else {
            Toast.makeText(this, "WRONG! The correct answer is: " + q.correctAnswer, Toast.LENGTH_LONG).show();
        }

        currentIndex++;
        if (currentIndex < questions.size()) {
            showQuestion();
        } else {
            goToResults();
        }
    }

    private void goToResults() {
        Intent intent = new Intent(playQuizActivity.this, quizResultsActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("quizId", quizId);
        startActivity(intent);
        finish();
    }
}
