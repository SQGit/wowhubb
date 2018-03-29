package com.wowhubb.Activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.wowhubb.Adapter.MyFeedsPaginationAdapter;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.MyFeedsData.Message;
import com.wowhubb.MyFeedsData.MyFeeds;
import com.wowhubb.R;
import com.wowhubb.Utils.ApiClient;
import com.wowhubb.Utils.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MyEventFeedsActivity extends Activity {


    public static final String BASE_URL = "http://104.197.80.225:3010/wow/event/";
    private static Retrofit retrofit = null;
    String token;
    int page;
    TextView backtv;
    private RecyclerView recyclerView = null;
    private ApiInterface movieApiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myeventfeed);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyEventFeedsActivity.this);
        token = sharedPreferences.getString("token", "");
        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(MyEventFeedsActivity.this, v1);
        recyclerView = (RecyclerView) findViewById(R.id.listview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        backtv = findViewById(R.id.backtv);

        backtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        connectAndGetApiData();

    }

    public void connectAndGetApiData() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }


        movieApiService = ApiClient.getClient().create(ApiInterface.class);

        myFeedsCall().enqueue(new Callback<MyFeeds>() {

            @Override
            public void onResponse(Call<MyFeeds> call, Response<MyFeeds> response) {
                if (response.isSuccessful()) {
                    Log.e("tag", "response----------->" + response.message());
                    List<Message> msg = response.body().getMessage();
                    Log.e("tag", "size----------->" + msg.size());
                    MyFeedsPaginationAdapter adapter = new MyFeedsPaginationAdapter(MyEventFeedsActivity.this);
                    adapter.addAll(msg);
                    recyclerView.setAdapter(adapter);


                } else {


                }
            }

            @Override
            public void onFailure(Call<MyFeeds> call, Throwable t) {
                t.printStackTrace();

            }
        });


    }

    private Call<MyFeeds> myFeedsCall() {
        return movieApiService.checkFeeds("application/x-www-form-urlencoded", token, page);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
