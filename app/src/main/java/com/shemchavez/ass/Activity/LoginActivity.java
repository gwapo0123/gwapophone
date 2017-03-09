package com.shemchavez.ass.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.shemchavez.ass.Custom.CustomEditText;
import com.shemchavez.ass.Request.AppController;
import com.shemchavez.ass.Shared.Config;
import com.shemchavez.ass.Shared.Global;
import com.shemchavez.ass.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity{

    private TextView tvLoginLabelApplicationName;
    private EditText edtLoginEmailAddress;
    private CustomEditText edtLoginPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Check if there's session
        if (Global.getSession(LoginActivity.this)) {
            Intent intent = new Intent(LoginActivity.this, ContainerActivity.class);
            startActivity(intent);
        }

        setContentView(R.layout.activity_login);

        prepareUI();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Global.IsReachable(LoginActivity.this)) { login(); }
                else{ Toast.makeText(LoginActivity.this, R.string.label_please_check_internet_connection, Toast.LENGTH_SHORT).show(); }
            }
        });

        // Show password button
        final Boolean[] viewStatus = {true};
        edtLoginPassword.setDrawableClickListener(new CustomEditText.DrawableClickListener() {

            public void onClick(DrawablePosition target) {
                switch (target) {
                    case RIGHT:
                        if(viewStatus[0] == true){
                            viewStatus[0] = false;
                            edtLoginPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

                        }else if(viewStatus[0] == false){
                            viewStatus[0] = true;
                            edtLoginPassword.setInputType(129);
                        }
                        break;
                }
            }
        });
    }

    /**
     * Prepare UI
     */
    private void prepareUI() {
        tvLoginLabelApplicationName = (TextView) findViewById(R.id.tvLabelLoginApplicationName);
        tvLoginLabelApplicationName.setTypeface(Global.setCustomFont(this, "Bebas"));
        edtLoginEmailAddress = (EditText) findViewById(R.id.edtLoginEmailAddress);
        edtLoginPassword = (CustomEditText) findViewById(R.id.edtLoginPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
    }

    /**
     * Login
     */
    private void login(){
        final JSONObject jsonObjectLogin = new JSONObject();
        try {
            jsonObjectLogin.put("email_address", edtLoginEmailAddress.getText().toString().trim());
            jsonObjectLogin.put("password", edtLoginPassword.getText().toString().trim());
            // Start SR_LOGIN
            StringRequest SR_LOGIN = new StringRequest(Request.Method.POST, Config.url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.equals("Error")) {
                                try {
                                    JSONObject jsonObjectResponse = new JSONObject(response);

                                    // Set temporary details
                                    Global.setTempImage(LoginActivity.this, jsonObjectResponse.getString("image"));
                                    Global.setTempID(LoginActivity.this, jsonObjectResponse.getString("id"));
                                    Global.setTempFullName(LoginActivity.this, Global.displayFullName(jsonObjectResponse.getString("fname"), jsonObjectResponse.getString("mname"), jsonObjectResponse.getString("lname")));

                                    int changePasswordStatus = Integer.parseInt(jsonObjectResponse.getString("change_password_status"));

                                    // Change password
                                    if(changePasswordStatus == 0){
                                        Intent intent = new Intent(LoginActivity.this, ChangePasswordActivity.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        // Add session
                                        Global.setSession(LoginActivity.this);
                                        Intent intent = new Intent(LoginActivity.this, ContainerActivity.class);
                                        startActivity(intent);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                Toast.makeText(LoginActivity.this, R.string.label_incorrect_email_or_password, Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(LoginActivity.this, R.string.label_please_cant_connect_to_server, Toast.LENGTH_SHORT).show();
                            Log.v("### Error", error.toString());
                        }
                    })

            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                        params.put("request", "login");
                        params.put("json", String.valueOf(jsonObjectLogin));
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(SR_LOGIN, "srLogin");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
