package com.wowhubb.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;
import com.wowhubb.Utils.Config;
import com.wowhubb.Utils.HttpUtils;
import com.wowhubb.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Ramya on 18-07-2017.
 */

public class LoginActivity extends Activity {


    Typeface latoheading, lato;
    TextView head_tv, forgotpwd_tv;
    TextInputLayout phone_til, pwd_til;
    ImageView submit, backiv;
    String str_pwd, phone_str;
    EditText phone, password;
    AVLoadingIndicatorView av_loader;
    TextView tv_snack;
    Snackbar snackbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        av_loader = (AVLoadingIndicatorView) findViewById(R.id.avi);

        //------------------------------FONT STYLE------------------------------------------------//
        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(LoginActivity.this, v1);
        latoheading = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/latoheading.ttf");
        lato = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lato.ttf");


        head_tv = (TextView) findViewById(R.id.head_tv);
        forgotpwd_tv = (TextView) findViewById(R.id.forgot_pwd_tv);
        backiv = (ImageView) findViewById(R.id.backiv);
        phone_til = (TextInputLayout) findViewById(R.id.til_otp);
        pwd_til = (TextInputLayout) findViewById(R.id.til_pwd);
        submit = (ImageView) findViewById(R.id.submit_iv);
        phone = findViewById(R.id.phone_et);
        password = findViewById(R.id.pwd_et);
        head_tv.setTypeface(latoheading);
        phone_til.setTypeface(lato);
        pwd_til.setTypeface(lato);

        //-----------------------------------------SNACKBAR----------------------------------------//
        snackbar = Snackbar.make(findViewById(R.id.top), R.string.networkError, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(lato);


        forgotpwd_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, LoginForgotActivity.class));
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        backiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_phone = phone.getText().toString();
                str_pwd = password.getText().toString();
                phone_til.setError(null);
                pwd_til.setError(null);

                if (!phone.getText().toString().trim().equalsIgnoreCase("")) {
                    phone.setError(null);
                    if (phone.getText().toString().trim().matches("[0-9]+")) {
                        phone_str = str_phone;
                        if (Utils.Operations.isOnline(LoginActivity.this)) {
                            new loginphone_customer(phone_str, str_pwd).execute();
                        } else {
                            snackbar.show();
                        }

                    } else if (!(!android.util.Patterns.EMAIL_ADDRESS.matcher(phone.getText().toString()).matches())) {
                        phone_til.setError(null);
                        phone_str = str_phone;
                        if (Utils.Operations.isOnline(LoginActivity.this)) {
                            new loginmail_customer(phone_str, str_pwd).execute();
                        } else {
                            snackbar.show();
                        }

                    } else {
                        phone_til.setError("Invalid Credential");
                        phone_til.requestFocus();
                    }


                } else {
                    phone_til.setError("Enter Phone/Email");
                    phone_til.requestFocus();
                }

            }


        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
        startActivity(intent);
    }

    public class loginphone_customer extends AsyncTask<String, Void, String> {
        String phone_str, pwd_str;

        public loginphone_customer(String phone_str, String pwd) {
            this.phone_str = phone_str;
            this.pwd_str = pwd;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            av_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("phone", "+1" + phone_str);
                jsonObject.accumulate("password", str_pwd);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest(Config.WEB_URL_LOGIN, json);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            av_loader.setVisibility(View.GONE);
            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("success");

                    if (status.equals("true")) {
                        String token = jo.getString("token");
                        JSONObject user_details = jo.getJSONObject("user");
                        String name = user_details.getString("firstname");
                        String lastname = user_details.getString("lastname");
                        String phone = user_details.getString("phone");
                        String email = user_details.getString("email");
                        String wowtagid = user_details.getString("wowtagid");
                        String firsttime = user_details.getString("firsttime");
                        SharedPreferences sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor edit = sharedPrefces.edit();
                        edit.putString("str_phone", phone);
                        edit.putString("str_name", name);
                        edit.putString("str_lname", lastname);
                        edit.putString("str_email", email);
                        edit.putString("token", token);
                        edit.putString("loginstatus", "email");
                        edit.putString("status", "true");
                        edit.putString("wowtagid", wowtagid);
                        edit.commit();
                        if (firsttime.equals("false")) {
                            startActivity(new Intent(LoginActivity.this, LandingPageActivity.class));
                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                            finish();
                        } else {
                            startActivity(new Intent(LoginActivity.this, InterestActivity.class));
                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                            finish();
                        }


                    } else {
                        String msg = jo.getString("message");

                        if (msg.equals("Authentication failed. User not found"))
                        {
                            phone_til.setError("Invalid User");
                            phone_til.setTypeface(lato);
                        }
                        else if (msg.equals("Authentication failed. Wrong Password"))
                        {
                            phone_til.setError(null);
                            pwd_til.setError("Invalid Password");
                            phone_til.setTypeface(lato);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                }
            } else {

            }

        }

    }

    public class loginmail_customer extends AsyncTask<String, Void, String> {
        String phone_str, str_pwd;

        public loginmail_customer(String phone_str, String str_pwd) {
            this.phone_str = phone_str;
            this.str_pwd = str_pwd;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            av_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("email", phone_str);
                jsonObject.accumulate("password", str_pwd);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest(Config.WEB_URL_EMAILLOGIN, json);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            av_loader.setVisibility(View.GONE);
            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("success");
                    if (status.equals("true")) {
                        String token = jo.getString("token");

                        JSONObject user_details = jo.getJSONObject("user");
                        String lastname = user_details.getString("lastname");
                        String name = user_details.getString("firstname");
                        String email = user_details.getString("email");
                        String phone = user_details.getString("phone");
                        String wowtagid = user_details.getString("wowtagid");
                        String firsttime = user_details.getString("firsttime");
                        SharedPreferences sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor edit = sharedPrefces.edit();
                        edit.putString("str_phone", phone);
                        edit.putString("str_name", name);
                        edit.putString("str_email", email);
                        edit.putString("str_lname", lastname);
                        edit.putString("token", token);
                        edit.putString("loginstatus", "email");
                        edit.putString("status", "true");
                        edit.putString("wowtagid", wowtagid);
                        edit.commit();

                        if (firsttime.equals("false")) {
                            startActivity(new Intent(LoginActivity.this, LandingPageActivity.class));
                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                            finish();
                        } else {
                            startActivity(new Intent(LoginActivity.this, InterestActivity.class));
                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                            finish();
                        }
                    } else {
                        String msg = jo.getString("message");
                        if (msg.equals("Authentication failed. User not found")) {
                            phone_til.setError("Invalid User");
                            phone_til.setTypeface(lato);

                        } else if (msg.equals("Authentication failed. Wrong Password")) {
                            phone_til.setError(null);
                            pwd_til.setError("Invalid Password");
                            pwd_til.setTypeface(lato);
                        } else {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            } else {

            }

        }

    }


}
