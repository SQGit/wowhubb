package vineture.wowhubb.wowtag.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import vineture.wowhubb.Activity.LandingPageActivity;
import vineture.wowhubb.Adapter.CustomAdapter;
import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.Utils.Config;
import vineture.wowhubb.Utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Guna on 23-03-2018.
 */

public class WowtagRsvp extends Activity {
    SharedPreferences.Editor editor;
    String token,get_wowid,get_wowname,str_person;
    Map<String,String> wowtag_hash=new HashMap<>();
    Set<String> keys;
    ArrayList<String> list=new ArrayList<>();
    TextView wowtagid,user_name,user_wowtagid,event_name,wowtag_event_time,wowtag_date,eventvenue,address;
    CircleImageView pro_img;
    Button btn_rsvp;
    VideoView wowtag_video;
    private Uri uri;
    LinearLayout img_back,viewmore,lnr_wowtag_reminder;
    ImageView wowtag_calendar;
    Dialog dialog;
    MaterialBetterSpinner persons_spn;
    TextInputLayout til_spinpersons;
    String[] ATTEN_PERSON = {"1 Person", "2 Persons", "3 Persons","4 Persons","5 Persons","6 Persons","7 Persons"};
    Typeface segoeui;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(vineture.wowhubb.R.layout.wowtag_rsvp);


        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(WowtagRsvp.this, v1);


        //token get from Login Activity:
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");



        //This intent get from WowFragment Class:
        Intent intent = getIntent();
        get_wowid = intent.getStringExtra("wowtag_id");
        get_wowname = intent.getStringExtra("wowtag_name");

        wowtagid=findViewById(vineture.wowhubb.R.id.wowtagid);
        user_name=findViewById(vineture.wowhubb.R.id.user_name);
        user_wowtagid=findViewById(vineture.wowhubb.R.id.user_wowtagid);
        pro_img=findViewById(vineture.wowhubb.R.id.pro_img);
        event_name=findViewById(vineture.wowhubb.R.id.event_name);
        btn_rsvp=findViewById(vineture.wowhubb.R.id.btn_rsvp);
        wowtag_video=findViewById(vineture.wowhubb.R.id.wowtag_video);
        img_back=findViewById(vineture.wowhubb.R.id.img_back);
        wowtag_event_time=findViewById(vineture.wowhubb.R.id.wowtag_event_time);
        wowtag_date=findViewById(vineture.wowhubb.R.id.wowtag_date);
        eventvenue=findViewById(vineture.wowhubb.R.id.eventvenue);
        address=findViewById(vineture.wowhubb.R.id.address);
        wowtag_calendar=findViewById(vineture.wowhubb.R.id.wowtag_calendar);
        viewmore=findViewById(vineture.wowhubb.R.id.viewmore);
        lnr_wowtag_reminder=findViewById(vineture.wowhubb.R.id.lnr_wowtag_reminder);

        segoeui = Typeface.createFromAsset(getAssets(), "fonts/segoeui.ttf");





        btn_rsvp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(WowtagRsvp.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(vineture.wowhubb.R.layout.rsvp_attending_person);
                View v1 = dialog.getWindow().getDecorView().getRootView();
                LinearLayout close = dialog.findViewById(vineture.wowhubb.R.id.closeiv);
                FontsOverride.overrideFonts(dialog.getContext(), v1);


                TextView head,msg;
                Button register;

                persons_spn=v1.findViewById(vineture.wowhubb.R.id.persons_spn);
                til_spinpersons=v1.findViewById(vineture.wowhubb.R.id.til_spinpersons);
                head=v1.findViewById(vineture.wowhubb.R.id.head);
                msg=v1.findViewById(vineture.wowhubb.R.id.msg);
                register=v1.findViewById(vineture.wowhubb.R.id.register);

                head.setTypeface(segoeui);
                msg.setTypeface(segoeui);
                register.setTypeface(segoeui);
                persons_spn.setTypeface(segoeui);
                til_spinpersons.setTypeface(segoeui);


                // Spinner Adapter:
                final CustomAdapter eventdayAdapter = new CustomAdapter(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, ATTEN_PERSON) {
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
                        tv.setTypeface(segoeui);
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
                        tv.setTypeface(segoeui);
                        if (position == 0) {
                            tv.setTextColor(Color.BLACK);
                        } else {
                            tv.setTextColor(Color.BLACK);
                        }
                        return view;
                    }
                };
                persons_spn.setAdapter(eventdayAdapter);



                persons_spn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        FontsOverride.overrideFonts(getApplicationContext(), view);
                        str_person = adapterView.getItemAtPosition(i).toString();

