package com.kubazawierucha.powerfulbodyapp.MainActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kubazawierucha.powerfulbodyapp.BodyActivity.BodyActivity;
import com.kubazawierucha.powerfulbodyapp.MusclesActivities.MuscleGroupListActivity;
import com.kubazawierucha.powerfulbodyapp.R;
import com.kubazawierucha.powerfulbodyapp.UserProfile.UserProfileActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button goToBodyActivityBtn = findViewById(R.id.go_to_body_activity_button);
        goToBodyActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bodyActivityIntent = new Intent(getApplicationContext(), BodyActivity.class);
                startActivity(bodyActivityIntent);
            }
        });

        Button goToMuscleGroupListViewBtn = findViewById(R.id.go_to_muscle_group_list_activity_btn);
        goToMuscleGroupListViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent muscleGroupListActivityIntent = new Intent(getApplicationContext(), MuscleGroupListActivity.class);
                startActivity(muscleGroupListActivityIntent);
            }
        });

        Button goToUserProfileActivity = findViewById(R.id.go_to_user_activity_btn);
        goToUserProfileActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userProfileActivityIntent = new Intent(getApplicationContext(), UserProfileActivity.class);
                startActivity(userProfileActivityIntent);
            }
        });
    }


}
