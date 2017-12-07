package com.wowhubb.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.Fragment.EventFeedFragment;
import com.wowhubb.Fragment.EventHubbFragment;
import com.wowhubb.Fragment.MyNetworkFragment;
import com.wowhubb.Fragment.WowFragment;
import com.wowhubb.R;
import com.wowhubb.Utils.Config;
import com.wowhubb.Utils.HttpUtils;
import com.wowhubb.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Ramya on 07-08-2017.
 */

public class EventFeedDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Typeface lato;
    ImageView backiv, btn_menu;
    DrawerLayout drawer;
    Toolbar toolbar;
    NavigationView navigationView;
    ImageView profileedit_iv;
    SharedPreferences.Editor editor;
    String token, personalimage, firstname, lastname, email;
    TextView profilenametv, profileemailtv, logouttv, eventcreate_tv;
    ImageView profile_iv;
    com.android.volley.toolbox.ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    EditText searchpeople_et;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        lato = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");
        View v1 = getWindow().getDecorView().getRootView();

        FontsOverride.overrideFonts(EventFeedDashboard.this, v1);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(EventFeedDashboard.this);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        backiv = findViewById(R.id.backiv);
        btn_menu = (ImageView) findViewById(R.id.img_menu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        profileedit_iv = findViewById(R.id.profile_editiv);
        profile_iv = findViewById(R.id.imageview_profile);
        profilenametv = findViewById(R.id.name_tv);
        profileemailtv = findViewById(R.id.email_tv);
        searchpeople_et = findViewById(R.id.searchpeople_et);
        logouttv = findViewById(R.id.logout_tv);
        eventcreate_tv = findViewById(R.id.eventcreate_tv);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.app_name, R.string.app_name);
        drawer.setDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.syncState();
        new getpersonalprofile().execute();

        navigationView.setNavigationItemSelectedListener(this);

        backiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EventFeedDashboard.this, LandingPageActivity.class));
            }
        });

        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.RIGHT);
            }
        });

        profileedit_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("profile", "true");
                startActivity(new Intent(EventFeedDashboard.this, ProfileActivity.class));
            }
        });

        eventcreate_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EventFeedDashboard.this, CreateEventActivity.class));
                finish();
            }
        });

        logouttv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(EventFeedDashboard.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Logout")
                        .setMessage("Do You want to Logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent setIntent = new Intent(Intent.ACTION_MAIN);
                                setIntent.addCategory(Intent.CATEGORY_HOME);
                                setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(setIntent);
                                finish();
                                SharedPreferences sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor edit = sharedPrefces.edit();
                                edit.putString("status", "false");
                                edit.commit();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        });

    }

    private void setupTabIcons() {
        View v2 = tabLayout.getRootView();
        FontsOverride.overrideFonts(EventFeedDashboard.this, v2);
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.customtab_title, null);
        tabOne.setText("Event Feed");
        tabOne.setTextSize(14);
        tabOne.setTypeface(lato);
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.eventfeed_tab, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.customtab_title, null);
        tabTwo.setText(" Network");
        tabTwo.setTypeface(lato);
        tabTwo.setTextSize(14);
        tabTwo.setGravity(View.TEXT_ALIGNMENT_CENTER);
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.network_tab, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.customtab_title, null);
        tabThree.setText(" Wowtag");
        tabThree.setTypeface(lato);
        tabThree.setTextSize(14);
        tabThree.setGravity(View.TEXT_ALIGNMENT_CENTER);

        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.wowtag_tab, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabfour = (TextView) LayoutInflater.from(this).inflate(R.layout.customtab_title, null);
        tabfour.setText(" Event Hubb");
        tabfour.setSingleLine();
        tabfour.setTypeface(lato);
        tabfour.setTextSize(14);

        tabThree.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
        tabfour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.eventhubb_tab, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabfour);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new EventFeedFragment(), "Event Feed");
        adapter.addFrag(new MyNetworkFragment(), "My Network");
        adapter.addFrag(new WowFragment(), "Wowtag");
        adapter.addFrag(new EventHubbFragment(), "Event Hubb");
        viewPager.setAdapter(adapter);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    //------------------------------ASYN TASK FOR PROFILE GET PROFILE------------------------------//

    public class getpersonalprofile extends AsyncTask<String, Void, String> {

        public getpersonalprofile() { }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL_GETPERSONAL, json, token);

            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "getprofileEven------->" + s);
            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("success");
                    if (status.equals("true"))
                    {
                        JSONObject message = jo.getJSONObject("message");
                        if (message.has("personalimage"))
                        {
                            personalimage = message.getString("personalimage");
                            if (!personalimage.equals(""))
                            {
                                Glide.with(EventFeedDashboard.this).load("http://104.197.80.225:3010/wow/media/personal/" + personalimage).into(profile_iv);
                            }
                        }
                        if (message.has("firstname")) {
                            firstname = message.getString("firstname");
                        }
                        if (message.has("lastname")) {
                            lastname = message.getString("lastname");
                            profilenametv.setText(firstname + " " + lastname);
                        }
                        if (message.has("email")) {
                            email = message.getString("email");
                            profileemailtv.setText(email);
                        }
                    }

                } catch (JSONException e) {

                }

            } else {

            }

        }

    }

}
