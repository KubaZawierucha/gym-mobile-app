package com.kubazawierucha.powerfulbodyapp.MainActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kubazawierucha.powerfulbodyapp.BodyActivity.BodyActivity;
import com.kubazawierucha.powerfulbodyapp.MusclesActivities.MuscleGroupListActivity;
import com.kubazawierucha.powerfulbodyapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button goToBodyActivityBtn = findViewById(R.id.goToBodyScreenBtn);
        goToBodyActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bodyActivityIntent = new Intent(getApplicationContext(), BodyActivity.class);
                startActivity(bodyActivityIntent);
            }
        });

        Button goToMuscleGroupListViewBtn = findViewById(R.id.goToMuscleGroupListViewBtn);
        goToMuscleGroupListViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent muscleGroupListActivityIntent = new Intent(getApplicationContext(), MuscleGroupListActivity.class);
                startActivity(muscleGroupListActivityIntent);
            }
        });
    }


}