package com.kubazawierucha.powerfulbodyapp.Exercises;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.kubazawierucha.powerfulbodyapp.DAO.ExerciseDAO;
import com.kubazawierucha.powerfulbodyapp.R;
import com.kubazawierucha.powerfulbodyapp.Models.Exercise;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExerciseGroupActivity extends AppCompatActivity {

    private ExerciseDAO exerciseDAO;
    private List<Exercise> exercises;

    private ListView listView;
    private List<Bitmap> images;

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
        images = new ArrayList<>();

        for (Exercise exercise: exercises) {
            ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
            File downloadDirectory = wrapper.getDir("images", MODE_PRIVATE);
            String fileName = exercise.getName() + "_1";
            File currFile = new File(downloadDirectory.getAbsolutePath() + "/" + fileName);

            if (currFile.exists()) {
                images.add(BitmapFactory.decodeFile(downloadDirectory.getAbsolutePath() + "/" + fileName));
            } else {
                images.add(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.placeholder_image_1));
            }
        }
    }
}
