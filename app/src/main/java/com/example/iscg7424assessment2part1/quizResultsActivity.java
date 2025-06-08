package com.example.iscg7424assessment2part1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class quizResultsActivity extends AppCompatActivity {

    TextView scoreTxt;
    Button likeBtn, backBtn;
    int score = 0;
    String quizId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz_results);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        scoreTxt = findViewById(R.id.scoreTxt);
        likeBtn = findViewById(R.id.likeBtn);
        backBtn = findViewById(R.id.backBtn);

        score = getIntent().getIntExtra("score", 0);
        quizId = getIntent().getStringExtra("quizId");

        scoreTxt.setText("Your final score was " + score);

        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quizId != null && !quizId.isEmpty()) {
                    DatabaseReference quizRef = FirebaseDatabase.getInstance().getReference("quizzes").child(quizId).child("likes");
                    quizRef.get().addOnSuccessListener(snapshot -> {
                        long currentLikes = 0;
                        if (snapshot.exists()) {
                            currentLikes = snapshot.getValue(Long.class);
                        }
                        quizRef.setValue(currentLikes + 1);
                        Toast.makeText(quizResultsActivity.this, "Quiz liked! Awesome!", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    Toast.makeText(quizResultsActivity.this, "Quiz ID not found??!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Back button click
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(quizResultsActivity.this, playerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
