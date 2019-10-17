package com.ritara.svustudent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.ritara.svustudent.utils.SharedPreferences_SVU;

import org.json.JSONObject;

import static com.ritara.svustudent.utils.Constants.AMOUNT;
import static com.ritara.svustudent.utils.Constants.BASE_URL;
import static com.ritara.svustudent.utils.Constants.CONTACT;
import static com.ritara.svustudent.utils.Constants.CURRENCY;
import static com.ritara.svustudent.utils.Constants.DESCRIPTION;
import static com.ritara.svustudent.utils.Constants.EMAIL;
import static com.ritara.svustudent.utils.Constants.ENROLL_NO;
import static com.ritara.svustudent.utils.Constants.FULL_DESC;
import static com.ritara.svustudent.utils.Constants.IMAGE;
import static com.ritara.svustudent.utils.Constants.INR;
import static com.ritara.svustudent.utils.Constants.MOBILE_NUMBER;
import static com.ritara.svustudent.utils.Constants.NAME;
import static com.ritara.svustudent.utils.Constants.PAY_FEES;
import static com.ritara.svustudent.utils.Constants.PREFILL;
import static com.ritara.svustudent.utils.Constants.RULE;
import static com.ritara.svustudent.utils.Constants.SVU;
import static com.ritara.svustudent.utils.Constants.SVU_LIVE_LOGO;
import static com.ritara.svustudent.utils.Constants.TR_ID;
import static com.ritara.svustudent.utils.Constants.USER_ID;

public class Payments extends BaseActivity implements PaymentResultListener {

    private String type = "fees", amt = "0";
    private SharedPreferences_SVU sharedPreferences_svu;
    private EditText etFor, etMobile, etName, etEnrollment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        sharedPreferences_svu = SharedPreferences_SVU.getInstance(this);

        final EditText etAmount = (EditText) findViewById(R.id.etAmount);

        etFor = findViewById(R.id.etFor);
        etMobile = findViewById(R.id.etMobile);
        etName = findViewById(R.id.etName);
        etEnrollment = findViewById(R.id.etEnrollment);

        ((TextView)findViewById(R.id.txtHeader)).setText("Make Payment");

        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.btnPayment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amt = etAmount.getText().toString();
                if(!amt.isEmpty()) {
                    startPayment();
                }
            }
        });
    }

    public void startPayment() {
        final Context activity = Payments.this;
        final Checkout co = new Checkout();
        try {
            JSONObject options = new JSONObject();
            options.put(NAME, SVU);
            options.put(FULL_DESC, "" + type);
            options.put(IMAGE, SVU_LIVE_LOGO);
            options.put(CURRENCY, INR);
            String payment = amt;
            double total = Double.parseDouble(payment);
            total = total * 100;
            options.put(AMOUNT, total);
            JSONObject preFill = new JSONObject();
            preFill.put(EMAIL, ""+sharedPreferences_svu.get_email());
            preFill.put(CONTACT, ""+sharedPreferences_svu.get_phone());
            options.put(PREFILL, preFill);
            co.open(this, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Log.e("Success", s);
        api_contribution(s);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e("Failed", s);
    }

    private void api_contribution(String txn) {
        AndroidNetworking.post(BASE_URL)
                .addBodyParameter(RULE, PAY_FEES)
                .addBodyParameter(TR_ID, txn)
                .addBodyParameter(USER_ID, sharedPreferences_svu.getUserId())
                .addBodyParameter(AMOUNT, "" + amt)
                .addBodyParameter(MOBILE_NUMBER, "" + (sharedPreferences_svu.get_phone().isEmpty() ? etMobile.getText().toString() : sharedPreferences_svu.get_phone()))
                .addBodyParameter(ENROLL_NO, "" + (sharedPreferences_svu.getUserId().isEmpty() ? etEnrollment.getText().toString() : sharedPreferences_svu.getUserId()))
                .addBodyParameter(NAME, "" + (sharedPreferences_svu.get_Username().isEmpty() ? etName.getText().toString() : sharedPreferences_svu.get_Username()))
                .addBodyParameter(DESCRIPTION, "" + etFor.getText().toString().trim())
                .setTag(PAY_FEES)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if (response.getString("status").equals("1")) {
                                showToast("Your transaction processed successfully.");
                                finish();
                            } else {
                                showToast("Something went wrong");
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        error.getResponse();
                    }
                });
    }

}
