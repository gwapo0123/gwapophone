package com.shemchavez.ass.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.shemchavez.ass.Custom.CustomEditText;
import com.shemchavez.ass.R;
import com.shemchavez.ass.Request.AppController;
import com.shemchavez.ass.Shared.Config;
import com.shemchavez.ass.Shared.Global;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity {

    private CircularImageView changePasswordCIImage;
    private TextView changePasswordTVFullName;
    private CustomEditText changePasswordEdtPassword, chnagePasswordEdtConfirmPassword;
    private Button changePasswordBtnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        prepareUI();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setDriverImage();
        changePasswordTVFullName.setText(Global.getTempFullName(ChangePasswordActivity.this));
        final Boolean[] viewStatusPassword = {true};

        changePasswordEdtPassword.setDrawableClickListener(new CustomEditText.DrawableClickListener() {
            public void onClick(DrawablePosition target) {
                switch (target) {
                    case RIGHT:
                        if(viewStatusPassword[0] == true){
                            viewStatusPassword[0] = false;
                            changePasswordEdtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

                        }else if(viewStatusPassword[0] == false){
                            viewStatusPassword[0] = true;
                            changePasswordEdtPassword.setInputType(129);
                        }
                        break;
                }
            }
        });

        final Boolean[] viewStatusConfirmPassword = {true};
        chnagePasswordEdtConfirmPassword.setDrawableClickListener(new CustomEditText.DrawableClickListener() {
            public void onClick(DrawablePosition target) {
                switch (target) {
                    case RIGHT:

                        if(viewStatusConfirmPassword[0] == true){
                            viewStatusConfirmPassword[0] = false;
                            chnagePasswordEdtConfirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

                        }else if(viewStatusConfirmPassword[0] == false){
                            viewStatusConfirmPassword[0] = true;
                            chnagePasswordEdtConfirmPassword.setInputType(129);
                        }
                        break;
                }
            }
        });

        changePasswordBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(changePasswordEdtPassword.getText().toString().trim().equals(chnagePasswordEdtConfirmPassword.getText().toString().trim())){
                    if (Global.IsReachable(ChangePasswordActivity.this)) {  changePassword(); }
                    else{ Toast.makeText(ChangePasswordActivity.this, R.string.label_please_check_internet_connection, Toast.LENGTH_SHORT).show(); }
                }
                else{
                    Toast.makeText(ChangePasswordActivity.this, "Password inputted not match!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Initializiaton of UI
     */
    private void prepareUI(){
        changePasswordCIImage = (CircularImageView) findViewById(R.id.changePasswordCIImage);
        changePasswordTVFullName = (TextView) findViewById(R.id.changePasswordTVFullName);
        changePasswordEdtPassword = (CustomEditText) findViewById(R.id.changePasswordEdtPassword);
        chnagePasswordEdtConfirmPassword = (CustomEditText)findViewById(R.id.chnagePasswordEdtConfirmPassword);
        changePasswordBtnSubmit = (Button) findViewById(R.id.changePasswordBtnSubmit);
    }

    /**
     * Set temporary driver image
     */
    private void setDriverImage(){
        String image = Global.getTempImage(ChangePasswordActivity.this);
        if (image.equals(Config.plainURL)){
            changePasswordCIImage.setImageResource(R.drawable.driver_thumbnail);
        }
        else{
            new fetchImageTask(changePasswordCIImage).execute(image);
        }
    }

    /**
     * Change password
     */
    private void changePassword(){
        final JSONObject jsonObjectChangePassword = new JSONObject();
        try {
            String id = Global.getTempID(ChangePasswordActivity.this);
            jsonObjectChangePassword.put("id", id);
            jsonObjectChangePassword.put("password", changePasswordEdtPassword.getText().toString().trim());
            jsonObjectChangePassword.put("fetchJSON", "false");
            StringRequest SR_CHANGE_PASSWORD = new StringRequest(Request.Method.POST, Config.url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Add session
                            Global.setSession(ChangePasswordActivity.this);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ChangePasswordActivity.this, "Cant connect to server!", Toast.LENGTH_SHORT).show();
                            Log.v("### Error", error.toString());
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                        params.put("request", "change_password");
                        params.put("json", String.valueOf(jsonObjectChangePassword));
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(SR_CHANGE_PASSWORD, "SR_CHANGE_PASSWORD");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(ChangePasswordActivity.this, ContainerActivity.class);
        startActivity(intent);
    }

    /**
     * Fetch image task
     */
    class fetchImageTask extends AsyncTask<String, Void, Bitmap> {
        CircularImageView ivImage;
        public fetchImageTask(CircularImageView ivImage) {
            this.ivImage = ivImage;
        }
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }
        protected void onPostExecute(Bitmap result) {
            ivImage.setImageBitmap(result);
        }
    }
}
