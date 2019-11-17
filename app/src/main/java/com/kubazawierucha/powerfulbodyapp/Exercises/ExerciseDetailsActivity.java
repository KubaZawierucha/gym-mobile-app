package com.kubazawierucha.powerfulbodyapp.Exercises;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kubazawierucha.powerfulbodyapp.DAO.ExerciseDAO;
import com.kubazawierucha.powerfulbodyapp.DAO.MuscleDAO;
import com.kubazawierucha.powerfulbodyapp.R;
import com.kubazawierucha.powerfulbodyapp.models.Exercise;

import java.util.ArrayList;
import java.util.List;

public class ExerciseDetailsActivity extends AppCompatActivity {

    private ExerciseDAO exerciseDAO;
    private Exercise exercise;

    private TextView exerciseNameTextView;
    private TextView muscleGroupNameTextView;
    private TextView muscleSimpleNameTextView;
    private TextView muscleFormalNameTextView;
    private TextView exerciseDescriptionTextView;

    private List<TextView> specificationValuesTextViews = new ArrayList<>();
    private List<EditText> specificationEditViews = new ArrayList<>();

    private Button modifySpecBtn;
    private Button saveSpecBtn;
    private boolean modifyToggle = true;

    private TextView weightTextView;
    private TextView seriesNumTextView;
    private TextView repetitionsNumTextView;
    private TextView breakTimeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_details);

        exerciseNameTextView = findViewById(R.id.detailed_exercise_name_text_view);
        muscleGroupNameTextView = findViewById(R.id.detailed_muscle_group_text_view);
        muscleSimpleNameTextView = findViewById(R.id.main_muscle_simple_name);
        muscleFormalNameTextView = findViewById(R.id.main_muscle_formal_name);
        exerciseDescriptionTextView = findViewById(R.id.exercise_description);

        specificationValuesTextViews.add((TextView) findViewById(R.id.weight_value));
        specificationValuesTextViews.add((TextView) findViewById(R.id.series_num_value));
        specificationValuesTextViews.add((TextView) findViewById(R.id.repetitions_num_value));
        specificationValuesTextViews.add((TextView) findViewById(R.id.break_time_value));

        specificationEditViews.add((EditText) findViewById(R.id.last_weight_edit_text));
        specificationEditViews.add((EditText) findViewById(R.id.last_series_edit_text));
        specificationEditViews.add((EditText) findViewById(R.id.last_rep_edit_text));
        specificationEditViews.add((EditText) findViewById(R.id.last_break_edit_text));

        modifySpecBtn = findViewById(R.id.modify_specification_button);
        modifySpecBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    enableEdit();
            }
        });

        saveSpecBtn = findViewById(R.id.save_specification_button);
        saveSpecBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSpecification();
            }
        });


        exerciseDAO = new ExerciseDAO(this);
        Intent getIntent = getIntent();
        String exerciseNameString = getIntent.getStringExtra("exerciseName");

        boolean isFocusRequestedFromIntent = getIntent.getBooleanExtra("requestFocusModify", false);
        if (isFocusRequestedFromIntent) {
            enableEdit();
            specificationEditViews.get(0).requestFocus();
            setResult(RESULT_OK, null);
        }


        loadDataAboutCurrentExercise(exerciseNameString);
    }

    private void loadDataAboutCurrentExercise(String exerciseName) {
        exercise = exerciseDAO.getExerciseByName(exerciseName);
        MuscleDAO muscleDAO = new MuscleDAO(this);

        exerciseNameTextView.setText(exercise.getName());
        muscleGroupNameTextView.setText(exercise.getMuscleGroupName());
        String muscleFormalName = exercise.getMuscleName();
        muscleSimpleNameTextView.setText(muscleFormalName);
        muscleFormalNameTextView.setText(muscleDAO.getMuscleByFormalName(muscleFormalName).getSimpleName());
        exerciseDescriptionTextView.setText(exercise.getDescription());

        updateSpecificationTextViews();
    }

    private void enableEdit() {
        for (int i = 0; i < specificationValuesTextViews.size(); i++) {
            specificationValuesTextViews.get(i).setVisibility(View.GONE);
            specificationEditViews.get(i).setVisibility(View.VISIBLE);
        }
        modifySpecBtn.setVisibility(View.GONE);
        saveSpecBtn.setVisibility(View.VISIBLE);
    }

    private void saveSpecification() {
        if (isDataOk()) {
            Exercise updatedExercise = new Exercise(exercise.getId());
            updatedExercise.setName(exercise.getName());
            updatedExercise.setDescription(exercise.getDescription());
            updatedExercise.setMuscleName(exercise.getMuscleName());
            updatedExercise.setMuscleGroupName(exercise.getMuscleGroupName());
            updatedExercise.setLastWeight(Integer.parseInt(specificationEditViews.get(0).getText().toString()));
            updatedExercise.setLastSeriesNumber(Integer.parseInt(specificationEditViews.get(1).getText().toString()));
            updatedExercise.setLastRepetitionsNumber(Integer.parseInt(specificationEditViews.get(2).getText().toString()));
            updatedExercise.setLastBreakTime(Integer.parseInt(specificationEditViews.get(3).getText().toString()));

            boolean result = exerciseDAO.updateExercise(updatedExercise);

            if (result) {
                exercise = exerciseDAO.getExerciseByName(updatedExercise.getName());
                updateSpecificationTextViews();
                for (int i = 0; i < specificationValuesTextViews.size(); i++) {
                    specificationValuesTextViews.get(i).setVisibility(View.VISIBLE);
                    specificationEditViews.get(i).setVisibility(View.GONE);
                }
                modifySpecBtn.setVisibility(View.VISIBLE);
                saveSpecBtn.setVisibility(View.GONE);
            }
        }
    }

    private boolean isDataOk() {
        if (areEditTextsEmpty()) {
            return false;
        }

        double weight = Integer.parseInt(specificationEditViews.get(0).getText().toString());
        if (weight <= 0 || weight >= 500) {
            specificationEditViews.get(0).setError("Weight must be bigger than 0 and lower than 500!");
            specificationEditViews.get(0).requestFocus();
            return false;
        }

        int series = Integer.parseInt(specificationEditViews.get(1).getText().toString());
        if (series <= 0 || series >= 20) {
            specificationEditViews.get(1).setError("Number of series must be bigger than 0 and lower than 20!");
            specificationEditViews.get(1).requestFocus();
            return false;
        }

        int rep = Integer.parseInt(specificationEditViews.get(2).getText().toString());
        if (rep <= 0 || rep >= 50) {
            specificationEditViews.get(2).setError("Number of repetitions must be bigger than 0 and lower than 50!");
            specificationEditViews.get(2).requestFocus();
            return false;
        }

        int breakTime = Integer.parseInt(specificationEditViews.get(3).getText().toString());
        if (breakTime <= 0 || breakTime >= 300) {
            specificationEditViews.get(3).setError("Break time must be bigger than 0 and lower than 300!");
            specificationEditViews.get(3).requestFocus();
            return false;
        }

        return true;
    }

    private boolean areEditTextsEmpty() {
        for (EditText editText: specificationEditViews) {
            if (TextUtils.isEmpty(editText.getText())) {
                editText.setError("Value cannot be empty!");
                editText.requestFocus();
                return true;
            }
        }
        return false;
    }

    private void updateSpecificationTextViews() {
        specificationValuesTextViews.get(0).setText(Integer.toString(exercise.getLastWeight()));
        specificationValuesTextViews.get(1).setText(Integer.toString(exercise.getLastSeriesNumber()));
        specificationValuesTextViews.get(2).setText(Integer.toString(exercise.getLastRepetitionsNumber()));
        specificationValuesTextViews.get(3).setText(Integer.toString(exercise.getLastBreakTime()));
    }

    private void refreshActivity() {
        finish();
        startActivity(getIntent());
    }
}
