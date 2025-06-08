package com.example.iscg7424assessment2part1;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class dogAddActivity extends AppCompatActivity {

    private EditText nameTxt, breedTxt, colourTxt, ageTxt;
    private Button addBtn;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_add);

        nameTxt = findViewById(R.id.nameTxt);
        breedTxt = findViewById(R.id.breedTxt);
        colourTxt = findViewById(R.id.colourTxt);
        ageTxt = findViewById(R.id.ageTxt);
        addBtn = findViewById(R.id.addBtn);

        dbRef = FirebaseDatabase.getInstance().getReference("dogs");

        addBtn.setOnClickListener(v -> {
            String name = nameTxt.getText().toString().trim();
            String breed = breedTxt.getText().toString().trim();
            String colour = colourTxt.getText().toString().trim();
            String age = ageTxt.getText().toString().trim();

            if (name.isEmpty() || breed.isEmpty() || colour.isEmpty() || age.isEmpty()) {
                Toast.makeText(this, "All fields must be entered!", Toast.LENGTH_SHORT).show();
                return;
            }

            String id = dbRef.push().getKey();
            Dog dog = new Dog(id, name, breed, colour, age);

            dbRef.child(id).setValue(dog)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Dog successfully added!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("FIREBASE_ERROR", "Creation of doggy has failed :/", e);
                    });
        });
    }
}
