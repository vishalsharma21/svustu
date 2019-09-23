package com.ritara.svustudent.ui.notifications;

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


public class NotifyFragment extends Fragment {
    private NotifyViewModel notifyViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notifyViewModel =
                ViewModelProviders.of(this).get(NotifyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notify, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        notifyViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}

