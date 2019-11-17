package com.kubazawierucha.powerfulbodyapp.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kubazawierucha.powerfulbodyapp.DbManagement.DatabaseOpenHelper;
import com.kubazawierucha.powerfulbodyapp.models.MuscleGroup;

import java.util.ArrayList;
import java.util.List;

public class MuscleGroupDAO {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;

    public MuscleGroupDAO(Context context) {
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

    public List<MuscleGroup> listMuscleGroups() throws SQLException {
        List<MuscleGroup> list = new ArrayList<>();
        connect();
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM MuscleGroup", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            MuscleGroup muscleGroup = new MuscleGroup(id, name);
            list.add(muscleGroup);
        }
        cursor.close();
        disconnect();
        return list;
    }

    public MuscleGroup getMuscleGroupByName(String name) throws SQLException {
        MuscleGroup muscleGroup = null;
        connect();
        Cursor cursor = db.rawQuery("SELECT * FROM MuscleGroup WHERE NAME = '" + name + "'", null);
        if (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            muscleGroup = new MuscleGroup(id, name);
        }
        cursor.close();
        disconnect();
        return  muscleGroup;
    }
}
