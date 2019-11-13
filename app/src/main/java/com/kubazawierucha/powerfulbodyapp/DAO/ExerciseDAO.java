package com.kubazawierucha.powerfulbodyapp.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kubazawierucha.powerfulbodyapp.DbManagement.DatabaseOpenHelper;
import com.kubazawierucha.powerfulbodyapp.models.Exercise;

import java.util.ArrayList;
import java.util.List;

public class ExerciseDAO {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;

    public ExerciseDAO(Context context) {
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

    public List<Exercise> getExercisesListByMuscleGroupName(String muscleGroupName) {
        List<Exercise> list = new ArrayList<>();
        connect();
        Cursor cursor = db.rawQuery("SELECT e.id, e.name, e.desc, m.name, last_weight, " +
                "last_rep_num, last_break_time, last_series_num, mg.name FROM Exercise e JOIN " +
                "Muscle m ON e.muscle_id = m.id JOIN MuscleGroup mg ON mg.id = m.muscle_group " +
                "WHERE mg.name = '" + muscleGroupName + "'", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String desc = cursor.getString(2);
            String muscle = cursor.getString(3);
            double lastWeight = cursor.getDouble(4);
            int lastRepNum = cursor.getInt(5);
            int lastBreakTime = cursor.getInt(6);
            int lastSeriesNum = cursor.getInt(7);

            Exercise exercise = new Exercise(id, name, desc, muscle, lastWeight, lastRepNum, lastBreakTime, lastSeriesNum, muscleGroupName);
            list.add(exercise);
        }

        cursor.close();
        disconnect();
        return list;
    }

    public Exercise getExerciseByName(String name) {
        Exercise exercise = null;
        connect();
        Cursor cursor = db.rawQuery("SELECT e.id, e.name, e.desc, m.name, last_weight, " +
                "last_rep_num, last_break_time, last_series_num, mg.name FROM Exercise e JOIN " +
                "Muscle m ON e.muscle_id = m.id JOIN MuscleGroup mg ON mg.id = m.muscle_group " +
                "WHERE e.name = '" + name + "'", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String desc = cursor.getString(2);
            String muscle = cursor.getString(3);
            double lastWeight = cursor.getDouble(4);
            int lastRepNum = cursor.getInt(5);
            int lastBreakTime = cursor.getInt(6);
            int lastSeriesNum = cursor.getInt(7);
            String muscleGroup = cursor.getString(8);

            exercise = new Exercise(id, name, desc, muscle, lastWeight, lastRepNum, lastBreakTime, lastSeriesNum, muscleGroup);
        }

        cursor.close();
        disconnect();
        return exercise;
    }
}
