package com.ritara.svustudent.fragments;

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

public class AdmissionFragment extends Fragment {

    private EditText name, email_id ,father_name,last_qualification,phone_number,course,final_marks,adhar_number,dob,address;
    private Button next_btn;
    private SharedPreferences_SVU sharedPreferences_svu;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admission,container,false);

        sharedPreferences_svu = SharedPreferences_SVU.getInstance(getActivity());
        name = view.findViewById(R.id.name);
        email_id = view.findViewById(R.id.email_id);
        father_name = view.findViewById(R.id.father_name);
        phone_number = view.findViewById(R.id.phone_number);
        adhar_number = view.findViewById(R.id.adhar_number);
        address = view.findViewById(R.id.address);
        dob = view.findViewById(R.id.dob);
        course = view.findViewById(R.id.course);
        final_marks= view.findViewById(R.id.final_marks);
        last_qualification = view.findViewById(R.id.last_qualification);
        next_btn = view.findViewById(R.id.next_btn);


        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                admission_query();
            }
        });

        sharedPreferences_svu.setFrom("");
        return view;
    }

    private void admission_query() {
        if (!((Dashboard)getActivity()).isloadershowing())
            ((Dashboard)getActivity()).showLoader();
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
                                startActivity(new Intent(getActivity(), Dashboard.class));

                        }
                        catch (
                                Exception e) {
                            e.getMessage();
                            e.printStackTrace();
                        } finally {
                            ((Dashboard)getActivity()).dismissLoader();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.getResponse();
                        ((Dashboard)getActivity()).dismissLoader();
                    }

                });
    }



}
