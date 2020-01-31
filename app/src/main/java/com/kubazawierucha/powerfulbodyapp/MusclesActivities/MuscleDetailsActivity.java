package com.kubazawierucha.powerfulbodyapp.MusclesActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.kubazawierucha.powerfulbodyapp.DAO.MuscleDAO;
import com.kubazawierucha.powerfulbodyapp.R;
import com.kubazawierucha.powerfulbodyapp.Models.Muscle;

public class MuscleDetailsActivity extends AppCompatActivity {

    private MuscleDAO muscleDAO;
    private Muscle muscle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muscle_details);

        Intent getIntent = getIntent();
        int muscleId = getIntent.getIntExtra("muscleId", -1);

        muscleDAO = new MuscleDAO(this);
        muscle = muscleDAO.getMuscleById(muscleId);

        TextView muscleGroupTextView = findViewById(R.id.muscle_details_muscle_group_name);
        TextView muscleSimpleNameTextView = findViewById(R.id.muscle_details_muscle_simple_name);
        TextView muscleFormalNameTextView = findViewById(R.id.muscle_details_muscle_formal_name);
        TextView muscleDescriptionTextView = findViewById(R.id.muscle_details_muscle_description_content);

        if (muscle != null) {
            muscleGroupTextView.setText(muscle.getMuscleGroupName());
            muscleSimpleNameTextView.setText(muscle.getSimpleName());
            muscleFormalNameTextView.setText(muscle.getFormalName());
            muscleDescriptionTextView.setText(muscle.getDescription());
        }




    }
}
