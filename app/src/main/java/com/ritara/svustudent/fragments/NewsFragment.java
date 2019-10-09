package com.ritara.svustudent.fragments;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ritara.svustudent.Dashboard;
import com.ritara.svustudent.MyMarksAdapter;
import com.ritara.svustudent.R;
import com.ritara.svustudent.utils.FeeModel;
import com.ritara.svustudent.utils.SharedPreferences_SVU;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsFragment extends Fragment {

    private NewsViewModel mViewModel;
    private RecyclerView rcMarks;
    private ArrayList<FeeModel> feeModels;
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private SharedPreferences_SVU sharedPreferences_svu;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_fragment, container, false);

        rcMarks = (RecyclerView) view.findViewById(R.id.rcMarks);
        sharedPreferences_svu = SharedPreferences_SVU.getInstance(getActivity());
        feeModels = new ArrayList<>();

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
                .addBodyParameter("rule", "get_notice")
                .addBodyParameter("type", "0")
//                .addBodyParameter("course", "" + sharedPreferences_svu.getCourse())
                .setTag("login")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ((Dashboard)getActivity()).dismissLoader();
                            for (int i = 0; i < response.getJSONArray("result").length(); i++) {
                                FeeModel feeModel = new FeeModel();
                                JSONObject object = response.getJSONArray("result").getJSONObject(i);
                                feeModel.setName(object.getString("message"));
                                feeModel.setFName(object.getString("from_name"));
                                feeModel.setTransDate(object.getString("date"));

                                feeModels.add(feeModel);
                            }
                            if (mColumnCount <= 1) {
                                rcMarks.setLayoutManager(new LinearLayoutManager(getActivity()));
                            } else {
                                rcMarks.setLayoutManager(new GridLayoutManager(getActivity(), mColumnCount));
                            }
                            rcMarks.setAdapter(new MyMarksAdapter(feeModels, getActivity()));
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
