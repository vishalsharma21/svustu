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

public class Payments extends BaseActivity implements PaymentResultListener {

    private String type = "fees", amt = "0";
    private SharedPreferences_SVU sharedPreferences_svu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        sharedPreferences_svu = SharedPreferences_SVU.getInstance(this);

        final EditText etAmount = (EditText) findViewById(R.id.etAmount);

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
            options.put("name", "Shri Venkateshwara University");
            options.put("description", "" + type);
            options.put("image", "https://mlacademians.com/wp-content/uploads/2017/09/logo_acad-cop5y-copy.png");
            options.put("currency", "INR");
            String payment = amt;
            double total = Double.parseDouble(payment);
            total = total * 100;
            options.put("amount", total);

            JSONObject preFill = new JSONObject();
            preFill.put("email", ""+sharedPreferences_svu.get_email());
            preFill.put("contact", ""+sharedPreferences_svu.get_phone());

            options.put("prefill", preFill);

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
        AndroidNetworking.post("Base URL here")
                .addBodyParameter("rule", "payment")
                .addBodyParameter("transaction_id", txn)
                .addBodyParameter("user_id", sharedPreferences_svu.getUserId())
                .addBodyParameter("amount", "" + amt)
                .setTag("set_contribution")
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
