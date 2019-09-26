package com.ritara.svustudent;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONObject;

public class Register extends BaseActivity {
    private CountryCodePicker ccp;
    private String countryCodeAndroid = "91";
    private EditText name, email_id, phone_number;
    private TextView support;
    private Spinner spinner_role;
    private Button next;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        name = findViewById(R.id.name);
        email_id = findViewById(R.id.email_id);
        phone_number = findViewById(R.id.phone_number);
        findViewById(R.id.next_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOtp();

            }
        });
        spinner_role = findViewById(R.id.role);
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
        AndroidNetworking.post("solutionsdot-com.in/SVU_api/svu_api.php")
                .addBodyParameter("rule", "sendotp")
                .addBodyParameter("mobile", phone_number.getText().toString())
                .addBodyParameter("email", email_id.getText().toString())
                .setTag("register")
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
