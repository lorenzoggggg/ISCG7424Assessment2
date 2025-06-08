package com.example.iscg7424assessment2part1;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class dogUpdateActivity extends AppCompatActivity {

    private Spinner dogSpin;
    private EditText nameTxt, breedTxt, colourTxt, ageTxt;
    private Button updateBtn;
    private DatabaseReference dbRef;
    private ArrayList<Dog> dogList = new ArrayList<>();
    private ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dog_update);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dogSpin = findViewById(R.id.dogSpin);
        nameTxt = findViewById(R.id.nameTxt);
        breedTxt = findViewById(R.id.breedTxt);
        colourTxt = findViewById(R.id.colourTxt);
        ageTxt = findViewById(R.id.ageTxt);
        updateBtn = findViewById(R.id.updateBtn);

        dbRef = FirebaseDatabase.getInstance().getReference("dogs");

        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dogSpin.setAdapter(spinnerAdapter);

        loadDogsFromFirebase();

        dogSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Dog selectedDog = dogList.get(position);
                populateFields(selectedDog);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        updateBtn.setOnClickListener(v -> {
            int position = dogSpin.getSelectedItemPosition();
            if (position == AdapterView.INVALID_POSITION) {
                Toast.makeText(dogUpdateActivity.this, "No dog selected", Toast.LENGTH_SHORT).show();
                return;
            }

            Dog selectedDog = dogList.get(position);

            String name = nameTxt.getText().toString().trim();
            String breed = breedTxt.getText().toString().trim();
            String colour = colourTxt.getText().toString().trim();
            String age = ageTxt.getText().toString().trim();

            if (name.isEmpty() || breed.isEmpty() || colour.isEmpty() || age.isEmpty()) {
                Toast.makeText(dogUpdateActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Dog updatedDog = new Dog(selectedDog.id, name, breed, colour, age);

            dbRef.child(updatedDog.id).setValue(updatedDog)
                    .addOnSuccessListener(aVoid -> Toast.makeText(dogUpdateActivity.this, "Dog updated successfully!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(dogUpdateActivity.this, "Update failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
        });
    }

    private void loadDogsFromFirebase() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                dogList.clear();
                ArrayList<String> dogNames = new ArrayList<>();

                for (DataSnapshot dogSnapshot : snapshot.getChildren()) {
                    String id = dogSnapshot.getKey();
                    Dog dog = dogSnapshot.getValue(Dog.class);
                    if (dog != null) {
                        dog.id = id;
                        dogList.add(dog);
                        dogNames.add(dog.name);
                    }
                }

                spinnerAdapter.clear();
                spinnerAdapter.addAll(dogNames);
                spinnerAdapter.notifyDataSetChanged();

                if (!dogList.isEmpty()) {
                    dogSpin.setSelection(0);
                    populateFields(dogList.get(0));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(dogUpdateActivity.this, "Failed to load dogs: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void populateFields(Dog dog) {
        nameTxt.setText(dog.name);
        breedTxt.setText(dog.breed);
        colourTxt.setText(dog.colour);
        ageTxt.setText(dog.age);
    }
}
