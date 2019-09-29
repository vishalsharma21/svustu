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
        AndroidNetworking.post("http://solutionsdot-com.in/SVU_api/suv_api/api.php")
                .addBodyParameter("rule", "admission_query" )
                .addBodyParameter("name", "" + name.getText().toString().trim())
                .addBodyParameter("email", "" + email_id.getText().toString().trim())
                .addBodyParameter("mobile", "" + phone_number.getText().toString().trim())
                .addBodyParameter("course", "" + course.getText().toString().trim())
                .addBodyParameter("last_qualification", "" + last_qualification.getText().toString().trim())
                .addBodyParameter("dob", "" + dob.getText().toString().trim())
                .addBodyParameter("father_name", "" + father_name.getText().toString().trim())
                .addBodyParameter("adhar_no", "" + adhar_number.getText().toString().trim())
                .addBodyParameter("final_marks", "" + final_marks.getText().toString().trim())
                .addBodyParameter("address", "" + address.getText().toString().trim())
                .setTag("admission_query")
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

