package com.ritara.svustudent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ritara.svustudent.utils.SharedPreferences_SVU;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.ritara.svustudent.utils.Constants.BASE_URL;
import static com.ritara.svustudent.utils.Constants.DEVICE_ID;
import static com.ritara.svustudent.utils.Constants.EMAIL;
import static com.ritara.svustudent.utils.Constants.MOBILE;
import static com.ritara.svustudent.utils.Constants.NAME;
import static com.ritara.svustudent.utils.Constants.PASSWORD;
import static com.ritara.svustudent.utils.Constants.RULE;
import static com.ritara.svustudent.utils.Constants.STUDENT_SIGN;

public class Password extends BaseActivity {
    private String name, phone_number, email_id;
    private EditText pass, cnfrmpass;
    SharedPreferences_SVU sharedPreferences_svu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password);
        name = getIntent().getStringExtra("name");
        email_id = getIntent().getStringExtra("email_id");
        phone_number = getIntent().getStringExtra("phone_number");
        pass = findViewById(R.id.pass);
        cnfrmpass = findViewById(R.id.cnfrmpass);
        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pass.getText().toString().trim().equalsIgnoreCase(cnfrmpass.getText().toString().trim())) {
                    register();
                } else {
                    Toast.makeText(Password.this, "Password and Confirm Password doesn't match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void register() {
        if (!isloadershowing())
            showLoader();
        AndroidNetworking.post(BASE_URL)
                .addBodyParameter(RULE, STUDENT_SIGN)
                .addBodyParameter(PASSWORD, "" +pass )
                .addBodyParameter(NAME, "" + name)
                .addBodyParameter(MOBILE, "" + phone_number)
                .addBodyParameter(EMAIL, "" + email_id)
                .addBodyParameter(DEVICE_ID, sharedPreferences_svu.getTokenIdForFirebase())
                .setTag(STUDENT_SIGN)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equalsIgnoreCase("1")) {
                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);

                            sharedPreferences_svu.setUserId(jsonObject1.getString("id"));
                            sharedPreferences_svu.set_Username(jsonObject1.getString("name"));
                            sharedPreferences_svu.set_email(jsonObject1.getString("email"));
                            sharedPreferences_svu.set_phone(jsonObject1.getString("contact"));
                            sharedPreferences_svu.setAddress(jsonObject1.getString("address"));
                            sharedPreferences_svu.set_deviceId(jsonObject1.getString("device_id"));
                            sharedPreferences_svu.set_Logged(true);
                            startActivity(new Intent(Password.this, Login.class));
                            finish();

                            }
                        }
                        }
                        catch (
                                Exception e) {
                            e.getMessage();
                            e.printStackTrace();
                        } finally {
                            dismissLoader();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }

});}}


