package vineture.wowhubb.Activity;

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
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;
import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.Utils.Config;
import vineture.wowhubb.Utils.HttpUtils;
import vineture.wowhubb.Utils.Utils;

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
    String str_pwd, phone_str, type,locale,CountryZipCode;
    EditText phone, password;
    AVLoadingIndicatorView av_loader;
    TextView tv_snack;
    Snackbar snackbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(vineture.wowhubb.R.layout.activity_signin);

        av_loader = (AVLoadingIndicatorView) findViewById(vineture.wowhubb.R.id.avi);

        //------------------------------------FONT STYLE------------------------------------------//
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        locale = tm.getSimCountryIso().toUpperCase();


        GetCountryZipCode();

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(LoginActivity.this, v1);
        latoheading = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/latoheading.ttf");
        lato = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lato.ttf");
        head_tv = findViewById(vineture.wowhubb.R.id.head_tv);
        forgotpwd_tv = findViewById(vineture.wowhubb.R.id.forgot_pwd_tv);
        backiv = findViewById(vineture.wowhubb.R.id.backiv);
        phone_til = findViewById(vineture.wowhubb.R.id.til_otp);
        pwd_til = findViewById(vineture.wowhubb.R.id.til_pwd);
        submit = findViewById(vineture.wowhubb.R.id.submit_iv);
        phone = findViewById(vineture.wowhubb.R.id.phone_et);
        password = findViewById(vineture.wowhubb.R.id.pwd_et);

        head_tv.setTypeface(latoheading);
        phone_til.setTypeface(lato);
        pwd_til.setTypeface(lato);

        //----------------------------------------SNACKBAR-----------------------------------------//

        snackbar = Snackbar.make(findViewById(vineture.wowhubb.R.id.top), vineture.wowhubb.R.string.networkError, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(lato);

        forgotpwd_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, LoginForgotActivity.class));
                overridePendingTransition(vineture.wowhubb.R.anim.left_to_right, vineture.wowhubb.R.anim.right_to_left);
                finish();
            }
        });


        backiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SplashActivity.class));
                overridePendingTransition(vineture.wowhubb.R.anim.left_to_right, vineture.wowhubb.R.anim.right_to_left);
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FontsOverride.hideSoftKeyboard(v);

                String str_phone = phone.getText().toString();
                str_pwd = password.getText().toString();
                phone_til.setError(null);
                pwd_til.setError(null);

                if (!phone.getText().toString().trim().equalsIgnoreCase("")) {
                    phone.setError(null);
                    if (phone.getText().toString().trim().matches("[0-9]+")) {
                        phone_str = str_phone;
                        type = "phone";

                        if (Utils.Operations.isOnline(LoginActivity.this)) {
                            new loginphone_customer(phone_str, str_pwd, type).execute();
                        } else {
                            snackbar.show();
                        }

                    } else if (!(!android.util.Patterns.EMAIL_ADDRESS.matcher(phone.getText().toString()).matches())) {
                        phone_til.setError(null);
                        phone_str = str_phone;
                        type = "email";

                        if (Utils.Operations.isOnline(LoginActivity.this)) {
                            new loginmail_customer(phone_str, str_pwd, type).execute();
                        } else {
                            snackbar.show();
                        }

                    } else {
                        SpannableString s = new SpannableString("Invalid Credential");
                        s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        phone_til.setError(s);
                    }

                } else {
                    phone_til.requestFocus();
                    SpannableString s = new SpannableString("Enter Phone/Email");
                    s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    phone_til.setError(s);
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
        String phone_str, pwd_str, type;

        public loginphone_customer(String phone_str, String pwd, String type) {
            this.phone_str = phone_str;
            this.pwd_str = pwd;
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
                        edit.putString("loginstatus", type);
                        edit.putString("status", "true");
                        edit.putString("wowtagid", wowtagid);
                        edit.putString("CountryZipCode", CountryZipCode);

                        if (user_details.has("personalimage")) {
                            String personalimage = user_details.getString("personalimage");
                            edit.putString("profilepath", personalimage);
                        }
                        if (user_details.has("personalself")) {
                            String personalself = user_details.getString("personalself");
                            edit.putString("personalself", personalself);
                        }

                        if (user_details.has("designation")) {
                            String userdesignation = user_details.getString("designation");
                            edit.putString("userdesignation", userdesignation);
                        }
                        if (user_details.has("_id")) {
                            String userid = user_details.getString("_id");
                            edit.putString("userid", userid);
                        }

                        edit.commit();
                        if (firsttime.equals("false")) {
                            startActivity(new Intent(LoginActivity.this, LandingPageActivity.class));
                            overridePendingTransition(vineture.wowhubb.R.anim.left_to_right, vineture.wowhubb.R.anim.right_to_left);
                            finish();
                        } else {
                            startActivity(new Intent(LoginActivity.this, LandingPageActivity.class));
                            overridePendingTransition(vineture.wowhubb.R.anim.left_to_right, vineture.wowhubb.R.anim.right_to_left);
                            SharedPreferences sharedPrefces1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor edit1 = sharedPrefces1.edit();
                            edit1.putString("firsttime", "true");
                            edit1.commit();
                            finish();
                        }


                    } else {
                        String code = jo.getString("code");

                        if (code.equals("403")) {
                            Intent intent = new Intent(LoginActivity.this, RegisterOtpActivity.class);
                            intent.putExtra("type", type);
                            intent.putExtra("value", "+1" + phone_str);
                            intent.putExtra("password", str_pwd);
                            startActivity(intent);
                            overridePendingTransition(vineture.wowhubb.R.anim.left_to_right, vineture.wowhubb.R.anim.right_to_left);
                            finish();
                        } else if (code.equals("401")) {
                            phone_til.setError(null);
                            // pwd_til.setError("Invalid Password");
                            SpannableString s1 = new SpannableString("Invalid Password");
                            s1.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            pwd_til.setError(s1);
                        } else if (code.equals("404")) {
                            SpannableString s1 = new SpannableString("Invalid User");
                            s1.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            phone_til.setError(s1);

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
        String phone_str, str_pwd, type;

        public loginmail_customer(String phone_str, String str_pwd, String type) {
            this.phone_str = phone_str;
            this.str_pwd = str_pwd;
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
                        edit.putString("loginstatus", type);
                        edit.putString("status", "true");
                        edit.putString("wowtagid", wowtagid);
                        edit.putString("CountryZipCode", CountryZipCode);
                        if (user_details.has("personalimage")) {
                            String personalimage = user_details.getString("personalimage");
                            edit.putString("profilepath", personalimage);
                        }
                        if (user_details.has("personalself")) {
                            String personalself = user_details.getString("personalself");
                            edit.putString("personalself", personalself);
                        }
                        if (user_details.has("designation")) {
                            String userdesignation = user_details.getString("designation");
                            edit.putString("userdesignation", userdesignation);
                        }
                        if (user_details.has("_id")) {
                            String userid = user_details.getString("_id");
                            edit.putString("userid", userid);
                        }
                        edit.commit();

                        if (firsttime.equals("false")) {
                            startActivity(new Intent(LoginActivity.this, LandingPageActivity.class));
                            overridePendingTransition(vineture.wowhubb.R.anim.left_to_right, vineture.wowhubb.R.anim.right_to_left);
                            finish();
                        } else {
                            startActivity(new Intent(LoginActivity.this, LandingPageActivity.class));
                            overridePendingTransition(vineture.wowhubb.R.anim.left_to_right, vineture.wowhubb.R.anim.right_to_left);
                            SharedPreferences sharedPrefces1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor edit1 = sharedPrefces1.edit();
                            edit1.putString("firsttime", "true");
                            edit1.commit();

                        }
                    } else {
                        String code = jo.getString("code");
                        if (code.equals("403")) {
                            Intent intent = new Intent(LoginActivity.this, RegisterOtpActivity.class);
                            intent.putExtra("type", type);
                            intent.putExtra("value", phone_str);
                            intent.putExtra("password", str_pwd);
                            startActivity(intent);
                            overridePendingTransition(vineture.wowhubb.R.anim.left_to_right, vineture.wowhubb.R.anim.right_to_left);
                            finish();
                        } else if (code.equals("401")) {
                            phone_til.setError(null);
                            //  pwd_til.setError("Invalid Password");
                            SpannableString s1 = new SpannableString("Invalid Password");
                            s1.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            pwd_til.setError(s1);
                        } else if (code.equals("404")) {
                            phone_til.setError("Invalid User");
                            SpannableString s1 = new SpannableString("Invalid User");
                            s1.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            phone_til.setError(s1);
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            } else {

            }

        }

    }
    private void GetCountryZipCode() {
        String CountryID="";
         CountryZipCode="";

       /* TelephonyManager manager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID= manager.getSimCountryIso().toUpperCase();
        Log.e("tag","aadhav1"+CountryID);*/

        String[] rl=this.getResources().getStringArray(vineture.wowhubb.R.array.CountryCodes);
        for(int i=0;i<rl.length;i++) {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(locale.trim())) {
                CountryZipCode = g[0];
                break;
            }
        }
        Log.e("tag","aadhav2"+CountryZipCode);
        //Toast.makeText(getApplicationContext(),"Checking Country"+"  "+locale+"  "+"Checking Country Code"+" "+ CountryZipCode,Toast.LENGTH_LONG).show();

    }




}
