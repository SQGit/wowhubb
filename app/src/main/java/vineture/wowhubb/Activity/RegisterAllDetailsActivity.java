package vineture.wowhubb.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbb20.CountryCodePicker;
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

public class RegisterAllDetailsActivity extends Activity {
    Typeface latoheading, lato;
    TextView head_tv;
    TextInputLayout fname_til, lname_til, wowtag_til;
    ImageView submit, backiv;
    EditText fname_et, lname_et, wowtag_et;
    String str_country, str_firstname, str_lastname, str_phone, str_email, str_password, str_gender, str_birthday;
    String str_wowtag;
    Snackbar snackbar;
    TextView tv_snack;
    LinearLayout question;
    Dialog dialog;
    EditText email_et, mobileno_et;
    TextInputLayout email_til, mobile_til;
    CountryCodePicker countryCodePicker;


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
        wowtag_til = (TextInputLayout) findViewById(R.id.wowtag_til);

        submit = (ImageView) findViewById(R.id.submit_iv);
        backiv = (ImageView) findViewById(R.id.backiv);

        fname_et = (EditText) findViewById(R.id.fname_et);
        lname_et = (EditText) findViewById(R.id.lname_et);


        wowtag_et = findViewById(R.id.wowtag_et);
        question = (LinearLayout) findViewById(R.id.question);
        // wowtag_et.addTextChangedListener(passwordWatcher);
        email_til = (TextInputLayout) findViewById(R.id.til_email);
        mobile_til = (TextInputLayout) findViewById(R.id.til_mobile);
        email_et = (EditText) findViewById(R.id.email_et);
        mobileno_et = (EditText) findViewById(R.id.mobile_et);
        countryCodePicker = findViewById(R.id.ccp);

        head_tv.setTypeface(latoheading);
        fname_til.setTypeface(lato);
        lname_til.setTypeface(lato);

        wowtag_til.setTypeface(lato);
        email_til.setTypeface(lato);
        mobile_til.setTypeface(lato);
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
                startActivity(new Intent(RegisterAllDetailsActivity.this, SplashActivity.class));
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
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
                // wowtag_et.setText("!");
                wowtag_et.setSelection(wowtag_et.getText().length());
                wowtag_et.setCursorVisible(true);
                return false;
            }


        });

     /*   pwd_et.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                str_wowtag = wowtag_et.getText().toString();
                if (!str_wowtag.equals("")) {
                    new checkingwowtag().execute();
                }
                return false;
            }
        });*/


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

        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(RegisterAllDetailsActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_wowtagid);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                View v1 = dialog.getWindow().getDecorView().getRootView();

                ImageView close = dialog.findViewById(R.id.closeiv);
                //TextView save_tv= dialog.findViewById(R.id.save_tv);

                FontsOverride.overrideFonts(dialog.getContext(), v1);
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Window view1 = ((Dialog) dialog).getWindow();

                    }
                });

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FontsOverride.hideSoftKeyboard(v);
                String fname_str = fname_et.getText().toString();
                String lname_str = lname_et.getText().toString();

                str_wowtag = wowtag_et.getText().toString();
                str_email = email_et.getText().toString();
                str_phone = mobileno_et.getText().toString();
                str_country = countryCodePicker.getSelectedCountryCodeWithPlus();
                Log.e("tag", "fcvjchjfdh---------------------------" + str_country + str_phone);
                Log.e("tag", "fcvjchjfdh---------------------------" + str_country);

                if (!fname_et.getText().toString().trim().equalsIgnoreCase("")) {
                    fname_til.setError(null);
                    if (!lname_et.getText().toString().trim().equalsIgnoreCase("")) {
                        lname_til.setError(null);
                        if(!(!android.util.Patterns.EMAIL_ADDRESS.matcher(email_et.getText().toString()).matches())) {
                            email_til.setError(null);
                            if (!mobileno_et.getText().toString().trim().equalsIgnoreCase("")) {
                                mobile_til.setError(null);
                                if (!wowtag_et.getText().toString().trim().equalsIgnoreCase("")) {
                                    wowtag_til.setError(null);

                                    SharedPreferences sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    SharedPreferences.Editor edit = sharedPrefces.edit();
                                    edit.putString("fname_str", fname_str);
                                    edit.putString("lname_str", lname_str);
                                    edit.putString("wowtagid", str_wowtag);
                                    edit.putString("str_email", str_email);
                                    edit.putString("str_phone", str_phone);
                                    edit.putString("str_country", str_country);
                                    edit.commit();
                                    startActivity(new Intent(RegisterAllDetailsActivity.this, RegisterBdayActivity.class));
                                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);


                                } else {
                                    wowtag_til.requestFocus();
                                    SpannableString s = new SpannableString("Enter Wowtag Id");
                                    s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    wowtag_til.setError(s);
                                }

                            } else {
                                //mobile_til.setError("Enter Mobile No");
                                SpannableString s = new SpannableString("Enter Mobile No");
                                s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                mobile_til.setError(s);
                            }
                        }
                        else {
                                // email_til.setError("Invalid Email");
                                SpannableString s = new SpannableString("Invalid Email");
                                s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                email_til.setError(s);

                            }
                    } else {

                        lname_til.requestFocus();
                        lname_til.setTypeface(lato);
                        SpannableString s = new SpannableString("Enter Last Name");
                        s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        lname_til.setError(s);
                    }

                } else {

                    fname_til.requestFocus();
                    fname_til.setTypeface(lato);
                    SpannableString s = new SpannableString("Enter First Name");
                    s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    fname_til.setError(s);
                }


            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegisterAllDetailsActivity.this, SplashActivity.class);
        startActivity(intent);
        finish();
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
                jsonObject.accumulate("tagid", "!" + str_wowtag);

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
                        wowtag_til.setError("Wowtag Id Exists");
                        wowtag_til.setTypeface(lato);
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

    public class TypefaceSpan extends MetricAffectingSpan {
        private Typeface mTypeface;

        public TypefaceSpan(Typeface typeface) {
            mTypeface = typeface;
        }

        @Override
        public void updateMeasureState(TextPaint p) {
            p.setTypeface(mTypeface);
            p.setFlags(p.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }

        @Override
        public void updateDrawState(TextPaint tp) {
            tp.setTypeface(mTypeface);
            tp.setFlags(tp.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
    }
}
