package com.wowhubb.Activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.wowhubb.Adapter.FeedListAdapter;
import com.wowhubb.Adapter.RecyclerAdapter;
import com.wowhubb.R;
import com.wowhubb.Utils.HttpUtils;
import com.wowhubb.app.AppController;
import com.wowhubb.data.FeedItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ramya on 20-09-2017.
 */

public class Test extends Activity {
   // private FeedListAdapter listAdapter;
    private List<FeedItem> feedItems;
    private String URL_FEED = "http://104.197.80.225:3010/wow/event/feed";
    String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        //to use RecycleView, you need a layout manager. default is LinearLayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1OWU0YWFlZWMxY2VjODFiMzA4YmM1MzkiLCJmaXJzdG5hbWUiOiJIYXJpIiwibGFzdG5hbWUiOiJWaWpheSIsImVtYWlsIjoiaGFyaUBzcWluZGlhLm5ldCIsInBob25lIjoiKzkxODg3MDQ2MzMzOSIsImJpcnRoZGF5IjoiNS8xOS8xOTk0Iiwid293dGFnaWQiOiJoYXJpdmlqYXkiLCJmaXJzdHRpbWUiOiJmYWxzZSIsImlhdCI6MTUwODc1MTk2NCwiZXhwIjoxNTA4OTI0NzY0fQ.fykDcioWW-kmhPwVxofKkQa4VHN_NG53baBgKYWLcMk";

        // listView = (ListView) view.findViewById(R.id.listview);
        feedItems = new ArrayList<FeedItem>();
        Log.e("tag", "entry----->>"+feedItems);
        RecyclerAdapter adapter = new RecyclerAdapter(Test.this,feedItems);
        recyclerView.setAdapter(adapter);


        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(URL_FEED);
        if (entry != null) {
            // fetch the data from cache
            Log.e("tag", "entry----->>");
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseJsonFeed(new JSONObject(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            Log.e("tag", "entry-------");
            // making fresh volley request and getting json
            JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.POST,
                    URL_FEED, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.e("tag", "Response: " + response.toString());
                    if (response != null) {
                        parseJsonFeed(response);
                    }
                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("tag", "Error: " + error.getMessage());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("token", token);
                    return headers;
                }


            };
            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(jsonReq);


        }


    }


    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("message");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                FeedItem item = new FeedItem();
                JSONObject user = feedObj.getJSONObject("userid");
                String fname = user.getString("firstname");
                String lname = user.getString("lastname");
                String wowtagid = user.getString("wowtagid");
                item.setName(fname + " " + lname);
                Log.e("tag","11111111"+fname+lname);
                Log.e("tag","11111111"+fname+lname);


                if (user.has("personalimage")) {

                    String personalimage = user.getString("personalimage");
                    Log.e("tag","personal img------------->"+personalimage);
                    item.setProfilePic(personalimage);

                }


                item.setEventname(feedObj.getString("eventname"));
                item.setDescription(feedObj.getString("highlightdescription"));
                item.setTimeStamp(feedObj.getString("createAt"));

                // Image might be null sometimes
                String image = feedObj.isNull("coverpage1") ? null : feedObj
                        .getString("coverpage1");
                item.setImge(image);

                String wowatagvideo1 = feedObj.isNull("wowtagvideo1") ? null : feedObj
                        .getString("wowtagvideo1");
                item.setWowtagvideo(wowatagvideo1);

                String eventhighlights1 = feedObj.isNull("eventhighlights1") ? null : feedObj
                        .getString("eventhighlights1");
                item.setHighlight1(eventhighlights1);
                String eventhighlights2 = feedObj.isNull("eventhighlights2") ? null : feedObj
                        .getString("eventhighlights2");
                item.setHighlight2(eventhighlights2);


                // url might be null sometimes
                String feedUrl = "http://104.197.80.225:3010/wow/media/event/";
                item.setUrl(feedUrl);

                feedItems.add(item);
            }

            // notify data changes to list adapater
           // re.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}
