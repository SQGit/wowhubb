package com.wowhubb.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageView;

import com.wowhubb.Adapter.TodayEventsAdapter;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;
import com.wowhubb.Utils.ApiClient;
import com.wowhubb.Utils.ApiInterface;
import com.wowhubb.todayevents.Main;
import com.wowhubb.todayevents.Message;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Salman on 07-05-2018.
 */

public class TodayEventActivity extends Activity {
    private static Retrofit retrofit = null;
    GridView gridView;
    SharedPreferences.Editor editor;
    String token;
    Dialog loader_dialog;
    ApiInterface apiInterface;
    TodayEventsAdapter todayEventsAdapter;
    ImageView closeiv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_todayevents);

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(TodayEventActivity.this, v1);
        gridView = findViewById(R.id.gridlayout);
        closeiv=findViewById(R.id.closeiv);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(TodayEventActivity.this);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");
        loader_dialog = new Dialog(TodayEventActivity.this);
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(R.layout.test_loader);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        connectAndGetApiData();

        closeiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void connectAndGetApiData() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://104.197.80.225:3010/wow/event/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        ApiInterface movieApiService = retrofit.create(ApiInterface.class);

        Call<Main> call = movieApiService.getTodayEvents("application/x-www-form-urlencoded", token);
        call.enqueue(new Callback<Main>() {
            @Override
            public void onResponse(Call<Main> call, Response<Main> response) {
                List<Message> movies = response.body().getMessage();
                gridView.setAdapter(new TodayEventsAdapter(movies, TodayEventActivity.this));
                Log.d("tag", "Number of movies received: " + movies.size());

            }

            @Override
            public void onFailure(Call<Main> call, Throwable throwable) {
                Log.e("tag", throwable.toString());
            }
        });
    }


}
