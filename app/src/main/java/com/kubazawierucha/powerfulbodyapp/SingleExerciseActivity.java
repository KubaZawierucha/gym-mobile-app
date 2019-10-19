package com.kubazawierucha.powerfulbodyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SingleExerciseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_exercise);

        TextView muscleGroupNameTextView = findViewById(R.id.muscle_group_name_text_view);
        Intent getIntent = getIntent();
        String muscleGroupName = getIntent.getStringExtra("name");
        muscleGroupNameTextView.setText(muscleGroupName);
    }
}
