package com.ritara.svustudent.ui.help;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ritara.svustudent.Adapters.AttendanceAdapter;
import com.ritara.svustudent.Dashboard;
import com.ritara.svustudent.MainActivity;
import com.ritara.svustudent.R;
import com.ritara.svustudent.utils.Model;
import com.ritara.svustudent.utils.SharedPreferences_SVU;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.ritara.svustudent.utils.Constants.AMOUNT;
import static com.ritara.svustudent.utils.Constants.BASE_URL;
import static com.ritara.svustudent.utils.Constants.COURSE;
import static com.ritara.svustudent.utils.Constants.DESCRIPTION;
import static com.ritara.svustudent.utils.Constants.ENROLL_NO;
import static com.ritara.svustudent.utils.Constants.GET_ATTENDANCE;
import static com.ritara.svustudent.utils.Constants.GRIEVANCES;
import static com.ritara.svustudent.utils.Constants.MOBILE_NUMBER;
import static com.ritara.svustudent.utils.Constants.NAME;
import static com.ritara.svustudent.utils.Constants.PAY_FEES;
import static com.ritara.svustudent.utils.Constants.RULE;
import static com.ritara.svustudent.utils.Constants.TR_ID;
import static com.ritara.svustudent.utils.Constants.TYPE;
import static com.ritara.svustudent.utils.Constants.USER_ID;
import static com.ritara.svustudent.utils.Constants.WRONG_MESSAGE;

public class HelpFragment extends Fragment {
    private TextView version,build,release,email,number;
    private String type;
    private SharedPreferences_SVU sharedPreferences_svu;
    private Dialog openHelpDialog;
    private ArrayList<Model> faculitiesModels;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_help, container, false);

        version = (TextView) root.findViewById(R.id.version_no);
        build = (TextView) root.findViewById(R.id.build_no);
        release = (TextView) root.findViewById(R.id.release_date);
        email = (TextView) root.findViewById(R.id.email_id);
        number = (TextView) root.findViewById(R.id.number);
        sharedPreferences_svu = SharedPreferences_SVU.getInstance(getActivity());
        faculitiesModels = new ArrayList<>();

        root.findViewById(R.id.next_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHelpDialog();
            }
        });

//        getGrievances();

        return root;
    }

//    private void getGrievances() {
//        if (!((Dashboard)getActivity()).isloadershowing())
//            ((Dashboard)getActivity()).showLoader();
//        AndroidNetworking.post(BASE_URL)
//                .addBodyParameter(RULE, GET_ATTENDANCE)
//                .addBodyParameter(ENROLL_NO, "" +sharedPreferences_svu.getUserId())
//                .setTag(GET_ATTENDANCE)
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            ((Dashboard)getActivity()).dismissLoader();
//                            for (int i = 0; i < response.getJSONArray("data").length(); i++) {
//                                Model model = new Model();
//                                JSONObject object = response.getJSONArray("data").getJSONObject(i);
//                                model.setDate(object.getString("date"));
//                                model.setAttendance(object.getString("attendance"));
//
//                                faculitiesModels.add(model);
//                            }
//                            if (mColumnCount <= 1) {
//                                rcAtt.setLayoutManager(new LinearLayoutManager(getActivity()));
//                            } else {
//                                rcAtt.setLayoutManager(new GridLayoutManager(getActivity(), mColumnCount));
//                            }
//                            rcAtt.setAdapter(new AttendanceAdapter(faculitiesModels));
//                        }
//                        catch (Exception e) {
//                            e.getMessage();
//                            e.printStackTrace();
//                            ((Dashboard)getActivity()).dismissLoader();
//                        }
//                    }
//                    @Override
//                    public void onError(ANError anError) {
//                        ((Dashboard)getActivity()).dismissLoader();
//                    }
//                });
//    }

    private void openHelpDialog() {
        final String spin = "";
        openHelpDialog = new Dialog(getActivity(),
                android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
        openHelpDialog.setContentView(R.layout.grievance_dialog);
        openHelpDialog.setCancelable(true);

        ArrayList<Model> faculitiesModels = new ArrayList<>();
        RecyclerView rcyklrGrievance = openHelpDialog.findViewById(R.id.rcyklrGrievance);

        openHelpDialog.findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etBody = openHelpDialog.findViewById(R.id.etBody);
                String body = etBody.getText().toString().trim();
                if(body.length()>0) {
                    ((Dashboard)getActivity()).showLoader();
                    sendGrievance(type, body);
                }else{
                    Toast.makeText(getActivity(), "Enter details.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Spinner spnrType = (Spinner) openHelpDialog.findViewById(R.id.spnrType);//fetch the spinner from layout file
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, getResources()
                .getStringArray(R.array.grievance_array));//setting the country_array to spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrType.setAdapter(adapter);

        spnrType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                type = arg0.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        openHelpDialog.show();
        Window window = openHelpDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }

    private void sendGrievance(String type, String body) {
        AndroidNetworking.post(BASE_URL)
                .addBodyParameter(RULE, GRIEVANCES)
                .addBodyParameter(ENROLL_NO, "" + (sharedPreferences_svu.getUserId()))
                .addBodyParameter(NAME, "" + (sharedPreferences_svu.get_Username()))
                .addBodyParameter(DESCRIPTION, "" + body)
                .addBodyParameter(TYPE, type)
                .addBodyParameter(COURSE, sharedPreferences_svu.getCourse())
                .setTag(GRIEVANCES)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ((Dashboard)getActivity()).dismissLoader();
                        try{
                            if (response.getString("status").equals("1")) {
                                openHelpDialog.dismiss();
                                ((Dashboard)getActivity()).showToast("Grievance sent to concern dept.");
//                                Toast.makeText(getActivity(), "Grievance sent to concern dept.", Toast.LENGTH_SHORT).show();
                            } else {
                                ((Dashboard)getActivity()).showToast(WRONG_MESSAGE);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            openHelpDialog.dismiss();
                            ((Dashboard)getActivity()).showToast(WRONG_MESSAGE);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        error.getResponse();
                        ((Dashboard)getActivity()).dismissLoader();
                        openHelpDialog.dismiss();
                        ((Dashboard)getActivity()).showToast(WRONG_MESSAGE);
                    }
                });
    }
}
