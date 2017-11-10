package com.wowhubb.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.wang.avi.AVLoadingIndicatorView;
import com.wowhubb.Activity.FeedActivityImage;
import com.wowhubb.Activity.FeedActivityVideo;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.ImageView.CircularNetworkImageView;
import com.wowhubb.R;
import com.wowhubb.app.AppController;
import com.wowhubb.data.FeedItem;

import java.util.HashMap;
import java.util.List;


public class FeedListAdapter extends BaseAdapter {
    static Bitmap bitmap;
    public com.wowhubb.app.ImageLoader imageLoader1;
    com.android.volley.toolbox.ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    FrameLayout frameLayout;
    ImageView video1plus, video2plus, video3plus;
    SharedPreferences sharedPrefces;
    SharedPreferences.Editor edit;
    AVLoadingIndicatorView av_loader;
    private Activity activity;
    private LayoutInflater inflater;
    private List<FeedItem> feedItems;
    private VideoView videoView;

    public FeedListAdapter(Activity activity, List<FeedItem> feedItems) {
        this.activity = activity;
        this.feedItems = feedItems;
        imageLoader1 = new com.wowhubb.app.ImageLoader(activity.getApplicationContext());
    }

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
            //   mediaMetadataRetriever.setDataSource(videoPath);
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
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public Object getItem(int location) {
        return feedItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.feed_items, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        FontsOverride.overrideFonts(activity, convertView);
        //  av_loader = (AVLoadingIndicatorView) convertView.findViewById(R.id.avi);
        //a//v_loader.setVisibility(View.VISIBLE);
        TextView name = (TextView) convertView.findViewById(R.id.hoster_name);
        TextView eventname_tv = (TextView) convertView.findViewById(R.id.eventname_tv);
        TextView timestamp = (TextView) convertView.findViewById(R.id.timestamp);
        TextView desc = (TextView) convertView.findViewById(R.id.desc_tv);
        CircularNetworkImageView profilePic = (CircularNetworkImageView) convertView.findViewById(R.id.profilePic);
        ImageView feedImageView = (ImageView) convertView.findViewById(R.id.feedImage1);
        sharedPrefces = PreferenceManager.getDefaultSharedPreferences(activity);
        edit = sharedPrefces.edit();
        frameLayout = (FrameLayout) convertView.findViewById(R.id.framevideo1);
        ImageView highlight1_iv = (ImageView) convertView.findViewById(R.id.highlight1);
        ImageView highlight2_iv = (ImageView) convertView.findViewById(R.id.highlight2);
        video1plus = convertView.findViewById(R.id.video1plus_iv);
        video2plus = convertView.findViewById(R.id.video2plus_iv);
        video3plus = convertView.findViewById(R.id.video3plus_iv);
        ImageView wowtagvideo = convertView.findViewById(R.id.video0_iv);

        FeedItem item = feedItems.get(position);

        name.setText(item.getName());
        eventname_tv.setText("- " + item.getEventname());
        timestamp.setText(item.getTimeStamp());
        desc.setText(item.getDescription());


        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FeedItem item = feedItems.get(position);
                Intent intent = new Intent(activity, FeedActivityVideo.class);
                intent.putExtra("status", "wowtag");
                intent.putExtra("wowtagvideo", item.getWowtagvideo());
                intent.putExtra("name", item.getName());
                intent.putExtra("eventname", item.getEventname());
                intent.putExtra("description", item.getDescription());
                intent.putExtra("timestamp", item.getTimeStamp());
                intent.putExtra("profilepicture", item.getProfilePic());
                intent.putExtra("eventdate", item.getEventdate());
                intent.putExtra("fulladdress", item.getFulladdress());
                activity.startActivity(intent);

            }
        });
        feedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FeedItem item = feedItems.get(position);
                Intent intent = new Intent(activity, FeedActivityImage.class);
                intent.putExtra("status", "cover");
                intent.putExtra("coverpage", item.getCoverimage());
                intent.putExtra("name", item.getName());
                intent.putExtra("eventname", item.getEventname());
                intent.putExtra("description", item.getDescription());
                intent.putExtra("timestamp", item.getTimeStamp());
                intent.putExtra("profilepicture", item.getProfilePic());
                intent.putExtra("eventdate", item.getEventdate());
                intent.putExtra("fulladdress", item.getFulladdress());
                activity.startActivity(intent);

            }
        });


        highlight1_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String hl1 = sharedPrefces.getString("hl1_status", "");
                FeedItem item = feedItems.get(position);
                Log.e("tag", "h12----------->" + hl1);
                if (hl1.equals("image")) {
                    Intent intent = new Intent(activity, FeedActivityImage.class);
                    intent.putExtra("status", "highlight1");
                    intent.putExtra("highligh1", item.getHighlight1());
                    intent.putExtra("name", item.getName());
                    intent.putExtra("eventname", item.getEventname());
                    intent.putExtra("description", item.getDescription());
                    intent.putExtra("timestamp", item.getTimeStamp());
                    intent.putExtra("profilepicture", item.getProfilePic());
                    intent.putExtra("eventdate", item.getEventdate());
                    intent.putExtra("fulladdress", item.getFulladdress());
                    activity.startActivity(intent);
                } else {
                    Intent intent = new Intent(activity, FeedActivityVideo.class);
                    intent.putExtra("status", "highlightvideo1");
                    intent.putExtra("highligh1", item.getHighlightvideo1());
                    intent.putExtra("name", item.getName());
                    intent.putExtra("eventname", item.getEventname());
                    intent.putExtra("description", item.getDescription());
                    intent.putExtra("timestamp", item.getTimeStamp());
                    intent.putExtra("profilepicture", item.getProfilePic());
                    intent.putExtra("eventdate", item.getEventdate());
                    intent.putExtra("fulladdress", item.getFulladdress());
                    activity.startActivity(intent);

                }

            }
        });

        highlight2_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hl2 = sharedPrefces.getString("hl2_status", "");
                FeedItem item = feedItems.get(position);
                Log.e("tag", "h12----------->" + hl2);

                if (hl2.equals("image")) {
                    Intent intent = new Intent(activity, FeedActivityImage.class);
                    intent.putExtra("status", "highlight2");
                    intent.putExtra("highligh2", item.getHighlight2());
                    intent.putExtra("name", item.getName());
                    intent.putExtra("eventname", item.getEventname());
                    intent.putExtra("description", item.getDescription());
                    intent.putExtra("timestamp", item.getTimeStamp());
                    intent.putExtra("profilepicture", item.getProfilePic());
                    intent.putExtra("eventdate", item.getEventdate());
                    intent.putExtra("fulladdress", item.getFulladdress());
                    activity.startActivity(intent);
                } else {
                    Intent intent = new Intent(activity, FeedActivityVideo.class);
                    intent.putExtra("status", "highlightvideo2");
                    intent.putExtra("highligh2", item.getHighlightvideo2());
                    intent.putExtra("name", item.getName());
                    intent.putExtra("eventname", item.getEventname());
                    intent.putExtra("description", item.getDescription());
                    intent.putExtra("timestamp", item.getTimeStamp());
                    intent.putExtra("profilepicture", item.getProfilePic());
                    intent.putExtra("eventdate", item.getEventdate());
                    intent.putExtra("fulladdress", item.getFulladdress());
                    activity.startActivity(intent);
                }


            }
        });
        if (item.getProfilePic() != null) {
            profilePic.setImageUrl("http://104.197.80.225:3010/wow/media/personal/" + item.getProfilePic(), imageLoader);
            // imageLoader1.DisplayImage("http://104.197.80.225:3010/wow/media/personal/" + item.getProfilePic(), profilePic);
            //  profilePic.setImageUrl("http://104.197.80.225:3010/wow/media/personal/" + item.getProfilePic(), imageLoader);
        } else {
            profilePic.setImageResource(R.drawable.profile_img);
        }

        if (!item.getImge().equals("null")) {
            Log.e("tag", "Imagge-------" + item.getImge());

            imageLoader1.DisplayImage("http://104.197.80.225:3010/wow/media/event/" + item.getImge(), feedImageView);


        } else {
            Log.e("tag", "Ghghghghgh-------" + item.getImge());
            feedImageView.setVisibility(View.GONE);
        }

        if (!item.getWowtagvideo().equals("null")) {
            String selectedVideoFilePath1 = "http://104.197.80.225:3010/wow/media/event/" + item.getWowtagvideo();
            //videoView.setVideoURI(Uri.parse("https://archive.org/download/Popeye_forPresident/Popeye_forPresident_512kb.mp4"));
            Log.d("tag", "567231546" + selectedVideoFilePath1);

            feedImageView.setVisibility(View.VISIBLE);
            try {
                retriveVideoFrameFromVideo(selectedVideoFilePath1);
                // wowtagvideo.setImageUrl(selectedVideoFilePath1,imageLoader);
                wowtagvideo.setImageBitmap(bitmap);
                //wowtagvideo.setImageBitmap("http://104.197.80.225:3010/wow/media/event/" + item.getWowtagvideo());
                video1plus.setImageDrawable(activity.getDrawable(R.drawable.video_icon));

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        }


        if (!item.getHighlight1().equals("null")) {
            Log.e("tag", "hllllllllll------" + item.getHighlight1());
            edit.putString("hl1_status", "image");
            edit.commit();
            imageLoader1.DisplayImage("http://104.197.80.225:3010/wow/media/event/" + item.getHighlight1(), highlight1_iv);
        }

        if (!item.getHighlight2().equals("null")) {
            edit.putString("hl2_status", "image");
            edit.commit();
            imageLoader1.DisplayImage("http://104.197.80.225:3010/wow/media/event/" + item.getHighlight2(), highlight2_iv);
        }
        Log.e("tag", "hllllllllllwerrtewtwttvideooooooooo------" + item.getHighlightvideo1());
        if (!item.getHighlightvideo1().equals("null")) {
            Log.e("tag", "hllllllllllvideooooooooo------" + item.getHighlightvideo1());

            String selectedVideoFilePath1 = "http://104.197.80.225:3010/wow/media/event/" + item.getHighlightvideo1();
            feedImageView.setVisibility(View.VISIBLE);
            try {
                retriveVideoFrameFromVideo(selectedVideoFilePath1);
                highlight1_iv.setImageBitmap(bitmap);
                video2plus.setImageDrawable(activity.getDrawable(R.drawable.video_icon));
                edit.putString("hl1_status", "video");
                edit.commit();

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        if (!item.getHighlightvideo2().equals("null")) {

            String selectedVideoFilePath1 = "http://104.197.80.225:3010/wow/media/event/" + item.getHighlightvideo2();
            //videoView.setVideoURI(Uri.parse("https://archive.org/download/Popeye_forPresident/Popeye_forPresident_512kb.mp4"));
            Log.d("tag", "567231546" + selectedVideoFilePath1);

            feedImageView.setVisibility(View.VISIBLE);
            try {
                retriveVideoFrameFromVideo(selectedVideoFilePath1);
                highlight2_iv.setImageBitmap(bitmap);
                video3plus.setImageDrawable(activity.getDrawable(R.drawable.video_icon));
                edit.putString("hl2_status", "video");
                edit.commit();

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        return convertView;
    }
}
