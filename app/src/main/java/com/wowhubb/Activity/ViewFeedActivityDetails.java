package com.wowhubb.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wowhubb.FeedsData.Programschedule;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;
import com.wowhubb.app.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Salman on 27-10-2017.
 */

public class ViewFeedActivityDetails extends AppCompatActivity {

    public static int eventdayscount;
    static Bitmap bitmap;
    public ImageLoader imageLoader1;
    String fulladdress, profilepicture, timestamp_str, description, eventname, eventdate, status, coverimage, str_position;
    ImageView videoView;
    FrameLayout frameLayout;
    ImageView video1plus, video2plus, video3plus, closeiv;
    SharedPreferences sharedPrefces;
    SharedPreferences.Editor edit;
    String eventtype, wowtagvideo, highligh1, highligh2, gifturl, donationurl, eventvenueaddress;
    TextView eventshedule_tv, eventdiscussion_tv;
    LinearLayout eventtours_lv;

    public static Bitmap retriveVideoFrameFromVideo(String videoPath)
            throws Throwable {
        bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewmore_dialog_image);

        final ArrayList<Programschedule> programschedules = getIntent().getParcelableArrayListExtra("program");
        sharedPrefces = PreferenceManager.getDefaultSharedPreferences(ViewFeedActivityDetails.this);
        edit = sharedPrefces.edit();
        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(ViewFeedActivityDetails.this, v1);

        videoView = findViewById(R.id.feedImage1);

        TextView timestamp = (TextView) findViewById(R.id.timestamp);
        TextView desc = (TextView) findViewById(R.id.desc_tv);
        TextView datetv = (TextView) findViewById(R.id.datetv);
        TextView ticketurltv = (TextView) findViewById(R.id.ticketurl_tv);
        TextView donatiionurltv = (TextView) findViewById(R.id.donationurl_tv);
        TextView addresstv = (TextView) findViewById(R.id.address_tv);
        TextView ticketrurl_tv = (TextView) findViewById(R.id.ticketurl_desc_tv);
        TextView donationurl_tv = (TextView) findViewById(R.id.donationturl_desc_tv);
        eventshedule_tv = findViewById(R.id.tv_eventschedule);
        eventdiscussion_tv = findViewById(R.id.tv_eventdiscussion);
        eventtours_lv = findViewById(R.id.eventtours_lv);
        closeiv = findViewById(R.id.close_iv);


        frameLayout = findViewById(R.id.framevideo1);
        ImageView highlight1_iv = findViewById(R.id.highlight1);
        ImageView highlight2_iv = findViewById(R.id.highlight2);
        video1plus = findViewById(R.id.video1plus_iv);
        video2plus = findViewById(R.id.video2plus_iv);
        video3plus = findViewById(R.id.video3plus_iv);
        ImageView wowtagvideo1 = findViewById(R.id.video0_iv);
        ImageView feedImageView = (ImageView) findViewById(R.id.feedImage1);


        closeiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            eventvenueaddress = extras.getString("eventvenueaddress");
            description = extras.getString("description");
            status = extras.getString("status");
            eventname = extras.getString("eventname");
            str_position = extras.getString("str_position");
            eventdate = extras.getString("eventstartdate");
            coverimage = extras.getString("coverpage");
            highligh1 = extras.getString("highlight1");
            highligh2 = extras.getString("highlight2");
            wowtagvideo = extras.getString("wowtagvideo");
            gifturl = extras.getString("gifturl");
            donationurl = extras.getString("donationurl");
            eventdayscount = extras.getInt("eventdayscount");

            eventtype = extras.getString("eventtype");

            Log.e("tag", "wowtagvideo-------" + wowtagvideo);
            Log.e("tag", "Imagge22222222-------" + gifturl + highligh1);


            if (description != null && !description.equals("null")) {
                desc.setText(description);
            }
            if (eventdate != null && !eventdate.equals("null")) {
                datetv.setText(eventdate);
            }
            if (eventvenueaddress != null && !eventvenueaddress.equals("null")) {
                addresstv.setText(eventvenueaddress);
            }

