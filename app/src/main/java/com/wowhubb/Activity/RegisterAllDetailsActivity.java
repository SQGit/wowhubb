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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class RegisterAllDetailsActivity extends Activity {
    Typeface latoheading, lato;
    TextView head_tv;
    TextInputLayout fname_til, lname_til, pwd_til, repwd_til, wowtag_til;
    ImageView submit, backiv;
    EditText fname_et, lname_et, pwd_et, repwd_et, wowtag_et;
    String str_firstname, str_lastname, str_phone, str_email, str_password, str_gender, str_birthday;
    String str_wowtag;
    Snackbar snackbar;
    TextView tv_snack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_alldetails);
        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(RegisterAllDetailsActivity.this, v1);

        latoheading = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/latoheading.ttf");
        lato = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lato.ttf");
        head_tv = (TextView) findViewById(R.id.head_tv);
        fname_til = (TextInputLayout) findViewById(R.id.til_fname);
        lname_til = (TextInputLayout) findViewById(R.id.til_lastname);
        pwd_til = (TextInputLayout) findViewById(R.id.til_pwd);
        repwd_til = (TextInputLayout) findViewById(R.id.til_repwd);
        wowtag_til = (TextInputLayout) findViewById(R.id.wowtag_til);

        submit = (ImageView) findViewById(R.id.submit_iv);
        backiv = (ImageView) findViewById(R.id.backiv);

        fname_et = (EditText) findViewById(R.id.fname_et);
        lname_et = (EditText) findViewById(R.id.lname_et);
        pwd_et = (EditText) findViewById(R.id.pwd_et);
        repwd_et = (EditText) findViewById(R.id.repwd_et);
        wowtag_et = findViewById(R.id.wowtag_et);

        // wowtag_et.addTextChangedListener(passwordWatcher);


        head_tv.setTypeface(latoheading);
        fname_til.setTypeface(lato);
        lname_til.setTypeface(lato);
        pwd_til.setTypeface(lato);
        repwd_til.setTypeface(lato);
        wowtag_til.setTypeface(lato);

        //--------------------------SNACKBAR-------------------------------------------------------//
        snackbar = Snackbar.make(findViewById(R.id.top), R.string.networkError, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(lato);
        if (!Utils.Operations.isOnline(RegisterAllDetailsActivity.this)) {
            snackbar.show();
            tv_snack.setText(R.string.networkError);
        }

        //-----------------------------------------------------------------------------------------//


        backiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        wowtag_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // wowtag_et.setText("!");
            }
        });

        lname_et.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                wowtag_et.setText("!");
                wowtag_et.setSelection(wowtag_et.getText().length());
                wowtag_et.setCursorVisible(true);
                return false;
            }


        });

        wowtag_et.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                str_wowtag = wowtag_et.getText().toString();
                if (!str_wowtag.equals("")) {
                    new checkingwowtag().execute();
                }

                return false;
            }


        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname_str = fname_et.getText().toString();
                String lname_str = lname_et.getText().toString();
                String pwd_str = pwd_et.getText().toString();
                String repwd_str = repwd_et.getText().toString();
                 str_wowtag = wowtag_et.getText().toString();

                if (!fname_et.getText().toString().trim().equalsIgnoreCase("")) {
                    fname_til.setError(null);
                    if (!lname_et.getText().toString().trim().equalsIgnoreCase("")) {
                        lname_til.setError(null);
                        if (!wowtag_et.getText().toString().trim().equalsIgnoreCase("")) {
                            wowtag_til.setError(null);
                            if (!pwd_et.getText().toString().trim().equalsIgnoreCase("")) {
                                pwd_til.setError(null);
                                if (pwd_et.getText().toString().trim().length() > 5) {
                                    pwd_til.setError(null);
                                    if (!repwd_et.getText().toString().trim().equalsIgnoreCase("")) {
                                        repwd_til.setError(null);

                                        if (pwd_et.getText().toString().trim().equals(repwd_et.getText().toString().trim()))

                                        {
                                            repwd_til.setError(null);
                                            SharedPreferences sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                            SharedPreferences.Editor edit = sharedPrefces.edit();
                                            edit.putString("fname_str", fname_str);
                                            edit.putString("lname_str", lname_str);
                                            edit.putString("pwd_str", pwd_str);
                                            edit.putString("repwd_str", repwd_str);
                                            edit.putString("wowtagid", str_wowtag);
                                            edit.commit();

                                            startActivity(new Intent(RegisterAllDetailsActivity.this, RegisterBdayActivity.class));
                                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                            finish();
                                        } else {
                                            repwd_til.setError("Password does not Match");
                                            repwd_til.requestFocus();
                                        }


                                    } else {
                                        repwd_til.setError("Enter Repwd");
                                        repwd_til.requestFocus();
                                    }


                                } else {
                                    pwd_til.setError("Password should be maximum 6 characters");
                                    pwd_til.requestFocus();
                                }
                            }
                        else {
                            pwd_til.setError("Enter Password");
                            pwd_til.requestFocus();
                        }
                        } else {
                            wowtag_til.setError("Enter Wowtag Id");
                            wowtag_til.requestFocus();
                        }
                    } else {
                        lname_til.setError("Enter LName");
                        lname_til.requestFocus();
                    }

                } else {
                    fname_til.setError("Enter FName");
                    fname_til.requestFocus();
                }


            }
        });

    }


    public class checkingwowtag extends AsyncTask<String, Void, String> {
        String phone_str, str_pwd;

        public checkingwowtag() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //av_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("tagid", str_wowtag);

                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest(Config.WEB_URL_GETCHECKTAG, json);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "tag" + s);
            //   av_loader.setVisibility(View.GONE);
            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("success");
                    if (status.equals("true")) {
                        wowtag_til.setError(null);

                    } else {
                        String msg = jo.getString("message");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        // wowtag_et.set
                        wowtag_til.setError("Wowtag Id Exists");
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                }
            } else {

            }

        }

    }


}
