package com.example.gymsorter.exercise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class EditExerciseType extends AppCompatActivity {

    Intent data;
    EditText editExerciseTitle, editExerciseWeight, editExerciseReps, editExerciseSets;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise__activity_edit_exercise);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fStore = fStore.getInstance();
        fAuth = fAuth.getInstance();

        data = getIntent();

        editExerciseTitle = findViewById(R.id.editExerciseTitle);
        editExerciseWeight = findViewById(R.id.editExerciseWeightContent);
        editExerciseReps = findViewById(R.id.editExerciseRepContent);
        editExerciseSets = findViewById(R.id.editExerciseSetContent);
        progressBar = findViewById(R.id.editNoteProgressBar);

        String exerciseTitle = data.getStringExtra("exerciseType");
        String exerciseWeight = data.getStringExtra("weight");
        String exerciseReps = data.getStringExtra("repetitions");
        String exerciseSets = data.getStringExtra("sets");

        editExerciseTitle.setText(exerciseTitle);
        editExerciseWeight.setText(exerciseWeight);
        editExerciseReps.setText(exerciseReps);
        editExerciseSets.setText(exerciseSets);

        FloatingActionButton fab = findViewById(R.id.editExerciseSave);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nTitle = editExerciseTitle.getText().toString();
                String nWeight = editExerciseWeight.getText().toString();
                String nReps = editExerciseReps.getText().toString();
                String nSets = editExerciseSets.getText().toString();

                if (nTitle.isEmpty() || nWeight.isEmpty() || nReps.isEmpty() || nSets.isEmpty()){
                    Toast.makeText(EditExerciseType.this, "Can not save when fields are empty.", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                DocumentReference docref = fStore.collection("users/" + fAuth.getCurrentUser().getUid() + "/exercise").document(data.getStringExtra("exerciseID"));

                Map<String, Object> note = new HashMap<>();
                note.put("exerciseType", nTitle);
                note.put("weight", nWeight);
                note.put("repetitions", nReps);
                note.put("sets", nSets);

                docref.update(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditExerciseType.this, "Exercise has been edited.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), ExerciseActivity.class));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditExerciseType.this, "Error editing exercise.", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

    }
}
