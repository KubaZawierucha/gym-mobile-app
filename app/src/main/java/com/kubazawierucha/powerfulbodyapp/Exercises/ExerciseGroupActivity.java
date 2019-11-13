package com.kubazawierucha.powerfulbodyapp.Exercises;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ListAdapter;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kubazawierucha.powerfulbodyapp.DAO.ExerciseDAO;
import com.kubazawierucha.powerfulbodyapp.DbManagement.DBManager;
import com.kubazawierucha.powerfulbodyapp.R;
import com.kubazawierucha.powerfulbodyapp.models.Exercise;

import java.util.ArrayList;
import java.util.List;

public class ExerciseGroupActivity extends AppCompatActivity {

    private ExerciseDAO exerciseDAO;
    private List<Exercise> exercises;

    //private DBManager myDB;
    private ListView listView;
    //private List<String> names;
    private List<Integer> images;

    String muscleGroupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_group);

        exerciseDAO = new ExerciseDAO(this);

        TextView muscleGroupNameTextView = findViewById(R.id.muscle_group_of_exercises);
        Intent getIntent = getIntent();
        muscleGroupName = getIntent.getStringExtra("name");
        muscleGroupNameTextView.setText(muscleGroupName);

        listView = findViewById(R.id.exercises_list_view);

        initData();

        final ExercisesList exercisesListAdapter = new ExercisesList(ExerciseGroupActivity.this,
                exercises, images);
        listView.setAdapter(exercisesListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent exerciseDetailsActivity = new Intent(getApplicationContext(), ExerciseDetailsActivity.class);
                exerciseDetailsActivity.putExtra("exerciseName", exercises.get(position).getName());
                startActivity(exerciseDetailsActivity);
            }
        });
    }

    private void initData() {
        exercises = exerciseDAO.getExercisesListByMuscleGroupName(muscleGroupName);

        //names = new ArrayList<>();
        images = new ArrayList<>();

        for (Exercise exercise: exercises) {
             //names.add(exercise.getName());
            images.add(R.drawable.body_screen_front);
        }
    }
}
