package vineture.wowhubb.Activity;

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
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;
import com.wang.avi.AVLoadingIndicatorView;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import vineture.wowhubb.Adapter.CustomAdapter;
import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.Utils.Config;
import vineture.wowhubb.Utils.HttpUtils;
import vineture.wowhubb.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Ramya on 18-07-2017.
 */

public class RegisterBdayActivity extends Activity implements com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener {
    String[] SPINNERLIST = {"Male", "Female"};
    Typeface latoheading, lato;
    TextView head_tv, bday_tv;
    ImageView bday_iv;
    ImageView backiv;
    String str_wowtag, str_firstname, str_lastname, str_phone, str_email, str_password, str_gender, str_birthday, str_country;
    AVLoadingIndicatorView av_loader;
    LinearLayout bday_lv;
    EditText pwd_et;
    Snackbar snackbar;
    TextView tv_snack;
    TextView submit;
    SimpleDateFormat simpleDateFormat;
    TextInputLayout til_spingender, pwd_til, bday_til;
    private int year, month, day;
    private Calendar calendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(vineture.wowhubb.R.layout.activity_register_bday);
        av_loader = (AVLoadingIndicatorView) findViewById(vineture.wowhubb.R.id.avi);

        //------------------------------FONT STYLE------------------------------------------------//
        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(RegisterBdayActivity.this, v1);
        latoheading = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/latoheading.ttf");
        lato = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lato.ttf");

        bday_lv = findViewById(vineture.wowhubb.R.id.bdaylv);
        head_tv = (TextView) findViewById(vineture.wowhubb.R.id.head_tv);
        bday_tv = (TextView) findViewById(vineture.wowhubb.R.id.bday_tv);
        bday_iv = (ImageView) findViewById(vineture.wowhubb.R.id.bday_iv);
        submit = (TextView) findViewById(vineture.wowhubb.R.id.submit_iv);
        backiv = (ImageView) findViewById(vineture.wowhubb.R.id.backiv);
        pwd_til = (TextInputLayout) findViewById(vineture.wowhubb.R.id.til_pwd);
        bday_til = (TextInputLayout) findViewById(vineture.wowhubb.R.id.til_bday);
        pwd_et = (EditText) findViewById(vineture.wowhubb.R.id.pwd_et);
        til_spingender = findViewById(vineture.wowhubb.R.id.til_spingender);


        head_tv.setTypeface(latoheading);

