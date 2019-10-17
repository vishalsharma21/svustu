package com.ritara.svustudent;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.hbb20.CountryCodePicker;
import com.ritara.svustudent.utils.SharedPreferences_SVU;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.ritara.svustudent.utils.Constants.BASE_URL;
import static com.ritara.svustudent.utils.Constants.EMAIL;
import static com.ritara.svustudent.utils.Constants.MOBILE;
import static com.ritara.svustudent.utils.Constants.RULE;
import static com.ritara.svustudent.utils.Constants.SEND_OTP;
import static com.ritara.svustudent.utils.Constants.TYPE;

public class Register extends BaseActivity {
    private CountryCodePicker ccp;
    private String countryCodeAndroid = "91";
    private EditText name, email_id, phone_number;
    private TextView support;
    private Spinner spinner_role;
    private Button next;
    int type = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        name = findViewById(R.id.name);
        email_id = findViewById(R.id.email_id);
        phone_number = findViewById(R.id.phone_number);
        spinner_role = findViewById(R.id.role);

        spinner_role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterView.getSelectedItem().toString().equalsIgnoreCase("Alumini")
                || adapterView.getSelectedItem().toString().equalsIgnoreCase("Existing Student")){
                    type = 1;
                }else{
                    type = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        findViewById(R.id.next_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type == 1){
                    Intent intent = new Intent(Register.this, Login.class);
                    intent.putExtra(TYPE, "admission");
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(Register.this, AdmissionActivity.class);
                    intent.putExtra(TYPE, "admission");
                    startActivity(intent);
                    SharedPreferences_SVU.getInstance(Register.this).setFrom("admission");
                }
                finish();
//                sendOtp();
            }
        });
    }

    public void onCountryPickerClick(View view) {
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                //Alert.showMessage(RegistrationActivity.this, ccp.getSelectedCountryCodeWithPlus());
                countryCodeAndroid = ccp.getSelectedCountryCodeWithPlus();
            }
        });
    }

    private void sendOtp() {
        if (!isloadershowing())
            showLoader();
        AndroidNetworking.post(BASE_URL)
                .addBodyParameter(RULE, SEND_OTP)
                .addBodyParameter(MOBILE, phone_number.getText().toString())
                .addBodyParameter(EMAIL, email_id.getText().toString())
                .setTag(SEND_OTP)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equalsIgnoreCase("1")) {
                                Toast.makeText(Register.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Register.this, Verify.class);
                                intent.putExtra("name", name.getText().toString());
                                intent.putExtra("email_id", email_id.getText().toString());
                                intent.putExtra("phone_number", phone_number.getText().toString());
                                startActivity(intent);
                            }
                        } catch (Exception e) {
                            e.getMessage();
                            e.printStackTrace();
                        } finally {
                            dismissLoader();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dismissLoader();
                        anError.getErrorCode();
                    }


                });
    }

}
