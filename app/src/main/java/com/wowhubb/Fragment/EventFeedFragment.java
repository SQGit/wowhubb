package com.wowhubb.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.wang.avi.AVLoadingIndicatorView;
import com.wowhubb.Adapter.FeedListAdapter;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;
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


public class EventFeedFragment extends Fragment {
    String token;
    private ListView listView;
    private FeedListAdapter listAdapter;
    private List<FeedItem> feedItems;
    private String URL_FEED = "http://104.197.80.225:3010/wow/event/newfeed";
    Dialog dialog;

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eventfeed, container, false);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        token = sharedPreferences.getString("token", "");

        FontsOverride.overrideFonts(getActivity(), view);

        listView = (ListView) view.findViewById(R.id.listview);
        feedItems = new ArrayList<FeedItem>();
        listAdapter = new FeedListAdapter(getActivity(), feedItems);
        listView.setAdapter(listAdapter);
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.test_loader);

        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Entry entry = cache.get(URL_FEED);

        if (entry != null) {
            Log.e("tag", "entry----->>");
            try {
                String data = new String(entry.data, "UTF-8");
                try
                {
                    dialog.show();
                    parseJsonFeed(new JSONObject(data));
                }
               catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            Log.e("tag", "entry-------");
            JsonObjectRequest jsonReq = new JsonObjectRequest(Method.POST,
                    URL_FEED, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("tag", "Response: " + response.toString());
                    dialog.show();
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
            AppController.getInstance().addToRequestQueue(jsonReq);

        }


        return view;
    }

    /**
     * Parsing json reponse and passing the data to feed view list adapter
     */
    private void parseJsonFeed(JSONObject response) {
        try {

            JSONArray feedArray = response.getJSONArray("message");
            //JSONArray feedArray1 = feedArray.getJSONArray("message");
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                FeedItem item = new FeedItem();
                JSONObject user = feedObj.getJSONObject("userid");
                String fname = user.getString("firstname");
                String lname = user.getString("lastname");
                String wowtagid = user.getString("wowtagid");
                item.setName(fname + " " + lname);

                if (user.has("personalimage")) {
                    String personalimage = user.getString("personalimage");
                    Log.e("tag", "personal img----------->" + personalimage);
                    item.setProfilePic(personalimage);
                }
                item.setEventid(feedObj.getString("_id"));
                item.setEventname(feedObj.getString("eventname"));
                item.setDescription(feedObj.getString("eventdescription"));
                item.setTimeStamp(feedObj.getString("createdAt"));
                item.setEventdate(feedObj.getString("eventdate"));
                item.setCoverimage(feedObj.getString("coverpage1"));
                item.setEventtopic(feedObj.getString("eventtopic"));
                item.setRuntimefrom(feedObj.getString("runtimefrom"));
                item.setRuntimeto(feedObj.getString("runtimeto"));
                item.setOthersurl(feedObj.getString("otherurl"));
                item.setDonationurl(feedObj.getString("donationsurl"));
                item.setGiftregistryurl(feedObj.getString("giftregistryurl"));

                if (user.has("designation"))
                {
                    item.setDesignation(user.getString("designation"));
                }
                if (feedObj.has("eventvenuefulladdress"))
                {
                    item.setFulladdress(feedObj.getString("eventvenuefulladdress"));
                }
                String image = feedObj.isNull("coverpage1") ? null : feedObj
                        .getString("coverpage1");
                item.setImge(image);

                String wowatagvideo1 = feedObj.isNull("wowtagvideo1") ? null : feedObj
                        .getString("wowtagvideo1");
                item.setWowtagvideo(wowatagvideo1);
                Log.e("tag", "wowtag--------->" + wowatagvideo1);

                String eventhighlights1 = feedObj.isNull("eventhighlights1") ? null : feedObj
                        .getString("eventhighlights1");
                item.setHighlight1(eventhighlights1);
                Log.e("tag", "eventhighlights1--------->" + eventhighlights1);

                String eventhighlights2 = feedObj.isNull("eventhighlights2") ? null : feedObj
                        .getString("eventhighlights2");
                item.setHighlight2(eventhighlights2);
                Log.e("tag", "eventhighlights1--------->" + eventhighlights1);

                String eventhighlightsvideo1 = feedObj.isNull("eventhighlightsvideo1") ? null : feedObj
                        .getString("eventhighlightsvideo1");
                item.setHighlightvideo1(eventhighlightsvideo1);
                Log.e("tag", "eventhighlightsvideo1--------->" + eventhighlightsvideo1);

                String eventhighlightsvideo2 = feedObj.isNull("eventhighlightsvideo2") ? null : feedObj
                        .getString("eventhighlightsvideo2");
                item.setHighlightvideo2(eventhighlightsvideo2);
                Log.e("tag", "eventhighlightsvideo2--------->" + eventhighlightsvideo2);

                String feedUrl = "http://104.197.80.225:3010/wow/media/event/";
                item.setUrl(feedUrl);
                feedItems.add(item);
                dialog.dismiss();

            }

            listAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
