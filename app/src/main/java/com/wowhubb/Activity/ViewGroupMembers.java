package com.wowhubb.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import com.wowhubb.Groups.Group;
import com.wowhubb.Groups.User;
import com.wowhubb.MyFeedsData.Rsvp;
import com.wowhubb.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.wowhubb.Activity.EventInviteActivity.token;

/**
 * Created by Salman on 17-04-2018.
 */

public class ViewGroupMembers extends Activity {
    ListView listview;
    SharedPreferences.Editor editor;
    List<User> userList;
    String memberstatus;
    ImageView closeiv;
    TextView memberstv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewgroupmembers);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ViewGroupMembers.this);
        token = sharedPreferences.getString("token", "");
        memberstatus = sharedPreferences.getString("membersstatus", "");

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(ViewGroupMembers.this, v1);

        listview = (ListView) findViewById(R.id.listview);
        closeiv = findViewById(R.id.closeiv);
        memberstv = findViewById(R.id.memberstv);
        if (memberstatus.equals("group")) {
            memberstv.setText("Group Members");
            //  Gson gson = new Gson();
            String json = sharedPreferences.getString("usergroups", null);
            Log.e("tag", "RSVP----------->>>>>>" + json);
            Type type = new TypeToken<ArrayList<User>>() {
            }.getType();
            // Rsvp rsvp=new Rsvp();
            // rsvpList=new ArrayList<>();
            userList = new Gson().fromJson(json, type);
            ViewListAdapter adapter = new ViewListAdapter(ViewGroupMembers.this, userList);
            listview.setAdapter(adapter);
        }
        closeiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }


    class ViewListAdapter extends BaseAdapter {
        SharedPreferences.Editor editor;
        String token, userId;
        Dialog dialog;
        private Activity activity;
        private LayoutInflater inflater;
        private List<User> feedItems;

        public ViewListAdapter(Activity activity, List<User> feedItems) {
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
                convertView = inflater.inflate(R.layout.viewgroup_list, null);
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

            if(feedItems.get(position).getUserid()!=null)
            {
                viewHolder.name.setText(feedItems.get(position).getUserid().getFirstname());
                viewHolder.mutual_tv.setText(feedItems.get(position).getUserid().getWowtagid());
                if (feedItems.get(position).getUserid().getDesignation() != null) {

                    viewHolder.prof_tv.setText(feedItems.get(position).getUserid().getDesignation());
                } else {
                    viewHolder.prof_tv.setText("");
                }

            //--------------------------------View Profile Image--------------------------------------//

            if (feedItems.get(position).getUserid().getPersonalimage() != null) {

                // Log.e("tag", "Imagge-------" + item.getImge())

                Glide.with(activity).load("http://104.197.80.225:3010/wow/media/personal/" + feedItems.get(position).getUserid().getPersonalimage()).into(viewHolder.profilePic);
            } else {
                viewHolder.profilePic.setImageResource(R.drawable.profile_img);
            }

            }
            viewHolder.position = position;

            if (position % 2 == 0) {
                convertView.setBackgroundColor(Color.parseColor("#f0f0f0"));
            } else {
                convertView.setBackgroundColor(Color.TRANSPARENT);
            }


            return convertView;
        }

        class ViewHolder {
            TextView name, mutual_tv, prof_tv;
            int position;
            ImageView profilePic;


        }


    }

/*
    class ViewListRSVPAdapter extends BaseAdapter {
        SharedPreferences.Editor editor;
        String token, userId;
        Dialog dialog;
        private Activity activity;
        private LayoutInflater inflater;
        private List<Rsvp> feedItems;

        public ViewListRSVPAdapter(Activity activity, List<Rsvp> feedItems) {
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
                convertView = inflater.inflate(R.layout.viewgroup_list, null);
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

            viewHolder.name.setText(feedItems.get(position).getUserid().getFirstname());
            viewHolder.mutual_tv.setText(feedItems.get(position).getUserid().getWowtagid());


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


            return convertView;
        }

        class ViewHolder {
            TextView name, mutual_tv, prof_tv;
            int position;
            ImageView profilePic;


        }


    }
*/

}
