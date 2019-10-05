package com.ritara.svustudent.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ritara.svustudent.R;


public class ScholarshipFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scholarship, container, false);

        ((TextView)view.findViewById(R.id.txtScho1)).setText(getString(R.string.scholarship_1));
        ((TextView)view.findViewById(R.id.txtScho2)).setText(getString(R.string.scholarship_2));
        ((TextView)view.findViewById(R.id.txtScho3)).setText(getString(R.string.scholarship_3));
        ((TextView)view.findViewById(R.id.txtScho4)).setText(getString(R.string.scholarship_4));
        ((TextView)view.findViewById(R.id.txtScho5)).setText(getString(R.string.scholarship_5));
        ((TextView)view.findViewById(R.id.txtScho6)).setText(getString(R.string.scholarship_6));
        ((TextView)view.findViewById(R.id.txtScho7)).setText(getString(R.string.scholarship_7));


        return view;
    }

}
