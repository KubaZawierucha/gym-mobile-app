package com.kubazawierucha.powerfulbodyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kubazawierucha.powerfulbodyapp.DAO.ExerciseDAO;
import com.kubazawierucha.powerfulbodyapp.DAO.WorkoutDAO;
import com.kubazawierucha.powerfulbodyapp.Workout.WorkoutList;
import com.kubazawierucha.powerfulbodyapp.Workout.WorkoutListActivity;
import com.kubazawierucha.powerfulbodyapp.models.Exercise;
import com.kubazawierucha.powerfulbodyapp.models.WorkoutDay;

import java.util.List;

public class WorkoutDetailsActivity extends AppCompatActivity {

    private WorkoutDAO workoutDAO;
    private ExerciseDAO exerciseDAO;
    private WorkoutDay workoutDay;
    private int workoutDayId;
    private List<Integer> exerciseListIds;
    private LinearLayout exercisesLinearLayout;
    private TextView dateTextView;
    private TextView muscleGroupTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_details);

        Intent intent = getIntent();
        workoutDayId = intent.getIntExtra("workoutDayId", -1);

        workoutDAO = new WorkoutDAO(this);
        exerciseDAO = new ExerciseDAO(this);
        exercisesLinearLayout = findViewById(R.id.workout_details_exercises_layout);
        dateTextView = findViewById(R.id.workout_details_date);
        muscleGroupTextView = findViewById(R.id.workout_details_muscle_group);

        initData();

        for (int singleExercise: exerciseListIds) {
            Exercise currExercise = exerciseDAO.getExerciseById(singleExercise);
            LinearLayout singleExerciseRow = new LinearLayout(this);
            singleExerciseRow.setOrientation(LinearLayout.VERTICAL);
            Button exerciseButton = new Button(this);
            exerciseButton.setText(currExercise.getName());
            singleExerciseRow.addView(exerciseButton);
            exercisesLinearLayout.addView(singleExerciseRow);
        }

        dateTextView.setText(workoutDay.getDate());
        muscleGroupTextView.setText(exerciseDAO.getExerciseById(exerciseListIds.get(0)).getMuscleGroupName());
    }

    private void initData() {
        workoutDay = workoutDAO.getWorkoutById(workoutDayId);
        if (workoutDay != null) {
            exerciseListIds = workoutDAO.getAllExercisesIdByWorkoutDayId(workoutDayId);
        }
    }
}
