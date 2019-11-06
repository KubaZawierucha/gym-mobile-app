package com.kubazawierucha.powerfulbodyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kubazawierucha.powerfulbodyapp.DbManagement.DBManager;
import com.kubazawierucha.powerfulbodyapp.MusclesActivities.MuscleDetailsActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExerciseGroupActivity extends AppCompatActivity {

    private DBManager myDB;
    private ListView listView;
    private ListAdapter listAdapter;
    private List<String> names;
    private List<Integer> images;

    String muscleGroupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_group);

        TextView muscleGroupNameTextView = findViewById(R.id.muscle_group_of_exercises);
        Intent getIntent = getIntent();
        muscleGroupName = getIntent.getStringExtra("name");
        muscleGroupNameTextView.setText(muscleGroupName);

        listView = findViewById(R.id.exercises_list_view);

        initData();

        final ExercisesList exercisesListAdapter = new ExercisesList(ExerciseGroupActivity.this,
                names, images);
        listView.setAdapter(exercisesListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent exerciseDetailsActivity = new Intent(getApplicationContext(), ExerciseDetailsActivity.class);
                exerciseDetailsActivity.putExtra("exerciseName", names.get(position));
                startActivity(exerciseDetailsActivity);
            }
        });
    }

    private void initData() {
        names = new ArrayList<>();
        images = new ArrayList<>();
        myDB = DBManager.getInstance(this);
        myDB.open();
        Cursor data = myDB.getData("Exercise", " JOIN Muscle ON Exercise.muscle_id" +
                " = Muscle.id JOIN MuscleGroup ON Muscle.muscle_group = MuscleGroup.id WHERE " +
                " MuscleGroup.name LIKE '" + muscleGroupName + "'");

        if (data.getCount() == 0) {
            Toast.makeText(this, "The Database is empty!", Toast.LENGTH_SHORT).show();
        } else {
            while (data.moveToNext()) {
                names.add(data.getString(1));
                images.add(R.drawable.body_screen_front);
            }
        }
        data.close();
    }
}
