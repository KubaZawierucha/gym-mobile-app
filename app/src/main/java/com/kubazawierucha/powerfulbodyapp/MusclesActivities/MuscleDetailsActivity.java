package com.kubazawierucha.powerfulbodyapp.MusclesActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.kubazawierucha.powerfulbodyapp.R;

public class MuscleDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muscle_details);

        TextView parents = findViewById(R.id.parents);
        TextView children = findViewById(R.id.children);
        Intent getIntent = getIntent();
        String parentsString = getIntent.getStringExtra("parents");
        String childrenString = getIntent.getStringExtra("children");
        parents.setText(parentsString);
        children.setText(childrenString);

    }
}
