package com.ritara.svustudent.fragments;

import android.content.Context;
import android.graphics.Color;
import android.hardware.camera2.params.ColorSpaceTransform;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.ritara.svustudent.Adapters.AttendanceAdapter;
import com.ritara.svustudent.Dashboard;
import com.ritara.svustudent.MyMarksAdapter;
import com.ritara.svustudent.R;
import com.ritara.svustudent.utils.FeeModel;
import com.ritara.svustudent.utils.Model;
import com.ritara.svustudent.utils.SharedPreferences_SVU;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.github.mikephil.charting.utils.ColorTemplate.COLORFUL_COLORS;
import static com.ritara.svustudent.utils.Constants.BASE_URL;
import static com.ritara.svustudent.utils.Constants.ENROLL_NO;
import static com.ritara.svustudent.utils.Constants.GET_ATTENDANCE;
import static com.ritara.svustudent.utils.Constants.RULE;

public class ViewAttendance extends Fragment {

    private SharedPreferences_SVU sharedPreferences_svu;
    private RecyclerView rcAtt;
    private ArrayList<Model> faculitiesModels;
    private int mColumnCount = 1;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_attendance, container, false);
        faculitiesModels = new ArrayList<>();
        rcAtt = (RecyclerView) view.findViewById(R.id.rcAtt);
        sharedPreferences_svu = SharedPreferences_SVU.getInstance(getActivity());
        GetAttendance();
        return view;
    }

    private void GetAttendance() {
        if (!((Dashboard)getActivity()).isloadershowing())
            ((Dashboard)getActivity()).showLoader();
        AndroidNetworking.post(BASE_URL)
                .addBodyParameter(RULE, GET_ATTENDANCE)
                .addBodyParameter(ENROLL_NO, "" +sharedPreferences_svu.getUserId())
                .setTag(GET_ATTENDANCE)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ((Dashboard)getActivity()).dismissLoader();
                            for (int i = 0; i < response.getJSONArray("data").length(); i++) {
                                Model model = new Model();
                                JSONObject object = response.getJSONArray("data").getJSONObject(i);
                                model.setDate(object.getString("date"));
                                model.setAttendance(object.getString("attendance"));

                                faculitiesModels.add(model);
                            }
                            if (mColumnCount <= 1) {
                                rcAtt.setLayoutManager(new LinearLayoutManager(getActivity()));
                            } else {
                                rcAtt.setLayoutManager(new GridLayoutManager(getActivity(), mColumnCount));
                            }
                            rcAtt.setAdapter(new AttendanceAdapter(faculitiesModels));
                        }
                        catch (Exception e) {
                            e.getMessage();
                            e.printStackTrace();
                            ((Dashboard)getActivity()).dismissLoader();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        ((Dashboard)getActivity()).dismissLoader();
                    }
                });
    }
}