        til_spingender.setTypeface(lato);
        pwd_til.setTypeface(lato);

        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        //--------------------------SNACKBAR-------------------------------------------------------//
        snackbar = Snackbar.make(findViewById(vineture.wowhubb.R.id.top), vineture.wowhubb.R.string.networkError, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(lato);

        if (!Utils.Operations.isOnline(RegisterBdayActivity.this)) {
            snackbar.show();
            tv_snack.setText(vineture.wowhubb.R.string.networkError);
        }

        //-----------------------------------------------------------------------------------------//

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        str_firstname = sharedPreferences.getString("fname_str", "");
        str_lastname = sharedPreferences.getString("lname_str", "");
        str_wowtag = sharedPreferences.getString("wowtagid", "");
        str_country = sharedPreferences.getString("str_country", "");
        str_phone = sharedPreferences.getString("str_phone", "");
        str_email = sharedPreferences.getString("str_email", "");
        Log.e("tag", "assss------>" + str_country);

        final MaterialBetterSpinner materialDesignSpinner = (MaterialBetterSpinner) findViewById(vineture.wowhubb.R.id.gender_spn);
        materialDesignSpinner.setTypeface(lato);


        final CustomAdapter arrayAdapter = new CustomAdapter(RegisterBdayActivity.this, android.R.layout.simple_dropdown_item_1line, SPINNERLIST) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    SpannableString s = new SpannableString("Gender");
                    s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    materialDesignSpinner.setHint(s);
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
        // materialDesignSpinner.setTypeface(lato);

        //SpannableString s = new SpannableString("Gender");
        // s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // materialDesignSpinner.

        materialDesignSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                materialDesignSpinner.setError(null);
                materialDesignSpinner.setHintTextColor(getResources().getColor(vineture.wowhubb.R.color.colorPrimaryDark));
                FontsOverride.overrideFonts(RegisterBdayActivity.this, view);
                str_gender = adapterView.getItemAtPosition(i).toString();
            }
        });
        bday_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bday_til.setError(null);
                showDate(1980, 0, 1, vineture.wowhubb.R.style.DatePickerSpinner);
            }
        });

        backiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // startActivity(new Intent(RegisterBdayActivity.this, RegisterAllDetailsActivity.class));
                // overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FontsOverride.hideSoftKeyboard(v);
                str_birthday = bday_tv.getText().toString();
                str_password= pwd_et.getText().toString();
                if (!pwd_et.getText().toString().trim().equalsIgnoreCase("")) {
                    pwd_til.setError(null);
                    if (pwd_et.getText().toString().trim().length() > 5) {
                        pwd_til.setError(null);

                        if (!materialDesignSpinner.getText().toString().equals("")) {
                            materialDesignSpinner.setError(null);
                            if (!bday_tv.getText().toString().trim().equalsIgnoreCase("")) {
                                bday_til.setError(null);
                                new register_alldetails().execute();
                            } else {
                                bday_til.requestFocus();
                                SpannableString s = new SpannableString("Select your Birthday");
                                s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                bday_til.setError(s);
                            }
                        } else {

                            SpannableString s = new SpannableString("Select Gender");
                            s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            materialDesignSpinner.setError(s);
                        }
                    } else {
                        pwd_til.requestFocus();
                        SpannableString s = new SpannableString("Password should be 6 characters");
                        s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        pwd_til.setError(s);
                    }
                } else {
                    pwd_til.requestFocus();
                    SpannableString s = new SpannableString("Enter Password");
                    s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    pwd_til.setError(s);
                }



                /*if (!(!android.util.Patterns.EMAIL_ADDRESS.matcher(email_et.getText().toString()).matches())) {
                    email_til.setError(null);
                    {
                        if (!mobileno_et.getText().toString().trim().equalsIgnoreCase("")) {
                            mobile_til.setError(null);
                            if (!materialDesignSpinner.getText().toString().equals("")) {
                                materialDesignSpinner.setError(null);

                            } else {

                                SpannableString s = new SpannableString("Select Gender");
                                s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                materialDesignSpinner.setError(s);
                            }


                        } else {
                            //mobile_til.setError("Enter Mobile No");
                            SpannableString s = new SpannableString("Enter Mobile No");
                            s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            mobile_til.setError(s);
                        }
                    }
                } else {
                    // email_til.setError("Invalid Email");
                    SpannableString s = new SpannableString("Invalid Email");
                    s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    email_til.setError(s);

                }*/


            }
        });

    }


    private void showDate(int year, int monthOfYear, int dayOfMonth, int datePickerSpinner) {
        new SpinnerDatePickerDialogBuilder()
                .context(RegisterBdayActivity.this)
                .callback(RegisterBdayActivity.this)
                .spinnerTheme(datePickerSpinner)
                .defaultDate(year, monthOfYear, dayOfMonth)
                .build()
                .show();
    }


    @Override
    public void onDateSet(com.tsongkha.spinnerdatepicker.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        bday_tv.setText(simpleDateFormat.format(calendar.getTime()));
    }


    //------------------------------ASYNC TASK FOR REGISTER---------------------------------------//

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //  Intent intent = new Intent(RegisterBdayActivity.this, RegisterAllDetailsActivity.class);
        // startActivity(intent);
        finish();
    }

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
                jsonObject.accumulate("phone", str_country + str_phone);
                jsonObject.accumulate("email", str_email);
                jsonObject.accumulate("wowtagid", "!" + str_wowtag);
                jsonObject.accumulate("password", str_password);
                jsonObject.accumulate("birthday", str_birthday);
                jsonObject.accumulate("gender", str_gender);
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
                        SharedPreferences sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor edit = sharedPrefces.edit();
                        edit.putString("str_phone", str_country + str_phone);
                        edit.putString("str_email", str_email);
                        edit.putString("str_password", str_password);
                        edit.commit();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(RegisterBdayActivity.this, RegisterCountryActivity.class));
                        overridePendingTransition(vineture.wowhubb.R.anim.left_to_right, vineture.wowhubb.R.anim.right_to_left);
                        finish();

                    } else {
                        String msg = jo.getString("message");
                        String code = jo.getString("code");
                        if (code.equals("409")) {

                            Toast.makeText(RegisterBdayActivity.this, msg, Toast.LENGTH_LONG).show();

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
