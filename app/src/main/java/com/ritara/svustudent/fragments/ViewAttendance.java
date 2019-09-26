package com.ritara.svustudent.fragments;

import android.content.Context;
import android.graphics.Color;
import android.hardware.camera2.params.ColorSpaceTransform;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.ritara.svustudent.R;

import java.util.ArrayList;

import static com.github.mikephil.charting.utils.ColorTemplate.COLORFUL_COLORS;


public class ViewAttendance extends Fragment {

private BarChart today_attendance , avg_attendance;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_attendance, container, false);
        today_attendance = (BarChart) view.findViewById(R.id.today_attendance);
        avg_attendance = (BarChart) view.findViewById(R.id.avg_attendance);


        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 0));
        entries.add(new BarEntry(5f, 1));
        entries.add(new BarEntry(10f, 2));
        entries.add(new BarEntry(15f, 3));
        entries.add(new BarEntry(20f, 4));

        BarDataSet barDataSet = new BarDataSet(entries, "Subjects");

        ArrayList<String> labels = new ArrayList<>();
            labels.add("SVU001");
            labels.add("SVU002");
            labels.add("SVU003");
            labels.add("SVU004");
            labels.add("SVU005");
//            BarData data = new BarData(labels , barDataSet);
//        today_attendance.setData(data);
        barDataSet.setColor(getActivity().getColor(R.color.black));
        today_attendance.animateY(5000);
        today_attendance.invalidate();


        ArrayList<BarEntry> average = new ArrayList<>();
        average.add(new BarEntry(0f, 0));
        average.add(new BarEntry(5f, 1));
        average.add(new BarEntry(10f, 2));
        average.add(new BarEntry(15f, 3));
        average.add(new BarEntry(20f, 4));

        BarDataSet barDataSet1 = new BarDataSet(entries, "Subjects");
        ArrayList<String> labels_avg = new ArrayList<>();
        labels_avg.add("SVU001");
        labels_avg.add("SVU002");
        labels_avg.add("SVU003");
        labels_avg.add("SVU004");
        labels_avg.add("SVU005");
//        BarData avg_data = new BarData(labels_avg, barDataSet1);
//        avg_attendance.setData(data);
        barDataSet1.setColor(getActivity().getColor(R.color.black));
        avg_attendance.animateY(5000);
        avg_attendance.invalidate();


        return view;
    }
}
