package com.example.gymsorter.exercise;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.gymsorter.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddExerciseType extends AppCompatActivity {

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    EditText exerciseType, exerciseWeight, exerciseReps, exerciseSets;
    ProgressBar addExerciseProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise__activity_add_exercise);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        exerciseType = findViewById(R.id.addExerciseType);
        exerciseWeight = findViewById(R.id.addExerciseWeightContent);
        exerciseReps = findViewById(R.id.addExerciseRepContent);
        exerciseSets = findViewById(R.id.addExerciseSetContent);
        addExerciseProgressBar = findViewById(R.id.addExerciseProgressBar);

        FloatingActionButton saveExercise = findViewById(R.id.saveExercise);
        saveExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nExerciseType = exerciseType.getText().toString();
                String nExerciseWeight = exerciseWeight.getText().toString();
                String nExerciseReps = exerciseReps.getText().toString();
                String nExerciseSets = exerciseSets.getText().toString();

                if (nExerciseType.isEmpty() || nExerciseWeight.isEmpty() || nExerciseReps.isEmpty() || nExerciseSets.isEmpty()){
                    Toast.makeText(AddExerciseType.this, "Can not save when fields are empty.", Toast.LENGTH_SHORT).show();
                    return;
                }

                addExerciseProgressBar.setVisibility(View.VISIBLE);

                DocumentReference docref = fStore.collection("users/" + fAuth.getCurrentUser().getUid() + "/exercise").document();
                Map<String, Object> exercise = new HashMap<>();
                exercise.put("exerciseType", nExerciseType);
                exercise.put("weight", nExerciseWeight);
                exercise.put("repetitions", nExerciseReps);
                exercise.put("sets", nExerciseSets);

                docref.set(exercise).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddExerciseType.this, "Exercise has been added.", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddExerciseType.this, "Error uploading exercise.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
