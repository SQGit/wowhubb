package com.wowhubb.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.ImageView.CircularNetworkImageView;
import com.wowhubb.R;
import com.wowhubb.app.ImageLoader;

/**
 * Created by Salman on 27-10-2017.
 */

public class FeedActivityImage extends Activity {
    public ImageLoader imageLoader1;
    String fulladdress, highligh1, highligh2, profilepicture, timestamp_str, description, eventname, name_str, eventdate, status, coverimage;
    ImageView videoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_image);
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
        FontsOverride.overrideFonts(FeedActivityImage.this, v1);
        if (extras != null) {
            // wowtagvideo = extras.getString("wowtagvideo");
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

            if (status.equals("cover")) {
                if (!coverimage.equals("null")) {
                    imageLoader1.DisplayImage("http://104.197.80.225:3010/wow/media/event/" + coverimage, videoView);
                }
            } else if (status.equals("highlight1")) {
                if (!highligh1.equals("null")) {

                    imageLoader1.DisplayImage("http://104.197.80.225:3010/wow/media/event/" + highligh1, videoView);
                }
            } else if (status.equals("highlight2")) {
                if (!highligh2.equals("null")) {
                    imageLoader1.DisplayImage("http://104.197.80.225:3010/wow/media/event/" + highligh2, videoView);
                }
            }

            //imageLoader1.DisplayImage("http://104.197.80.225:3010/wow/media/personal/" + profilepicture, profilePic);
            name.setText(name_str);
            eventname_tv.setText("- " + eventname);
            timestamp.setText(timestamp_str);
            desc.setText(description);
            datetv.setText(eventdate);
            addresstv.setText(fulladdress);

        }
        if (profilepicture != null) {
            imageLoader1.DisplayImage("http://104.197.80.225:3010/wow/media/personal/" + profilepicture, profilePic);


        } else {
            profilePic.setImageResource(R.drawable.profile_img);
        }

    }
}
