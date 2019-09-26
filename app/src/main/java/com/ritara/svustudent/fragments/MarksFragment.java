package com.ritara.svustudent.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.ritara.svustudent.Dashboard;
import com.ritara.svustudent.MyMarksAdapter;
import com.ritara.svustudent.R;
import com.ritara.svustudent.utils.FeeModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MarksFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private ArrayList<FeeModel> feeModels;
    private View view;
    int debit = 0, credit = 0;
    private TextView txtDebit, txtCredit;
    RecyclerView rcMarks;
    private OnListFragmentInteractionListener mListener;

    public MarksFragment() {
    }

    @SuppressWarnings("unused")
    public static MarksFragment newInstance(int columnCount) {
        MarksFragment fragment = new MarksFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_marks_list, container, false);

        rcMarks = (RecyclerView) view.findViewById(R.id.rcMarks);

        feeModels = new ArrayList<>();
        GetMarksInfo();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(ArrayList<FeeModel> item);
    }

    private void GetMarksInfo() {
        if (!((Dashboard)getActivity()).isloadershowing())
            ((Dashboard)getActivity()).showLoader();
        AndroidNetworking.post("http://svu.svu.edu.in/svustuservice.asmx/GetMarksheetInfo?EnrollNo=SET14A00030058&key=rky8UCIdFnfFUVzS8MC9zWVxI1ktu4ht/hO0msS+rSE")
                .addBodyParameter("EnrollNo", "SET14A00030058")
                .addBodyParameter("key", "rky8UCIdFnfFUVzS8MC9zWVxI1ktu4ht/hO0msS+rSE")
                .setTag("login")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                FeeModel feeModel = new FeeModel();
                                JSONObject object = response.getJSONObject(i);
                                feeModel.setName(object.getString("YearSem"));
                                feeModels.add(feeModel);

                            }

                                if (mColumnCount <= 1) {
                                    rcMarks.setLayoutManager(new LinearLayoutManager(getActivity()));
                                } else {
                                    rcMarks.setLayoutManager(new GridLayoutManager(getActivity(), mColumnCount));
                                }
                            rcMarks.setAdapter(new MyMarksAdapter(feeModels, mListener));

                        }
                        catch (
                                Exception e) {
                            e.getMessage();
                            e.printStackTrace();
                        } finally {
                            ((Dashboard)getActivity()).dismissLoader();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.getResponse();
                        ((Dashboard)getActivity()).dismissLoader();
                    }

                });
    }

}
