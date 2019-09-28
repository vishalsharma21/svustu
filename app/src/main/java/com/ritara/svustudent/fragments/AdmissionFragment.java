package com.ritara.svustudent.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ritara.svustudent.R;

public class AdmissionFragment extends Fragment {

    private EditText name, email_id ,father_name,mother_name,phone_number,batch,course,tenth_marks,twelth_marks,adhar_number;
    private Button next_btn;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admission,container,false);
    }





}
