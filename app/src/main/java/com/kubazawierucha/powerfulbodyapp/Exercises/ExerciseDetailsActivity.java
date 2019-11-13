package com.kubazawierucha.powerfulbodyapp.Exercises;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.kubazawierucha.powerfulbodyapp.DAO.ExerciseDAO;
import com.kubazawierucha.powerfulbodyapp.R;
import com.kubazawierucha.powerfulbodyapp.models.Exercise;

public class ExerciseDetailsActivity extends AppCompatActivity {

    private ExerciseDAO exerciseDAO;
    private Exercise exercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_details);

        exerciseDAO = new ExerciseDAO(this);
        TextView exerciseNameTextView = findViewById(R.id.detailed_exercise_name_text_view);
        Intent getIntent = getIntent();
        String exerciseNameString = getIntent.getStringExtra("exerciseName");

        exercise = exerciseDAO.getExerciseByName(exerciseNameString);

        exerciseNameTextView.setText(exercise.getName());

        //loadDataAboutCurrentExercise();

    }
}
