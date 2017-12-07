package com.wowhubb.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.Fragment.MyNetworksTabFragment;
import com.wowhubb.R;
import com.wowhubb.Utils.HttpUtils;
import com.wowhubb.data.FeedItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class FriendListAdapter extends BaseAdapter {


    SharedPreferences.Editor editor;
    String token, userId;
    Dialog dialog;
    private Activity activity;
    private LayoutInflater inflater;
    private List<FeedItem> feedItems;

    public FriendListAdapter(Activity activity, List<FeedItem> feedItems) {
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.mynetwork_tab, null);
            viewHolder = new ViewHolder();
            viewHolder.position = position;
            FontsOverride.overrideFonts(activity, convertView);
            viewHolder.name = (TextView) convertView.findViewById(R.id.friend_name);
            viewHolder.addtv = convertView.findViewById(R.id.addtv);
            viewHolder.removetv = convertView.findViewById(R.id.removetv);
            viewHolder.profilePic = (ImageView) convertView.findViewById(R.id.imageview_profile);
            convertView.setTag(viewHolder);
            dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.test_loader);
            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
            editor = sharedPreferences.edit();
            token = sharedPreferences.getString("token", "");

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        FeedItem item = feedItems.get(position);
        viewHolder.name.setText(item.getFriendname());
        viewHolder.position = position;


        //--------------------------------View Profile Image--------------------------------------//

        if (item.getFriendpic() != null) {
            // Log.e("tag", "Imagge-------" + item.getImge());
            Glide.with(activity).load("http://104.197.80.225:3010/wow/media/personal/" + item.getFriendpic()).into(viewHolder.profilePic);
        } else {
            viewHolder.profilePic.setImageResource(R.drawable.profile_img);
        }

        if (item.getFriiendsstatus().equals("add")) {
            viewHolder.addtv.setText("Add Friend");
            viewHolder.removetv.setVisibility(View.INVISIBLE);
        } else if (item.getFriiendsstatus().equals("sent")) {
            viewHolder.addtv.setText("Request sent");
            viewHolder.removetv.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.addtv.setText("Accept");
            viewHolder.removetv.setVisibility(View.VISIBLE);
        }


        viewHolder.addtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FeedItem item = feedItems.get(position);
                userId = item.getFriendid();

                if (item.getFriiendsstatus().equals("add")) {
                    new sendRequest().execute();
                    viewHolder.addtv.setClickable(true);
                }
                else if (item.getFriiendsstatus().equals("received"))
                {
                    viewHolder.addtv.setClickable(true);
                    new acceptRequest().execute();
                } else {
                    viewHolder.addtv.setClickable(false);
                }
            }
        });


        return convertView;
    }

    static class ViewHolder {
        TextView name, addtv, removetv;
        int position;
        ImageView profilePic;


    }

    public class sendRequest extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("userid", userId);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1("http://104.197.80.225:3010/wow/network/sendrequest", json, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "tagqqqqqqqqqq------------------>>>>>>" + s);
            dialog.dismiss();
            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("success");

                    if (status.equals("true")) {
                        String message = jo.getString("message");
                        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                       // adapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                }
            } else {

            }

        }

    }

    public class acceptRequest extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("userid", userId);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1("http://104.197.80.225:3010/wow/network/acceptrequest", json, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "tagqqqqqqqqqq------------------>>>>>>" + s);
            dialog.dismiss();
            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("success");

                    if (status.equals("true")) {
                        String message = jo.getString("message");
                        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                }
            } else {

            }

        }

    }
}
