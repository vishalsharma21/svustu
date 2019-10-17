package com.ritara.svustudent.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferences_SVU {

    private Context mContext;
    private SharedPreferences.Editor editor;
    private static SharedPreferences_SVU sharedpreference_main;
    private SharedPreferences sharedPreference;
    private static final String PREFS_USER_ID = "userId";

    public SharedPreferences_SVU(Context mContext) {
        this.mContext = mContext;
        sharedPreference = mContext.getSharedPreferences("PREF_READ", Context.MODE_PRIVATE);
        editor = sharedPreference.edit();
    }

    public static SharedPreferences_SVU getInstance(Context context) {
        if (sharedpreference_main == null) {
            sharedpreference_main = new SharedPreferences_SVU(context);
        }
        return sharedpreference_main;
    }

    public String getCourse() {
        return sharedPreference.getString("Course", "");
    }

    public void setCourse(String Course) {
        editor = sharedPreference.edit();
        editor.putString("Course", Course);
        editor.commit();
    }

    public String getUserId() {
        return sharedPreference.getString(PREFS_USER_ID, "");
    }

    public void setUserId(String userId) {
        editor = sharedPreference.edit();
        editor.putString(PREFS_USER_ID, userId);
        editor.commit();
    }

    public boolean get_Logged() {
        return sharedPreference.getBoolean("Logged", false);
    }

    public void set_Logged(boolean logged) {
        editor.putBoolean("Logged", logged);
        editor.commit();
    }
    public void setAddress(String Address) {
        editor = sharedPreference.edit();
        editor.putString("Address", Address);
        editor.commit();
    }

    public String getAddress() {
        return sharedPreference.getString("Address", "");
    }


    public String getdeviceId() {
        return sharedPreference.getString("deviceId", "");
    }

    public void set_deviceId(String deviceId) {
        editor = sharedPreference.edit();
        editor.putString("deviceId", deviceId);
        editor.commit();
    }

    public void set_phone(String Mobile) {
        editor.putString("Mobile", Mobile);
        editor.commit();
    }

    public String get_phone() {
        return sharedPreference.getString("Mobile", "");
    }
    public void set_Username(String username) {
        editor.putString("username", username);
        editor.commit();
    }

    public String get_Username() {
        return sharedPreference.getString("username", "");
    }

    public String getTokenIdForFirebase() {
        return sharedPreference.getString("getTokenIdForFirebase", "");
    }

    public void setTokenIdForFirebase(String tokenId) {
        editor.putString("getTokenIdForFirebase", tokenId);
        editor.commit();
    }

    public void remove_Preference() {
        editor.clear();
        editor.apply();
    }

    public void set_email(String fullname) {
        editor.putString("email", fullname);
        editor.commit();
    }

    public String get_email() {
        return sharedPreference.getString("email", "");
    }


    public void setTrainingDone(boolean getTrainingDone) {
        editor.putBoolean("getTrainingDone", getTrainingDone);
        editor.commit();
    }

    public boolean getTrainingDone() {
        return sharedPreference.getBoolean("getTrainingDone", false);
    }

    public void set_MotherName(String getTrainingDone) {
        editor.putString("set_MotherName", getTrainingDone);
        editor.commit();
    }

    public String get_MotherName() {
        return sharedPreference.getString("set_MotherName", "");
    }

    public void set_FatherName(String getTrainingDone) {
        editor.putString("set_FatherName", getTrainingDone);
        editor.commit();
    }

    public String get_FatherName() {
        return sharedPreference.getString("set_FatherName", "");
    }

    public void set_CurrentAddress(String getTrainingDone) {
        editor.putString("set_CurrentAddress", getTrainingDone);
        editor.commit();
    }

    public String get_CurrentAddress() {
        return sharedPreference.getString("set_CurrentAddress", "");
    }

    public boolean getOnChat() {
        return sharedPreference.getBoolean("chat_page", false);
    }

    public void setOnChat(boolean onPage) {
        editor = sharedPreference.edit();
        editor.putBoolean("chat_page", onPage);
        editor.commit();
    }

    public String getBatch() {
        return sharedPreference.getString("Batch", "");

    }
    public void setBatch(String Batch) {
        editor = sharedPreference.edit();
        editor.putString("Batch", Batch);
        editor.commit();
    }

    public String getProfilepic() {
        return sharedPreference.getString("Profilepic", "");

    }

    public void setProfilepic(String Profilepic) {
        editor = sharedPreference.edit();
        editor.putString("Profilepic", Profilepic);
        editor.commit();
    }

    public Integer getChatNotiCount() {
        return sharedPreference.getInt("getChatNotiCount", 0);
    }

    public void setChatNotiCount(int userId) {
        editor = sharedPreference.edit();
        editor.putInt("getChatNotiCount", userId);
        editor.commit();
    }

    public String getFrom() {
        return sharedPreference.getString("getFrom", "");
    }

    public void setFrom(String userId) {
        editor = sharedPreference.edit();
        editor.putString("getFrom", userId);
        editor.commit();
    }
}









