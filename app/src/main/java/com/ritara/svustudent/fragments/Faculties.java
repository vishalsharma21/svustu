package com.ritara.svustudent.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ritara.svustudent.Dashboard;
import com.ritara.svustudent.MyMarksAdapter;
import com.ritara.svustudent.R;
import com.ritara.svustudent.utils.FeeModel;
import com.ritara.svustudent.utils.SharedPreferences_SVU;

import org.json.JSONObject;

import java.util.ArrayList;


public class Faculties extends Fragment {
    private SharedPreferences_SVU sharedPreferences_svu;
    private FaculitiesViewModel mViewModel;
    private RecyclerView rcMarks;
    private ArrayList<FeeModel> faculitiesModels;
    private int mColumnCount = 1;
    private NewsFragment.OnListFragmentInteractionListener mListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faculties, container, false);

        sharedPreferences_svu = SharedPreferences_SVU.getInstance(getActivity());
        rcMarks = (RecyclerView) view.findViewById(R.id.rcMarks);
        faculitiesModels = new ArrayList<>();

        GetNotice();

        return view;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(ArrayList<FeeModel> item);
    }

    private void GetNotice() {
        if (!((Dashboard)getActivity()).isloadershowing())
            ((Dashboard)getActivity()).showLoader();
        AndroidNetworking.post("http://solutionsdot-com.in/SVU_api/svu_api.php/")
                .addBodyParameter("rule", "faculties")
                .addBodyParameter("course", "" +sharedPreferences_svu.getCourse() )
                .setTag("login")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ((Dashboard)getActivity()).dismissLoader();
                            for (int i = 0; i < response.getJSONArray("data").length(); i++) {
                                FeeModel faculitiesModel = new FeeModel();
                                JSONObject object = response.getJSONArray("data").getJSONObject(i);
                                faculitiesModel.setName(object.getString("name") + " For " + object.getString("course"));
                                faculitiesModel.setFac_name(object.getString("course"));
                                faculitiesModel.setEmp_id(object.getString("emp_id"));

                                faculitiesModels.add(faculitiesModel);
                            }
                            if (mColumnCount <= 1) {
                                rcMarks.setLayoutManager(new LinearLayoutManager(getActivity()));
                            } else {
                                rcMarks.setLayoutManager(new GridLayoutManager(getActivity(), mColumnCount));
                            }
                            rcMarks.setAdapter(new MyMarksAdapter(faculitiesModels));
                        }
                        catch (Exception e) {
                            e.getMessage();
                            e.printStackTrace();
                            ((Dashboard)getActivity()).dismissLoader();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        ((Dashboard)getActivity()).dismissLoader();
                    }
                });
    }












}
