package vineture.wowhubb.Activity;

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
import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.R;
import vineture.wowhubb.Utils.Config;
import vineture.wowhubb.Utils.HttpUtils;
import vineture.wowhubb.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Ramya on 18-07-2017.
 */

public class LoginResetActivity extends Activity {

    TextView tv_snack;
    Typeface latoheading, lato;
    TextView head_tv;
    TextInputLayout  pwd_til, cpwd_til, otp_til;
    ImageView submit, backiv;
    EditText otp_et, pwd_et, cpwd_et;
    String phone_str, type, otp, password;
    Snackbar snackbar;
    AVLoadingIndicatorView av_loader;
    Bundle extras;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpwd);

        extras = getIntent().getExtras();

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(LoginResetActivity.this, v1);
        latoheading = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/latoheading.ttf");
        lato = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lato.ttf");
        otp_til = (TextInputLayout) findViewById(R.id.til_otp);
        pwd_til = (TextInputLayout) findViewById(R.id.til_pwd);
        cpwd_til = (TextInputLayout) findViewById(R.id.til_cpwd);

        otp_et = findViewById(R.id.otp_et);
        pwd_et = findViewById(R.id.pwd_et);
        cpwd_et = findViewById(R.id.cpwd_et);

        head_tv = (TextView) findViewById(R.id.head_tv);
        submit = (ImageView) findViewById(R.id.submit_iv);
        backiv = (ImageView) findViewById(R.id.backiv);
        head_tv.setTypeface(latoheading);

        otp_til.setTypeface(lato);
        pwd_til.setTypeface(lato);
        cpwd_til.setTypeface(lato);

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
                startActivity(new Intent(LoginResetActivity.this, LoginForgotActivity.class));
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FontsOverride.hideSoftKeyboard(v);

                otp = otp_et.getText().toString();
                password = pwd_et.getText().toString();

                if (!otp_et.getText().toString().trim().equalsIgnoreCase("")) {
                    otp_til.setError(null);
                    if (!pwd_et.getText().toString().trim().equalsIgnoreCase("")) {
                        pwd_til.setError(null);
                        if (pwd_et.getText().toString().trim().length() > 5) {
                            pwd_til.setError(null);
                            if (!cpwd_et.getText().toString().trim().equalsIgnoreCase("")) {
                                cpwd_et.setError(null);

                                if (pwd_et.getText().toString().trim().equals(cpwd_et.getText().toString().trim()))
                                {
                                    cpwd_et.setError(null);
                                    if (extras != null) {
                                        type = extras.getString("type");
                                        phone_str = extras.getString("value");
                                        if (type.equals("email"))
                                        {
                                            if (Utils.Operations.isOnline(LoginResetActivity.this)) {
                                                new resetpwd_email(phone_str, otp, password, type).execute();

                                            } else {
                                                snackbar.show();
                                            }
                                        }
                                        else
                                        {
                                            if (Utils.Operations.isOnline(LoginResetActivity.this)) {
                                                new resetpwd_phone(phone_str, otp, password, type).execute();
                                            } else {
                                                snackbar.show();
                                            }
                                        }

                                    }




                                } else {
                                    cpwd_et.requestFocus();
                                    SpannableString s = new SpannableString("Password does not Match");
                                    s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    cpwd_til.setError(s);
                                }


                            } else {
                                cpwd_til.requestFocus();
                                SpannableString s = new SpannableString("Enter Repassword");
                                s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                cpwd_til.setError(s);
                            }


                        } else {
                            // pwd_til.setError("Password should be maximum 6 characters");
                            pwd_til.requestFocus();
                            SpannableString s = new SpannableString("Password should be maximum 6 characters");
                            s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            pwd_til.setError(s);
                        }
                    }
                    else {
                        pwd_til.requestFocus();
                        SpannableString s = new SpannableString("Enter Password");
                        s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        pwd_til.setError(s);
                    }
                } else {
                    otp_til.requestFocus();
                    SpannableString s = new SpannableString("Enter Otp");
                    s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    otp_til.setError(s);
                }



            }


        });
    }

    public class resetpwd_phone extends AsyncTask<String, Void, String> {
        String phone_str, type, otp, password;

        public resetpwd_phone(String phone_str, String otp, String passowrd, String type) {
            this.phone_str = phone_str;
            this.otp = otp;
            this.password = passowrd;
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

                jsonObject.accumulate("otp", otp);
                jsonObject.accumulate("newpassword", password);
                //otp, newpassword
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest(Config.WEB_URL_RESETPWD, json);
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
                                Toast.makeText(LoginResetActivity.this, msg, Toast.LENGTH_LONG).show();
                            } else {
                                String msg = jo.getString("message");
                                Toast.makeText(LoginResetActivity.this, msg, Toast.LENGTH_LONG).show();
                            }
                        }


                    } else {

                        if (jo.has("code")) {
                            String code = jo.getString("code");
                            if (code.equals("200")) {
                                String msg = jo.getString("message");
                                Toast.makeText(LoginResetActivity.this, msg, Toast.LENGTH_LONG).show();
                                startActivity(new Intent(LoginResetActivity.this, LoginActivity.class));
                                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                            } else {
                                String msg = jo.getString("message");
                                Toast.makeText(LoginResetActivity.this, msg, Toast.LENGTH_LONG).show();
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

    public class resetpwd_email extends AsyncTask<String, Void, String> {
        String phone_str, type, otp, password;

        public resetpwd_email(String phone_str, String otp, String password, String type) {
            this.phone_str = phone_str;
            this.type = type;
            this.otp = otp;
            this.password = password;
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
                jsonObject.accumulate("otp", otp);
                jsonObject.accumulate("newpassword", password);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest(Config.WEB_URL_RESETPWD, json);
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
                                Toast.makeText(LoginResetActivity.this, msg, Toast.LENGTH_LONG).show();
                                startActivity(new Intent(LoginResetActivity.this, LoginActivity.class));
                                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                            } else {
                                String msg = jo.getString("message");
                                Toast.makeText(LoginResetActivity.this, msg, Toast.LENGTH_LONG).show();
                            }
                        }


                    } else {
                        if (jo.has("code")) {
                            String code = jo.getString("code");
                            if (code.equals("200")) {
                                String msg = jo.getString("message");
                                Toast.makeText(LoginResetActivity.this, msg, Toast.LENGTH_LONG).show();
                                startActivity(new Intent(LoginResetActivity.this, LoginActivity.class));
                                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                            } else {
                                String msg = jo.getString("message");
                                Toast.makeText(LoginResetActivity.this, msg, Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LoginResetActivity.this, LoginForgotActivity.class);
        startActivity(intent);
    }
}
