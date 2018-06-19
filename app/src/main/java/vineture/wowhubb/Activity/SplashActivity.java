package vineture.wowhubb.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import vineture.wowhubb.Adapter.ViewPagerAdapter;
import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.Pager.SimpleViewPagerIndicator;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Ramya on 19-07-2017.
 */

public class SplashActivity extends Activity {

    final long DELAY_MS = 300;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.
    int currentPage = 0;
    Timer timer;
    TextView splashcontent_tv;
    int i;
    TextView registertv, logintv;
    int NUM_PAGES = 4;
    String status;
    String currentVersion, playstoreVersion;
    Dialog dg_show_update;
    TextView tv_dg_txt, tv_dg_txt2;
    Button btn_dg_download;
    private ViewPager mPager;
    private SimpleViewPagerIndicator pageIndicator;
    private int resourceList[] = {vineture.wowhubb.R.drawable.splash_image1,
            vineture.wowhubb.R.drawable.splash_image2, vineture.wowhubb.R.drawable.splash_image3
    };

    private int List[] = {vineture.wowhubb.R.string.splashtwo, vineture.wowhubb.R.string.splashtwo, vineture.wowhubb.R.string.splashtwo};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(vineture.wowhubb.R.layout.activity_splash);

        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            Log.e("tag", "version:" + currentVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.e("tag", "err:" + e.toString());
        }

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
        splashcontent_tv = (TextView) findViewById(vineture.wowhubb.R.id.splashcontent);
        status = sharedPreferences.getString("status", "");
        registertv = findViewById(vineture.wowhubb.R.id.registertv);
        logintv = findViewById(vineture.wowhubb.R.id.logintv);

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(SplashActivity.this, v1);
        initView();
        Log.e("tag", "aaaaa--->>>>>>>>>>>>" + status);

        registertv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplashActivity.this, RegisterAllDetailsActivity.class));
                overridePendingTransition(vineture.wowhubb.R.anim.slide_in_up, vineture.wowhubb.R.anim.slide_out_up);
                finish();
            }
        });

        logintv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                overridePendingTransition(vineture.wowhubb.R.anim.slide_in_up, vineture.wowhubb.R.anim.slide_out_up);
                finish();
            }
        });


        if (status == "" || status == null) {
            logintv.setVisibility(View.VISIBLE);
            registertv.setVisibility(View.VISIBLE);
            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {

                    if (currentPage == 1) {
                        String splashone = getString(vineture.wowhubb.R.string.splashtwo);
                        splashcontent_tv.setText("");
                        splashcontent_tv.setText(splashone);

                    } else if (currentPage == 2) {
                        String splashtwo = getString(vineture.wowhubb.R.string.splashothree);
                        splashcontent_tv.setText("");
                        splashcontent_tv.setText(splashtwo);
                    } else if (currentPage == 3) {
                        String splashthree = getString(vineture.wowhubb.R.string.splashone);
                        splashcontent_tv.setText("");
                        splashcontent_tv.setText(splashthree);

                    }


                    if (currentPage == NUM_PAGES - 1) {
                        currentPage = 0;
                    }
                    mPager.setCurrentItem(currentPage++, true);
                }
            };

            timer = new Timer(); // This will create a new Thread
            timer.schedule(new TimerTask() { // task to be scheduled

                @Override
                public void run() {

                    handler.post(Update);

                }
            }, DELAY_MS, PERIOD_MS);


        } else if (status.equals("false")) {

            logintv.setVisibility(View.VISIBLE);
            registertv.setVisibility(View.VISIBLE);
            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {

                    if (currentPage == 1) {

                        Log.e("tag", "page1---------->" + currentPage);
                        String splashone = getString(vineture.wowhubb.R.string.splashtwo);
                        splashcontent_tv.setText("");
                        splashcontent_tv.setText(splashone);


                    } else if (currentPage == 2) {

                        Log.e("tag", "page2---------->" + currentPage);
                        String splashtwo = getString(vineture.wowhubb.R.string.splashothree);
                        splashcontent_tv.setText("");
                        splashcontent_tv.setText(splashtwo);

                    } else if (currentPage == 3) {

                        Log.e("tag", "page3---------->" + currentPage);
                        String splashthree = getString(vineture.wowhubb.R.string.splashone);
                        splashcontent_tv.setText("");
                        splashcontent_tv.setText(splashthree);


                    }


                    if (currentPage == NUM_PAGES - 1) {
                        currentPage = 0;
                    }
                    mPager.setCurrentItem(currentPage++, true);
                }
            };

            timer = new Timer(); // This will create a new Thread
            timer.schedule(new TimerTask() { // task to be scheduled

                @Override
                public void run() {

                    handler.post(Update);

                }
            }, DELAY_MS, PERIOD_MS);


        } else {
            logintv.setVisibility(View.INVISIBLE);
            registertv.setVisibility(View.INVISIBLE);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    startActivity(new Intent(SplashActivity.this, LandingPageActivity.class));
                    overridePendingTransition(vineture.wowhubb.R.anim.slide_in_up, vineture.wowhubb.R.anim.slide_out_up);

                }
            }, 2000);

        }
    }

    public void initView() {
        // TODO Auto-generated method stub
        mPager = (ViewPager) findViewById(vineture.wowhubb.R.id.pager);
        pageIndicator = (SimpleViewPagerIndicator) findViewById(vineture.wowhubb.R.id.page_indicator);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        ViewPagerAdapter mPagerAdapter = new ViewPagerAdapter(this,
                resourceList, List, width, height);
        mPager.setOffscreenPageLimit(1);
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(0); // current item number
        pageIndicator.setViewPager(mPager);
        pageIndicator.notifyDataSetChanged();
    }

  /*  private class GetVersionCode extends AsyncTask<Void, String, String> {

        @Override

        protected String doInBackground(Void... voids) {

            String newVersion = null;

            try {

                Log.e("tag", "https://play.google.com/store/apps/details?id=" + SplashActivity.this.getPackageName() + "&hl=it");

                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + SplashActivity.this.getPackageName() + "&hl=it").timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div[itemprop=softwareVersion]")
                        .first()
                        .ownText();

                Log.e("Tag", "new: " + newVersion);

                return newVersion;

            } catch (Exception e) {

                Log.e("Tag", "dreerr: " + e.toString());
                return newVersion;

            }

        }


        @Override

        protected void onPostExecute(String onlineVersion) {


            Log.e("tag", "OUTPUT: " + onlineVersion);

            super.onPostExecute(onlineVersion);

            playstoreVersion = onlineVersion;

            if (playstoreVersion != null && !playstoreVersion.isEmpty()) {

                if (Float.valueOf(currentVersion) < Float.valueOf(playstoreVersion)) {

                    //show dialog
                    dg_show_update.show();

                }
            } else {
                //nextpage
            }

            Log.e("update", "Current version " + currentVersion + "playstore version " + playstoreVersion);

        }
    }
*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
