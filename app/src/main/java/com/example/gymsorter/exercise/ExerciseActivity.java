package com.example.gymsorter.exercise;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymsorter.R;
import com.example.gymsorter.activities.HomeActivity;
import com.example.gymsorter.maps.MapsActivity;
import com.example.gymsorter.models.Exercises;
import com.example.gymsorter.notes.NotesActivity;
import com.example.gymsorter.profile.UserProfile;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ExerciseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView exerciseList;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    FirebaseUser currentUser;
    FloatingActionButton addExercise;
    FirestoreRecyclerAdapter<Exercises, ExerciseViewHolder> exerciseAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise__activity_nav);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        updateNavHeader();

        Query query = fStore.collection("users/" + fAuth.getCurrentUser().getUid() + "/exercise").orderBy("exerciseType", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Exercises> allExercises = new FirestoreRecyclerOptions.Builder<Exercises>()
                .setQuery(query, Exercises.class)
                .build();

        exerciseAdapter = new FirestoreRecyclerAdapter<Exercises, ExerciseViewHolder>(allExercises) {
            @Override
            protected void onBindViewHolder(@NonNull ExerciseViewHolder exerciseViewHolder, int i, @NonNull final Exercises exercises) {
                exerciseViewHolder.exerciseType.setText(exercises.getExerciseType());
                exerciseViewHolder.exerciseWeight.setText(exercises.getWeight());
                exerciseViewHolder.exerciseReps.setText(exercises.getNumberOfReps());
                exerciseViewHolder.exerciseSets.setText(exercises.getNumberOfSets());

                final String docID = exerciseAdapter.getSnapshots().getSnapshot(i).getId();

                exerciseViewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(v.getContext(), ExerciseTypeDetails.class);
                        i.putExtra("exerciseType", exercises.getExerciseType());
                        i.putExtra("weight", exercises.getWeight());
                        i.putExtra("repetitions", exercises.getNumberOfReps());
                        i.putExtra("sets", exercises.getNumberOfSets());
                        i.putExtra("exerciseID", docID);
                        v.getContext().startActivity(i);
                    }
                });

            }

            @NonNull
            @Override
            public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise__view_layout, parent, false);
                return new ExerciseViewHolder(view);
            }
        };

        exerciseList = findViewById(R.id.exerciseList);
        addExercise = findViewById(R.id.addNewExerciseType);

        exerciseList.setLayoutManager(new LinearLayoutManager(this));
        exerciseList.setAdapter(exerciseAdapter);

        addExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), AddExerciseType.class);
                startActivity(i);
            }
        });

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_home){

            startActivity(new Intent(getApplicationContext(), HomeActivity.class));

        } else if (id == R.id.nav_exercise){
            startActivity(new Intent(getApplicationContext(), ExerciseActivity.class));

        } else if (id == R.id.nav_maps){
            startActivity(new Intent(getApplicationContext(), MapsActivity.class));

        } else if (id == R.id.nav_userProfile){
            startActivity(new Intent(getApplicationContext(), UserProfile.class));

        } else if (id == R.id.nav_notes){
            startActivity(new Intent(getApplicationContext(), NotesActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void updateNavHeader(){

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.nav_username);
        TextView navEmail = headerView.findViewById(R.id.nav_email);

        navUsername.setText(currentUser.getDisplayName());
        navEmail.setText(currentUser.getEmail());
    }

    public class ExerciseViewHolder extends RecyclerView.ViewHolder{

        TextView exerciseType, exerciseWeight, exerciseReps, exerciseSets;
        View view;
        CardView mCardView, mCardViewContent;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);

            exerciseType = itemView.findViewById(R.id.exerciseType);
            exerciseWeight = itemView.findViewById(R.id.weight);
            exerciseReps = itemView.findViewById(R.id.numberOfReps);
            exerciseSets = itemView.findViewById(R.id.numberOfSets);
            mCardView = itemView.findViewById(R.id.exerciseCardView);
            mCardViewContent = itemView.findViewById(R.id.cardViewContent);
            view = itemView;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        exerciseAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (exerciseAdapter != null){
            exerciseAdapter.startListening();
        }
    }
}
