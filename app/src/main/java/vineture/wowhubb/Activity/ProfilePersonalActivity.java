package vineture.wowhubb.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vineture.wowhubb.Adapter.CustomAdapter;
import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.Profile.Message;
import vineture.wowhubb.Profile.Profile;
import vineture.wowhubb.R;
import vineture.wowhubb.Utils.ApiClient;
import vineture.wowhubb.Utils.ApiInterface;
import vineture.wowhubb.Utils.HttpUtils;

/**
 * Created by Salman on 05-10-2017.
 */

public class ProfilePersonalActivity extends Activity {
    private static Retrofit retrofit = null;
    ApiInterface apiInterface;
    SharedPreferences.Editor editor;
    String token, str_relationship, relationshipwith, state, country;
    ImageView cityiv, contactiv, relationshipiv, basicinfoiv;
    LinearLayout intro_lv, city_lv, contact_lv, relationship_lv, basicinfo_lv, relationship_edit;
    LinearLayout addintro_lv, addcity_lv, addcontact_lv, addrelationship_lv, addbasicinfo_lv;
    MaterialBetterSpinner relationship_spn;
    Dialog dialog, d, loader_dialog;
    TextView backtv, statetv, countrytv, emailtv, phonetv, bdaytv, gendertv, wowtagtv, relationshipstatustv, relationshipwithtv;
    String[] RELATIONSHIPLIST = {"Single", "In a Relationship", "Encaged", "Married", "Divorced", "Widowed"};

