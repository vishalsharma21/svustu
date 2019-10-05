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

//            .addBodyParameter("mob_email", "" + username)
//            .addBodyParameter("device_id", sharedPreferences_svu.getTokenIdForFirebase())
                .setTag("admission_query")
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
