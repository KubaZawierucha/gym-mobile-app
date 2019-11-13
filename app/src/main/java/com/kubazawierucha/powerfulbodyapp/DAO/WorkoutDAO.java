package com.kubazawierucha.powerfulbodyapp.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kubazawierucha.powerfulbodyapp.DbManagement.DatabaseOpenHelper;
import com.kubazawierucha.powerfulbodyapp.models.WorkoutDay;

import java.util.ArrayList;
import java.util.List;

public class WorkoutDAO {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    //private DBManager dbManager;

    public WorkoutDAO(Context context) {
        //this.dbManager = new DBManager(context);
        this.openHelper = new DatabaseOpenHelper(context);
    }

    public void connect() {
        try {
            this.db = openHelper.getWritableDatabase();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    public void disconnect() {
        if (db != null) {
            try {
                this.db.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }

    public List<WorkoutDay> getListOfWorkoutDays() throws SQLException {
        List<WorkoutDay> list = new ArrayList<>();
        connect();
        Cursor data = db.rawQuery("SELECT * FROM WorkoutDay wd JOIN MuscleGroup mg ON " +
                "wd.muscle_group = mg.id", null);
        while (data.moveToNext()) {
            int id = data.getInt(0);
            String date = data.getString(1);
            String muscleGroupName = data.getString(4);
            List<String> exercises = new ArrayList<>();
            Cursor subData = db.rawQuery("SELECT e.name FROM WorkoutExercise we JOIN Exercise e ON " +
                    "we.exercise_id = e.id WHERE we.workout_day_id = " + id, null);
            while (subData.moveToNext()) {
                exercises.add(subData.getString(0));
            }
            subData.close();
            WorkoutDay workoutDay = new WorkoutDay(id, date, muscleGroupName, exercises);
            list.add(workoutDay);
        }
        data.close();
        disconnect();

        return list;
    }
}