                        Log.e("tag", "str_person------>" + str_person);


                    }
                });


                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


                register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(str_person!=null)
                        {
                            Toast.makeText(getApplicationContext(),"Successfully Registered for the Event!",Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Please Select the Attending Person!",Toast.LENGTH_LONG).show();
                        }


                    }
                });

                dialog.show();

            }
        });


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),LandingPageActivity.class);
                startActivity(i);
                finish();
            }
        });


        wowtag_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Under Development",Toast.LENGTH_LONG).show();
            }
        });



        viewmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Under Development",Toast.LENGTH_LONG).show();
            }
        });


        lnr_wowtag_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        //call Particular WOWTAG List:
        new callParticularWowtag().execute();



    }

    //Describe Asynctask for Particular WOWTAG List:
    private class callParticularWowtag extends AsyncTask<String, String, String> {


        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... strings) {

            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("eventid", get_wowid);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL_FRTCH_WOWTAGID, json, token);
            } catch (Exception e) {

            }
            return null;
        }


        @Override
        protected void onPostExecute(String jsonstr) {
            super.onPostExecute(jsonstr);
            if (jsonstr.equals("")) {
                Toast.makeText(getApplicationContext(), "Check Network Connection", Toast.LENGTH_LONG).show();
            } else {

                try {

                    JSONObject jo = new JSONObject(jsonstr);
                    String status = jo.getString("success");
                    if (status.equals("true")) {

                        JSONObject jsonObject = jo.getJSONObject("event");
                        Log.e("tag", "wowtag0" + jsonObject);

                        JSONObject user_det=jsonObject.getJSONObject("userid");
                        user_name.setText(user_det.getString("firstname"));
                        user_wowtagid.setText(user_det.getString("wowtagid"));
                        String profile_img=user_det.getString("personalimage");
                        Log.e("tag","profile"+profile_img);

                        Glide.with(getApplicationContext()).load("http://104.197.80.225:3010/wow/media/personal/"+ profile_img)
                                .into(pro_img);


                        event_name.setText(jsonObject.getString("eventname"));

                        String str_wowtag_video=jsonObject.getString("wowtagvideourl");

                        if(!str_wowtag_video.equals("null"))
                        {
                            Log.e("tag","NOT NULL");
                           // wowtag_video.setVideoURI(Uri.parse("http://104.197.80.225:3010/wow/media/event/"+str_wowtag_video));
                            wowtag_video.setVideoURI(Uri.parse(str_wowtag_video));
                        }
                        else
                        {
                            Log.e("tag","NULL");
                            wowtag_video.setPreviewImage(vineture.wowhubb.R.drawable.no_video);

                        }



                        wowtagid.setText(jsonObject.getString("eventtitle"));

                        String e_start_date = jsonObject.getString("eventstartdate");
                        Log.e("tag", "wowtag2" + jsonObject.getString("eventstartdate"));
                        String e_end_date = jsonObject.getString("eventenddate");
                        Log.e("tag", "wowtag3" + jsonObject.getString("eventenddate"));

                        try {
                            String[] splited_start = e_start_date.split("\\s+");
                            String date1 = splited_start[0];
                            String time1 = splited_start[1];
                            String ampm1 = splited_start[2];

                            String[] splitdate_from=date1.split("/");
                            String dd1=splitdate_from[0];
                            String mm1=splitdate_from[1];
                            String yy1=splitdate_from[2];


                            String[] splited_end = e_end_date.split("\\s+");
                            String date2 = splited_end[0];
                            String time2 = splited_end[1];
                            String ampm2 = splited_end[2];

                            String[] splitdate_to=date2.split("/");
                            String dd2=splitdate_to[0];
                            String mm2=splitdate_to[1];
                            String yy2=splitdate_to[2];


                            Log.e("tag","print val"+date1+"  "+time1+"   "+date2+"  "+time2);

                            wowtag_event_time.setText(time1 + " AM-" + time2+" PM");



                            wowtag_date.setText(mm1+"/"+yy1+"/"+dd1+ " to "+mm2+"/"+yy2+"/"+dd2);

                            JSONArray event=jsonObject.getJSONArray("eventvenue");
                            Log.e("tag","JsonArray"+event.length());

                            if (event.length() > 0) {


                                for (int g = 0; g < event.length(); g++) {
                                    JSONObject data=event.getJSONObject(g);
                                    String eventvenuecity=data.getString("eventvenuecity");
                                    String eventvenueaddress1=data.getString("eventvenueaddress1");
                                    String eventvenuezipcode=data.getString("eventvenuezipcode");

                                    eventvenue.setText(data.getString("eventvenuename"));
                                    address.setText(eventvenueaddress1+" ,"+eventvenuecity+" ,"+eventvenuezipcode);
                                }
                            }


                        } catch (ArrayIndexOutOfBoundsException e) {

                        }




                    } else {

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(getApplicationContext(),LandingPageActivity.class);
        startActivity(i);
        finish();
    }
}

