package com.ritara.svustudent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ritara.svustudent.ui.home.HomeFragment;
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

    try {
      Intent intent = getIntent();
      intent.getStringExtra("type");
      if(intent.getStringExtra("type").equalsIgnoreCase("admission")){
        Toast.makeText(this, "Enter enrollment number and your password or DATE Of Birth.", Toast.LENGTH_LONG).show();
      }
    }catch (Exception e){

    }
    username = (EditText) findViewById(R.id.username);
    password = (EditText)findViewById(R.id.password);
    login = (Button) findViewById(R.id.btnSubmit);
    forgotpassword = (TextView)findViewById(R.id.forgot);
    sharedPreferences_svu = SharedPreferences_SVU.getInstance(this);

    login.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        register();
      }
    });

  }

  private void register() {
    if (!isloadershowing())
      showLoader();
    AndroidNetworking.post("http://svu.svu.edu.in/svustuservice.asmx/GetPersonalInfo?EnrollNo=" + username.getText().toString().trim() + "&key=rky8UCIdFnfFUVzS8MC9zWVxI1ktu4ht/hO0msS+rSE")
            .addBodyParameter("EnrollNo", "" + username.getText().toString().trim())
            .addBodyParameter("key", "rky8UCIdFnfFUVzS8MC9zWVxI1ktu4ht/hO0msS+rSE")
            .setTag("login")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONArray(new JSONArrayRequestListener() {
              @Override
              public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                      JSONObject jsonObject1 = (JSONObject) response.get(i);

                      if(password.getText().toString().trim().equalsIgnoreCase(jsonObject1.getString("MName"))
                              || password.getText().toString().trim().contains(jsonObject1.getString("MName"))
                              || jsonObject1.getString("MName").contains(password.getText().toString().trim())){

                          sharedPreferences_svu.setUserId(jsonObject1.getString("EnrollNo"));
                          sharedPreferences_svu.set_Username(jsonObject1.getString("Name"));
                          sharedPreferences_svu.set_email(jsonObject1.getString("EmailId"));
                          sharedPreferences_svu.setAddress(jsonObject1.getString("PAddress"));

                          sharedPreferences_svu.set_MotherName(jsonObject1.getString("MName"));
                          sharedPreferences_svu.set_FatherName(jsonObject1.getString("FName"));
                          sharedPreferences_svu.set_CurrentAddress(jsonObject1.getString("CAddress"));


                          sharedPreferences_svu.set_Logged(true);
                          startActivity(new Intent(Login.this, Dashboard.class));
                          finish();
                      }else{
                        Toast.makeText(Login.this, "Password or mother's name nat matched.", Toast.LENGTH_SHORT).show();
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
                anError.getResponse();
                dismissLoader();
              }

            });
  }

  @Override
  public void onBackPressed() {
//    super.onBackPressed();
    if(!SharedPreferences_SVU.getInstance(this).get_Logged()){
      finish();
      startActivity(new Intent(this, Dashboard.class));
    }
  }
}
