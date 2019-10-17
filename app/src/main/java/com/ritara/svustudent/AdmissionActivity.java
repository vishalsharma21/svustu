package com.ritara.svustudent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.ritara.svustudent.Dashboard;
import com.ritara.svustudent.Login;
import com.ritara.svustudent.R;
import com.ritara.svustudent.utils.SharedPreferences_SVU;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.ritara.svustudent.utils.Constants.ADDRESS;
import static com.ritara.svustudent.utils.Constants.ADHAR_NO;
import static com.ritara.svustudent.utils.Constants.ADMISSION_QUERY;
import static com.ritara.svustudent.utils.Constants.BASE_URL_MAIN;
import static com.ritara.svustudent.utils.Constants.COURSE;
import static com.ritara.svustudent.utils.Constants.DOB;
import static com.ritara.svustudent.utils.Constants.EMAIL;
import static com.ritara.svustudent.utils.Constants.FATHER_NAME;
import static com.ritara.svustudent.utils.Constants.FINAL_MARKS;
import static com.ritara.svustudent.utils.Constants.LAST_QUALIFICATION;
import static com.ritara.svustudent.utils.Constants.MOBILE;
import static com.ritara.svustudent.utils.Constants.NAME;
import static com.ritara.svustudent.utils.Constants.RULE;

public class AdmissionActivity extends BaseActivity {

    private EditText name, email_id ,father_name,last_qualification,phone_number,course,final_marks,adhar_number,dob,address;
    private Button next_btn;
    private SharedPreferences_SVU sharedPreferences_svu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_admission);

        sharedPreferences_svu = SharedPreferences_SVU.getInstance(this);
        name = findViewById(R.id.name);
        email_id = findViewById(R.id.email_id);
        father_name = findViewById(R.id.father_name);
        phone_number = findViewById(R.id.phone_number);
        adhar_number = findViewById(R.id.adhar_number);
        address = findViewById(R.id.address);
        dob = findViewById(R.id.dob);
        course = findViewById(R.id.course);
        final_marks= findViewById(R.id.final_marks);
        last_qualification = findViewById(R.id.last_qualification);
        next_btn = findViewById(R.id.next_btn);

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                admission_query();
            }
        });

        sharedPreferences_svu.setFrom("");
    }

    private void admission_query() {
            showLoader();
        AndroidNetworking.post(BASE_URL_MAIN)
                .addBodyParameter(RULE, ADMISSION_QUERY )
                .addBodyParameter(NAME, "" + name.getText().toString().trim())
                .addBodyParameter(EMAIL, "" + email_id.getText().toString().trim())
                .addBodyParameter(MOBILE, "" + phone_number.getText().toString().trim())
                .addBodyParameter(COURSE, "" + course.getText().toString().trim())
                .addBodyParameter(LAST_QUALIFICATION, "" + last_qualification.getText().toString().trim())
                .addBodyParameter(DOB, "" + dob.getText().toString().trim())
                .addBodyParameter(FATHER_NAME, "" + father_name.getText().toString().trim())
                .addBodyParameter(ADHAR_NO, "" + adhar_number.getText().toString().trim())
                .addBodyParameter(FINAL_MARKS, "" + final_marks.getText().toString().trim())
                .addBodyParameter(ADDRESS, "" + address.getText().toString().trim())
                .setTag(ADMISSION_QUERY)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            sharedPreferences_svu.set_Logged(true);
                            startActivity(new Intent(AdmissionActivity.this, Dashboard.class));
                            finish();
                        }
                        catch (
                                Exception e) {
                            e.getMessage();
                            e.printStackTrace();
                        } finally {
                           dismissLoader();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.getResponse();
                       dismissLoader();
                    }

                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(AdmissionActivity.this, Dashboard.class));
    }
}

