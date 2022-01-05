package com.example.gymsorter.exercise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.gymsorter.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ExerciseTypeDetails extends AppCompatActivity {

    Intent data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise__activity_exercise_type_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        data = getIntent();

        TextView exerciseType = findViewById(R.id.exerciseTypeDetailsTitle);
        TextView weight = findViewById(R.id.exerciseWeightContent);
        TextView reps = findViewById(R.id.exerciseRepContent);
        TextView sets = findViewById(R.id.exerciseSetContent);

        exerciseType.setText(data.getStringExtra("exerciseType"));
        weight.setText(data.getStringExtra("weight"));
        reps.setText(data.getStringExtra("repetitions"));
        sets.setText(data.getStringExtra("sets"));

        FloatingActionButton fab = findViewById(R.id.editExercise);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(view.getContext(), EditExerciseType.class);
                i.putExtra("exerciseType", data.getStringExtra("exerciseType"));
                i.putExtra("weight", data.getStringExtra("weight"));
                i.putExtra("repetitions", data.getStringExtra("repetitions"));
                i.putExtra("sets", data.getStringExtra("sets"));
                i.putExtra("exerciseID", data.getStringExtra("exerciseID"));
                startActivity(i);
            }
        });

    }
}
