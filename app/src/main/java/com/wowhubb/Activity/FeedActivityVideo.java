package com.wowhubb.Activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.ImageView.CircularNetworkImageView;
import com.wowhubb.R;
import com.wowhubb.app.AppController;
import com.wowhubb.app.ImageLoader;

/**
 * Created by Salman on 27-10-2017.
 */

public class FeedActivityVideo extends Activity {
    String fulladdress,wowtagvideo, profilepicture, timestamp_str, description, eventname, name_str,eventdate,status,coverimage,highligh1,highligh2;
    VideoView videoView;
    public com.wowhubb.app.ImageLoader imageLoader1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_video);
        videoView = findViewById(R.id.video_view);
        imageLoader1 = new ImageLoader(getApplicationContext());
        TextView name = (TextView) findViewById(R.id.hoster_name);
        TextView eventname_tv = (TextView) findViewById(R.id.eventname_tv);
        TextView timestamp = (TextView) findViewById(R.id.timestamp);
        TextView desc = (TextView) findViewById(R.id.desc_tv);
        TextView datetv = (TextView) findViewById(R.id.datetv);
        TextView addresstv = (TextView) findViewById(R.id.address_tv);

        CircularNetworkImageView profilePic = (CircularNetworkImageView) findViewById(R.id.profilePic);
        Bundle extras = getIntent().getExtras();
        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(FeedActivityVideo.this, v1);
        if (extras != null)
        {
            wowtagvideo = extras.getString("wowtagvideo");
            highligh1 = extras.getString("highligh1");
            highligh2 = extras.getString("highligh2");
            profilepicture = extras.getString("profilepicture");
            timestamp_str = extras.getString("timestamp");
            description = extras.getString("description");
            status = extras.getString("status");
            eventname = extras.getString("eventname");
            name_str = extras.getString("name");
            eventdate = extras.getString("eventdate");
            coverimage = extras.getString("coverpage");
            fulladdress = extras.getString("fulladdress");

            Log.e("tag","324434343"+status);


            if(status.equals("wowtag"))
            {
                if (!wowtagvideo.equals("null")) {
                    videoView.setVideoURI(Uri.parse("http://104.197.80.225:3010/wow/media/event/" + wowtagvideo));
                    videoView.start();
                }
            }

            else if(status.equals("highlightvideo1"))
            {
                Log.e("tag","324434343"+highligh1);
                if (!highligh1.equals("null")) {

                    videoView.setVideoURI(Uri.parse("http://104.197.80.225:3010/wow/media/event/" + highligh1));
                    videoView.start();
                }
            }
            else if(status.equals("highlightvideo2"))
            {
                if (!highligh2.equals("null")) {
                    videoView.setVideoURI(Uri.parse("http://104.197.80.225:3010/wow/media/event/" + highligh2));
                    videoView.start();
                }
            }


           // imageLoader1.DisplayImage("http://104.197.80.225:3010/wow/media/personal/" +profilepicture,profilePic);
            name.setText(name_str);
            eventname_tv.setText("- " + eventname);
            timestamp.setText(timestamp_str);
            desc.setText(description);
            datetv.setText(eventdate);
            addresstv.setText(fulladdress);

        }
        if (profilepicture != null) {
            imageLoader1.DisplayImage("http://104.197.80.225:3010/wow/media/personal/" +profilepicture,profilePic);


        } else {
            profilePic.setImageResource(R.drawable.profile_img);
        }


    }
}
