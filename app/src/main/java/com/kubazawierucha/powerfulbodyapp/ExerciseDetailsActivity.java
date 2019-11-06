package com.kubazawierucha.powerfulbodyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ExerciseDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_details);

        TextView exerciseNameTextView = findViewById(R.id.detailed_exercise_name_text_view);
        Intent getIntent = getIntent();
        String exerciseNameString = getIntent.getStringExtra("exerciseName");
        exerciseNameTextView.setText(exerciseNameString);

        //loadDataAboutCurrentExercise();

    }
}
