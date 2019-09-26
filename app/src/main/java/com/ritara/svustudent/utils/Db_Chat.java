package com.ritara.svustudent.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


public class Db_Chat extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    private static final String CHAT_DB = "CHAT_DB";
    private String TABLE_CHAT = "chat_table";
    private static final String local_ts = "local_ts";
    private static final String id = "id";
    private static final String user_id = "user_id";
    private static final String timestamp = "timestamp";
    private static final String text = "text";
    private static final String image = "image";
    private static final String video = "video";
    private static final String file = "file";
    private static final String name = "name";
    private static final String house_number = "house_number";
    private static final String mobile_number = "mobile_number";
    private static final String receiver_id = "receiver_id";

    static Db_Chat DB_Chat;
    Context context;
    private ArrayList<Model> arrayAddIncidentModels;

    public Db_Chat(Context context) {
        super(context, CHAT_DB, null, DATABASE_VERSION);
        this.context = context;
    }

    public static synchronized Db_Chat getInstance(Context context) {
        if (DB_Chat == null) {
            DB_Chat = new Db_Chat(context);
        }
        return DB_Chat;
    }

    public long insertDB_ChatDetails(Model modelScan) {
        long isinserted = 0;
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(id, modelScan.getId());
            values.put(user_id, modelScan.getUser_id());
            values.put(timestamp, modelScan.getTimestamp());
            values.put(text, modelScan.getText());
            values.put(image, modelScan.getImage());
            values.put(video, modelScan.getVideo());
            values.put(file, modelScan.getFile());
            values.put(name, modelScan.getName());
            values.put(house_number, modelScan.getHouse_no());
            values.put(mobile_number, modelScan.getMobile_no());
            values.put(receiver_id, modelScan.getGroup_chat_id());
            values.put(local_ts, (System.currentTimeMillis() / 1000));

            isinserted = database.insertWithOnConflict(TABLE_CHAT, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            Log.e("DB_Upcming_Rides_status", "" + isinserted);
        } catch (Exception e) {
            Log.e("exception", e + "");
        } finally {
            database.close();
        }

        return isinserted;
    }

    public ArrayList<Model> get_DB_Chat_Details() {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from " + TABLE_CHAT + " ORDER BY " + local_ts + " ASC", null);
        arrayAddIncidentModels = new ArrayList<>();
        try {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                Model incidentModel = new Model();
                incidentModel.setId(cursor.getString(cursor.getColumnIndex(id)));
                incidentModel.setUser_id(cursor.getString(cursor.getColumnIndex(user_id)));
                incidentModel.setTimestamp(cursor.getString(cursor.getColumnIndex(timestamp)));
                incidentModel.setText(cursor.getString(cursor.getColumnIndex(text)));
                incidentModel.setImage(cursor.getString(cursor.getColumnIndex(image)));
                incidentModel.setVideo(cursor.getString(cursor.getColumnIndex(video)));
                incidentModel.setFile(cursor.getString(cursor.getColumnIndex(file)));
                incidentModel.setName(cursor.getString(cursor.getColumnIndex(name)));
                incidentModel.setHouse_no(cursor.getString(cursor.getColumnIndex(house_number)));
                incidentModel.setMobile_no(cursor.getString(cursor.getColumnIndex(mobile_number)));
                incidentModel.setGroup_chat_id(cursor.getString(cursor.getColumnIndex(receiver_id)));

                arrayAddIncidentModels.add(incidentModel);
                cursor.moveToNext();
            }
        } catch (Exception e) {
        } finally {
            cursor.close();
        }
        return arrayAddIncidentModels;
    }

    public boolean IsItemExist(String ID) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT " + id + " FROM " + TABLE_CHAT + " WHERE " + id + "=?", new String[]{ID});
            if (cursor.moveToFirst()) {
                Log.e("Record  Already Exists", "Table is:" + TABLE_CHAT + " ColumnName:" + ID);
                return true;
            }
            Log.e("New Record  ", "Table is:" + TABLE_CHAT + " ColumnName:" + ID + " Column Value:" + ID);
        } catch (Exception errorException) {
            Log.e("Exception occured", "Exception occured " + errorException);
        }
        return false;
    }

    public void delete(String id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("delete from " + TABLE_CHAT + " WHERE id =" + id);
        database.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PROD_TABLE = "CREATE TABLE " + TABLE_CHAT + "("
                + id + " TEXT UNIQUE,"
                + user_id + " TEXT,"
                + local_ts + " INTEGER,"
                + timestamp + " TEXT,"
                + text + " TEXT,"
                + name + " TEXT,"
                + house_number + " TEXT,"
                + mobile_number + " TEXT,"
                + receiver_id + " TEXT,"
                + image + " TEXT,"
                + video + " TEXT,"
                + file + " TEXT)";
        db.execSQL(CREATE_PROD_TABLE);
        Log.e(TABLE_CHAT, "Table Creadted " + TABLE_CHAT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT);
        Log.e(TABLE_CHAT, "DROP");
        onCreate(db);
    }

}