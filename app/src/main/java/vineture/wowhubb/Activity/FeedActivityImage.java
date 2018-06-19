package vineture.wowhubb.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.R;
import vineture.wowhubb.app.ImageLoader;

/**
 * Created by Salman on 27-10-2017.
 */

public class FeedActivityImage extends Activity {
    public ImageLoader imageLoader1;
    String eventstartdate,eventenddate,fulladdress, highligh1, highligh2, profilepicture, timestamp_str, description, eventname, name_str, eventdate, status, coverimage;
    ImageView videoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_image);

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(FeedActivityImage.this, v1);

        videoView = findViewById(R.id.video_view);
        imageLoader1 = new ImageLoader(getApplicationContext());

        TextView name = (TextView) findViewById(R.id.hoster_name);
        TextView eventname_tv = (TextView) findViewById(R.id.eventname_tv);
        TextView timestamp = (TextView) findViewById(R.id.timestamp);
        TextView desc = (TextView) findViewById(R.id.desc_tv);

        TextView addresstv = (TextView) findViewById(R.id.address_tv);
        ImageView profilePic = (ImageView) findViewById(R.id.imageview_profile);
        TextView fromdatetv=findViewById(R.id.fromdatetv);
        TextView todatetv=findViewById(R.id.todatetv);

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
            eventstartdate = extras.getString("eventstartdate");
            eventenddate = extras.getString("eventenddate");


            if (status.equals("cover")) {
                if (!coverimage.equals("null")) {
                    imageLoader1.DisplayImage("http://104.197.80.225:3010/wow/media/event/" + coverimage, videoView);}
            } else if (status.equals("highlight1")) {
                if (!highligh1.equals("null")) {
                    imageLoader1.DisplayImage("http://104.197.80.225:3010/wow/media/event/" + highligh1, videoView);}
            } else if (status.equals("highlight2")) {
                if (!highligh2.equals("null")) {
                    imageLoader1.DisplayImage("http://104.197.80.225:3010/wow/media/event/" + highligh2, videoView);}
            }

            name.setText(name_str);
            eventname_tv.setText("- " + eventname);
            timestamp.setText(timestamp_str);
            desc.setText(description);

            addresstv.setText(fulladdress);
            fromdatetv.setText(eventstartdate);
            todatetv.setText(eventenddate);


        }
        Log.e("tag","PPPPPPPPP------------>"+profilepicture);
        if (profilepicture != null)
        {
            //  imageLoader1.DisplayImage("http://104.197.80.225:3010/wow/media/personal/" + profilepicture, profilePic);
            Glide.with(FeedActivityImage.this).load("http://104.197.80.225:3010/wow/media/personal/" + profilepicture).into(profilePic);
        } else {
           // profilePic.setImageResource(R.drawable.profile_img);
        }

    }
}
