package com.wowhubb.Activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.ImageView.CircularNetworkImageView;
import com.wowhubb.R;
import com.wowhubb.Utils.ItemDetailsWrapper;
import com.wowhubb.app.ImageLoader;
import com.wowhubb.data.FeedItem;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Salman on 27-10-2017.
 */

public class ViewFeedActivityDetails extends Activity {
    static Bitmap bitmap;
    public ImageLoader imageLoader1;
    String fulladdress, profilepicture, timestamp_str, description, eventname, name_str, eventdate, status, coverimage;
    ImageView videoView;
    FrameLayout frameLayout;
    ImageView video1plus, video2plus, video3plus;
    SharedPreferences sharedPrefces;
    SharedPreferences.Editor edit;
    String wowtagvideo, highligh1, highligh2, h_status, hl2_status,gifturl,donationurl;

    List<FeedItem> list;

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

       // ItemDetailsWrapper wrap = (ItemDetailsWrapper) getIntent().getSerializableExtra("obj");
        //list = wrap.getItemDetails();
        Log.e("tag","programshedule-------------->>>"+list);
        sharedPrefces = PreferenceManager.getDefaultSharedPreferences(ViewFeedActivityDetails.this);
        edit = sharedPrefces.edit();
        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(ViewFeedActivityDetails.this, v1);

        videoView = findViewById(R.id.video_view);
        imageLoader1 = new ImageLoader(getApplicationContext());

        TextView name = (TextView) findViewById(R.id.hoster_name);
        TextView eventname_tv = (TextView) findViewById(R.id.eventname_tv);
        TextView timestamp = (TextView) findViewById(R.id.timestamp);
        TextView desc = (TextView) findViewById(R.id.desc_tv);
        TextView datetv = (TextView) findViewById(R.id.datetv);
        TextView addresstv = (TextView) findViewById(R.id.address_tv);
        TextView giftrurl_tv = (TextView) findViewById(R.id.ticketurl_desc_tv);
        TextView donationurl_tv = (TextView) findViewById(R.id.donationturl_desc_tv);
        ImageView profilePic = (ImageView) findViewById(R.id.imageview_profile);
        frameLayout = findViewById(R.id.framevideo1);
        ImageView highlight1_iv = findViewById(R.id.highlight1);
        ImageView highlight2_iv = findViewById(R.id.highlight2);
        video1plus = findViewById(R.id.video1plus_iv);
        video2plus = findViewById(R.id.video2plus_iv);
        video3plus = findViewById(R.id.video3plus_iv);
        ImageView wowtagvideo1 = findViewById(R.id.video0_iv);
        ImageView feedImageView = (ImageView) findViewById(R.id.feedImage1);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            profilepicture = extras.getString("profilepicture");
            timestamp_str = extras.getString("timestamp");
            description = extras.getString("description");
            status = extras.getString("status");
            eventname = extras.getString("eventname");
            name_str = extras.getString("name");
            eventdate = extras.getString("eventdate");
            coverimage = extras.getString("coverpage");
            highligh1 = extras.getString("highligh1");
            highligh2 = extras.getString("highligh2");
            fulladdress = extras.getString("fulladdress");
            wowtagvideo = extras.getString("wowtagvideo");
            h_status = extras.getString("hl1_status");
            hl2_status = extras.getString("hl2_status");
            gifturl = extras.getString("gifturl");
            donationurl = extras.getString("donationurl");

            Log.e("tag", "Imagge-------" + h_status+hl2_status);

             if (!coverimage.equals("null")) {
                    imageLoader1.DisplayImage("http://104.197.80.225:3010/wow/media/event/" + coverimage, videoView);
             }

            name.setText(name_str);
            eventname_tv.setText("- " + eventname);
            timestamp.setText(timestamp_str);
            desc.setText(description);
            datetv.setText(eventdate);
            addresstv.setText(fulladdress);
            giftrurl_tv.setText(gifturl);
            donationurl_tv.setText(donationurl);
        }
        if (!coverimage.equals("null"))
        {
            imageLoader1.DisplayImage("http://104.197.80.225:3010/wow/media/event/" + coverimage, feedImageView);
        }
        else
        {
            feedImageView.setVisibility(View.GONE);
        }
        if (profilepicture != null) {
           // imageLoader1.DisplayImage("http://104.197.80.225:3010/wow/media/personal/" + profilepicture, profilePic);
            Glide.with(ViewFeedActivityDetails.this).load("http://104.197.80.225:3010/wow/media/personal/" +profilepicture).into(profilePic);

        } else {
            profilePic.setImageResource(R.drawable.profile_img);
        }

        if (!wowtagvideo.equals("null")) {
            String selectedVideoFilePath1 = "http://104.197.80.225:3010/wow/media/event/" + wowtagvideo;
            //videoView.setVideoURI(Uri.parse("https://archive.org/download/Popeye_forPresident/Popeye_forPresident_512kb.mp4"));
            Log.d("tag", "567231546" + selectedVideoFilePath1);
            try {
                retriveVideoFrameFromVideo(selectedVideoFilePath1);
                wowtagvideo1.setImageBitmap(bitmap);
                video1plus.setImageDrawable(ViewFeedActivityDetails.this.getDrawable(R.drawable.video_icon));

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        }
        if (h_status!=null) {


            if (h_status.equals("image")) {
                edit.putString("hl1_status", "image");
                edit.commit();
                imageLoader1.DisplayImage("http://104.197.80.225:3010/wow/media/event/" + highligh1, highlight1_iv);
            }
           else {
                String selectedVideoFilePath1 = "http://104.197.80.225:3010/wow/media/event/" + highligh1;
                try {
                    retriveVideoFrameFromVideo(selectedVideoFilePath1);
                    highlight1_iv.setImageBitmap(bitmap);
                    video2plus.setImageDrawable(ViewFeedActivityDetails.this.getDrawable(R.drawable.video_icon));
                    edit.putString("hl1_status", "video");
                    edit.commit();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
        if (hl2_status!=null) {


            if (hl2_status.equals("image"))
            {
                edit.putString("hl2_status", "image");
                edit.commit();
                imageLoader1.DisplayImage("http://104.197.80.225:3010/wow/media/event/" + highligh2, highlight2_iv);
            }
            else
                {
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
}
