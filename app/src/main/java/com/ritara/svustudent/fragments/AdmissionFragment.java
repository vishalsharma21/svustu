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
        View view = inflater.inflate(R.layout.fragment_admission,container,false);
        name = view.findViewById(R.id.name);
        email_id = view.findViewById(R.id.email_id);
        father_name = view.findViewById(R.id.father_name);
        mother_name = view.findViewById(R.id.mother_name);
        phone_number = view.findViewById(R.id.phone_number);
        batch = view.findViewById(R.id.name);
        name = view.findViewById(R.id.name);
        name = view.findViewById(R.id.name);




        return view;
    }





}
