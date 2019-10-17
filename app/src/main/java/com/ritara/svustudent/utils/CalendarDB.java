package com.ritara.svustudent.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class CalendarDB {

    private DBCalendarHelper dbHelper;

    private SQLiteDatabase database;

    public final static String EMP_TABLE="MyCalendar"; // name of table

    public final static String EMP_ID="_id"; // id value for employee
    public final static String EMP_DATE="date";  // name of employee
    public final static String EMP_TIME="time";  // name of employee
    public final static String EMP_MESSAGE="message";  // name of employee

    public CalendarDB(Context context){
        dbHelper = new DBCalendarHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public long createRecords(String id, String date, String time, String message){
        ContentValues values = new ContentValues();
        values.put(EMP_ID, id);
        values.put(EMP_DATE, date);
        values.put(EMP_TIME, time);
        values.put(EMP_MESSAGE, message);

        return database.insert(EMP_TABLE, null, values);
    }

    public ArrayList<UserModel> getAllRecords(){

        Cursor cursor = database.rawQuery("select * from MyCalendar",null);
        ArrayList<UserModel> arrayList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                UserModel userModel = new UserModel();
                userModel.setId(cursor.getString(cursor.getColumnIndex(EMP_ID)));
                userModel.setDate(cursor.getString(cursor.getColumnIndex(EMP_DATE)));
                userModel.setTime(cursor.getString(cursor.getColumnIndex(EMP_TIME)));
                userModel.setMessage(cursor.getString(cursor.getColumnIndex(EMP_MESSAGE)));
                arrayList.add(userModel);
                cursor.moveToNext();
            }
        }
        return arrayList;
    }

    public ArrayList<UserModel> getRecordsByDate(String date){

        Cursor cursor = database.rawQuery("select * from MyCalendar",null);
        ArrayList<UserModel> arrayList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                    if (cursor.moveToFirst()) {
                        while (!cursor.isAfterLast()) {
                            UserModel userModel = new UserModel();
                            userModel.setId(cursor.getString(cursor.getColumnIndex(EMP_ID)));
                            userModel.setDate(cursor.getString(cursor.getColumnIndex(EMP_DATE)));
                            userModel.setTime(cursor.getString(cursor.getColumnIndex(EMP_TIME)));
                            userModel.setMessage(cursor.getString(cursor.getColumnIndex(EMP_MESSAGE)));

                            if(cursor.getString(cursor.getColumnIndex(EMP_DATE)).equalsIgnoreCase(date)){
                                arrayList.add(userModel);
                            }
                            cursor.moveToNext();
                        }
                    }
                cursor.moveToNext();
            }
        }
        return arrayList;
    }

    public void updateStatus(String rowId){
        ContentValues cv = new ContentValues();
    }

}
