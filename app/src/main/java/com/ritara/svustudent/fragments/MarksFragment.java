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
        view = inflater.inflate(R.layout.fragment_paidfee_list, container, false);

        txtCredit = (TextView) view.findViewById(R.id.txtCredit);
        txtCredit = (TextView) view.findViewById(R.id.txtCredit);

        feeModels = new ArrayList<>();

        GetFeeInfo();

        // Set the adapter

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

//    @Override
//    public void onListFragmentInteraction(ArrayList<FeeModel> item) {
//
//    }


    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(ArrayList<FeeModel> item);
    }

    private void GetFeeInfo() {
        if (!((Dashboard)getActivity()).isloadershowing())
            ((Dashboard)getActivity()).showLoader();
        AndroidNetworking.post("http://svu.svu.edu.in/svustuservice.asmx/GetFeeInfo?EnrollNo=SET14A00030058&key=rky8UCIdFnfFUVzS8MC9zWVxI1ktu4ht/hO0msS+rSE")
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

                                JSONObject jsonObject1 = (JSONObject) response.get(i);

                                feeModel.setBranch(jsonObject1.getString("Branch"));
                                feeModel.setCourse(jsonObject1.getString("Course"));
                                feeModel.setCredit(jsonObject1.getString("Credit"));
                                feeModel.setDebit(jsonObject1.getString("Debit"));
                                feeModel.setDescription(jsonObject1.getString("Description"));
                                feeModel.setEnrollNo(jsonObject1.getString("EnrollNo"));
                                feeModel.setFName(jsonObject1.getString("FName"));
                                feeModel.setReceiptNo(jsonObject1.getString("ReceiptNo"));
                                feeModel.setTransDate(jsonObject1.getString("TransDate"));
                                feeModel.setName(jsonObject1.getString("Name"));

                                try {
                                    debit = Integer.parseInt(jsonObject1.getString("Debit")) + debit;
                                }catch (Exception e){

                                }

                                try {
                                    credit = Integer.parseInt(jsonObject1.getString("Credit")) + credit;
                                }catch (Exception e){

                                }

                                feeModels.add(feeModel);
                            }

                            txtCredit.setText("Total Credit : " + credit);
                            txtDebit.setText("Total Debit : " + debit);

                            /*if (view instanceof RecyclerView) {
                                Context context = view.getContext();
                                RecyclerView recyclerView = (RecyclerView) view;
                                if (mColumnCount <= 1) {
                                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                } else {
                                    recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                                }
                                recyclerView.setAdapter(new MyPaidFeeRecyclerViewAdapter(feeModels, mListener));
                            }*/

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
