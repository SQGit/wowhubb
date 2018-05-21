package com.wowhubb.Activity;

import android.app.Activity;
import android.content.Context;
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
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class RegisterCountryActivity extends Activity {

    Typeface latoheading, lato;
    TextView head_tv;
    TextInputLayout phone_til;
    ImageView submit;
    Config config;
    EditText phone_et;
    String str_phone, str_email, val_status, str_country,str_password;
    // CountryCodePicker countryCodePicker;
    Snackbar snackbar;
    TextView tv_snack;
    AVLoadingIndicatorView av_loader;
    RadioButton ByEmail, ByPhone;
    RadioGroup radioGroup;
    SharedPreferences sharedPrefces;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_country);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        str_phone = sharedPreferences.getString("str_phone", "");
        str_email = sharedPreferences.getString("str_email", "");
        str_password = sharedPreferences.getString("str_password", "");
        config = new Config();

        //------------------------------FONT STYLE------------------------------------------------//

        latoheading = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/latoheading.ttf");
        lato = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lato.ttf");
        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(RegisterCountryActivity.this, v1);

        //----------------------------------------------------------------------------------------//

        sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        edit = sharedPrefces.edit();
        edit.putString("val_status", "phone");
        //    edit.remove("str_email");
        // edit.remove("str_phone");
        edit.apply();
        edit.commit();

        head_tv = findViewById(R.id.head_tv);
        submit = findViewById(R.id.submit_iv);
        phone_et = findViewById(R.id.phone_et);
        av_loader = findViewById(R.id.avi);
        phone_til = findViewById(R.id.til_phone);
        head_tv.setTypeface(latoheading);
        phone_til.setTypeface(lato);
        ByEmail = findViewById(R.id.rademail);
        ByPhone = findViewById(R.id.radiophone);
        radioGroup = findViewById(R.id.radioGroup);
        //  countryCodePicker = findViewById(R.id.ccp);
        phone_et.setText("");
        phone_et.setText(str_phone);
        phone_et.setSelection(phone_et.getText().length());
        //-----------------------------------------SNACKBAR----------------------------------------//

        snackbar = Snackbar.make(findViewById(R.id.top), R.string.networkError, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(lato);
        if (!Utils.Operations.isOnline(RegisterCountryActivity.this)) {
            snackbar.show();
            tv_snack.setText(R.string.networkError);
        }


        //----------------------------TO CHECK EMAIL OR PHONE-------------------------------------//

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (ByPhone.isChecked()) {
                    //  countryCodePicker.setVisibility(View.VISIBLE);
                    phone_et.setText("");
                    phone_et.setText(str_phone);
                    phone_et.setSelection(phone_et.getText().length());
                    phone_et.setError(null);
                    phone_til.setHint("Mobile Number");
                    phone_til.setPadding(0, 0, 0, 0);
                    edit.putString("val_status", "phone");
                    edit.commit();
                    final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RegisterCountryActivity.this);
                    val_status = sharedPreferences.getString("val_status", "");

                } else {
                    phone_et.setText("");
                    phone_et.setText(str_email);
                    phone_et.setError(null);
                    phone_et.setSelection(phone_et.getText().length());
                    phone_til.setPadding(0, 0, 0, 0);
                    phone_til.setHint("Email");
                    edit.putString("val_status", "email");
                    edit.commit();
                    final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RegisterCountryActivity.this);
                    val_status = sharedPreferences.getString("val_status", "");

                }


            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  str_country = countryCodePicker.getSelectedCountryCodeWithPlus();
                phone_et.setEnabled(true);
                str_phone = phone_et.getText().toString();


                    if (!phone_et.getText().toString().trim().equalsIgnoreCase("")) {
                        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RegisterCountryActivity.this);
                        val_status = sharedPreferences.getString("val_status", "");
                        if (val_status.equals("phone")) {
                            if (phone_et.getText().toString().trim().length() > 9) {
                                phone_til.setError(null);
                                if (!Utils.Operations.isOnline(RegisterCountryActivity.this)) {
                                    snackbar.show();
                                    tv_snack.setText(R.string.networkError);
                                } else {
                                    new login_customer().execute();
                                }

                            } else {
                                phone_til.setError("Invalid Phone Number");
                                phone_et.requestFocus();
                            }
                        } else {

                            if (!(!android.util.Patterns.EMAIL_ADDRESS.matcher(phone_et.getText().toString()).matches())) {
                                phone_til.setError(null);
                                {
                                    phone_et.requestFocus();
                                    if (!Utils.Operations.isOnline(RegisterCountryActivity.this)) {
                                        snackbar.show();
                                        tv_snack.setText(R.string.networkError);
                                    } else {
                                        new login_customer_email().execute();
                                    }


                                }
                            } else {
                                phone_til.setError("Invalid Email");
                                phone_et.requestFocus();
                            }


                        }

                    } else {
                        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RegisterCountryActivity.this);
                        val_status = sharedPreferences.getString("val_status", "");
                        if (val_status.equals("phone")) {
                            phone_til.setError(null);
                            phone_til.setError("Enter Mobile Number");
                            phone_et.requestFocus();
                        } else {
                            phone_til.setError(null);
                            phone_til.setError("Enter Email");
                            phone_et.requestFocus();
                        }

                    }



            }
        });


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegisterCountryActivity.this, SplashActivity.class);
        startActivity(intent);
    }


    //------------------------------ASYNC TASK FOR PHONE NO---------------------------------------//

    public class login_customer extends AsyncTask<String, Void, String> {
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
                jsonObject.accumulate("tonumber",  str_phone);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest(Config.WEB_URL_OTP, json);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "tag" + s);
            av_loader.setVisibility(View.GONE);
            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);

                    if (jo.has("success")) {
                        String status = jo.getString("success");
                        if (status.equals("true")) {
                            //String otp = jo.getString("otp");
                            String msg = jo.getString("message");
                            SharedPreferences sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor edit = sharedPrefces.edit();
                            edit.putString("str_phone", str_phone);
                            edit.putString("loginstatus", "phone");
                            edit.putString("str_password",str_password);
                            edit.commit();
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RegisterCountryActivity.this, RegisterOtpActivity.class));
                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                            finish();
                        } else {
                            String msg = jo.getString("message");
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        String msg = jo.getString("message");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                }
            } else {


            }

        }

    }

    //------------------------------ASYNC TASK FOR EMAIL-------------------------------------------//
    public class login_customer_email extends AsyncTask<String, Void, String> {
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
                jsonObject.accumulate("toemail", str_phone);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest(Config.WEB_URL_MAILOTP, json);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "tag" + s);
            av_loader.setVisibility(View.GONE);
            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);

                    if (jo.has("success")) {
                        String status = jo.getString("success");
                        if (status.equals("true")) {
                            // String otp = jo.getString("otp");
                            String msg = jo.getString("message");
                            SharedPreferences sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor edit = sharedPrefces.edit();
                           edit.putString("str_password", str_password);
                            edit.putString("str_email", str_phone);
                            edit.putString("loginstatus", "email");
                            edit.commit();
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RegisterCountryActivity.this, RegisterOtpActivity.class));
                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                        } else {
                            String msg = jo.getString("message");
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        String msg = jo.getString("message");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            } else {

            }

        }

    }










}
