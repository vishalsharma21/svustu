package com.ritara.svustudent.ui.help;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ritara.svustudent.R;

public class HelpFragment extends Fragment {
    private HelpViewModel helpViewModel;
    private TextView version,build,release,email,number;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        helpViewModel =
                ViewModelProviders.of(this).get(HelpViewModel.class);
        View root = inflater.inflate(R.layout.fragment_help, container, false);
        version = (TextView) root.findViewById(R.id.version_no);
        build = (TextView) root.findViewById(R.id.build_no);
        release = (TextView) root.findViewById(R.id.release_date);
        email = (TextView) root.findViewById(R.id.email_id);
        number = (TextView) root.findViewById(R.id.number);


        return root;
    }
}
