package com.example.iscg7424assessment2part1;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class playerActivity extends AppCompatActivity {

    private TextView welcomeTxt;
    private RecyclerView quizRecyclerView;
    private PlayerQuizAdapter adapter;
    private List<Quiz> quizList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_player);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        welcomeTxt = findViewById(R.id.welcomeTxt);
        quizRecyclerView = findViewById(R.id.playQuizzesRV);
        quizRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        quizList = new ArrayList<>();
        adapter = new PlayerQuizAdapter(this, quizList);
        quizRecyclerView.setAdapter(adapter);

        loadQuizzes();

        String username = getIntent().getStringExtra("username");
        if (username != null && !username.isEmpty()) {
            welcomeTxt.setText("Welcome " + username + "!");
        } else {
            welcomeTxt.setText("Welcome Player!");
        }
    }

    private void loadQuizzes() {
        FirebaseDatabase.getInstance().getReference("quizzes")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        quizList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Quiz quiz = dataSnapshot.getValue(Quiz.class);
                            if (quiz != null) {
                                quiz.id = dataSnapshot.getKey();
                                quizList.add(quiz);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }
}
