package com.example.mahfuz.personaldiary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class UserDataSource {

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    public UserDataSource(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public void openConnection() {
        db = databaseHelper.getWritableDatabase();
    }

    public void closeConnection() {
        db.close();
    }

    public boolean save(User user) {
        openConnection();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.USER_COL_PASSWORD, user.getPassword());
        contentValues.put(DatabaseHelper.USER_COL_ISlOGGEDIN, user.isLoggedIn());
        long i = db.insert(DatabaseHelper.USER_TABLE_NAME, null, contentValues);
        closeConnection();
        if (i>0) {
            return true;
        } else {
            return false;
        }
    }

    public List<User> getAll() {
        List<User> list = new ArrayList<>();

        openConnection();
        Cursor cursor = db.query(DatabaseHelper.USER_TABLE_NAME, null, null,
                null,null,null,null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.USER_COL_ID));
                String password = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_COL_PASSWORD));
                int Loggedin = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.USER_COL_ISlOGGEDIN));
                User user = new User();
                user.setId(id);
                user.setPassword(password);
                if (Loggedin == 1) {
                    user.setLoggedIn(true);
                } else {
                    user.setLoggedIn(false);
                }
                list.add(user);
            } while (cursor.moveToNext());

        }
        closeConnection();
        return list;
    }



    public boolean update(int id, String updatedValue) {
        openConnection();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.USER_COL_PASSWORD, updatedValue);
        String whereClause = DatabaseHelper.USER_COL_ID+"=?";
        String whereArgs[] = {String.valueOf(id)};
        int rowAffected = db.update(DatabaseHelper.USER_TABLE_NAME,
                contentValues, whereClause, whereArgs);
        closeConnection();

        if (rowAffected > 0) {
            return true;
        } else {
            return false;
        }

    }

    public boolean update(int id, int updatedValue) {
        openConnection();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.USER_COL_ISlOGGEDIN, updatedValue);
        String whereClause = DatabaseHelper.USER_COL_ID+"=?";
        String whereArgs[] = {String.valueOf(id)};
        int rowAffected = db.update(DatabaseHelper.USER_TABLE_NAME,
                contentValues, whereClause, whereArgs);
        closeConnection();

        if (rowAffected > 0) {
            return true;
        } else {
            return false;
        }

    }

    public String getPasswordFromDB(int id) {
        openConnection();
        Cursor cursor = db.query(DatabaseHelper.USER_TABLE_NAME, new String[] { DatabaseHelper.USER_COL_PASSWORD,
                        }, DatabaseHelper.USER_COL_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        String password = cursor.getString(0);
        closeConnection();
        return password;
    }
}
