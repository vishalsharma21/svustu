package com.ritara.svustudent.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DBName";

    private static final int DATABASE_VERSION = 4;

    public final static String EMP_ID="_id"; // id value for employee
    public final static String EMP_NAME="name";  // name of employee
    public final static String EMP_ADDRESS="adress";  // name of employee
    public final static String EMP_LONG="lng";  // name of employee
    public final static String EMP_LAT="lat";  // name of employee
    public final static String EMP_DESC="dsc";  // name of employee
    public final static String EMP_DATE="date";  // name of employee
    public final static String EMP_TIME="time";  // name of employee
    public final static String EMP_MOBILE="mobile";  // name of employee
    public final static String EMP_ORDER_ID="ordr";  // name of employee
    public final static String EMP_STATUS="status";  // name of employee

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table MyEmployees( _id integer, name text not null, " +
            "adress text, lng text, lat text, dsc text, date text, time text, mobile text, ordr text,  status text);";

    public MyDatabaseHelper(Context context) {
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
        database.execSQL("DROP TABLE IF EXISTS MyEmployees");
        onCreate(database);
    }
}
