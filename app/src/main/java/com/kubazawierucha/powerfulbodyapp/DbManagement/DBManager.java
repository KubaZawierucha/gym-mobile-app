package com.kubazawierucha.powerfulbodyapp.DbManagement;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
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













   // private SQLiteOpenHelper openHelper;
   // private SQLiteDatabase db;
   // private static DBManager instance;









    //private DBManager(Context context) {
    //    this.openHelper = new DatabaseOpenHelper(context);
   // }

//    public static DBManager getInstance(Context context) {
//        if (instance == null) {
//            instance = new DBManager(context);
//        }
//        return instance;
//    }
//
//    public void open() {
//        this.db = openHelper.getWritableDatabase();
//    }
//
//    public void close() {
//        if (db != null) {
//            this.db.close();
//        }
//    }
//
//    public Cursor getData(String tableName, String condition) {
//        Cursor cursor;
//        //System.out.println("I am doing: \n\t" + "SELECT * FROM " + tableName + condition);
//        if (condition == null) {
//            cursor = db.rawQuery("SELECT * FROM " + tableName, null);
//        } else {
//            //cursor = db.rawQuery("SELECT * FROM " + tableName + " WHERE " + condition, null);
//            cursor = db.rawQuery("SELECT * FROM " + tableName + condition, null);
//        }
//        return cursor;
//    }
//
//    public boolean insertData(String tableName, String values) {
//        db.execSQL("INSERT INTO " + tableName + " VALUES(" + values);
//        return true;
//    }
//
//    public void deleteTable(String tableName) {
//        db.execSQL("DELETE FROM " + tableName);
//    }
}
