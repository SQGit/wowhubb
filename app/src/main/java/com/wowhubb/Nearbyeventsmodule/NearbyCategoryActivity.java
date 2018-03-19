package com.wowhubb.Nearbyeventsmodule;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;
import com.wowhubb.EventServiceProvider.EventServiceProviderModel;
import com.wowhubb.EventServiceProvider.MyDividerItemDecoration;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;
import com.wowhubb.Utils.Config;
import com.wowhubb.Utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;

public class NearbyCategoryActivity extends AppCompatActivity implements NearByEventAdapter.NearByEventListener {
    private static final String TAG = NearbyCategoryActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<EventServiceProviderModel> eventServiceProviderList;
    private NearbyEventsCategoryAdapter mAdapter;
    private SearchView searchView;

    AVLoadingIndicatorView av_loader;
    String token;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_service_provider);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.nearbyevent);
        toolbar.setTitleTextColor(Color.WHITE);

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(NearbyCategoryActivity.this, v1);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(NearbyCategoryActivity.this);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");
        Log.e("tag","token---------->"+token);



        av_loader = findViewById(R.id.avi);

        recyclerView = findViewById(R.id.recycler_view);
        eventServiceProviderList = new ArrayList<>();
        mAdapter = new NearbyEventsCategoryAdapter(this, eventServiceProviderList, this);

        // white background notification bar
        whiteNotificationBar(recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);

//        fetchContacts();

        new eventServiceProviderAsync().execute();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Log.e("tag","check_back");
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }



    class eventServiceProviderAsync extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            av_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {

            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();

                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL_GET_NEAR_BY_EVENTS, json,token);
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonstr) {
            Log.e("tag", "<-----0000000--------->" + jsonstr);
            av_loader.setVisibility(View.GONE);
            super.onPostExecute(jsonstr);
            List<EventServiceProviderModel> data=new ArrayList<>();
            if (jsonstr.equals("")) {
                Toast.makeText(getApplicationContext(),"Check Network Connection",Toast.LENGTH_LONG).show();
            } else {

                try {

                    JSONObject jo = new JSONObject(jsonstr);
                    String status = jo.getString("success");
                    if(status.equals("true")) {

                        JSONArray event_service = jo.getJSONArray("message");
                        Log.e("tag", "message value----->" + event_service);
                        if (event_service.length() > 0) {
                            for (int i1 = 0; i1 < event_service.length(); i1++) {
                                HashMap<String, String> map = new HashMap<String, String>();
                                EventServiceProviderModel eventData = new EventServiceProviderModel();
                                JSONObject jsonObject = event_service.getJSONObject(i1);
                                eventData.providerId=jsonObject.getString("_id");
                                eventData.providerName=jsonObject.getString("eventtype");
                                eventData.providerLogo=jsonObject.getString("eventlogo");

                                eventServiceProviderList.add(eventData);
                            }



                            mAdapter.notifyDataSetChanged();

                        } else {
                            //data empty
                        }

                    }
                    else
                    {

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }





}