            if (eventtype != null && !eventtype.equals("null")) {

                if (eventtype.equals("personal_event")) {
                    eventtours_lv.setVisibility(View.GONE);

                    if (donationurl != null && !donationurl.equals("")) {
                        donationurl_tv.setVisibility(View.VISIBLE);
                        donatiionurltv.setVisibility(View.VISIBLE);
                        donationurl_tv.setText(donationurl);
                    } else {
                        donationurl_tv.setVisibility(View.GONE);
                        donatiionurltv.setVisibility(View.GONE);
                    }

                    if (gifturl != null && !gifturl.equals("")) {
                        ticketrurl_tv.setVisibility(View.VISIBLE);
                        ticketurltv.setVisibility(View.VISIBLE);
                        ticketrurl_tv.setText(gifturl);
                    } else {
                        ticketrurl_tv.setVisibility(View.GONE);
                        ticketurltv.setVisibility(View.GONE);
                    }
                } else {
                    eventtours_lv.setVisibility(View.VISIBLE);
                    donationurl_tv.setVisibility(View.GONE);
                    donatiionurltv.setVisibility(View.GONE);
                    ticketrurl_tv.setVisibility(View.GONE);
                    ticketurltv.setVisibility(View.GONE);
                }
            }
        }
        if (coverimage != null && !coverimage.equals("null")) {
            Log.e("tag", "coverrrr------->");
            Glide.with(ViewFeedActivityDetails.this).load("http://104.197.80.225:3010/wow/media/event/" + coverimage)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .crossFade()
                    .into(videoView);
        } else {
            feedImageView.setVisibility(View.GONE);
        }
        if (programschedules != null && !programschedules.equals("null")) {
            if (programschedules.size() > 0) {
                eventshedule_tv.setVisibility(View.VISIBLE);
            } else {
                eventshedule_tv.setVisibility(View.GONE);
            }
        }
        eventdiscussion_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewFeedActivityDetails.this, ViewEventDiscussions.class);
                intent.putParcelableArrayListExtra("program", programschedules);
                startActivity(intent);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        eventshedule_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

         /*       Log.e("tag", "11111111------->" + programschedules.size());
                if (programschedules.size() > 0) {
                    Intent intent = new Intent(ViewFeedActivityDetails.this, ViewProgramSchedule.class);
                    intent.putParcelableArrayListExtra("program", programschedules);
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                } else {
                    Toast.makeText(ViewFeedActivityDetails.this, "Sorry Schedule is Empty", Toast.LENGTH_LONG).show();
                }*/


            }

        });

        if (wowtagvideo != null && !wowtagvideo.equals("null")) {
            Log.e("tag", "wowtag------->");
            String selectedVideoFilePath1 = "http://104.197.80.225:3010/wow/media/event/" + wowtagvideo;
            //videoView.setVideoURI(Uri.parse("https://archive.org/download/Popeye_forPresident/Popeye_forPresident_512kb.mp4"));
            //  Log.d("tag", "567231546" + selectedVideoFilePath1);
            try {
                retriveVideoFrameFromVideo(selectedVideoFilePath1);
                wowtagvideo1.setImageBitmap(bitmap);
                video1plus.setImageDrawable(ViewFeedActivityDetails.this.getDrawable(R.drawable.video_icon));

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        }
        if (highligh1 != null && !highligh1.equals("null")) {

            Glide.with(ViewFeedActivityDetails.this).load("http://104.197.80.225:3010/wow/media/event/" + highligh1)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                    .centerCrop()
                    .crossFade()
                    .into(highlight1_iv);


        }
        if (highligh2 != null && !highligh2.equals("null")) {

            String selectedVideoFilePath1 = "http://104.197.80.225:3010/wow/media/event/" + highligh2;
            //videoView.setVideoURI(Uri.parse("https://archive.org/download/Popeye_forPresident/Popeye_forPresident_512kb.mp4"));
            Log.d("tag", "567231546" + selectedVideoFilePath1);
            try {
                retriveVideoFrameFromVideo(selectedVideoFilePath1);
                highlight2_iv.setImageBitmap(bitmap);
                video3plus.setImageDrawable(ViewFeedActivityDetails.this.getDrawable(R.drawable.video_icon));
                edit.putString("hl2_status", "video");
                edit.commit();

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }


    }


}
