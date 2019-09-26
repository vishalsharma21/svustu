package com.ritara.svustudent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

public class BaseActivity extends AppCompatActivity {
    private static OkHttpClient httpClient;
    ProgressDialog progressDialog;
    private float mDensity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(BaseActivity.this);
        mDensity = getResources().getDisplayMetrics().density;

    }

    public boolean checkPermissions(Activity activity, String[] permissions) {
        int result;
        List<String> list = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(activity, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                list.add(p);
            }
        }
        if (!list.isEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.requestPermissions(list.toArray(new String[list.size()]), 100);
            }
            return false;
        } else
            return true;
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showLoader() {
        try {
            progressDialog.setMessage("please_wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismissLoader() {
        try {
            progressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean isloadershowing() {
        try {
            if (progressDialog.isShowing()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ProgressDialog giveLoaderObject() {
        return progressDialog;
    }

    public void showToast(String msg) {
        Toast.makeText(this, "" + msg, Toast.LENGTH_SHORT).show();
    }

    public static boolean isNetworkEnabled(Context pContext) {
        NetworkInfo activeNetwork = getActiveNetwork(pContext);
        return activeNetwork != null && activeNetwork.isConnected();
    }

    public static NetworkInfo getActiveNetwork(Context pContext) {
        ConnectivityManager conMngr = (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return conMngr == null ? null : conMngr.getActiveNetworkInfo();
    }

    public void hideSoftKeyBoard(View view) {
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showSoftKeyBoard(View view) {
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }

    }

    public void hideSoftKeyBoard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public String getExactDateFromTimeStamp(String date) {
        String[] date_time = date.split(" ");
        String[] time = date_time[1].split(":");
        int hr = 0;
        String am_pm = "";
        if (Integer.parseInt(time[0]) > 12) {
            hr = Integer.parseInt(time[0]) - 12;
            am_pm = "PM";
        } else {
            hr = Integer.parseInt(time[0]);
            am_pm = "AM";
        }
        String delivery_date = date_time[0] + " " + hr + ":" + time[1] + " " + am_pm;
        return delivery_date;
    }

    public int convertPxToDp(int px) {
        return Math.round(px / (Resources.getSystem().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static String getEncodedString(String rawString) {
        try {
            return URLEncoder.encode(rawString, "UTF-8");
        } catch (Exception e1) {
            e1.printStackTrace();
            return rawString;
        }
    }

    public static String getenDecoodedString(String rawString) {
        try {
            return URLDecoder.decode(rawString);
        } catch (Exception e1) {
            e1.printStackTrace();
            return rawString;
        }
    }

}
