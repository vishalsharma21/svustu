package com.ritara.svustudent.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ritara.svustudent.Login;
import com.ritara.svustudent.R;
import com.ritara.svustudent.SplashScreenActivity;
import com.ritara.svustudent.utils.SharedPreferences_SVU;

public class AccountFragment extends Fragment {
    private SharedPreferences_SVU sharedPreferences_svu;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_account, container, false);

        sharedPreferences_svu = SharedPreferences_SVU.getInstance(getActivity());
        Button btnLogOut = root.findViewById(R.id.btnLogOut);
        if(sharedPreferences_svu.get_Logged()){
            btnLogOut.setText("Logout");
        }else {
            btnLogOut.setText("Login");
        }

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sharedPreferences_svu.get_Logged()){
                    sharedPreferences_svu.remove_Preference();
                    startActivity(new Intent(getActivity(), SplashScreenActivity.class));
                }else {
                    startActivity(new Intent(getActivity(), Login.class));
                }

            }
        });
        return root;
    }
}
