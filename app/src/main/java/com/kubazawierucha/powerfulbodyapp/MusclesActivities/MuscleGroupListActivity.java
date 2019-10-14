package com.kubazawierucha.powerfulbodyapp.MusclesActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.kubazawierucha.powerfulbodyapp.DbManagement.DBManager;
import com.kubazawierucha.powerfulbodyapp.ListAdapters.ExpandableListAdapter;
import com.kubazawierucha.powerfulbodyapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MuscleGroupListActivity extends AppCompatActivity {

    private DBManager myDB;
    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muscle_group_list);

        listView = findViewById(R.id.muscle_group_lv);
        initData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listHashMap);
        listView.setAdapter(listAdapter);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent muscleDetailsActivity = new Intent(getApplicationContext(), MuscleDetailsActivity.class);
                muscleDetailsActivity.putExtra("parents", listAdapter.getGroup(groupPosition).toString());
                muscleDetailsActivity.putExtra("children", listAdapter.getChild(groupPosition, childPosition).toString());
                startActivity(muscleDetailsActivity);
                return true;
            }
        });

    }

    private void initData() {
        listDataHeader = new ArrayList<>();
        listHashMap = new HashMap<>();
        myDB = DBManager.getInstance(this);
        myDB.open();
        Cursor data = myDB.getData("MuscleGroup", null);
        if (data.getCount() == 0) {
            Toast.makeText(this, "The Database is empty!", Toast.LENGTH_SHORT).show();
        } else {
            while (data.moveToNext()) {
                listDataHeader.add(data.getString(1));
            }
        }
        data.close();

        List<String> itemsGroup;
        for (int i = 0; i < listDataHeader.size(); i++) {
            data = myDB.getData("Muscle", " JOIN MuscleGroup ON Muscle.muscle_group " +
                    "= MuscleGroup.id WHERE MuscleGroup.name = '" + listDataHeader.get(i) + "'");
            itemsGroup = new ArrayList<>();

            if (data.getCount() == 0) {
                Toast.makeText(this, "The Database is empty!", Toast.LENGTH_SHORT).show();
            } else {
                while (data.moveToNext()) {
                    itemsGroup.add(data.getString(1));

                }
            }
            listHashMap.put(listDataHeader.get(i), itemsGroup);
        }
        data.close();
    }
}
