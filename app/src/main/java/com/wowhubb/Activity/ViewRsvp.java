package com.wowhubb.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wowhubb.Fonts.FontsOverride;

import com.wowhubb.MyFeedsData.Message;
import com.wowhubb.MyFeedsData.Rsvp;
import com.wowhubb.R;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.wowhubb.Activity.EventInviteActivity.token;

/**
 * Created by Salman on 17-04-2018.
 */

public class ViewRsvp extends Activity {
    ListView listview;
    SharedPreferences.Editor editor;

    List<Rsvp> rsvpList;
    List<Message> messageList;

    ImageView closeiv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewrsvp);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ViewRsvp.this);
        //editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");
        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(ViewRsvp.this, v1);
        listview = (ListView) findViewById(R.id.listview);
        closeiv = findViewById(R.id.closeiv);

        closeiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //  Gson gson = new Gson();
        String json = sharedPreferences.getString("rsvp", null);
        Log.e("tag", "RSVP----------->>>>>>" + json);
        Type type = new TypeToken<ArrayList<Rsvp>>() {
        }.getType();
        // Rsvp rsvp=new Rsvp();
        // rsvpList=new ArrayList<>();
        rsvpList = new Gson().fromJson(json, type);
        Log.e("tag", "RSVP----------->>>>>>" + rsvpList);
        ViewListAdapter adapter = new ViewListAdapter(ViewRsvp.this, rsvpList);
        listview.setAdapter(adapter);
    }


    class ViewListAdapter extends BaseAdapter {
        SharedPreferences.Editor editor;
        String token, userId;
        Dialog dialog;
        private Activity activity;
        private LayoutInflater inflater;
        private List<Rsvp> feedItems;

        public ViewListAdapter(Activity activity, List<Rsvp> feedItems) {
            this.activity = activity;
            this.feedItems = feedItems;
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

        @SuppressLint("ResourceAsColor")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;

            if (inflater == null)
                inflater = (LayoutInflater) activity
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.viewrsvp_list, null);
                viewHolder = new ViewHolder();
                viewHolder.position = position;
                FontsOverride.overrideFonts(activity, convertView);
                viewHolder.name = (TextView) convertView.findViewById(R.id.friend_name);
                viewHolder.mutual_tv = convertView.findViewById(R.id.mutual_tv);
                viewHolder.prof_tv = convertView.findViewById(R.id.prof_tv);
                viewHolder.profilePic = (ImageView) convertView.findViewById(R.id.imageview_profile);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }


            //rsvpList = mains.get(i).getRsvp();
            viewHolder.name.setText(feedItems.get(position).getUserid().getFirstname());
            viewHolder.mutual_tv.setText(feedItems.get(position).getUserid().getWowtagid());
            viewHolder.prof_tv.setText(feedItems.get(position).getExtra() + " Members");

            viewHolder.prof_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {

                }
            });


            //--------------------------------View Profile Image--------------------------------------//

            if (feedItems.get(position).getUserid().getPersonalimage() != null) {

                // Log.e("tag", "Imagge-------" + item.getImge())

                Glide.with(activity).load("http://104.197.80.225:3010/wow/media/personal/" + feedItems.get(position).getUserid().getPersonalimage()).into(viewHolder.profilePic);
            } else {
                viewHolder.profilePic.setImageResource(R.drawable.profile_img);
            }


            viewHolder.position = position;

            if (position % 2 == 0) {
                convertView.setBackgroundColor(Color.parseColor("#f0f0f0"));
            } else {
                convertView.setBackgroundColor(Color.TRANSPARENT);
            }

           /* convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Log.e("tag", "Gropupname---------" + feedItems);
                    Intent intent = new Intent(activity, ViewGroupMembers.class);
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ViewRsvp.this);
                    SharedPreferences.Editor editor = prefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(feedItems);
                    editor.putString("membersstatus", "rsvp");
                    editor.putString("usergroups", json);
                    editor.apply();
                    startActivity(intent);

                }
            });*/
            return convertView;
        }

        class ViewHolder {
            TextView name, mutual_tv, prof_tv;
            int position;
            ImageView profilePic;


        }


    }
}
