package com.example.iscg7424assessment2part1;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class manageQuizActivity extends AppCompatActivity {

    TextView startDMTxt, endDMTxt;
    EditText nameMTxt;
    Button updateQBtn, deleteQBtn;
    RecyclerView quizzesRV;

    List<Quiz> quizList = new ArrayList<>();
    QuizAdapter adapter;

    DatabaseReference quizRef;

    Quiz selectedQuiz = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_quiz);

        // View bindings
        nameMTxt = findViewById(R.id.nameMTxt);
        startDMTxt = findViewById(R.id.startDMTxt);
        endDMTxt = findViewById(R.id.endDMTxt);
        updateQBtn = findViewById(R.id.updateQBtn);
        deleteQBtn = findViewById(R.id.deleteQBtn);
        quizzesRV = findViewById(R.id.quizzesMRV);

        quizRef = FirebaseDatabase.getInstance().getReference("quizzes");

        quizzesRV.setLayoutManager(new LinearLayoutManager(this));
        adapter = new QuizAdapter(this, quizList, quiz -> {
            selectedQuiz = quiz;
            nameMTxt.setText(quiz.name);
            startDMTxt.setText(quiz.startDate);
            endDMTxt.setText(quiz.endDate);
        });
        quizzesRV.setAdapter(adapter);

        loadQuizzes();

        setupDatePicker(startDMTxt);
        setupDatePicker(endDMTxt);

        updateQBtn.setOnClickListener(v -> {
            if (selectedQuiz == null) {
                Toast.makeText(this, "Select a quiz to update", Toast.LENGTH_SHORT).show();
                return;
            }

            selectedQuiz.name = nameMTxt.getText().toString();
            selectedQuiz.startDate = startDMTxt.getText().toString();
            selectedQuiz.endDate = endDMTxt.getText().toString();

            quizRef.child(selectedQuiz.id).setValue(selectedQuiz)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Quiz updated!", Toast.LENGTH_SHORT).show();
                        loadQuizzes();
                    });
        });

        deleteQBtn.setOnClickListener(v -> {
            if (selectedQuiz == null) {
                Toast.makeText(this, "Select a quiz to delete", Toast.LENGTH_SHORT).show();
                return;
            }

            quizRef.child(selectedQuiz.id).removeValue()
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Quiz deleted!", Toast.LENGTH_SHORT).show();
                        selectedQuiz = null;
                        nameMTxt.setText("");
                        startDMTxt.setText("");
                        endDMTxt.setText("");
                        loadQuizzes();
                    });
        });
    }

    private void loadQuizzes() {
        quizRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                quizList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Quiz quiz = snap.getValue(Quiz.class);
                    quiz.id = snap.getKey(); // Save key as ID for editing
                    quizList.add(quiz);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(manageQuizActivity.this, "Failed to load quizzes.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupDatePicker(TextView textView) {
        Calendar calendar = Calendar.getInstance();

        textView.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            new DatePickerDialog(this, (view, y, m, d) -> {
                String date = (m + 1) + "/" + d + "/" + y;
                textView.setText(date);
            }, year, month, day).show();
        });
    }
}
