package com.kubazawierucha.powerfulbodyapp.Workout;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.kubazawierucha.powerfulbodyapp.DAO.WorkoutDAO;
import com.kubazawierucha.powerfulbodyapp.DbManagement.DBManager;
import com.kubazawierucha.powerfulbodyapp.R;
import com.kubazawierucha.powerfulbodyapp.models.WorkoutDay;

import java.util.ArrayList;
import java.util.List;

public class WorkoutListActivity extends AppCompatActivity {

    private WorkoutDAO workoutDAO;
    private List<WorkoutDay> workoutDays;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_list);

        workoutDAO = new WorkoutDAO(this);

        listView = findViewById(R.id.workout_list_view);

        initData();

        final WorkoutList workoutListAdapter = new WorkoutList(WorkoutListActivity.this, workoutDays);
        listView.setAdapter(workoutListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Intent exerciseDetailsActivity = new Intent(getApplicationContext(), ExerciseDetailsActivity.class);
                //exerciseDetailsActivity.putExtra("exerciseName", names.get(position));
                //startActivity(exerciseDetailsActivity);
            }
        });
    }

    private void initData() {
        workoutDays = workoutDAO.getListOfWorkoutDays();
    }
}
