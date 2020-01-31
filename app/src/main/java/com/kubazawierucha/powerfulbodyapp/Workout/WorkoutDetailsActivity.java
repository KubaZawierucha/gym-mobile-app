package com.kubazawierucha.powerfulbodyapp.Workout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kubazawierucha.powerfulbodyapp.DAO.ExerciseDAO;
import com.kubazawierucha.powerfulbodyapp.DAO.WorkoutDAO;
import com.kubazawierucha.powerfulbodyapp.R;
import com.kubazawierucha.powerfulbodyapp.Models.Exercise;
import com.kubazawierucha.powerfulbodyapp.Models.WorkoutDay;

import java.util.List;

import static android.view.Gravity.CENTER;

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
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = CENTER;
            layoutParams.topMargin = 50;
            singleExerciseRow.setLayoutParams(layoutParams);

            singleExerciseRow.setOrientation(LinearLayout.VERTICAL);

            Button exerciseButton = new Button(this);
            exerciseButton.setText(currExercise.getName());
            singleExerciseRow.addView(exerciseButton);
            TextView weightTV = (TextView) getLayoutInflater().inflate(R.layout.auto_generated_textview_template, null);
            weightTV.setText("Weight: " + currExercise.getLastWeight());
            singleExerciseRow.addView(weightTV);
            TextView seriesTV = (TextView) getLayoutInflater().inflate(R.layout.auto_generated_textview_template, null);
            seriesTV.setText("Series: " + currExercise.getLastSeriesNumber());
            singleExerciseRow.addView(seriesTV);
            TextView repTV = (TextView) getLayoutInflater().inflate(R.layout.auto_generated_textview_template, null);
            repTV.setText("Repetitions: " + currExercise.getLastRepetitionsNumber());
            singleExerciseRow.addView(repTV);
            TextView breakTV = (TextView) getLayoutInflater().inflate(R.layout.auto_generated_textview_template, null);
            breakTV.setText("Break: " + currExercise.getLastBreakTime());
            singleExerciseRow.addView(breakTV);
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
