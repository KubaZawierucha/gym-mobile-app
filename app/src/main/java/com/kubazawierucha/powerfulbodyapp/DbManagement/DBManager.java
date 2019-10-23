package com.kubazawierucha.powerfulbodyapp.DbManagement;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DBManager instance;

    private DBManager(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    public static DBManager getInstance(Context context) {
        if (instance == null) {
            instance = new DBManager(context);
        }
        return instance;
    }

    public void open() {
        this.db = openHelper.getWritableDatabase();
    }

    public void close() {
        if (db != null) {
            this.db.close();
        }
    }

    public Cursor getData(String tableName, String condition) {
        Cursor cursor;
        //System.out.println("I am doing: \n\t" + "SELECT * FROM " + tableName + condition);
        if (condition == null) {
            cursor = db.rawQuery("SELECT * FROM " + tableName, null);
        } else {
            //cursor = db.rawQuery("SELECT * FROM " + tableName + " WHERE " + condition, null);
            cursor = db.rawQuery("SELECT * FROM " + tableName + condition, null);
        }
        return cursor;
    }

    public boolean insertData(String tableName, String values) {
        db.execSQL("INSERT INTO " + tableName + " VALUES(" + values);
        return true;
    }

    public void deleteTable(String tableName) {
        db.execSQL("DELETE FROM " + tableName);
    }
}
