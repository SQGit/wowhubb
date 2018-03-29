package com.wowhubb.EventServiceProvider.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.wowhubb.EventServiceProvider.Adapter.EventServiceProviderAdapter;
import com.wowhubb.EventServiceProvider.Model.EventServiceProviderModel;
import com.wowhubb.EventServiceProvider.MyDividerItemDecoration;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;
import com.wowhubb.Utils.Config;
import com.wowhubb.Utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;

public class EventServiceProviderActivity extends AppCompatActivity implements EventServiceProviderAdapter.EventServiceAdapterListener {
    private static final String TAG = EventServiceProviderActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<EventServiceProviderModel> eventServiceProviderList;
    private EventServiceProviderAdapter mAdapter;
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
        getSupportActionBar().setTitle(R.string.event_service);
        toolbar.setTitleTextColor(Color.WHITE);

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(EventServiceProviderActivity.this, v1);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(EventServiceProviderActivity.this);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");

        av_loader = findViewById(R.id.avi);

        recyclerView = findViewById(R.id.recycler_view);
        eventServiceProviderList = new ArrayList<>();
        mAdapter = new EventServiceProviderAdapter(this, eventServiceProviderList, this);

        // white background notification bar
        whiteNotificationBar(recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);

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
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Log.e("tag","check_back");
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

    @Override
    public void onEventServiceProviderSelected(EventServiceProviderModel eventServiceProviderModel) {

    }


    //describe Service Provider:
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
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL_EVENT_SERVICE_PROVIDER, json,token);
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonstr) {
            av_loader.setVisibility(View.GONE);
            super.onPostExecute(jsonstr);
            if (jsonstr.equals("")) {
                Toast.makeText(getApplicationContext(),"Check Network Connection",Toast.LENGTH_LONG).show();
            } else {

                try {

                    JSONObject jo = new JSONObject(jsonstr);
                    String status = jo.getString("success");
                    if(status.equals("true")) {

                        JSONArray event_service = jo.getJSONArray("message");
                        if (event_service.length() > 0) {
                            for (int i1 = 0; i1 < event_service.length(); i1++) {
                                HashMap<String, String> map = new HashMap<String, String>();
                                EventServiceProviderModel eventData = new EventServiceProviderModel();
                                JSONObject jsonObject = event_service.getJSONObject(i1);
                                eventData.providerId=jsonObject.getString("_id");
                                eventData.providerName=jsonObject.getString("provider");
                                eventData.providerLogo=jsonObject.getString("providerlogo");
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
