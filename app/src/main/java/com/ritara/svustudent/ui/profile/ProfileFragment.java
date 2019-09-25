package com.ritara.svustudent.ui.profile;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ritara.svustudent.Dashboard;
import com.ritara.svustudent.R;
import com.ritara.svustudent.fragments.PaidFeeFragment;
import com.ritara.svustudent.ui.home.HomeFragment;
import com.ritara.svustudent.utils.SharedPreferences_SVU;

public class ProfileFragment extends Fragment {

    private SharedPreferences_SVU sharedPreferences_svu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        sharedPreferences_svu = SharedPreferences_SVU.getInstance(getActivity());

        ((TextView)view.findViewById(R.id.txtName)).setText(sharedPreferences_svu.get_Username());
        ((TextView)view.findViewById(R.id.txtEmail)).setText(sharedPreferences_svu.get_email());

        view.findViewById(R.id.rlFees).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Dashboard)getActivity()).changeFragment(new PaidFeeFragment() , "My Fees");
            }
        });
        return view;
    }

}
