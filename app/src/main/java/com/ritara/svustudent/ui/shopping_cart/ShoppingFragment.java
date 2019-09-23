package com.ritara.svustudent.ui.shopping_cart;

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

public class ShoppingFragment extends Fragment {
    private ShoppingViewModel shoppingViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shoppingViewModel =
                ViewModelProviders.of(this).get(ShoppingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_shopping, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        shoppingViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }}