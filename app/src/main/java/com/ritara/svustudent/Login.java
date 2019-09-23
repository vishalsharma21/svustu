package com.ritara.svustudent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ritara.svustudent.utils.SharedPreferences_SVU;

import org.json.JSONArray;
import org.json.JSONObject;

public class Login extends BaseActivity {

  private EditText username,password;
  private Button login;
  private TextView forgotpassword;
  private SharedPreferences_SVU sharedPreferences_svu;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.login);
    username = (EditText) findViewById(R.id.username);
    password = (EditText)findViewById(R.id.password);
    login = (Button) findViewById(R.id.btnSubmit);
    forgotpassword = (TextView)findViewById(R.id.forgot);

    login.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          Intent intent = new Intent(Login.this, Dashboard.class);
          startActivity(intent);
      }
    });

  }


  private void register() {
    if (!isloadershowing())
      showLoader();
    AndroidNetworking.post("solutionsdot-com.in/SVU_api/svu_api.php")
            .addBodyParameter("rule", "login")
            .addBodyParameter("password", "" +password )
            .addBodyParameter("mob_email", "" + username)
            .addBodyParameter("device_id", sharedPreferences_svu.getTokenIdForFirebase())
            .setTag("login")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
              @Override
              public void onResponse(JSONObject response) {
                try {
                  if (response.getString("status").equalsIgnoreCase("0")) {
                    JSONArray jsonArray = response.getJSONArray("message");
                    for (int i = 0; i < jsonArray.length(); i++) {
                      JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);

                      sharedPreferences_svu.setUserId(jsonObject1.getString("id"));
                      sharedPreferences_svu.set_Username(jsonObject1.getString("name"));
                      sharedPreferences_svu.set_email(jsonObject1.getString("email"));
                      sharedPreferences_svu.set_phone(jsonObject1.getString("contact"));
                      sharedPreferences_svu.setAddress(jsonObject1.getString("address"));
                      sharedPreferences_svu.set_deviceId(jsonObject1.getString("device_id"));
                      sharedPreferences_svu.set_Logged(true);
                      startActivity(new Intent(Login.this, Dashboard.class));
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








