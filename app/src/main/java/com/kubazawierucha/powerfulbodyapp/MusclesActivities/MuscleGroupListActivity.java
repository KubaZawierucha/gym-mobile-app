package com.kubazawierucha.powerfulbodyapp.MusclesActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.kubazawierucha.powerfulbodyapp.DAO.MuscleDAO;
import com.kubazawierucha.powerfulbodyapp.DAO.MuscleGroupDAO;
import com.kubazawierucha.powerfulbodyapp.DbManagement.DBManager;
import com.kubazawierucha.powerfulbodyapp.ListAdapters.ExpandableListAdapter;
import com.kubazawierucha.powerfulbodyapp.R;
import com.kubazawierucha.powerfulbodyapp.models.Muscle;
import com.kubazawierucha.powerfulbodyapp.models.MuscleGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MuscleGroupListActivity extends AppCompatActivity {

    private MuscleGroupDAO muscleGroupDAO;
    private MuscleDAO muscleDAO;
    private List<MuscleGroup> muscleGroupName;
    private HashMap<MuscleGroup, List<Muscle>> muscleGroupMuscleHashMap;

    private DBManager myDB;
    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muscle_group_list);

        muscleGroupDAO = new MuscleGroupDAO(this);
        muscleDAO = new MuscleDAO(this);
        listView = findViewById(R.id.muscle_group_lv);
        initData();
        listAdapter = new ExpandableListAdapter(this, muscleGroupName, muscleGroupMuscleHashMap);

        listView.setAdapter(listAdapter);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent muscleDetailsActivity = new Intent(getApplicationContext(), MuscleDetailsActivity.class);
                MuscleGroup muscleGroup = (MuscleGroup) listAdapter.getGroup(groupPosition);
                muscleDetailsActivity.putExtra("parents", muscleGroup.getName());
                Muscle muscle = (Muscle) listAdapter.getChild(groupPosition, childPosition);
                muscleDetailsActivity.putExtra("children", muscle.getSimpleName());
                startActivity(muscleDetailsActivity);
                return true;
            }
        });

    }

    private void initData() {
        muscleGroupName = muscleGroupDAO.listMuscleGroups();
        muscleGroupMuscleHashMap = new HashMap<>();

        List<Muscle> muscles;

        for (MuscleGroup singleGroup: muscleGroupName) {
            // here we have a list of muscles
            muscles = muscleDAO.getMuscleByMuscleGroupId(singleGroup.getId());
            // to each muscle group there are muscles assigned
            muscleGroupMuscleHashMap.put(singleGroup, muscles);
        }
    }
}
