package com.example.mahfuz.personaldiary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "personal_diary";
    public static final int VERSION = 1;

    // table info for login
    public static final String USER_TABLE_NAME = "user";
    public static final String USER_COL_ID = "id";
    public static final String USER_COL_PASSWORD= "password";
    public static final String USER_COL_ISlOGGEDIN = "is_loggedin";
    public static final String CREATE_USER_TABLE = "create table user(" +
            ""+USER_COL_ID+" integer primary key," +
            ""+USER_COL_PASSWORD+" text," +
            ""+USER_COL_ISlOGGEDIN+" integer)";

    // table info for event
    public static final String EVENT_TABLE_NAME = "event";
    public static final String EVENT_COL_ID = "id";
    public static final String EVENT_COL_TITLE = "title";
    public static final String EVENT_COL_DATE = "date";
    public static final String EVENT_COL_DESCRIPTION = "description";

    public static final String CREATE_EVENT_TABLE = "create table event(" +
            ""+EVENT_COL_ID+" integer primary key, " +
            ""+EVENT_COL_TITLE+" text, " +
            ""+EVENT_COL_DATE+" text, "+
            EVENT_COL_DESCRIPTION+" text)";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(CREATE_EVENT_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