    TextInputLayout relationship_til, with_til;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personl_profile);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ProfilePersonalActivity.this);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");

        final Typeface lato = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lato.ttf");
        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(ProfilePersonalActivity.this, v1);

        backtv = findViewById(R.id.backtv);

        addintro_lv = findViewById(R.id.addintro_lv);
        addcity_lv = findViewById(R.id.addcity_lv);
        addcontact_lv = findViewById(R.id.addcontact_lv);
        addrelationship_lv = findViewById(R.id.addrelationship_lv);
        addbasicinfo_lv = findViewById(R.id.addbasicinfo_lv);

        intro_lv = findViewById(R.id.intro_lv);
        city_lv = findViewById(R.id.city_lv);
        contact_lv = findViewById(R.id.contact_lv);
        relationship_lv = findViewById(R.id.relationship_lv);
        basicinfo_lv = findViewById(R.id.basicinfo_lv);

        statetv = findViewById(R.id.statetv);
        countrytv = findViewById(R.id.countrytv);
        wowtagtv = findViewById(R.id.wowtagtv);
        relationshipstatustv = findViewById(R.id.relationshiptv);
        relationshipwithtv = findViewById(R.id.relationshipwithtv);
        relationship_edit = findViewById(R.id.relationship_edit);

        emailtv = findViewById(R.id.emailtv);
        phonetv = findViewById(R.id.phonetv);
        bdaytv = findViewById(R.id.bdytv);
        gendertv = findViewById(R.id.gendertv);

        cityiv = findViewById(R.id.city_edit);
        basicinfoiv = findViewById(R.id.basicinfo_edit);

        loader_dialog = new Dialog(ProfilePersonalActivity.this);
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(vineture.wowhubb.R.layout.test_loader);

        final CustomAdapter arrayAdapter = new CustomAdapter(ProfilePersonalActivity.this, android.R.layout.simple_dropdown_item_1line, RELATIONSHIPLIST) {
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
                tv.setTextSize(9);
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
                tv.setTextSize(15);
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
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        connectAndGetApiData();

        relationship_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d = new Dialog(ProfilePersonalActivity.this);
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                d.setContentView(R.layout.dialog_profilerelationship);
                d.setCanceledOnTouchOutside(false);
                ImageView close = d.findViewById(R.id.closeiv);
                final EditText relationshipwith_et = d.findViewById(R.id.with_et);
                TextView save = d.findViewById(R.id.save_tv);
                with_til = d.findViewById(R.id.til_with);
                relationship_spn = (MaterialBetterSpinner) d.findViewById(R.id.relationship_spn);
                relationship_spn.setAdapter(arrayAdapter);

                relationship_spn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        str_relationship = adapterView.getItemAtPosition(i).toString();
                        Log.e("tag", "str_category------>" + str_relationship);
                        if (str_relationship.equals("Single")) {
                            with_til.setVisibility(View.INVISIBLE);

                        } else {
                            with_til.setVisibility(View.VISIBLE);
                        }

                    }
                });

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });

                d.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Window view1 = ((Dialog) d).getWindow();
                        view1.setBackgroundDrawableResource(R.drawable.border_graybg);
                    }
                });

                View v2 = d.getWindow().getDecorView().getRootView();
                FontsOverride.overrideFonts(ProfilePersonalActivity.this, v2);
                d.show();


                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        relationshipwith = relationshipwith_et.getText().toString();
                        if (str_relationship != null) {
                            new updateRelationship().execute();
                        } else {
                            SpannableString s = new SpannableString("Select Relationship");
                            s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                        }

                    }
                });


            }
        });


        cityiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               d = new Dialog(ProfilePersonalActivity.this);
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                d.setContentView(R.layout.dialog_profilecity);
                d.setCanceledOnTouchOutside(false);
                ImageView close = d.findViewById(R.id.closeiv);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });
                TextView save = d.findViewById(R.id.save_tv);
                final EditText country_et = d.findViewById(R.id.country_et);
                final EditText state_et = d.findViewById(R.id.state_et);
                d.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Window view1 = ((Dialog) d).getWindow();
                        view1.setBackgroundDrawableResource(R.drawable.border_graybg);
                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        country = country_et.getText().toString();
                        state = state_et.getText().toString();
                        if (country != null) {
                            new updateCountry().execute();
                        } else {
                            SpannableString s = new SpannableString("Enter Country");
                            s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                        }

                    }
                });
                d.show();

            }
        });


        //-----------------------------------BACK NAVIGATION--------------------------------------//

        backtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //-----------------------------ADD CURRENTCITY---------------------------------------//

        addcity_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               d = new Dialog(ProfilePersonalActivity.this);
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                d.setContentView(R.layout.dialog_profilecity);
                d.setCanceledOnTouchOutside(false);
                ImageView close = d.findViewById(R.id.closeiv);
                final EditText country_et = d.findViewById(R.id.country_et);
                final EditText state_et = d.findViewById(R.id.state_et);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });
                TextView save = d.findViewById(R.id.save_tv);
                d.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Window view1 = ((Dialog) d).getWindow();
                        view1.setBackgroundDrawableResource(R.drawable.border_graybg);
                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        country = country_et.getText().toString();
                        state = state_et.getText().toString();
                        if (country != null) {
                            new updateCountry().execute();
                        } else {
                            SpannableString s = new SpannableString("Enter Country");
                            s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                        }

                    }
                });
                View v2 = d.getWindow().getDecorView().getRootView();
                FontsOverride.overrideFonts(ProfilePersonalActivity.this, v2);
                d.show();
            }
        });
        //-----------------------------ADD RELATIONSHIP---------------------------------------//

        addrelationship_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d = new Dialog(ProfilePersonalActivity.this);
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                d.setContentView(R.layout.dialog_profilerelationship);
                d.setCanceledOnTouchOutside(false);
                ImageView close = d.findViewById(R.id.closeiv);
                final EditText relationshipwith_et = d.findViewById(R.id.with_et);

                TextView save = d.findViewById(R.id.save_tv);

                with_til = d.findViewById(R.id.til_with);
                relationship_spn = (MaterialBetterSpinner) d.findViewById(R.id.relationship_spn);
                relationship_spn.setAdapter(arrayAdapter);

                relationship_spn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        str_relationship = adapterView.getItemAtPosition(i).toString();
                        Log.e("tag", "str_category------>" + str_relationship);
                        if (str_relationship.equals("Single")) {
                            with_til.setVisibility(View.INVISIBLE);

                        } else {
                            with_til.setVisibility(View.VISIBLE);
                        }

                    }
                });

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });

                d.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Window view1 = ((Dialog) d).getWindow();
                        view1.setBackgroundDrawableResource(R.drawable.border_graybg);
                    }
                });

                View v2 = d.getWindow().getDecorView().getRootView();
                FontsOverride.overrideFonts(ProfilePersonalActivity.this, v2);
                d.show();


                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        relationshipwith = relationshipwith_et.getText().toString();
                        if (str_relationship != null) {
                            new updateRelationship().execute();
                        } else {
                            SpannableString s = new SpannableString("Select Relationship");
                            s.setSpan(new FontsOverride.TypefaceSpan(lato), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        });
        //-----------------------------ADD BASIC INFO---------------------------------------//

        addbasicinfo_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog d = new Dialog(ProfilePersonalActivity.this);
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                d.setContentView(R.layout.dialog_profilebasicinfo);
                d.setCanceledOnTouchOutside(false);
                ImageView close = d.findViewById(R.id.closeiv);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });

                d.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Window view1 = ((Dialog) d).getWindow();
                        view1.setBackgroundDrawableResource(R.drawable.border_graybg);
                    }
                });

                View v2 = d.getWindow().getDecorView().getRootView();
                FontsOverride.overrideFonts(ProfilePersonalActivity.this, v2);
                d.show();
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

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(ProfilePersonalActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(ProfilePersonalActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if ((result == PackageManager.PERMISSION_GRANTED) && (result1 == PackageManager.PERMISSION_GRANTED)) {
            Log.e("tag", "Permission is granted");
            return true;
        } else {
            Log.e("tag", "Permission is revoked");
            ActivityCompat.requestPermissions(ProfilePersonalActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return false;
        }
    }

    public void connectAndGetApiData() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://104.197.80.225:3010/wow/user/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        ApiInterface movieApiService = retrofit.create(ApiInterface.class);

        Call<Profile> call = movieApiService.getProfile("application/x-www-form-urlencoded", token);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                Log.e("tag", "888888888 " + response.body());


                Message profileList = response.body().getMessage();
                //     gridView.setAdapter(new TodayEventsAdapter(movies, TodayEventActivity.this));

                Log.e("tag", "Number of movies received: " + profileList);

                Log.e("tag", "marital WOWATAADSHJDHJD: " + profileList.getWowtagid());

                if (profileList.getWowtagid() != null) {
                    wowtagtv.setText(profileList.getWowtagid());
                }
                if (profileList.getCountry() != null) {

                    addcity_lv.setVisibility(View.GONE);
                    city_lv.setVisibility(View.VISIBLE);
                    countrytv.setText(profileList.getCountry());
                }
                if (profileList.getState() != null) {
                    city_lv.setVisibility(View.VISIBLE);
                    addcity_lv.setVisibility(View.GONE);
                    statetv.setText(profileList.getState());
                }
                if (profileList.getEmail() != null) {
                    contact_lv.setVisibility(View.VISIBLE);
                    addcontact_lv.setVisibility(View.GONE);
                    emailtv.setText(profileList.getEmail());
                }
                if (profileList.getPhone() != null) {
                    contact_lv.setVisibility(View.VISIBLE);
                    addcontact_lv.setVisibility(View.GONE);
                    phonetv.setText(profileList.getPhone());
                }
                if (profileList.getBirthday() != null) {
                    basicinfo_lv.setVisibility(View.VISIBLE);
                    addbasicinfo_lv.setVisibility(View.GONE);
                    bdaytv.setText(profileList.getBirthday());
                    gendertv.setText(profileList.getGender());
                }
                if (profileList.getRelationshipstatus() != null) {
                    if (profileList.getRelationshipstatus().length() > 0) {
                        relationship_lv.setVisibility(View.VISIBLE);
                        addrelationship_lv.setVisibility(View.GONE);
                        relationshipstatustv.setText(profileList.getRelationshipstatus());
                        if (profileList.getRelationshipwith().length() > 0) {
                            relationship_lv.setVisibility(View.VISIBLE);
                            addrelationship_lv.setVisibility(View.GONE);
                            relationshipwithtv.setText("With " + profileList.getRelationshipwith());

                        }
                    }

                }


            }

            @Override
            public void onFailure(Call<Profile> call, Throwable throwable) {
                Log.e("tag", throwable.toString());
            }
        });
    }

    public class updateRelationship extends AsyncTask<String, Void, String> {

        public updateRelationship() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader_dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("relationshipstatus", str_relationship);
                jsonObject.accumulate("relationshipwith", relationshipwith);
                Log.e("tag", "jsArray--------------->>>" + jsonObject);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1("http://104.197.80.225:3010/wow/user/updaterelationshipstatus", json, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "tag" + s);
            loader_dialog.dismiss();
            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("success");
                    if (status.equals("true")) {
                        d.dismiss();
                        startActivity(new Intent(ProfilePersonalActivity.this, ProfilePersonalActivity.class));
                        finish();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {

            }

        }

    }

    public class updateCountry extends AsyncTask<String, Void, String> {

        public updateCountry() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader_dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("country", country);
                jsonObject.accumulate("state", state);
                Log.e("tag", "jsArray--------------->>>" + jsonObject);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1("http://104.197.80.225:3010/wow/user/profilecontact", json, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "tag" + s);
            loader_dialog.dismiss();
            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("success");
                    if (status.equals("true")) {
                        d.dismiss();
                        startActivity(new Intent(ProfilePersonalActivity.this, ProfilePersonalActivity.class));
                        finish();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {

            }

        }

    }


}
