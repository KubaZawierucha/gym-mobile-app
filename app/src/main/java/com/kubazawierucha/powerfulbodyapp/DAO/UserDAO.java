package com.kubazawierucha.powerfulbodyapp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kubazawierucha.powerfulbodyapp.DbManagement.DatabaseOpenHelper;
import com.kubazawierucha.powerfulbodyapp.Models.User;

public class UserDAO {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;

    public UserDAO(Context context) {
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

    public User getUserCreatedByApp() throws SQLException {
        User user = null;
        connect();
        Cursor cursor = db.rawQuery("SELECT * FROM User WHERE createdByApp = 1", null);
        if (cursor.moveToNext()) {
            String name = cursor.getString(0);
            int age = cursor.getInt(1);
            String  gender = cursor.getString(2);
            double height = cursor.getDouble(3);
            double weight = cursor.getDouble(4);
            double waist = cursor.getDouble(5);
            double neck = cursor.getDouble(6);
            double hip = cursor.getDouble(7);
            boolean isCreatedByApp = true;

            user = new User(name, age, gender, height, weight, waist, neck, hip, isCreatedByApp);
        }

        cursor.close();
        disconnect();
        return user;
    }

    public boolean updateOrCreateUser(User user, boolean userExists) throws SQLException {
        connect();
        ContentValues cv = new ContentValues();
        cv.put("name", user.getName());
        cv.put("age", Integer.toString(user.getAge()));
        cv.put("gender", user.getGender());
        cv.put("height", Double.toString(user.getHeight()));
        cv.put("weight", Double.toString(user.getWeight()));
        cv.put("waist", Double.toString(user.getWaist()));
        cv.put("neck", Double.toString(user.getNeck()));
        cv.put("hip", Double.toString(user.getHip()));
        cv.put("createdByApp", "1");

        boolean result;

        if (userExists) {
            result = db.update("User", cv, "createdByApp = ?", new String[]{"1"}) != 0;
        } else {
            result = db.insert("User", null, cv) != 0;
        }

        disconnect();
        return result;
    }
}
