package com.ritara.svustudent.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.ritara.svustudent.Dashboard;
import com.ritara.svustudent.Login;
import com.ritara.svustudent.Payments;
import com.ritara.svustudent.R;
import com.ritara.svustudent.fragments.dummy.DummyContent;
import com.ritara.svustudent.fragments.dummy.DummyContent.DummyItem;
import com.ritara.svustudent.utils.FeeModel;
import com.ritara.svustudent.utils.SharedPreferences_SVU;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static com.ritara.svustudent.utils.Constants.ENo;
import static com.ritara.svustudent.utils.Constants.GET_FEE_INFO;
import static com.ritara.svustudent.utils.Constants.KEY;
import static com.ritara.svustudent.utils.Constants.KEY_NAME;
import static com.ritara.svustudent.utils.Constants.SVU_BASE_URL;

public class PaidFeeFragment extends Fragment {

    private int mColumnCount = 1;
    private ArrayList<FeeModel> feeModels;
    private View view;
    double debit = 0, credit = 0;
    private TextView txtDebit, txtCredit;
    private RecyclerView recyclerView;
    private ImageView makepayment;
    private SharedPreferences_SVU sharedPreferences_svu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_paidfee_list, container, false);

        if(!SharedPreferences_SVU.getInstance(getActivity()).get_Logged()){
            startActivity(new Intent(getActivity(), Login.class));
        }else {
            sharedPreferences_svu = SharedPreferences_SVU.getInstance(getActivity());
            txtCredit = (TextView) view.findViewById(R.id.txtCredit);
            txtDebit = (TextView) view.findViewById(R.id.txtDebit);
            makepayment = (ImageView) view.findViewById(R.id.make_payment);
            recyclerView = (RecyclerView) view.findViewById(R.id.list);
            feeModels = new ArrayList<>();

            makepayment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), Payments.class));
                }
            });

            GetFeeInfo(view);
        }

        return view;
    }


    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(ArrayList<FeeModel> item);
    }

    private void GetFeeInfo(final View view) {
        if (!((Dashboard)getActivity()).isloadershowing())
            ((Dashboard)getActivity()).showLoader();
        AndroidNetworking.post(SVU_BASE_URL+GET_FEE_INFO+"?"+ENo+"="+sharedPreferences_svu.getUserId()+"8&"+KEY_NAME+"="+KEY)
                .addBodyParameter(ENo, ""+sharedPreferences_svu.getUserId())
                .addBodyParameter(KEY_NAME, KEY)
                .setTag("Fee")
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
                                    debit = Double.parseDouble(jsonObject1.getString("Debit")) + debit;
                                }catch (Exception e){

                                }

                                try {
                                    credit = Double.parseDouble(jsonObject1.getString("Credit")) + credit;
                                }catch (Exception e){

                                }

                                feeModels.add(feeModel);
                            }

                            txtCredit.setText("Total Credit : " + credit);
                            txtDebit.setText("Total Debit : " + debit);
                            ((TextView)view.findViewById(R.id.txtDue)).setText("Due amount : " + (debit-credit));
                                Context context = PaidFeeFragment.this.view.getContext();
                                if (mColumnCount <= 1) {
                                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                } else {
                                    recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                                }
                                recyclerView.setAdapter(new MyPaidFeeRecyclerViewAdapter(feeModels));
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
