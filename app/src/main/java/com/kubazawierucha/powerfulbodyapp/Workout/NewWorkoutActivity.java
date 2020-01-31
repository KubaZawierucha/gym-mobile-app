package com.kubazawierucha.powerfulbodyapp.Workout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.kubazawierucha.powerfulbodyapp.Alarm.AlarmReceiver;
import com.kubazawierucha.powerfulbodyapp.DAO.ExerciseDAO;
import com.kubazawierucha.powerfulbodyapp.DAO.MuscleGroupDAO;
import com.kubazawierucha.powerfulbodyapp.DAO.WorkoutDAO;
import com.kubazawierucha.powerfulbodyapp.Exercises.ExerciseDetailsActivity;
import com.kubazawierucha.powerfulbodyapp.R;
import com.kubazawierucha.powerfulbodyapp.Models.Exercise;
import com.kubazawierucha.powerfulbodyapp.Models.MuscleGroup;
import com.kubazawierucha.powerfulbodyapp.Models.WorkoutDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.view.Gravity.CENTER;

public class NewWorkoutActivity extends AppCompatActivity {

    private MuscleGroupDAO muscleGroupDAO;
    private ExerciseDAO exerciseDAO;
    private WorkoutDAO workoutDAO;

    private ArrayAdapter<String> adapter;
    private String muscleGroupName;
    private ArrayList<String> exercisesToWorkout;
    private int mYear;
    private int mMonth;
    private int mDay;
    private String currentExercise;
    private CheckBox reminderCheckBox;
    private String workoutDateFromDatePicker = null;
    private TimePicker timePicker;
    private Intent getIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_workout);

        workoutDAO = new WorkoutDAO(this);
        muscleGroupDAO = new MuscleGroupDAO(this);
        exerciseDAO = new ExerciseDAO(this);

        getIntent = getIntent();

        setUpDatePicker();
        setUpTimePicker();

        exercisesToWorkout = new ArrayList<>();
        if (getIntent.hasExtra("listOfExercises")) {
            exercisesToWorkout = getIntent.getStringArrayListExtra("listOfExercises");
        }
        if (getIntent.hasExtra("currentExercise")) {
            currentExercise = getIntent.getStringExtra("currentExercise");
        } else {
            currentExercise = null;
        }

        setUpMuscleGroupSpinner();

        String muscleGroupName = "Chest";
        final int muscleGroupId = muscleGroupDAO.getMuscleGroupByName(muscleGroupName).getId();

        Button saveWorkoutButton = findViewById(R.id.save_workout_button);
        saveWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checks date
                List<String> busyDates = findBusyDates();
                if (busyDates.contains(workoutDateFromDatePicker)) {
                    Toast.makeText(NewWorkoutActivity.this, "You already have workout " +
                            "planned on this day!", Toast.LENGTH_LONG).show();
                    return;
                }

                // checks number of exercises
                if (exercisesToWorkout != null && exercisesToWorkout.size() != 0) {

                    // tries to insert a new workout day
                    if (workoutDAO.insertNewWorkoutDay(workoutDateFromDatePicker, muscleGroupId)) {
                        int currWorkoutDayId = workoutDAO.getWorkoutDayIdByDate(workoutDateFromDatePicker);

                        // if workout day was created
                        if (currWorkoutDayId > -1) {

                            // creates a list of exercises ids for this training
                            List<Integer> exercisesIds = new ArrayList<>();
                            for (String singleExerciseName : exercisesToWorkout) {
                                exercisesIds.add(exerciseDAO.getExerciseByName(singleExerciseName).getId());
                            }

                            // checks if the exercises were inserted to db
                            if (!workoutDAO.insertExercisesIntoWorkoutExercise(exercisesIds, currWorkoutDayId)) {
                                Toast.makeText(NewWorkoutActivity.this, "Error while " +
                                        "inserting exercise", Toast.LENGTH_SHORT).show();
                                workoutDAO.deleteWorkoutDayById(currWorkoutDayId);
                                return;
                            }

                            // manage the reminders
                            if (reminderCheckBox.isChecked()) {
                                Intent pendingIntent = new Intent(NewWorkoutActivity.this,
                                        AlarmReceiver.class);
                                pendingIntent.putExtra("notificationId", currWorkoutDayId);
                                pendingIntent.putExtra("todo", "You have a workout today!");

                                PendingIntent alarmIntent = PendingIntent.getBroadcast(NewWorkoutActivity.this,
                                        currWorkoutDayId, pendingIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                                AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);

                                int hour = timePicker.getCurrentHour();
                                int minute = timePicker.getCurrentMinute();
                                Calendar startTime = Calendar.getInstance();
                                startTime.set(Calendar.DAY_OF_MONTH, mDay);
                                startTime.set(Calendar.MONTH, mMonth);
                                startTime.set(Calendar.YEAR, mYear);
                                startTime.set(Calendar.HOUR_OF_DAY, hour);
                                startTime.set(Calendar.MINUTE, minute);
                                startTime.set(Calendar.SECOND, 0);

                                long alarmStartTime = startTime.getTimeInMillis();

                                alarm.set(AlarmManager.RTC_WAKEUP, alarmStartTime, alarmIntent);
                                Toast.makeText(NewWorkoutActivity.this, "Reminder set!",
                                        Toast.LENGTH_LONG).show();
                            }
                            setResult(RESULT_OK, null);
                            finish();
                        }
                    }
                } else {
                    Toast.makeText(NewWorkoutActivity.this, "You have to add at least one exercise!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setUpDatePicker() {
        Calendar calendar;
        CalendarView calendarView;

        calendarView = findViewById(R.id.date_picker);
        calendarView.setMinDate(System.currentTimeMillis() - 1000);

        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        workoutDateFromDatePicker = mDay + "/" + (mMonth + 1) + "/" + mYear;

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                mYear = year;
                mMonth = month;
                mDay = dayOfMonth;
                workoutDateFromDatePicker = dayOfMonth + "/" + (month+1) + "/" + year;
            }
        });
    }

    private void setUpTimePicker() {
        timePicker = findViewById(R.id.reminder_time_picker);
        timePicker.setCurrentHour(12);
        timePicker.setCurrentMinute(0);

        reminderCheckBox = findViewById(R.id.reminder_checkbox);
        reminderCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timePicker.getVisibility() == View.VISIBLE) {
                    timePicker.setVisibility(View.GONE);
                } else {
                    timePicker.setVisibility(View.VISIBLE);
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

    private void setUpMuscleGroupSpinner() {
        List<String> muscleGroupsNames = new ArrayList<>();
        List<MuscleGroup> muscleGroups = muscleGroupDAO.listMuscleGroups();
        for (MuscleGroup muscleGroup : muscleGroups) {
            muscleGroupsNames.add(muscleGroup.getName());
        }
        Spinner spinnerMuscleGroups = findViewById(R.id.muscleGroups_spinner);
        adapter = new ArrayAdapter<>(this, R.layout.exercise_spinner_item, muscleGroupsNames);
        adapter.setDropDownViewResource(R.layout.exercise_spinner_dropdown_item);
        spinnerMuscleGroups.setAdapter(adapter);

        if (getIntent.hasExtra("muscleGroupName")) {
            for (int i = 0; i < spinnerMuscleGroups.getCount(); i++) {
                if (spinnerMuscleGroups.getItemAtPosition(i).toString().equals(getIntent.
                        getStringExtra("muscleGroupName"))) {
                    spinnerMuscleGroups.setSelection(i);
                }
            }
        }

        spinnerMuscleGroups.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                muscleGroupName = adapter.getItem(position);
                generateExercisesRows(adapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void generateExercisesRows(String muscleGroupName) {
        LinearLayout exercisesWrapper = findViewById(R.id.checkbox_wrapper);
        exercisesWrapper.removeAllViews();
        List<Exercise> exercises = exerciseDAO.getExercisesListByMuscleGroupName(muscleGroupName);

        for (final Exercise exercise : exercises) {
            final LinearLayout exerciseNameRow = new LinearLayout(this);
            exerciseNameRow.setOrientation(LinearLayout.VERTICAL);

            final Button exerciseButton = new Button(this);
            exerciseButton.setText(exercise.getName());

            exerciseNameRow.addView(exerciseButton);
            exercisesWrapper.addView(exerciseNameRow);

            final LinearLayout exerciseSpecificationRow = new LinearLayout(this);
            exerciseSpecificationRow.setOrientation(LinearLayout.HORIZONTAL);
            exerciseSpecificationRow.setVisibility(View.GONE);

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

            final LinearLayout exerciseSpecificationTextsWrapper = new LinearLayout(this);
            exerciseSpecificationTextsWrapper.setOrientation(LinearLayout.VERTICAL);

            createSpecificationFields(exerciseSpecificationTextsWrapper, exercise);

            exerciseSpecificationRow.addView(exerciseSpecificationTextsWrapper);
            exerciseSpecificationRow.setGravity(CENTER);

            LinearLayout exerciseSpecificationButtonsWrapper = new LinearLayout(this);
            exerciseSpecificationButtonsWrapper.setOrientation(LinearLayout.VERTICAL);
            exerciseSpecificationButtonsWrapper.setPadding(80,0,0,0);

            Button modifySpecificationButton = new Button(this);
            modifySpecificationButton.setText(R.string.modify);
            modifySpecificationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentExercise = exercise.getName();
                    Intent exerciseDetailsActivity = new Intent(getApplicationContext(),
                            ExerciseDetailsActivity.class);
                    exerciseDetailsActivity.putExtra("exerciseName", exercise.getName());
                    exerciseDetailsActivity.putExtra("requestFocusModify", true);
                    startActivityForResult(exerciseDetailsActivity, 1);
                }
            });

            exerciseSpecificationButtonsWrapper.addView(modifySpecificationButton);

            final Button addButton = new Button(this);
            addButton.setText("+");
            if (exercisesToWorkout != null) {
                if (exercisesToWorkout.contains(exercise.getName())) {
                    exerciseButton.setTextColor(Color.GREEN);
                    exerciseButton.setTypeface(null, Typeface.BOLD);
                    addButton.setText("-");
                }
            }

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (addButton.getText().toString().equals("+")) {
                        exerciseButton.setTextColor(Color.GREEN);
                        exerciseButton.setTypeface(null, Typeface.BOLD);
                        addButton.setText("-");
                        exercisesToWorkout.add(exercise.getName());
                    } else {
                        exerciseButton.setTextColor(Color.WHITE);
                        exerciseButton.setTypeface(null, Typeface.NORMAL);
                        exercisesToWorkout.remove(exercise.getName());
                        addButton.setText("+");
                    }
                }
            });

            exerciseSpecificationButtonsWrapper.addView(addButton);
            exerciseSpecificationRow.addView(exerciseSpecificationButtonsWrapper);

            if (exercise.getName().equals(currentExercise)) {
                exerciseSpecificationRow.setVisibility(View.VISIBLE);
            }
            exercisesWrapper.addView(exerciseSpecificationRow);
        }
    }

    private void createSpecificationFields(LinearLayout specificationLayout, Exercise exercise) {
        TextView weightTextView = (TextView) View.inflate(this, R.layout.auto_generated_textview_template,
                null);
        weightTextView.setText(R.string.weight);
        weightTextView.append(Integer.toString(exercise.getLastWeight()));
        specificationLayout.addView(weightTextView);

        TextView seriesTextView = (TextView) View.inflate(this, R.layout.auto_generated_textview_template,
                null);
        seriesTextView.setText(R.string.series);
        seriesTextView.append(Integer.toString(exercise.getLastSeriesNumber()));
        specificationLayout.addView(seriesTextView);

        TextView repTextView = (TextView) View.inflate(this, R.layout.auto_generated_textview_template,
                null);
        repTextView.setText(R.string.repetitions);
        repTextView.append(Integer.toString(exercise.getLastRepetitionsNumber()));
        specificationLayout.addView(repTextView);

        TextView breakTextView = (TextView) View.inflate(this, R.layout.auto_generated_textview_template,
                null);
        breakTextView.setText(R.string.break_time);
        breakTextView.append(Integer.toString(exercise.getLastBreakTime()));
        specificationLayout.addView(breakTextView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Intent refresh = new Intent(this, NewWorkoutActivity.class);
            refresh.putExtra("muscleGroupName", muscleGroupName);
            refresh.putStringArrayListExtra("listOfExercises", exercisesToWorkout);
            refresh.putExtra("currentExercise", currentExercise);

            startActivity(refresh);
            this.finish();
        }
    }
}
