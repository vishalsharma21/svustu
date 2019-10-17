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

import static com.ritara.svustudent.utils.Constants.BASE_URL;
import static com.ritara.svustudent.utils.Constants.EMAIL;
import static com.ritara.svustudent.utils.Constants.EMAIL_ID;
import static com.ritara.svustudent.utils.Constants.MOBILE;
import static com.ritara.svustudent.utils.Constants.NAME;
import static com.ritara.svustudent.utils.Constants.OTP;
import static com.ritara.svustudent.utils.Constants.PHONE_NUM;
import static com.ritara.svustudent.utils.Constants.RULE;
import static com.ritara.svustudent.utils.Constants.SEND_OTP;
import static com.ritara.svustudent.utils.Constants.STATUS;
import static com.ritara.svustudent.utils.Constants.VERIFYOTP;

public class Verify extends BaseActivity {
    private String name, phone_number, email_id;
    private OtpView otp_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify);

        otp_view = findViewById(R.id.otp_view);
        name = getIntent().getStringExtra(NAME);
        email_id = getIntent().getStringExtra(EMAIL_ID);
        phone_number = getIntent().getStringExtra(PHONE_NUM);
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
        AndroidNetworking.post(BASE_URL)
                .addBodyParameter(RULE, VERIFYOTP)
                .addBodyParameter(OTP, "" + otp_view.getOTP())
                .addBodyParameter(MOBILE, "" + phone_number)
                .setTag(VERIFYOTP)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("res").equalsIgnoreCase("1")) {
                                Toast.makeText(Verify.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Verify.this, Password.class);
                                intent.putExtra(NAME, name);
                                intent.putExtra(EMAIL_ID, email_id);
                                intent.putExtra(PHONE_NUM, phone_number);
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
        AndroidNetworking.post(BASE_URL)
                .addBodyParameter(RULE, SEND_OTP)
                .addBodyParameter(MOBILE, phone_number)
                .addBodyParameter(EMAIL, email_id)
                .setTag(SEND_OTP)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString(STATUS).equalsIgnoreCase("1")) {
                                Toast.makeText(Verify.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Verify.this, Verify.class);
                                intent.putExtra(NAME, name);
                                intent.putExtra(EMAIL_ID, email_id);
                                intent.putExtra(PHONE_NUM, phone_number);
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
