package com.ritara.svustudent.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.ritara.svustudent.Dashboard;
import com.ritara.svustudent.Login;
import com.ritara.svustudent.R;
import com.ritara.svustudent.fragments.PaidFeeFragment;
import com.ritara.svustudent.ui.home.HomeFragment;
import com.ritara.svustudent.utils.SharedPreferences_SVU;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProfileFragment extends Fragment {

    private SharedPreferences_SVU sharedPreferences_svu;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);

        sharedPreferences_svu = SharedPreferences_SVU.getInstance(getActivity());

        if(sharedPreferences_svu.get_Logged()) {
            ((TextView) view.findViewById(R.id.txtName)).setText(sharedPreferences_svu.get_Username());
            ((TextView) view.findViewById(R.id.txtEmail)).setText("Email : " + sharedPreferences_svu.get_email());

            ((TextView) view.findViewById(R.id.txtPAddress)).setText("Address : " + sharedPreferences_svu.getAddress());
            ((TextView) view.findViewById(R.id.txtFather)).setText("Father's Name : " + sharedPreferences_svu.get_FatherName());
            ((TextView) view.findViewById(R.id.txtMother)).setText("Mother's Name : " + sharedPreferences_svu.get_MotherName());

            view.findViewById(R.id.rlFees).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((Dashboard) getActivity()).changeFragment(new PaidFeeFragment(), "My Fees");
                }
            });

            GetAcademicInfo();
        }else {
            ((Dashboard)getActivity()).changeFragment(new HomeFragment(), "Home");
        }
        return view;
    }

    private void GetAcademicInfo() {
        if (!((Dashboard)getActivity()).isloadershowing())
            ((Dashboard)getActivity()).showLoader();
        AndroidNetworking.post("http://svu.svu.edu.in/svustuservice.asmx/GetAcademicInfo?EnrollNo=SET14A00030058&key=rky8UCIdFnfFUVzS8MC9zWVxI1ktu4ht/hO0msS+rSE")
                .addBodyParameter("EnrollNo", "SET14A00030058")
                .addBodyParameter("key", "rky8UCIdFnfFUVzS8MC9zWVxI1ktu4ht/hO0msS+rSE")
                .setTag("login")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject object = response.getJSONObject(i);
                                ((TextView)view.findViewById(R.id.txtEnroll)).setText("Roll No. : "+sharedPreferences_svu.getUserId());
                                ((TextView)view.findViewById(R.id.txtCourse)).setText("Course : "+object.getString("Course"));
                                ((TextView)view.findViewById(R.id.txtAdDate)).setText("Admission Date : "+object.getString("AdmDate"));
                                ((TextView)view.findViewById(R.id.txtLoc)).setText("Location : "+object.getString("BrLoction"));
                            }
                        }
                        catch (Exception e) {
                            e.getMessage();
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
