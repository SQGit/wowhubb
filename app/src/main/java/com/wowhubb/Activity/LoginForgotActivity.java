package com.wowhubb.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.Spannable;
import android.text.SpannableString;
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

public class LoginForgotActivity extends Activity {

    TextView tv_snack;
    Typeface latoheading, lato;
    TextView head_tv;
    TextInputLayout email_til;
    ImageView submit, backiv;
    EditText phone;
    String phone_str, type;
    Snackbar snackbar;
    AVLoadingIndicatorView av_loader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpwd);
        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(LoginForgotActivity.this, v1);
        latoheading = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/latoheading.ttf");
        lato = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lato.ttf");
        email_til = (TextInputLayout) findViewById(R.id.til_email);
        phone = findViewById(R.id.phone_et);
        head_tv = (TextView) findViewById(R.id.head_tv);
        submit = (ImageView) findViewById(R.id.submit_iv);
        backiv = (ImageView) findViewById(R.id.backiv);
        head_tv.setTypeface(latoheading);
        email_til.setTypeface(lato);
        //-----------------------------------------SNACKBAR----------------------------------------//
        snackbar = Snackbar.make(findViewById(R.id.top), R.string.networkError, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(lato);
        av_loader = (AVLoadingIndicatorView) findViewById(R.id.avi);
        backiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginForgotActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FontsOverride.hideSoftKeyboard(v);
                String str_phone = phone.getText().toString();
                email_til.setError(null);

                if (!phone.getText().toString().trim().equalsIgnoreCase("")) {
                    phone.setError(null);
                    if (phone.getText().toString().trim().matches("[0-9]+")) {
                        phone_str = str_phone;
                        type = "phone";
                        if (Utils.Operations.isOnline(LoginForgotActivity.this)) {
                            new forgotpwd_phone(phone_str, type).execute();
                        } else {
                            snackbar.show();
                        }

                    } else if (!(!android.util.Patterns.EMAIL_ADDRESS.matcher(phone.getText().toString()).matches())) {
                        email_til.setError(null);
                        phone_str = str_phone;
                        type = "email";
                        if (Utils.Operations.isOnline(LoginForgotActivity.this)) {
                            new forgotpwd_email(phone_str, type).execute();

                        } else {
                            snackbar.show();
                        }

                    } else {

                        SpannableString s = new SpannableString("Invalid Credential");
                        s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        email_til.setError(s);
                    }


                } else {

                    email_til.requestFocus();
                    SpannableString s = new SpannableString("Enter Phone/Email");
                    s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    email_til.setError(s);
                }

            }


        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LoginForgotActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public class forgotpwd_phone extends AsyncTask<String, Void, String> {
        String phone_str, type;

        public forgotpwd_phone(String phone_str, String type) {
            this.phone_str = phone_str;
            this.type = type;
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
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest(Config.WEB_URL_FORGOT, json);
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

                        if (jo.has("code")) {
                            String code = jo.getString("code");
                            if (code.equals("200")) {
                                String msg = jo.getString("message");
                                Toast.makeText(LoginForgotActivity.this, msg, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LoginForgotActivity.this, LoginResetActivity.class);
                                intent.putExtra("type", type);
                                intent.putExtra("value", phone_str);
                                startActivity(intent);
                                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                finish();

                            } else {
                                String msg = jo.getString("message");
                                Toast.makeText(LoginForgotActivity.this, msg, Toast.LENGTH_LONG).show();
                            }
                        }


                    } else {
                        if (jo.has("code")) {
                            String code = jo.getString("code");
                            if (code.equals("200")) {
                                String msg = jo.getString("message");
                                Toast.makeText(LoginForgotActivity.this, msg, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LoginForgotActivity.this, LoginResetActivity.class);
                                intent.putExtra("type", type);
                                intent.putExtra("value", phone_str);
                                startActivity(intent);
                                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                finish();

                            } else {
                                String msg = jo.getString("message");
                                Toast.makeText(LoginForgotActivity.this, msg, Toast.LENGTH_LONG).show();
                            }
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

    public class forgotpwd_email extends AsyncTask<String, Void, String> {
        String phone_str, type;

        public forgotpwd_email(String phone_str, String type) {
            this.phone_str = phone_str;
            this.type = type;
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
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest(Config.WEB_URL_FORGOT, json);
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

                        if (jo.has("code")) {
                            String code = jo.getString("code");
                            if (code.equals("200")) {
                                String msg = jo.getString("message");
                                Toast.makeText(LoginForgotActivity.this, msg, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LoginForgotActivity.this, LoginResetActivity.class);
                                intent.putExtra("type", type);
                                intent.putExtra("value", phone_str);
                                startActivity(intent);
                                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                finish();
                            } else {
                                String msg = jo.getString("message");
                                Toast.makeText(LoginForgotActivity.this, msg, Toast.LENGTH_LONG).show();
                            }
                        }


                    } else {

                        if (jo.has("code")) {
                            String code = jo.getString("code");
                            if (code.equals("200")) {
                                String msg = jo.getString("message");
                                Toast.makeText(LoginForgotActivity.this, msg, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LoginForgotActivity.this, LoginResetActivity.class);
                                intent.putExtra("type", type);
                                intent.putExtra("value", phone_str);
                                startActivity(intent);
                                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                finish();
                            } else {
                                String msg = jo.getString("message");
                                Toast.makeText(LoginForgotActivity.this, msg, Toast.LENGTH_LONG).show();
                            }
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
