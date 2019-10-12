package com.kubazawierucha.powerfulbodyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MuscleGroupListActivity extends AppCompatActivity {

    private DBManager myDb;
    private ListView muscleGroupListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muscle_group_list);

        this.muscleGroupListView = findViewById(R.id.muscle_group_lv);
        myDb = DBManager.getInstance(this);
        myDb.open();
        ArrayList<String> muscleGroupList = new ArrayList<>();
        Cursor data = myDb.getData("MuscleGroup", null);
        if (data.getCount() == 0) {
            Toast.makeText(this, "The Database is empty!", Toast.LENGTH_SHORT).show();
        } else {
            while (data.moveToNext()) {
                muscleGroupList.add(data.getString(1));
                ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, muscleGroupList);
                muscleGroupListView.setAdapter(listAdapter);
            }
        }
    }
}
