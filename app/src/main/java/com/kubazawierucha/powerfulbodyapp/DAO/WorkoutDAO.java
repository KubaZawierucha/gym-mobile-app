package com.kubazawierucha.powerfulbodyapp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kubazawierucha.powerfulbodyapp.DbManagement.DatabaseOpenHelper;
import com.kubazawierucha.powerfulbodyapp.Models.WorkoutDay;

import java.util.ArrayList;
import java.util.List;

public class WorkoutDAO {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;

    public WorkoutDAO(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    private void connect() {
        try {
            this.db = openHelper.getWritableDatabase();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    private void disconnect() {
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

    public boolean insertExercisesIntoWorkoutExercise(List<Integer> listOfExercises, int day_id) throws SQLException {
        boolean result = false;
        connect();
        for (int singleExerciseId: listOfExercises) {
            ContentValues cv = new ContentValues();
            cv.put("exercise_id", singleExerciseId);
            cv.put("workout_day_id", day_id);
            result = db.insert("WorkoutExercise", null, cv) != 0;
        }
        disconnect();

        return result;
    }

    public boolean insertNewWorkoutDay(String date, int muscleGroupId) throws SQLException {
        boolean result;
        connect();
        ContentValues cv = new ContentValues();
        cv.put("date", date);
        cv.put("muscle_group", muscleGroupId);
        result = db.insert("WorkoutDay", null, cv) != 0;

        disconnect();
        return result;
    }

    public int getWorkoutDayIdByDate(String date) throws SQLException {
        int id = -1;
        connect();
        Cursor data = db.rawQuery("SELECT id FROM WorkoutDay WHERE date = '" + date + "'", null);
        while (data.moveToNext()) {
            id = data.getInt(0);
        }
        data.close();
        disconnect();

        return id;
    }

    public WorkoutDay getWorkoutById(int id) throws SQLException {
        WorkoutDay workoutDay = null;
        connect();
        Cursor data = db.rawQuery("SELECT * FROM WorkoutDay WHERE id = " + id, null);
        while (data.moveToNext()) {
            String date = data.getString(1);
            workoutDay = new WorkoutDay(date);
        }
        data.close();
        disconnect();
        return workoutDay;
    }

    public List<Integer> getAllExercisesIdByWorkoutDayId(int workoutDayId) throws SQLException {
        List<Integer> ids = new ArrayList<>();
        connect();
        Cursor data = db.rawQuery("SELECT exercise_id FROM WorkoutExercise WHERE workout_day_id = " + workoutDayId, null);
        while (data.moveToNext()) {
            int id = data.getInt(0);
            ids.add(id);
        }
        data.close();
        disconnect();

        return ids;
    }

    public boolean deleteWorkoutDayById(int id) throws SQLException {
        boolean result;
        connect();
        db.delete("WorkoutExercise", "workout_day_id = " + id, null);
        result = db.delete("WorkoutDay", "id = " + id, null) > 0;
        disconnect();
        return result;
    }
}
