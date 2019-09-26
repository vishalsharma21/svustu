package com.ritara.svustudent.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBCalendarHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DBCalendarHelper";

    private static final int DATABASE_VERSION = 5;

    public final static String EMP_ID="_id"; // id value for employee
    public final static String EMP_DATE="date";  // name of employee
    public final static String EMP_TIME="time";  // name of employee
    public final static String EMP_MESSAGE="message";  // name of employee

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table MyCalendar( _id integer, date text not null, " +
            "time text, message text);";

    public DBCalendarHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    // Method is called during an upgrade of the database,
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
        Log.w(MyDatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS MyCalendar");
        onCreate(database);
    }
}