package com.wowhubb.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wang.avi.AVLoadingIndicatorView;
import com.wowhubb.Activity.FeedActivityImage;
import com.wowhubb.Activity.FeedActivityVideo;
import com.wowhubb.Activity.ViewFeedActivityDetails;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;
import com.wowhubb.Utils.HttpUtils;
import com.wowhubb.Utils.ItemDetailsWrapper;
import com.wowhubb.app.AppController;
import com.wowhubb.data.FeedItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;


public class FeedListAdapter extends BaseAdapter {
    static Bitmap bitmap;
    public com.wowhubb.app.ImageLoader imageLoader1;
    com.android.volley.toolbox.ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    //FrameLayout frameLayout;
    SharedPreferences sharedPrefces;
    SharedPreferences.Editor edit;
    AVLoadingIndicatorView av_loader;
    String str_frommonth, str_tomonth, str_fromdate, str_todate, str_fromtime, str_totime, token, str_timefrom, str_timeto;
    LinearLayout runttime_lv;
    int from, to;
    ArrayList<FeedItem> list;
    private Activity activity;
    private LayoutInflater inflater;
    private List<FeedItem> feedItems;

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
        final ViewHolder viewHolder;
        //final TextView wowsome_tv,viewwowsome;
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);

        token = sharedPreferences.getString("token", "");
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.feed_items_professional, null);
            viewHolder = new ViewHolder();
            viewHolder.position = position;
            if (imageLoader == null)
                imageLoader = AppController.getInstance().getImageLoader();

            FontsOverride.overrideFonts(activity, convertView);

            TimeZone tz = TimeZone.getDefault();
            tz = TimeZone.getTimeZone(tz.getID());
            for (String s : TimeZone.getAvailableIDs(tz.getOffset(System.currentTimeMillis()))) {
                System.out.print(s + ",");
                Log.e("tag", "TIME ZONEIDDsssssssssss------" + s);

            }


            viewHolder.name = (TextView) convertView.findViewById(R.id.hoster_name);
            viewHolder.eventname_tv = (TextView) convertView.findViewById(R.id.eventname_tv);
            viewHolder.timestamp = (TextView) convertView.findViewById(R.id.timestamp);
            viewHolder.desc = (TextView) convertView.findViewById(R.id.desc_tv);
            viewHolder.profilePic = (ImageView) convertView.findViewById(R.id.imageview_profile);
            viewHolder.feedImageView = (ImageView) convertView.findViewById(R.id.feedImage1);

            viewHolder.wowsome_tv = convertView.findViewById(R.id.wowsome_tv);
            viewHolder.comments = convertView.findViewById(R.id.comments_tv);
            viewHolder.share = convertView.findViewById(R.id.share_tv);
            viewHolder.viewmore_tv = convertView.findViewById(R.id.viewmore_tv);


            viewHolder.viewwowsome = convertView.findViewById(R.id.viewwow_tv);
            viewHolder.viewcomments = convertView.findViewById(R.id.viewcomments_tv);
            viewHolder.viewshare = convertView.findViewById(R.id.viewshare_tv);


            viewHolder.month_tv = convertView.findViewById(R.id.month_tv);
            viewHolder.date_tv = convertView.findViewById(R.id.date_tv);
            viewHolder.time_tv = convertView.findViewById(R.id.time_tv);

            runttime_lv = convertView.findViewById(R.id.runtimelv);

            viewHolder.eventtopic_tv = convertView.findViewById(R.id.eventtopic_tv);
            viewHolder.otherurl_tv = convertView.findViewById(R.id.otherurl_tv);
            viewHolder.eventaddress_tv = convertView.findViewById(R.id.address_tv);

            sharedPrefces = PreferenceManager.getDefaultSharedPreferences(activity);
            edit = sharedPrefces.edit();
            viewHolder.frameLayout = convertView.findViewById(R.id.framevideo1);

            viewHolder.highlight1_iv = convertView.findViewById(R.id.highlight1);
            viewHolder.highlight2_iv = convertView.findViewById(R.id.highlight2);
            viewHolder.video1plus = convertView.findViewById(R.id.video1plus_iv);
            viewHolder.video2plus = convertView.findViewById(R.id.video2plus_iv);
            viewHolder.video3plus = convertView.findViewById(R.id.video3plus_iv);
            viewHolder.wowtagvideo = convertView.findViewById(R.id.video0_iv);

            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        FeedItem item = feedItems.get(position);
        viewHolder.name.setText(item.getName());
        viewHolder.eventname_tv.setText("- " + item.getEventname());
        viewHolder.desc.setText(item.getDescription());

        viewHolder.wowsome_tv.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                FeedItem item = feedItems.get(viewHolder.position);
                String eventId = item.getEventid();
                new addWowsome(eventId).execute();
                viewHolder.viewwowsome.setVisibility(view.VISIBLE);
            }
        });
        viewHolder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.viewcomments.setVisibility(view.VISIBLE);
            }
        });
        viewHolder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.viewshare.setVisibility(view.VISIBLE);
            }
        });


        if (item.getDesignation() != null) {
            viewHolder.timestamp.setText(item.getDesignation());
        }
        else
        {

        }
        //--------------------------------View Profile Image--------------------------------------//

        if (item.getProfilePic() != null) {
            // Log.e("tag", "Imagge-------" + item.getImge());
            Glide.with(activity).load("http://104.197.80.225:3010/wow/media/personal/" + item.getProfilePic()).into(viewHolder.profilePic);
        } else {
            viewHolder.profilePic.setImageResource(R.drawable.profile_img);
        }

        //--------------------------------View Cover Image----------------------------------------//
        if (!item.getImge().equals("null")) {
            Log.e("tag", "Imagge-------" + item.getImge());
            imageLoader1.DisplayImage("http://104.197.80.225:3010/wow/media/event/" + item.getImge(), viewHolder.feedImageView);
            // com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(item.getImge(), viewHolder.feedImageView, options, animateFirstListener);

        } else {
            Log.e("tag", "Ghghghghgh-------" + item.getImge());
            //viewHolder.feedImageView.setVisibility(View.GONE);
        }


        //---------------------------------View Wowtag Video--------------------------------------//

        if (!item.getWowtagvideo().equals("null")) {
            String selectedVideoFilePath1 = "http://104.197.80.225:3010/wow/media/event/" + item.getWowtagvideo();
            Log.d("tag", "567231546" + selectedVideoFilePath1);
            try {
                retriveVideoFrameFromVideo(selectedVideoFilePath1);
                viewHolder.wowtagvideo.setImageBitmap(bitmap);
                viewHolder.video1plus.setImageDrawable(activity.getDrawable(R.drawable.video_icon));
                //  com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(String.valueOf(bitmap), viewHolder.wowtagvideo, options, animateFirstListener);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        }


        viewHolder.frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FeedItem item = feedItems.get(viewHolder.position);
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

        viewHolder.feedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FeedItem item = feedItems.get(viewHolder.position);
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
        viewHolder.viewmore_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FeedItem item = feedItems.get(viewHolder.position);
                ItemDetailsWrapper wrapper = new ItemDetailsWrapper(feedItems);
                Intent intent = new Intent(activity, ViewFeedActivityDetails.class);
                intent.putExtra("wowtagvideo", item.getWowtagvideo());
                intent.putExtra("coverpage", item.getCoverimage());
                intent.putExtra("name", item.getName());
                intent.putExtra("eventname", item.getEventname());
                intent.putExtra("description", item.getDescription());
                intent.putExtra("timestamp", item.getTimeStamp());
                intent.putExtra("profilepicture", item.getProfilePic());
                intent.putExtra("eventdate", item.getEventdate());
                intent.putExtra("fulladdress", item.getFulladdress());
                intent.putExtra("gifturl", item.getGiftregistryurl());
                intent.putExtra("donationurl", item.getDonationurl());
                //intent.putExtra("obj", wrapper);
                Log.e("tag", "Imagge11-------" + item.getHighlight1());
                Log.e("tag", "Imagge11-------" + item.getHighlight2());
                if (!item.getHighlight1().equals("null")) {
                    Log.e("tag", "1111111111" + item.getHighlight1());
                    intent.putExtra("hl1_status", "image");
                    intent.putExtra("highligh1", item.getHighlight1());
                }

                if (!item.getHighlightvideo1().equals("null")) {
                    Log.e("tag", "2222" + item.getHighlightvideo1());
                    intent.putExtra("hl1_status", "video");
                    intent.putExtra("highligh1", item.getHighlightvideo1());
                }

                if (!item.getHighlight2().equals("null")) {
                    Log.e("tag", "2222" + item.getHighlight2());
                    intent.putExtra("hl2_status", "image");
                    intent.putExtra("highligh2", item.getHighlight2());
                }
                if (!item.getHighlightvideo2().equals("null")) {
                    Log.e("tag", "2222" + item.getHighlightvideo2());
                    intent.putExtra("hl2_status", "video");
                    intent.putExtra("highligh2", item.getHighlightvideo2());
                }
                activity.startActivity(intent);

            }
        });

        viewHolder.highlight1_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String hl1 = sharedPrefces.getString("hl1_status", "");
                FeedItem item = feedItems.get(viewHolder.position);
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

        viewHolder.highlight2_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hl2 = sharedPrefces.getString("hl2_status", "");
                FeedItem item = feedItems.get(viewHolder.position);
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

        if ((!item.getRuntimefrom().equals("null")) && (!item.getRuntimeto().equals("null"))) {
            runttime_lv.setVisibility(View.VISIBLE);
            String fromTime = item.getRuntimefrom();
            String toTime = item.getRuntimeto();
            Log.e("tag", "DATeeeee------->" + fromTime + toTime);
            try {
                String[] separated = fromTime.split(" ");
                String runFromDate = separated[0];
                String runFromTime = separated[1];
                //  String strDateFormatTo = fromTime;
                SimpleDateFormat spf = new SimpleDateFormat("MM/dd/yyyy");
                Date newDate = null;
                try {
                    newDate = spf.parse(runFromDate);
                    spf = new SimpleDateFormat("MMM/dd/yyyy EEE");
                    runFromDate = spf.format(newDate);
                    System.out.println(runFromDate);

                    String str_fromdate1[] = runFromDate.split("/");
                    str_frommonth = str_fromdate1[0];
                    str_fromdate = str_fromdate1[1];
                    String str_fromtime[] = runFromDate.split(" ");
                    str_timefrom = str_fromtime[1];
                    Log.e("tag", "date2222-------->" + str_timefrom);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                SimpleDateFormat outputFormat = new SimpleDateFormat("KK:mma");
                SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm");
                String strDateFormat = runFromTime + "a";
                Log.e("tag", "date-------->" + runFromTime);
                try {
                    Date dt = parseFormat.parse(strDateFormat);
                    System.out.println(outputFormat.format(dt));
                    str_fromtime = (outputFormat.format(dt));
                    Log.e("tag", "todate-------->" + outputFormat.format(dt));
                } catch (ParseException exc) {
                    exc.printStackTrace();
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }

            try {
                String[] separated1 = toTime.split(" ");
                String runFromDate1 = separated1[0];
                String runFromTime1 = separated1[1];
                SimpleDateFormat spf1 = new SimpleDateFormat("MM/dd/yyyy");
                Date newDate1 = null;
                try {
                    newDate1 = spf1.parse(runFromDate1);
                    spf1 = new SimpleDateFormat("MMM/dd/yyyy EEE");
                    runFromDate1 = spf1.format(newDate1);
                    Log.e("tag", "date-------->" + runFromDate1);
                    String str_fromdate12[] = runFromDate1.split("/");
                    str_tomonth = str_fromdate12[0];
                    str_todate = str_fromdate12[1];
                    String str_totime[] = runFromDate1.split(" ");
                    str_timeto = str_totime[1];
                    Log.e("tag", "date2222-------->" + str_timeto);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                SimpleDateFormat outputFormat1 = new SimpleDateFormat("KK:mma");
                SimpleDateFormat parseFormat1 = new SimpleDateFormat("hh:mm");
                String strDateFormat1 = runFromTime1 + "a";
                Log.e("tag", "date-------->" + runFromTime1);

                try {
                    Date dt = parseFormat1.parse(strDateFormat1);
                    System.out.println(outputFormat1.format(dt));
                    str_totime = (outputFormat1.format(dt));
                    Log.e("tag", "todate-------->" + outputFormat1.format(dt));
                } catch (ParseException exc) {
                    exc.printStackTrace();
                }
            } catch (ArrayIndexOutOfBoundsException e) {

            }
            try {
                if (str_frommonth.equals(str_tomonth)) {
                    viewHolder.month_tv.setText(str_frommonth);
                } else {
                    viewHolder.month_tv.setText(str_frommonth + " to " + str_tomonth);
                }
            } catch (NullPointerException e) {

            }

            // date_tv.setText(str_fromdate + "-" + str_todate);
            viewHolder.time_tv.setText(str_fromtime + "-" + str_totime);
            try {
                from = Integer.parseInt(str_fromdate);
                to = Integer.parseInt(str_todate);
            } catch (NumberFormatException e) {

            }

            if (from == 01) {
                str_fromdate = str_fromdate + "st";
            } else if (from == 02) {
                str_fromdate = str_fromdate + "nd";
            } else if (from == 03) {
                str_fromdate = str_fromdate + "rd";
            } else {
                str_fromdate = str_fromdate + "th";
            }
            if (to == 01) {
                str_todate = str_todate + "st";
                //date_tv.setText("");
                viewHolder.date_tv.setText(str_timefrom + " " + str_fromdate + "-" + str_timeto + " " + str_todate);
            } else if (to == 02) {
                str_todate = str_todate + "nd";
                //  date_tv.setText("");
                viewHolder.date_tv.setText(str_timefrom + " " + str_fromdate + "-" + str_timeto + " " + str_todate);
            } else if (to == 03) {
                str_todate = str_todate + "rd";
                //date_tv.setText("");
                viewHolder.date_tv.setText(str_timefrom + " " + str_fromdate + "-" + str_timeto + " " + str_todate);
            } else {
                str_todate = str_todate + "th";
                //date_tv.setText("");
                viewHolder.date_tv.setText(str_timefrom + " " + str_fromdate + "-" + str_timeto + " " + str_todate);
            }
        } else {
            runttime_lv.setVisibility(View.GONE);
        }


        if (!item.getEventtopic().equals("null")) {
            viewHolder.eventtopic_tv.setText(item.getEventtopic());
        }
        if (!item.getFulladdress().equals("null")) {
            viewHolder.eventaddress_tv.setText(item.getFulladdress());
        }
        if (!item.getDonationurl().equals("null")) {
            viewHolder.otherurl_tv.setText(item.getDonationurl());
        }

        if (!item.getHighlight1().equals("null")) {
            Log.e("tag", "hllllllllll------" + item.getHighlight1());
            edit.putString("hl1_status", "image");
            edit.commit();
            imageLoader1.DisplayImage("http://104.197.80.225:3010/wow/media/event/" + item.getHighlight1(), viewHolder.highlight1_iv);
        }

        if (!item.getHighlight2().equals("null")) {
            edit.putString("hl2_status", "image");
            edit.commit();
            imageLoader1.DisplayImage("http://104.197.80.225:3010/wow/media/event/" + item.getHighlight2(), viewHolder.highlight2_iv);
        }


        Log.e("tag", "hllllllllllwerrtewtwttvideooooooooo------" + item.getHighlightvideo1());
        if (!item.getHighlightvideo1().equals("null")) {
            Log.e("tag", "hllllllllllvideooooooooo------" + item.getHighlightvideo1());
            String selectedVideoFilePath1 = "http://104.197.80.225:3010/wow/media/event/" + item.getHighlightvideo1();
            viewHolder.feedImageView.setVisibility(View.VISIBLE);
            try {
                retriveVideoFrameFromVideo(selectedVideoFilePath1);
                viewHolder.highlight1_iv.setImageBitmap(bitmap);
                viewHolder.video2plus.setImageDrawable(activity.getDrawable(R.drawable.video_icon));
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

            viewHolder.feedImageView.setVisibility(View.VISIBLE);
            try {
                retriveVideoFrameFromVideo(selectedVideoFilePath1);
                viewHolder.highlight2_iv.setImageBitmap(bitmap);
                viewHolder.video3plus.setImageDrawable(activity.getDrawable(R.drawable.video_icon));
                edit.putString("hl2_status", "video");
                edit.commit();

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        viewHolder.position = position;


        return convertView;
    }

    static class ViewHolder {
        TextView timestamp, eventname_tv, name, desc, share, comments, eventtopic_tv, otherurl_tv, eventaddress_tv, month_tv, date_tv, time_tv;
        TextView viewmore_tv, viewcomments, viewshare, wowsome_tv, viewwowsome;
        int position;
        ImageView profilePic;
        ImageView feedImageView, wowtagvideo, highlight1_iv, highlight2_iv;
        ImageView video1plus, video2plus, video3plus;
        FrameLayout frameLayout;

    }


    private class addWowsome extends AsyncTask<String, Void, String> {
        String eventId, str_pwd;

        public addWowsome(String eventId) {
            this.eventId = eventId;
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
                jsonObject.accumulate("eventid", eventId);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1("http://104.197.80.225:3010/wow/event/wowsome", json, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "RESSSSSSS-------->" + s.toString());

            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("success");
                    if (status.equals("true")) {
                        JSONObject message = jo.getJSONObject("message");
                        String wowsomestr = message.getString("wowsome");
                        Log.e("tag", "wowsomestr-------->" + wowsomestr);
                    }

                } catch (JSONException e) {

                }

            } else {
            }

        }


    }

}
