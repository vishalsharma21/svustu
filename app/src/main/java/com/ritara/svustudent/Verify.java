package com.ritara.svustudent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.mukesh.OtpView;

import org.json.JSONArray;
import org.json.JSONObject;

public class Verify extends BaseActivity {
    private String name, phone_number, email_id;
    private OtpView otp_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify);
        otp_view = findViewById(R.id.otp_view);
        name = getIntent().getStringExtra("name");
        email_id = getIntent().getStringExtra("email_id");
        phone_number = getIntent().getStringExtra("phone_number");
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.resend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               sendOtp();
            }
        });
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.verify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOtp();
            }
        });
    }

    private void verifyOtp() {
        if (!isloadershowing())
            showLoader();
        AndroidNetworking.post("solutionsdot-com.in/SVU_api/svu_api.php")
                .addBodyParameter("rule", "verify_otp")
                .addBodyParameter("otp", "" + otp_view.getOTP())
                .addBodyParameter("mobile", "" + phone_number)
                .setTag("register")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("res").equalsIgnoreCase("1")) {
                                Toast.makeText(Verify.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Verify.this, Password.class);
                                intent.putExtra("name", name);
                                intent.putExtra("email_id", email_id);
                                intent.putExtra("phone_number", phone_number);
                                startActivity(intent);
                            }
                        } catch (
                                Exception e) {
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


    private void sendOtp() {
        if (!isloadershowing())
            showLoader();
        AndroidNetworking.post("solutionsdot-com.in/SVU_api/svu_api.php")
                .addBodyParameter("rule", "sendotp")
                .addBodyParameter("mobile", phone_number)
                .addBodyParameter("email", email_id)
                .setTag("register")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equalsIgnoreCase("1")) {
                                Toast.makeText(Verify.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Verify.this, Verify.class);
                                intent.putExtra("name", name);
                                intent.putExtra("email_id", email_id);
                                intent.putExtra("phone_number", phone_number);
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
