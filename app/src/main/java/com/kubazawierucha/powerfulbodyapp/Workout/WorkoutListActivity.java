package com.kubazawierucha.powerfulbodyapp.Workout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.kubazawierucha.powerfulbodyapp.DAO.WorkoutDAO;
import com.kubazawierucha.powerfulbodyapp.DbManagement.DBManager;
import com.kubazawierucha.powerfulbodyapp.Exercises.ExerciseDetailsActivity;
import com.kubazawierucha.powerfulbodyapp.NewWorkoutActivity;
import com.kubazawierucha.powerfulbodyapp.R;
import com.kubazawierucha.powerfulbodyapp.WorkoutDetailsActivity;
import com.kubazawierucha.powerfulbodyapp.models.WorkoutDay;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class WorkoutListActivity extends AppCompatActivity {

    private WorkoutDAO workoutDAO;
    private List<WorkoutDay> workoutDays;

    private Button addNewTrainingBtn;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_list);

        workoutDAO = new WorkoutDAO(this);

        addNewTrainingBtn = findViewById(R.id.add_new_workout_btn);
        addNewTrainingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newWorkoutFormActivityIntent = new Intent(getApplicationContext(), NewWorkoutActivity.class);
                startActivityForResult(newWorkoutFormActivityIntent, 1);
            }
        });

        listView = findViewById(R.id.workout_list_view);

        initData();

        final WorkoutList workoutListAdapter = new WorkoutList(WorkoutListActivity.this, workoutDays);
        listView.setAdapter(workoutListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent workoutDetailsActivity = new Intent(getApplicationContext(), WorkoutDetailsActivity.class);
                workoutDetailsActivity.putExtra("workoutDayId", workoutDays.get(position).getId());
                startActivity(workoutDetailsActivity);
            }
        });
    }

    private void initData() {
        workoutDays = workoutDAO.getListOfWorkoutDays();
        Collections.reverse(workoutDays);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Intent refresh = new Intent(this, WorkoutListActivity.class);

            startActivity(refresh);
            this.finish();
        }
    }
}
