package com.example.iscg7424assessment2part1;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.LinkedHashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class addQuizActivity extends AppCompatActivity {

    Spinner diffSpin, catSpin;
    TextView txtStartDate, txtEndDate;
    Map<String, Integer> categoryMap = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_quiz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // datepicker setup to allow for a textview which displays the date chosen,
        // upon click opens the calendar to select date from.
        txtStartDate = findViewById(R.id.startDateTxt);
        txtEndDate = findViewById(R.id.endDateTxt);
        Calendar calendar = Calendar.getInstance();

        txtStartDate.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            new DatePickerDialog(this, (view, y, m, d) -> {
                String date = (m + 1) + "/" + d + "/" + y;
                txtStartDate.setText(date);
            }, year, month, day).show();
        });

        txtEndDate.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            new DatePickerDialog(this, (view, y, m, d) -> {
                String date = (m + 1) + "/" + d + "/" + y;
                txtEndDate.setText(date);
            }, year, month, day).show();
        });

        // spinner setup to hold quiz category and difficulty options
        diffSpin = findViewById(R.id.diffSpin);
        catSpin = findViewById(R.id.catSpin);

        String[] difficulties = {"easy", "medium", "hard"};
        ArrayAdapter<String> diffAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, difficulties);
        diffAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diffSpin.setAdapter(diffAdapter);

        categoryMap.put("General Knowledge", 9);
        categoryMap.put("Film", 11);
        categoryMap.put("Music", 12);
        categoryMap.put("Video Games", 15);
        categoryMap.put("Nature & Science", 17);
        categoryMap.put("Computer Science", 18);
        categoryMap.put("Geography", 22);
        categoryMap.put("History", 23);
        categoryMap.put("Animals", 27);
        categoryMap.put("Vehicles", 28);
        categoryMap.put("Anime & Manga", 31);

        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(categoryMap.keySet()));
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catSpin.setAdapter(catAdapter);

        findViewById(R.id.addBtn).setOnClickListener(v -> {
            String name = ((EditText) findViewById(R.id.qNameTxt)).getText().toString().trim();
            String difficulty = diffSpin.getSelectedItem().toString();
            String selectedCategoryName = catSpin.getSelectedItem().toString();

            // using map to convert category name to ID for the API link
            int category = categoryMap.get(selectedCategoryName);

            String startDate = txtStartDate.getText().toString().trim();
            String endDate = txtEndDate.getText().toString().trim();

            if (name.isEmpty() || startDate.equals("Start Date...") || endDate.equals("End Date...")) {
                Toast.makeText(this, "Please enter a date!", Toast.LENGTH_SHORT).show();
                return;
            }

            CreateQuiz(name, difficulty, category, startDate, endDate);
        });
    }

    private void CreateQuiz(String name, String difficulty, int category, String startDate, String endDate) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://opentdb.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        QuizApiService api = retrofit.create(QuizApiService.class);
        Call<QuizResponse> call = api.getQuestions(10, category, difficulty, "multiple");

        call.enqueue(new Callback<QuizResponse>() {
            @Override
            public void onResponse(Call<QuizResponse> call, Response<QuizResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Question> formattedQuestions = new ArrayList<>();

                    for (QuizResult result : response.body().results) {
                        List<String> options = result.wrongAnswers != null ? new ArrayList<>(result.wrongAnswers) : new ArrayList<>();
                        options.add(result.correctAnswer);
                        Collections.shuffle(options);
                        formattedQuestions.add(new Question(result.question, options, result.correctAnswer));
                    }

                    String id = UUID.randomUUID().toString();
                    Quiz quiz = new Quiz(id, name, difficulty, String.valueOf(category), startDate, endDate, formattedQuestions);

                    FirebaseDatabase.getInstance().getReference("quizzes")
                            .child(id)
                            .setValue(quiz)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(addQuizActivity.this, "Quiz successfully Added!", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(addQuizActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }
            }

            @Override
            public void onFailure(Call<QuizResponse> call, Throwable t) {
                Toast.makeText(addQuizActivity.this, "API Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
