package vineture.wowhubb.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gun0912.tedpicker.ImagePickerActivity;
import com.wang.avi.AVLoadingIndicatorView;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import vineture.wowhubb.Adapter.CustomAdapter;
import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.Utils.Config;
import vineture.wowhubb.Utils.HttpUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Salman on 05-10-2017.
 */

public class ProfilePersonal extends Activity {

    public final static int REQUEST_PROFILE = 1;
    public com.gun0912.tedpicker.Config img_config;
    ImageView backiv;
    EditText dobv_tv, wedding_et, anniversaries_et, social_function_et, parties_et, place_et, email_et, aboutme_et;
    LinearLayout layout_dob, layout_weddings, layout_anniversaries, layout_social, layout_parties;
    TextInputLayout til_spinmarried,bday_til, wedding_til, anniversay_til, social_til, parties_til, place_til, email_til, til_aboutme;
    TextView save_et, uname_tv;
    String personalimage, maritalstatus, profilepath, aboutme_str, name, lname, str_profile_img, bday_str, wedding_str, anniversary_str, social_str, parties_str, marital_str, place_str, username, useremail, token;
    AVLoadingIndicatorView av_loader;
    ArrayList<Uri> image_uris;
    ImageView profile_iv;
    String[] SPINNERLIST = {"Single", "Married"};
    MaterialBetterSpinner married_spn;
    SharedPreferences.Editor editor;
    private int year, month, day;
    private Calendar calendar;


    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    showDateBday(arg1, arg2 + 1, arg3);
                }
            };

    private DatePickerDialog.OnDateSetListener myDateListener1 = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    showDateWedding(arg1, arg2 + 1, arg3);
                }
            };

    private DatePickerDialog.OnDateSetListener myDateListeneranni = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    showDateAnniversaries(arg1, arg2 + 1, arg3);
                }
            };
    private DatePickerDialog.OnDateSetListener myDateListenersocial = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    showDateSocialfunction(arg1, arg2 + 1, arg3);
                }
            };
    private DatePickerDialog.OnDateSetListener myDateListenerparties = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    showDateParties(arg1, arg2 + 1, arg3);
                }
            };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(vineture.wowhubb.R.layout.activity_personalprofile);

        final Typeface lato = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lato.ttf");
        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(ProfilePersonal.this, v1);

        new getpersonalprofile().execute();

        img_config = new com.gun0912.tedpicker.Config();
        img_config.setCameraHeight(vineture.wowhubb.R.dimen.app_camera_height);
        img_config.setSelectedBottomHeight(vineture.wowhubb.R.dimen.bottom_height);
        img_config.setCameraBtnBackground(vineture.wowhubb.R.drawable.round_rd);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ProfilePersonal.this);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");
        name = sharedPreferences.getString("str_name", "");
        lname = sharedPreferences.getString("str_lname", "");
        useremail = sharedPreferences.getString("str_email", "");
        profilepath = sharedPreferences.getString("profilepath", "");

        profile_iv = findViewById(vineture.wowhubb.R.id.imageview_profile);
        av_loader = (AVLoadingIndicatorView) findViewById(vineture.wowhubb.R.id.avi);
        uname_tv = findViewById(vineture.wowhubb.R.id.username);
        backiv = findViewById(vineture.wowhubb.R.id.backiv);
        dobv_tv = findViewById(vineture.wowhubb.R.id.dob);
        wedding_et = findViewById(vineture.wowhubb.R.id.wedding_et);
        anniversaries_et = findViewById(vineture.wowhubb.R.id.anni_et);
        social_function_et = findViewById(vineture.wowhubb.R.id.socialfunction_et);
        parties_et = findViewById(vineture.wowhubb.R.id.parties_et);
        save_et = findViewById(vineture.wowhubb.R.id.save_tv);
        place_et = findViewById(vineture.wowhubb.R.id.place_et);
        email_et = findViewById(vineture.wowhubb.R.id.email_et);
        aboutme_et = findViewById(vineture.wowhubb.R.id.aboutme_et);
        til_aboutme = findViewById(vineture.wowhubb.R.id.til_aboutme);
        til_spinmarried=findViewById(vineture.wowhubb.R.id.til_spinmarried);

        bday_til = findViewById(vineture.wowhubb.R.id.til_bday);
        wedding_til = findViewById(vineture.wowhubb.R.id.til_wedding);
        anniversay_til = findViewById(vineture.wowhubb.R.id.til_ann);
        social_til = findViewById(vineture.wowhubb.R.id.til_socialfunction);
        parties_til = findViewById(vineture.wowhubb.R.id.til_parties);
        email_til = findViewById(vineture.wowhubb.R.id.til_email);
        place_til = findViewById(vineture.wowhubb.R.id.til_place);

        layout_dob = findViewById(vineture.wowhubb.R.id.layout_fromdate1);
        layout_weddings = findViewById(vineture.wowhubb.R.id.layout_wedding);
        layout_anniversaries = findViewById(vineture.wowhubb.R.id.layout_anniversaries);
        layout_social = findViewById(vineture.wowhubb.R.id.layout_socialfunction);
        layout_parties = findViewById(vineture.wowhubb.R.id.layout_parties);
        married_spn = (MaterialBetterSpinner) findViewById(vineture.wowhubb.R.id.married_spn);

        bday_til.setTypeface(lato);
        wedding_til.setTypeface(lato);
        anniversay_til.setTypeface(lato);
        social_til.setTypeface(lato);
        parties_til.setTypeface(lato);
        email_til.setTypeface(lato);
        place_til.setTypeface(lato);
        til_aboutme.setTypeface(lato);
        til_spinmarried.setTypeface(lato);
        Log.e("tag", "cbvcv" + profilepath);

        if (profilepath != null) {
            Log.e("tag", "222222222----------" + profilepath);
            Glide.with(ProfilePersonal.this).load("http://104.197.80.225:3010/wow/media/personal/" + profilepath).into(profile_iv);
        }

        final CustomAdapter arrayAdapter = new CustomAdapter(ProfilePersonal.this, android.R.layout.simple_dropdown_item_1line, SPINNERLIST) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
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
                tv.setTextSize(10);
                tv.setPadding(10, 15, 10, 15);
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
                tv.setPadding(5, 5, 5, 5);
                tv.setTypeface(lato);
                if (position == 0) {
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        married_spn.setAdapter(arrayAdapter);

        married_spn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                marital_str = adapterView.getItemAtPosition(i).toString();
            }
        });

        if (name != null) {
            uname_tv.setText(name + " " + lname);
            email_et.setText(useremail);
        }

        backiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfilePersonal.this.finish();
            }
        });
        save_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                place_str = place_et.getText().toString();
                bday_str = dobv_tv.getText().toString();
                wedding_str = wedding_et.getText().toString();
                anniversary_str = anniversaries_et.getText().toString();
                social_str = social_function_et.getText().toString();
                parties_str = parties_et.getText().toString();
                aboutme_str = aboutme_et.getText().toString();
                maritalstatus = sharedPreferences.getString("maritalstatus", "");

                if (!(!android.util.Patterns.EMAIL_ADDRESS.matcher(email_et.getText().toString()).matches())) {
                    email_til.setError(null);
                    new updateprofile().execute();
                } else {
                    email_til.setError("Invalid Email");
                    email_til.requestFocus();
                }


            }
        });


        layout_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                showDialog(100);
            }
        });

        layout_weddings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                showDialog(200);
            }
        });

        layout_anniversaries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                showDialog(300);
            }
        });

        layout_social.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                showDialog(400);
            }
        });
        layout_parties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                showDialog(500);
            }
        });

        profile_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    img_config.setSelectionMin(1);
                    img_config.setSelectionLimit(1);
                    img_config.setToolbarTitleRes(vineture.wowhubb.R.string.img_profile);
                    ImagePickerActivity.setConfig(img_config);
                    Intent intent = new Intent(ProfilePersonal.this, com.gun0912.tedpicker.ImagePickerActivity.class);
                    startActivityForResult(intent, REQUEST_PROFILE);
                }
            }
        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 100) {
            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        } else if (id == 200) {

            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(this,
                    myDateListener1, year, month, day);
        } else if (id == 300) {

            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(this,
                    myDateListeneranni, year, month, day);
        } else if (id == 400) {

            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(this,
                    myDateListenersocial, year, month, day);
        } else if (id == 500) {

            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(this,
                    myDateListenerparties, year, month, day);
        }


        return null;
    }

    private void showDateBday(int year, int month, int day) {
        dobv_tv.setText(new StringBuilder().append(month).append("/")
                .append(day).append("/").append(year));
    }

    private void showDateWedding(int year, int month, int day) {
        wedding_et.setText(new StringBuilder().append(month).append("/")
                .append(day).append("/").append(year));
    }

    private void showDateAnniversaries(int year, int month, int day) {
        anniversaries_et.setText(new StringBuilder().append(month).append("/")
                .append(day).append("/").append(year));
    }

    private void showDateSocialfunction(int year, int month, int day) {
        social_function_et.setText(new StringBuilder().append(month).append("/")
                .append(day).append("/").append(year));
    }

    private void showDateParties(int year, int month, int day) {
        parties_et.setText(new StringBuilder().append(month).append("/")
                .append(day).append("/").append(year));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<String> photos = null;
        if (requestCode == REQUEST_PROFILE && resultCode == Activity.RESULT_OK) {
            try {
                image_uris = data.getParcelableArrayListExtra(com.gun0912.tedpicker.ImagePickerActivity.EXTRA_IMAGE_URIS);
                Log.e("tag", "12345----------->>>>>>" + image_uris.get(0).toString());
            } catch (IndexOutOfBoundsException e) {
                Log.e("tag", "xgdfdg" + e.toString());
            }
            if (image_uris != null) {
                str_profile_img = image_uris.get(0).toString();
                Glide.with(ProfilePersonal.this).load(new File(str_profile_img)).into(profile_iv);
                new profileupload_task().execute();
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(ProfilePersonal.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(ProfilePersonal.this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if ((result == PackageManager.PERMISSION_GRANTED) && (result1 == PackageManager.PERMISSION_GRANTED)) {
            Log.e("tag", "Permission is granted");
            return true;
        } else {
            Log.e("tag", "Permission is revoked");
            ActivityCompat.requestPermissions(ProfilePersonal.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return false;
        }
    }

    public class updateprofile extends AsyncTask<String, Void, String> {
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

                if (!place_str.equals("")) {
                    Log.e("tag", "place1112------>" + place_str);
                    jsonObject.accumulate("place", place_str);
                } else {
                    jsonObject.accumulate("place", "null");
                }
                if (!bday_str.equals("")) {
                    Log.e("tag", "place1112------>" + place_str);
                    jsonObject.accumulate("birthday", bday_str);
                } else {
                    jsonObject.accumulate("birthday", "null");
                }

                jsonObject.accumulate("maritalstatus", marital_str);
                jsonObject.accumulate("wedding", wedding_str);
                jsonObject.accumulate("socialfunction", social_str);
                jsonObject.accumulate("parties", parties_str);
                jsonObject.accumulate("anniversary", anniversary_str);
                jsonObject.accumulate("aboutme", aboutme_str);
                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL_UPDATE_PERSONAL, json, token);
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
                            JSONObject msg = jo.getJSONObject("message");
                            Log.e("tag", "nt" + msg);
                            finish();
                            Toast.makeText(getApplicationContext(), "Profile has been updated", Toast.LENGTH_LONG).show();

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

    public class profileupload_task extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            av_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                String responseString = null;
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Config.WEB_URL_PROFILE_IMG);
                httppost.setHeader("token", token);
                HttpResponse response = null;
                HttpEntity r_entity = null;

                if (str_profile_img != null) {
                    Log.e("tag", "strt111" + str_profile_img);
                    MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                    entity.addPart("personalimage", new FileBody(new File(str_profile_img), "image/jpeg"));
                    httppost.setEntity(entity);
                }

                try {

                    try {
                        response = httpclient.execute(httppost);
                    } catch (Exception e) {
                        Log.e("tag", "ds:" + e.toString());
                    }

                    try {
                        r_entity = response.getEntity();
                    } catch (Exception e) {
                        Log.e("tag", "dsa:" + e.toString());
                    }

                    int statusCode = response.getStatusLine().getStatusCode();
                    Log.e("tag", response.getStatusLine().toString());
                    if (statusCode == 200) {
                        responseString = EntityUtils.toString(r_entity);
                        Log.e("tag", "rssss" + responseString);
                        JSONObject jo = new JSONObject(responseString);
                        String success = jo.getString("success");
                        return success;


                    } else {
                        responseString = "Error occurred! Http Status Code: "
                                + statusCode;
                        Log.e("tag3", responseString);
                    }
                } catch (ClientProtocolException e) {
                    responseString = e.toString();
                    Log.e("tag44", responseString);
                } catch (IOException e) {
                    responseString = e.toString();
                    Log.e("tag45", responseString);
                }
                return responseString;
            } catch (Exception e) {
                Log.e("tag_InputStream0", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            av_loader.setVisibility(View.GONE);

        }

    }


    public class getpersonalprofile extends AsyncTask<String, Void, String> {
        String phone_str, str_pwd;

        public getpersonalprofile() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();

                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL_GETPERSONAL, json, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "getprofile------->" + s);
            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("success");
                    if (status.equals("true")) {
                        JSONObject message = jo.getJSONObject("message");
                        String email = message.getString("email");
                        String place = message.getString("place");
                        String birthday = message.getString("birthday");
                        String wedding = message.getString("wedding");
                        String parties = message.getString("parties");
                        String anniverasaries = message.getString("anniversary");
                        String aboutme = message.getString("aboutme");
                        String socialfunction = message.getString("socialfunction");
                        String maritalstatus = message.getString("maritalstatus");

                        email_et.setText(email);
                        if (message.has("personalimage")) {
                            personalimage = message.getString("personalimage");
                            if (!personalimage.equals("")) {
                                Log.e("tag", "getprofileelseee------->" + personalimage);
                                try {
                                    Glide.with(ProfilePersonal.this).load("http://104.197.80.225:3010/wow/media/personal/" + personalimage).into(profile_iv);
                                    editor.putString("profilepath", personalimage);
                                    editor.commit();
                                } catch (IllegalArgumentException e) {

                                }
                            }
                        }
                        if (place.equals("null")) {
                            place_et.setText("");
                        } else {
                            place_et.setText(place);
                        }
                        if (birthday.equals("null")) {
                            dobv_tv.setText("");
                        } else {
                            dobv_tv.setText(birthday);
                        }

                        if (wedding.equals("null")) {
                            wedding_et.setText("");
                        } else {
                            wedding_et.setText(wedding);
                        }
                        if (anniverasaries.equals("null")) {
                            anniversaries_et.setText("");
                        } else {
                            anniversaries_et.setText(anniverasaries);
                        }
                        if (socialfunction.equals("null")) {
                            social_function_et.setText("");
                        } else {
                            social_function_et.setText(socialfunction);
                        }
                        if (parties.equals("null")) {
                            parties_et.setText("");
                        } else {
                            parties_et.setText(parties);
                        }
                        if (aboutme.equals("null")) {
                            aboutme_et.setText("");
                        } else {
                            aboutme_et.setText(aboutme);
                        }
                        if (!maritalstatus.equals("null")) {
                            if (maritalstatus.equals("Single")) {
                                married_spn.setText("Single");
                                editor.putString("maritalstatus", "Single");
                                editor.commit();
                            } else {
                                married_spn.setText("Married");
                                editor.putString("maritalstatus", "Married");
                                editor.commit();
                            }
                        }

                    }

                } catch (JSONException e) {

                }

            } else {


            }

        }

    }


}
