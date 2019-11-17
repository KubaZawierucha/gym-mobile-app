package com.kubazawierucha.powerfulbodyapp.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kubazawierucha.powerfulbodyapp.DbManagement.DatabaseOpenHelper;
import com.kubazawierucha.powerfulbodyapp.models.Muscle;
import com.kubazawierucha.powerfulbodyapp.models.MuscleGroup;

import java.util.ArrayList;
import java.util.List;

public class MuscleDAO {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;

    public MuscleDAO(Context context) {
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

    public List<Muscle> getMuscleByMuscleGroupId(int muscleGroup) throws SQLException {
        List<Muscle> list = new ArrayList<>();
        connect();
        Cursor cursor;
        cursor = db.rawQuery("SELECT m.id, m.name, desc, simple_name, mg.name FROM Muscle m " +
                "JOIN MuscleGroup mg ON m.muscle_group = mg.id WHERE muscle_group = " + muscleGroup, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String formalName = cursor.getString(1);
            String simpleName = cursor.getString(3);
            String description = cursor.getString(2);
            String muscleGroupName = cursor.getString(4);
            Muscle muscle = new Muscle(id, formalName, simpleName, description, muscleGroupName);
            list.add(muscle);
        }
        cursor.close();
        disconnect();
        return list;
    }

    public Muscle getMuscleById(int id) throws SQLException {
        Muscle muscle = null;
        connect();
        Cursor cursor = db.rawQuery("SELECT m.id, m.name, desc, simple_name, mg.name FROM Muscle m " +
                "JOIN MuscleGroup mg ON m.muscle_group = mg.id WHERE m.id = " + id, null);
        while (cursor.moveToNext()) {
            String formalName = cursor.getString(1);
            String simpleName = cursor.getString(3);
            String description = cursor.getString(2);
            String muscleGroupName = cursor.getString(4);
            muscle = new Muscle(id, formalName, simpleName, description, muscleGroupName);
        }
        cursor.close();
        disconnect();
        return muscle;
    }

    public Muscle getMuscleByFormalName(String formalName) throws SQLException {
        Muscle muscle = null;
        connect();
        Cursor cursor = db.rawQuery("SELECT m.id, m.name, desc, simple_name, mg.name FROM Muscle m " +
                "JOIN MuscleGroup mg ON m.muscle_group = mg.id WHERE m.name = '" + formalName + "'", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String simpleName = cursor.getString(3);
            String description = cursor.getString(2);
            String muscleGroupName = cursor.getString(4);
            muscle = new Muscle(id, formalName, simpleName, description, muscleGroupName);
        }
        cursor.close();
        disconnect();
        return muscle;
    }
}
