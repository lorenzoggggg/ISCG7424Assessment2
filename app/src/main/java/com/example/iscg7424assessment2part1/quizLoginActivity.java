package com.example.iscg7424assessment2part1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class quizLoginActivity extends AppCompatActivity {

    Button loginBtn;
    EditText userTxt, passTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loginBtn = findViewById(R.id.loginBtn);
        userTxt = findViewById(R.id.usernameTxt);
        passTxt = findViewById(R.id.passwordTxt);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = userTxt.getText().toString().trim();
                String password = passTxt.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(quizLoginActivity.this, "Please enter both username and password.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (username.equals("admin")) {
                    if (password.equals("adminpass")) {
                        // Correct admin login
                        Intent intent = new Intent(quizLoginActivity.this, adminActivity.class);
                        startActivity(intent);
                    } else {
                        // Admin username but wrong password
                        Toast.makeText(quizLoginActivity.this, "Incorrect password!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Regular player login
                    Intent intent = new Intent(quizLoginActivity.this, playerActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
            }
        });
    }
}
