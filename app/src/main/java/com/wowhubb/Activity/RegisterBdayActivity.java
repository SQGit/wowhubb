package com.wowhubb.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import com.wowhubb.Adapter.CustomAdapter;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;
import com.wowhubb.Utils.Config;
import com.wowhubb.Utils.HttpUtils;
import com.wowhubb.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by Ramya on 18-07-2017.
 */

public class RegisterBdayActivity extends Activity {
    String[] SPINNERLIST = {"Male", "Female"};
    Typeface latoheading, lato;
    TextView head_tv, bday_tv;
    ImageView bday_iv;
    ImageView submit, backiv;
    String str_wowtag, str_firstname, str_lastname, str_phone, str_email, str_password, str_gender, str_birthday, str_country;
    AVLoadingIndicatorView av_loader;
    LinearLayout bday_lv;
    EditText email_et, mobileno_et;
    TextInputLayout email_til, mobile_til;
    Snackbar snackbar;
    TextView tv_snack;
    private int year, month, day;
    private Calendar calendar;
    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    showDate(arg1, arg2 + 1, arg3);
                }
            };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_bday);
        av_loader = (AVLoadingIndicatorView) findViewById(R.id.avi);

        //------------------------------FONT STYLE------------------------------------------------//
        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(RegisterBdayActivity.this, v1);
        latoheading = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/latoheading.ttf");
        lato = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lato.ttf");

        bday_lv = findViewById(R.id.bdaylv);
        head_tv = (TextView) findViewById(R.id.head_tv);
        bday_tv = (TextView) findViewById(R.id.bday_tv);
        bday_iv = (ImageView) findViewById(R.id.bday_iv);
        submit = (ImageView) findViewById(R.id.submit_iv);
        backiv = (ImageView) findViewById(R.id.backiv);
        email_til = (TextInputLayout) findViewById(R.id.til_email);
        mobile_til = (TextInputLayout) findViewById(R.id.til_mobile);
        email_et = (EditText) findViewById(R.id.email_et);
        mobileno_et = (EditText) findViewById(R.id.mobile_et);
        head_tv.setTypeface(latoheading);
        email_til.setTypeface(lato);
        mobile_til.setTypeface(lato);

        //--------------------------SNACKBAR-------------------------------------------------------//
        snackbar = Snackbar.make(findViewById(R.id.top), R.string.networkError, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(lato);
        if (!Utils.Operations.isOnline(RegisterBdayActivity.this)) {
            snackbar.show();
            tv_snack.setText(R.string.networkError);
        }

        //-----------------------------------------------------------------------------------------//

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        str_firstname = sharedPreferences.getString("fname_str", "");
        str_lastname = sharedPreferences.getString("lname_str", "");
        str_password = sharedPreferences.getString("pwd_str", "");
        str_phone = sharedPreferences.getString("str_phone", "");
        str_email = sharedPreferences.getString("str_email", "");
        str_wowtag = sharedPreferences.getString("wowtagid", "");
        //str_country= sharedPreferences.getString("str_country", "");
        Log.e("tag", "assss------>" + str_country);


        str_country = "+1";
        if (!str_email.equals("")) {
            email_et.setText(str_email);
            email_et.setEnabled(false);
            mobileno_et.setText(str_country);
            mobileno_et.setCursorVisible(true);
            mobileno_et.setFocusable(true);
            mobileno_et.setSelection(mobileno_et.getText().length());
            mobileno_et.requestFocus();
        }
        if (!str_phone.equals("")) {
            mobileno_et.setText(str_country + str_phone);
            mobileno_et.setEnabled(false);
        }

        final MaterialBetterSpinner materialDesignSpinner = (MaterialBetterSpinner) findViewById(R.id.gender_spn);
        materialDesignSpinner.setTypeface(lato);

        final CustomAdapter arrayAdapter = new CustomAdapter(RegisterBdayActivity.this, android.R.layout.simple_dropdown_item_1line, SPINNERLIST) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    materialDesignSpinner.setHint("ggggggggg");
                    return true;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTypeface(lato);
                tv.setTextSize(14);
                tv.setPadding(30, 55, 10, 25);
                if (position == 0) {
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }


            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextSize(14);
                tv.setPadding(10, 20, 0, 20);
                tv.setTypeface(lato);
                if (position == 0) {
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        materialDesignSpinner.setAdapter(arrayAdapter);
        materialDesignSpinner.setTypeface(lato);

        materialDesignSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                materialDesignSpinner.setHintTextColor(getResources().getColor(R.color.colorPrimaryDark));
                FontsOverride.overrideFonts(RegisterBdayActivity.this, view);
                str_gender = adapterView.getItemAtPosition(i).toString();
            }
        });
        bday_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(999);
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
                str_birthday = bday_tv.getText().toString();
                str_email = email_et.getText().toString();
                str_phone = mobileno_et.getText().toString();

                if (!(!android.util.Patterns.EMAIL_ADDRESS.matcher(email_et.getText().toString()).matches())) {
                    email_til.setError(null);
                    {
                        if (!mobileno_et.getText().toString().trim().equalsIgnoreCase("")) {
                            mobile_til.setError(null);
                            if (!materialDesignSpinner.getText().toString().equals("")) {
                                materialDesignSpinner.setError(null);
                                new register_alldetails().execute();
                            } else {
                                materialDesignSpinner.setError("Select Gender");
                                materialDesignSpinner.setTypeface(lato);
                            }


                        } else {
                            mobile_til.setError("Enter Mobile No");
                        }
                    }
                } else {
                    email_til.setError("Invalid Email");
                }


            }
        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private void showDate(int year, int month, int day) {
        bday_tv.setText(new StringBuilder().append(month).append("/")
                .append(day).append("/").append(year));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegisterBdayActivity.this, RegisterCountryActivity.class);
        startActivity(intent);
    }

    @SuppressLint("ValidFragment")
    public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);

        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm + 1, dd);

        }

        public void populateSetDate(int year, int month, int day) {
            //edittext_offdate.setText(month+"/"+day+"/"+year);
            //  .setText(year + "/" + month + "/" + day);

        }
    }

    //------------------------------ASYNC TASK FOR REGISTER---------------------------------------//

    public class register_alldetails extends AsyncTask<String, Void, String> {
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
                jsonObject.accumulate("firstname", str_firstname);
                jsonObject.accumulate("lastname", str_lastname);
                jsonObject.accumulate("phone", str_phone);
                jsonObject.accumulate("email", str_email);
                jsonObject.accumulate("wowtagid", str_wowtag);
                jsonObject.accumulate("password", str_password);
                //jsonObject.accumulate("gender", str_gender);
                jsonObject.accumulate("birthday", str_birthday);

                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest(Config.WEB_URL_SIGNUP, json);
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
                        String message = jo.getString("message");
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(RegisterBdayActivity.this, LoginActivity.class));
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                        finish();
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
