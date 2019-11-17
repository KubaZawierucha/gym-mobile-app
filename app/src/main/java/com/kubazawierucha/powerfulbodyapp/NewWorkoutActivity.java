package com.kubazawierucha.powerfulbodyapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kubazawierucha.powerfulbodyapp.DAO.ExerciseDAO;
import com.kubazawierucha.powerfulbodyapp.DAO.MuscleGroupDAO;
import com.kubazawierucha.powerfulbodyapp.DAO.WorkoutDAO;
import com.kubazawierucha.powerfulbodyapp.Exercises.ExerciseDetailsActivity;
import com.kubazawierucha.powerfulbodyapp.models.Exercise;
import com.kubazawierucha.powerfulbodyapp.models.Muscle;
import com.kubazawierucha.powerfulbodyapp.models.MuscleGroup;
import com.kubazawierucha.powerfulbodyapp.models.WorkoutDay;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewWorkoutActivity extends AppCompatActivity {

    private List<String> muscleGroupsNames;
    //private List<String> exercisesNames;
    private MuscleGroupDAO muscleGroupDAO;
    private ExerciseDAO exerciseDAO;
    private ArrayAdapter<String> adapter;
    //private CheckBox checkBoxExercises;
    //private LinearLayout mainLayout;
    private LinearLayout checkboxWrapper;
    //private LinearLayout specificationWrapper;
    //private List<CheckBox> exerciseCheckboxes;
    private String muscleGroupName;
    private ArrayList<String> exercisesToWorkout;
    private Button saveWorkoutButton;
    private boolean addExerciseToListButtonToggle;
    private Button selectDateButton;
    private TextView selectedDateTextView;
    private DatePickerDialog datePickerDialog;
    private int year;
    private int month;
    private int day;
    private Calendar calendar;
    private String currentExercise;
    private WorkoutDAO workoutDAO;
    //private MuscleGroupDAO muscleGroupDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_workout);

        workoutDAO = new WorkoutDAO(this);
        muscleGroupDAO = new MuscleGroupDAO(this);

        Intent getIntent = getIntent();
        exercisesToWorkout = new ArrayList<>();
        if (getIntent.hasExtra("listOfExercises")) {
            exercisesToWorkout = getIntent.getStringArrayListExtra("listOfExercises");
        }
        if (getIntent.hasExtra("currentExercise")) {
            currentExercise = getIntent.getStringExtra("currentExercise");
        } else {
            currentExercise = null;
        }

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        String defaultDate = day + "/" + (month + 1) + "/" + year;

        selectDateButton = findViewById(R.id.select_date_button);
        selectedDateTextView = findViewById(R.id.selected_date_text_view);
        selectedDateTextView.setText(defaultDate);

        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(NewWorkoutActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String text = dayOfMonth + "/" + (month+1) + "/" + year;
                        selectedDateTextView.setText(text);
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        saveWorkoutButton = findViewById(R.id.save_workout_button);

        checkboxWrapper = findViewById(R.id.checkbox_wrapper);

        exerciseDAO = new ExerciseDAO(this);
        muscleGroupsNames = new ArrayList<>();
       // mainLayout = findViewById(R.id.new_workout_activity);

        initList();

        Spinner spinnerMuscleGroups = findViewById(R.id.muscleGroups_spinner);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, muscleGroupsNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMuscleGroups.setAdapter(adapter);

        if (getIntent.hasExtra("muscleGroupName")) {
            for (int i = 0; i < spinnerMuscleGroups.getCount(); i++) {
                if (spinnerMuscleGroups.getItemAtPosition(i).toString().equals(getIntent.getStringExtra("muscleGroupName"))) {
                    spinnerMuscleGroups.setSelection(i);
                }
            }
        }
        addExerciseToListButtonToggle = getIntent.getBooleanExtra("addingExerciseToggle", true);

        spinnerMuscleGroups.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                muscleGroupName = adapter.getItem(position);
                initCheckBox(adapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        saveWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> busyDates = findBusyDates();
                String date = selectedDateTextView.getText().toString();

                if (busyDates.contains(date)) {
                    Toast.makeText(NewWorkoutActivity.this, "You already have workout planned on this day!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (exercisesToWorkout != null && exercisesToWorkout.size() != 0) {
                    // change it later !
                    String muscleGroupName = "Chest";
                    int muscleGroupId = muscleGroupDAO.getMuscleGroupByName(muscleGroupName).getId();
                    if (workoutDAO.insertNewWorkoutDay(date, muscleGroupId)) {
                        int currWorkoutDayId = workoutDAO.getWorkoutDayIdByDate(date);
                        if (currWorkoutDayId > -1) {
                            List<Integer> exercisesIds = new ArrayList<>();
                            for (String singleExerciseName : exercisesToWorkout) {
                                exercisesIds.add(exerciseDAO.getExerciseByName(singleExerciseName).getId());
                            }
                            if (!workoutDAO.insertExercisesIntoWorkoutExercise(exercisesIds, currWorkoutDayId)) {
                                Toast.makeText(NewWorkoutActivity.this, "Error while inserting exercise", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            setResult(RESULT_OK, null);
                            finish();
                        }
                    }
                } else {
                    Toast.makeText(NewWorkoutActivity.this, "You have to add at least one exercise!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private List<String> findBusyDates() {
        List<String> dates = new ArrayList<>();
        List<WorkoutDay> workoutDays = workoutDAO.getListOfWorkoutDays();
        for (WorkoutDay wd: workoutDays) {
            dates.add(wd.getDate());
        }
        return dates;
    }

    private void initList() {
        List<MuscleGroup> muscleGroups = muscleGroupDAO.listMuscleGroups();
        muscleGroupsNames.add("Default");
        for (MuscleGroup muscleGroup : muscleGroups) {
            muscleGroupsNames.add(muscleGroup.getName());
        }
    }

    private void initCheckBox(String muscleGroupName) {
        // checkboxWrapper = all exercises rows
        checkboxWrapper.removeAllViews();
        List<Exercise> exercises = exerciseDAO.getExercisesListByMuscleGroupName(muscleGroupName);
        for (final Exercise exercise : exercises) {
            // singleExerciseRow = title button + exerciseSpecificationRow
            final LinearLayout singleExerciseRow = new LinearLayout(this);
            singleExerciseRow.setOrientation(LinearLayout.VERTICAL);

            Button exerciseButton = new Button(this);
            exerciseButton.setText(exercise.getName());


            singleExerciseRow.addView(exerciseButton);
            checkboxWrapper.addView(singleExerciseRow);

            // exerciseSpecificationRow = specification values text views + buttons
            final LinearLayout exerciseSpecificationRow = new LinearLayout(this);
            exerciseSpecificationRow.setOrientation(LinearLayout.HORIZONTAL);
            exerciseSpecificationRow.setVisibility(View.GONE);

            // exerciseSpecificationLayout = text views with specification values
            final LinearLayout exerciseSpecificationLayout = new LinearLayout(this);
            exerciseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (exerciseSpecificationRow.getVisibility() == View.VISIBLE) {
                        exerciseSpecificationRow.setVisibility(View.GONE);
                    } else {
                        exerciseSpecificationRow.setVisibility(View.VISIBLE);
                    }
                }
            });
            exerciseSpecificationLayout.setOrientation(LinearLayout.VERTICAL);
            createSpecificationFields(exerciseSpecificationLayout, exercise);

            exerciseSpecificationRow.addView(exerciseSpecificationLayout);

            Button modifySpecificationButton = new Button(this);
            modifySpecificationButton.setText("MODIFY");
            modifySpecificationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentExercise = exercise.getName();
                    Intent exerciseDetailsActivity = new Intent(getApplicationContext(), ExerciseDetailsActivity.class);
                    exerciseDetailsActivity.putExtra("exerciseName", exercise.getName());
                    exerciseDetailsActivity.putExtra("requestFocusModify", true);
                    startActivityForResult(exerciseDetailsActivity, 1);
                }
            });
            exerciseSpecificationRow.addView(modifySpecificationButton);
            final Button addButton = new Button(this);
            addButton.setText("+");
            if (exercisesToWorkout != null) {
                if (exercisesToWorkout.contains(exercise.getName())) {
                    singleExerciseRow.setBackgroundColor(Color.GREEN);
                    exerciseSpecificationRow.setBackgroundColor(Color.GREEN);
                    addButton.setText("-");
                }
            }

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (addButton.getText().toString().equals("+")) {
                        singleExerciseRow.setBackgroundColor(Color.GREEN);
                        exerciseSpecificationRow.setBackgroundColor(Color.GREEN);
                        addButton.setText("-");
                        exercisesToWorkout.add(exercise.getName());
                    } else {
                        singleExerciseRow.setBackgroundColor(Color.WHITE);
                        exerciseSpecificationRow.setBackgroundColor(Color.WHITE);
                        exercisesToWorkout.remove(exercise.getName());
                        addButton.setText("+");
                    }
                }
            });
            exerciseSpecificationRow.addView(addButton);
            if (exercise.getName().equals(currentExercise)) {
                exerciseSpecificationRow.setVisibility(View.VISIBLE);
            }
            checkboxWrapper.addView(exerciseSpecificationRow);
        }
    }

    private void createSpecificationFields(LinearLayout specificationLayout, Exercise exercise) {
        TextView weightTextView = new TextView(this);
        weightTextView.setText("Weight: ");
        weightTextView.append(Integer.toString(exercise.getLastWeight()));
        specificationLayout.addView(weightTextView);

        TextView seriesTextView = new TextView(this);
        seriesTextView.setText("Series: ");
        seriesTextView.append(Integer.toString(exercise.getLastSeriesNumber()));
        specificationLayout.addView(seriesTextView);

        TextView repTextView = new TextView(this);
        repTextView.setText("Repetitions: ");
        repTextView.append(Integer.toString(exercise.getLastRepetitionsNumber()));
        specificationLayout.addView(repTextView);

        TextView breakTextView = new TextView(this);
        breakTextView.setText("Break Time: ");
        breakTextView.append(Integer.toString(exercise.getLastBreakTime()));
        specificationLayout.addView(breakTextView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Intent refresh = new Intent(this, NewWorkoutActivity.class);
            refresh.putExtra("muscleGroupName", muscleGroupName);
            refresh.putExtra("addingExerciseToggle", addExerciseToListButtonToggle);
            refresh.putStringArrayListExtra("listOfExercises", exercisesToWorkout);
            refresh.putExtra("currentExercise", currentExercise);

            startActivity(refresh);
            this.finish();
        }
    }
}
