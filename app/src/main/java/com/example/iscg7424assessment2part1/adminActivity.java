package com.example.iscg7424assessment2part1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class adminActivity extends AppCompatActivity {

    Button addQuizBtn, manageBtn;
    RecyclerView QuizzesRV;

    List<Quiz> quizList;
    QuizAdapter quizAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addQuizBtn = findViewById(R.id.addQuizBtn);
        manageBtn = findViewById(R.id.manageQuizBtn);
        QuizzesRV = findViewById(R.id.quizzesRV);

        // Button to add a quiz
        addQuizBtn.setOnClickListener(view -> {
            Intent intent = new Intent(adminActivity.this, addQuizActivity.class);
            startActivity(intent);
        });

        // Button to manage quizzes
        manageBtn.setOnClickListener(view -> {
            Intent intent = new Intent(adminActivity.this, manageQuizActivity.class);
            startActivity(intent);
        });

        // Setup RecyclerView
        QuizzesRV.setLayoutManager(new LinearLayoutManager(this));
        quizList = new ArrayList<>();
        quizAdapter = new QuizAdapter(this, quizList, null);
        QuizzesRV.setAdapter(quizAdapter);

        // Load quizzes from Firebase
        loadQuizzesFromFirebase();
    }

    private void loadQuizzesFromFirebase() {
        DatabaseReference quizRef = FirebaseDatabase.getInstance().getReference("quizzes");

        quizRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                quizList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Quiz quiz = snap.getValue(Quiz.class);
                    quizList.add(quiz);
                }
                quizAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(adminActivity.this, "Failed to load quizzes", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
