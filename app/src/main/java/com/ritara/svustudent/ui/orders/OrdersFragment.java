package com.ritara.svustudent.ui.orders;

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

import com.ritara.svustudent.Login;
import com.ritara.svustudent.R;
import com.ritara.svustudent.utils.SharedPreferences_SVU;

public class OrdersFragment extends Fragment{
        private OrdersViewModel ordersViewModel;

        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            ordersViewModel =
                    ViewModelProviders.of(this).get(OrdersViewModel.class);
            View root = inflater.inflate(R.layout.fragment_orders, container, false);

            if(SharedPreferences_SVU.getInstance(getActivity()).get_Logged()){

            }else {
                startActivity(new Intent(getActivity(), Login.class));
            }

            return root;
        }
}

