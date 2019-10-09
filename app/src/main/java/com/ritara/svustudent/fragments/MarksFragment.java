package com.ritara.svustudent.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.ritara.svustudent.utils.Constants;
import com.ritara.svustudent.utils.FeeModel;
import com.ritara.svustudent.utils.SharedPreferences_SVU;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;
import static com.ritara.svustudent.utils.Constants.KEY;

public class MarksFragment extends Fragment {
    private SharedPreferences_SVU sharedPreferences_svu;
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private ArrayList<FeeModel> feeModels;
    private View view;
    int debit = 0, credit = 0;
    private TextView txtDebit, txtCredit;
    RecyclerView rcMarks;
    private OnListFragmentInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_marks_list, container, false);

        sharedPreferences_svu = SharedPreferences_SVU.getInstance(getActivity());
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
        AndroidNetworking.post("http://svu.svu.edu.in/svustuservice.asmx/GetMarksheetInfo?EnrollNo="+sharedPreferences_svu.getUserId()+"&key=rky8UCIdFnfFUVzS8MC9zWVxI1ktu4ht/hO0msS+rSE")
                .addBodyParameter("EnrollNo", ""+sharedPreferences_svu.getUserId())
                .addBodyParameter("key", "rky8UCIdFnfFUVzS8MC9zWVxI1ktu4ht/hO0msS+rSE")
                .setTag("login")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if(response.length()>0) {
                                for (int i = 0; i < response.length(); i++) {
                                    FeeModel feeModel = new FeeModel();
                                    JSONObject object = response.getJSONObject(i);
                                    feeModel.setName("Marks : " + object.getString("YearSem"));
                                    feeModels.add(feeModel);
                                }
                                if (mColumnCount <= 1) {
                                    rcMarks.setLayoutManager(new LinearLayoutManager(getActivity()));
                                } else {
                                    rcMarks.setLayoutManager(new GridLayoutManager(getActivity(), mColumnCount));
                                }
                                rcMarks.setAdapter(new MyMarksAdapter(feeModels, getActivity()));
                            }else{
                                Toast.makeText(getActivity(), "No data found.", Toast.LENGTH_SHORT).show();
                            }
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
