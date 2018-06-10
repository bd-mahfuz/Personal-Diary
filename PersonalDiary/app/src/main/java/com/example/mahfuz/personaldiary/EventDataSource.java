package com.example.mahfuz.personaldiary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class EventDataSource {

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    public EventDataSource(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public void openConnection() {
        db = databaseHelper.getWritableDatabase();
    }

    public void closeConnection() {
        db.close();
    }


    public boolean save(Event event) {
        openConnection();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.EVENT_COL_TITLE, event.getTitle());
        contentValues.put(DatabaseHelper.EVENT_COL_DATE, event.getDate());
        contentValues.put(DatabaseHelper.EVENT_COL_DESCRIPTION, event.getDescription());
        long rowAffected = db.insert(DatabaseHelper.EVENT_TABLE_NAME, null, contentValues);
        closeConnection();
        if (rowAffected>0) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean update(Event event) {
        openConnection();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.EVENT_COL_TITLE, event.getTitle());
        contentValues.put(DatabaseHelper.EVENT_COL_DATE, event.getDate());
        contentValues.put(DatabaseHelper.EVENT_COL_DESCRIPTION, event.getDescription());

        String whereClause = DatabaseHelper.EVENT_COL_ID+"=?";
        String whereArgs[] = {String.valueOf(event.getId())};
        int rowAffected = db.update(DatabaseHelper.EVENT_TABLE_NAME, contentValues,whereClause, whereArgs);
        closeConnection();
        if (rowAffected>0) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean delete(int id) {
        openConnection();
        String whereClause = DatabaseHelper.EVENT_COL_ID+"=?";
        String whereArgs[] = {String.valueOf(id)};
        int rowAffected = db.delete(DatabaseHelper.EVENT_TABLE_NAME, whereClause, whereArgs);
        closeConnection();
        if (rowAffected>0) {
            return true;
        }
        else {
            return false;
        }
    }

    public List<Event> getAllEvent () {
        List<Event> events = new ArrayList<>();
        openConnection();

        Cursor cursor = db.query("event", null, null,
                null, null,null,DatabaseHelper.EVENT_COL_ID+" DESC");
        if (cursor != null && cursor.getCount() > 0) {

            cursor.moveToFirst();
            do {
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.EVENT_COL_ID));
                String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EVENT_COL_TITLE));
                String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EVENT_COL_DATE));
                String description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EVENT_COL_DESCRIPTION));

                Event event = new Event();
                event.setId(id);
                event.setTitle(title);
                event.setDate(date);
                event.setDescription(description);

                events.add(event);
            } while(cursor.moveToNext());
        }

        closeConnection();
        return events;
    }

    public Event get(int id) {
        openConnection();
        Cursor cursor = db.query(DatabaseHelper.EVENT_TABLE_NAME, new String[] { DatabaseHelper.EVENT_COL_ID,
                        DatabaseHelper.EVENT_COL_TITLE, DatabaseHelper.EVENT_COL_DATE,
                        DatabaseHelper.EVENT_COL_DESCRIPTION}, DatabaseHelper.EVENT_COL_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Event event = new Event();
        event.setId(cursor.getInt(0));
        event.setTitle(cursor.getString(1));
        event.setDate(cursor.getString(2));
        event.setDescription(cursor.getString(3));
        closeConnection();
        return event;
    }
}
