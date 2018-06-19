package vineture.wowhubb.Nearbyeventsmodule;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;
import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.R;

/**
 * Created by Guna on 13-02-2018.
 */

public class PlayMultipleActivity extends AppCompatActivity implements EasyVideoCallback {

    private static final String TEST_URL1 = "http://104.197.80.225:3010/wow/media/personal/will.mp4";
    private static final String TEST_URL2 = "http://104.197.80.225:3010/wow/media/personal/emma.mp4";
    private static final String TEST_URL3 = "http://104.197.80.225:3010/wow/media/personal/kriti.mp4";


    private EasyVideoPlayer player1,player2,player3;

    TextView register;
            //,points_three,points_six,points_ten;
    ImageView img_back,video1plus1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playvideo_multiple_activity);
        getSupportActionBar().hide();

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(PlayMultipleActivity.this, v1);

        img_back=findViewById(R.id. img_back);
        register=findViewById(R.id.register);



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Under Development",Toast.LENGTH_LONG).show();
            }
        });


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               finish();
            }
        });


        // Grabs a reference to the player view
        player1 = (EasyVideoPlayer) findViewById(R.id.player1);
        player2 = (EasyVideoPlayer) findViewById(R.id.player2);
        player3 = (EasyVideoPlayer) findViewById(R.id.player3);

        // Sets the callback to this Activity, since it inherits EasyVideoCallback
        player1.setCallback(this);
        player2.setCallback(this);
        player3.setCallback(this);

        // Sets the source to the HTTP URL held in the TEST_URL variable.
        // To play files, you can use Uri.fromFile(new File("..."))
        player1.setSource(Uri.parse(TEST_URL1));

        player2.setSource(Uri.parse(TEST_URL2));

        player3.setSource(Uri.parse(TEST_URL3));

        // From here, the player view will show a progress indicator until the player is prepared.
        // Once it's prepared, the progress indicator goes away and the controls become enabled for the user to begin playback.
    }

    @Override
    public void onPause() {
        super.onPause();
        // Make sure the player stops playing if the user presses the home button.
        player1.pause();
        player2.pause();
        player3.pause();

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

       Log.e("tag","PLAYER START");
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

    }

    @Override
    public void onRetry(EasyVideoPlayer player, Uri source) {
        // TODO handle if used
        Log.e("tag","RETRY");
    }

    @Override
    public void onSubmit(EasyVideoPlayer player, Uri source) {
        // TODO handle if used
    }

    @Override
    public void onStarted(final EasyVideoPlayer player) {
        // TODO handle if needed

        Log.e("tag","STARTED");


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms






            }
        }, 500);

    }

    @Override
    public void onPaused(EasyVideoPlayer player) {
        // TODO handle if needed
    }
}