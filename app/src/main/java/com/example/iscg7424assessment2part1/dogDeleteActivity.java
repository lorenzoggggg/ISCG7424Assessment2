package com.example.iscg7424assessment2part1;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
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

public class dogDeleteActivity extends AppCompatActivity {

    private Spinner dogSpin;
    private Button deleteBtn;

    private DatabaseReference dbRef;
    private ArrayList<Dog> dogList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dog_delete);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dogSpin = findViewById(R.id.dogSpin);
        deleteBtn = findViewById(R.id.deleteBtn);

        dbRef = FirebaseDatabase.getInstance().getReference("dogs");
        dogList = new ArrayList<>();

        loadDogsIntoSpinner();

        deleteBtn.setOnClickListener(v -> {
            int selectedPosition = dogSpin.getSelectedItemPosition();
            if (selectedPosition == Spinner.INVALID_POSITION || dogList.isEmpty()) {
                Toast.makeText(this, "Please select a dog to delete!", Toast.LENGTH_SHORT).show();
                return;
            }

            Dog selectedDog = dogList.get(selectedPosition);

            new AlertDialog.Builder(this)
                    .setTitle("ARE YOU SURE?!")
                    .setMessage("Are you sure you want to destroy '" + selectedDog.name + "' ??")
                    .setPositiveButton("Yes, down with the beast", (dialog, which) -> deleteDog(selectedDog))
                    .setNegativeButton("No, i cant...", null)
                    .show();
        });
    }

    private void loadDogsIntoSpinner() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                dogList.clear();
                ArrayList<String> dogNames = new ArrayList<>();

                for (DataSnapshot dogSnap : snapshot.getChildren()) {
                    Dog dog = dogSnap.getValue(Dog.class);
                    if (dog != null) {
                        dogList.add(dog);
                        dogNames.add(dog.name);
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(dogDeleteActivity.this,
                        android.R.layout.simple_spinner_item, dogNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dogSpin.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(dogDeleteActivity.this, "Doggies failed to load :/", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteDog(Dog dog) {
        if (dog.id == null) {
            Toast.makeText(this, "Dog ID null, cannot delete!", Toast.LENGTH_SHORT).show();
            return;
        }

        dbRef.child(dog.id).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, dog.name + " has been destroyed...", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to delete " + dog.name, Toast.LENGTH_SHORT).show();
                });
    }
}
