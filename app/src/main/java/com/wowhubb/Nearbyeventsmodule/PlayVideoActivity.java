package com.wowhubb.Nearbyeventsmodule;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;

/**
 * Created by Guna on 13-02-2018.
 */

public class PlayVideoActivity extends AppCompatActivity implements EasyVideoCallback {

    private static final String TEST_URL = "http://104.197.80.225:3010/wow/media/personal/kriti.mp4";
    TextView tenSeconds, twentySeconds, thirtySeconds;
    ImageView img_close;
    Button claim_btn;
    private EasyVideoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playvideo_activity);
        getSupportActionBar().hide();

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(PlayVideoActivity.this, v1);


        tenSeconds = findViewById(R.id.ten_seconds);
        twentySeconds = findViewById(R.id.twenty_seconds);
        thirtySeconds = findViewById(R.id.thirty_seconds);
        claim_btn = findViewById(R.id.claim_btn);

      /*  points_three = findViewById(R.id.points_three);
        points_six = findViewById(R.id.points_six);
        points_ten = findViewById(R.id.points_ten);*/
        img_close = findViewById(R.id.img_close);

        tenSeconds.setBackgroundResource(R.drawable.video_button_gray);
        twentySeconds.setBackgroundResource(R.drawable.video_button_gray);
        thirtySeconds.setBackgroundResource(R.drawable.video_button_gray);
        tenSeconds.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textcolr));


        claim_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Under Development", Toast.LENGTH_LONG).show();
            }
        });


        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(PlayVideoActivity.this, AllNearbyEventActivity.class));
                overridePendingTransition(R.anim.left_to_right, R.anim.left_to_right);
            }
        });


        // Grabs a reference to the player view
        player = (EasyVideoPlayer) findViewById(R.id.player);

        // Sets the callback to this Activity, since it inherits EasyVideoCallback
        player.setCallback(this);

        // Sets the source to the HTTP URL held in the TEST_URL variable.
        // To play files, you can use Uri.fromFile(new File("..."))
        player.setSource(Uri.parse(TEST_URL));


        // From here, the player view will show a progress indicator until the player is prepared.
        // Once it's prepared, the progress indicator goes away and the controls become enabled for the user to begin playback.
    }

    @Override
    public void onPause() {
        super.onPause();
        // Make sure the player stops playing if the user presses the home button.
        player.pause();
    }

    // Methods for the implemented EasyVideoCallback

    @Override
    public void onPreparing(EasyVideoPlayer player) {
        // TODO handle if needed
    }

    @Override
    public void onPrepared(EasyVideoPlayer player) {
        // TODO handle
        player.start();

        Log.e("tag", "PLAYER START");
    }

    @Override
    public void onBuffering(int percent) {
        // TODO handle if needed
    }

    @Override
    public void onError(EasyVideoPlayer player, Exception e) {
        // TODO handle
    }

    @Override
    public void onCompletion(EasyVideoPlayer player) {
        // TODO handle if needed
        thirtySeconds.setBackgroundResource(R.drawable.btnbg);
        Log.e("tag", "COMPLETION");
    }

    @Override
    public void onRetry(EasyVideoPlayer player, Uri source) {
        // TODO handle if used
        Log.e("tag", "RETRY");
    }

    @Override
    public void onSubmit(EasyVideoPlayer player, Uri source) {
        // TODO handle if used
    }

    @Override
    public void onStarted(final EasyVideoPlayer player) {
        // TODO handle if needed

        Log.e("tag", "STARTED");


        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms

                if (player.getCurrentPosition() >= 24000)
                    tenSeconds.setBackgroundResource(R.drawable.btnbg);


                if (player.getCurrentPosition() >= 48000)
                    twentySeconds.setBackgroundResource(R.drawable.btnbg);


                if (player.getCurrentPosition() > 70000)
                    thirtySeconds.setBackgroundResource(R.drawable.btnbg);
                handler.postDelayed(this, 500);

            }
        }, 500);

    }

    @Override
    public void onPaused(EasyVideoPlayer player) {
        // TODO handle if needed
    }
}