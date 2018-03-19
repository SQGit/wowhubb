package com.wowhubb.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.Fragment.EventFeeds;
import com.wowhubb.Fragment.EventServiceFragment;
import com.wowhubb.Fragment.MyNetworkFragment;
import com.wowhubb.R;
import com.wowhubb.Utils.Config;
import com.wowhubb.Utils.HttpUtils;
import com.wowhubb.app.AppController;
import com.wowhubb.wowtag.Fragment.WowFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Ramya on 07-08-2017.
 */

public class EventFeedDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Typeface lato;
    ImageView backiv, btn_menu;
    DrawerLayout drawer;
    NavigationView navigationView;
    ImageView profileedit_iv;
    SharedPreferences.Editor editor;
    String token, personalimage, firstname, lastname, email, str_email, str_name, str_lname, personalself;
    TextView profilenametv, profileemailtv, logouttv, eventcreate_tv, interest_tv, profile_tv, settings_tv, serviceprovider_tv, notificationtv;
    ImageView profile_iv;
    com.android.volley.toolbox.ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    Toolbar toolbar, searchtollbar;
    Menu search_menu;
    MenuItem item_search;
    TextView tv_snack;
    Snackbar snackbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        lato = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");

        View v1 = getWindow().getDecorView().getRootView();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        FontsOverride.overrideFonts(EventFeedDashboard.this, v1);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(EventFeedDashboard.this);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");
        str_name = sharedPreferences.getString("str_name", "");
        str_lname = sharedPreferences.getString("str_lname", "");
        personalimage = sharedPreferences.getString("profilepath", "");
        str_email = sharedPreferences.getString("str_email", "");

        profile_iv = findViewById(R.id.imageview_profile);
        profilenametv = findViewById(R.id.name_tv);
        profileemailtv = findViewById(R.id.email_tv);

        Log.e("tag", "kvkvpersonal image----------" + str_name);

        //-----------------------------------------SNACKBAR----------------------------------------//
        snackbar = Snackbar.make(findViewById(R.id.drawer_layout), R.string.networkError, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(lato);
        tv_snack.setTypeface(lato);

        if (personalimage != null) {
            Glide.with(EventFeedDashboard.this).load("http://104.197.80.225:3010/wow/media/personal/" + personalimage).into(profile_iv);
        } else {
            profile_iv.setImageResource(R.drawable.profile_img);
        }


        if (str_email != null) {
            profileemailtv.setVisibility(View.VISIBLE);
            profileemailtv.setText(str_email);

        } else {
            profileemailtv.setVisibility(View.INVISIBLE);
        }
        if (str_name != null) {
            profilenametv.setText(str_name);
        }

        if (str_lname != null) {
            profilenametv.append(" " + str_lname);
        }

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        backiv = findViewById(R.id.backiv);
        // btn_menu = (ImageView) findViewById(R.id.img_menu);
        // toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        profileedit_iv = findViewById(R.id.profile_editiv);

        notificationtv = findViewById(R.id.notification_tv);
        // searchpeople_et = findViewById(R.id.searchpeople_et);
        logouttv = findViewById(R.id.logout_tv);
        eventcreate_tv = findViewById(R.id.eventcreate_tv);
        // serviceprovider_tv = findViewById(R.id.serviceprovider_tv);
        // feeds_tv = findViewById(R.id.eventfeed_tv);
        interest_tv = findViewById(R.id.interest_tv);
        profile_tv = findViewById(R.id.profile_tv);
        settings_tv = findViewById(R.id.settings_tv);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.app_name, R.string.app_name);
        drawer.setDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);

        backiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EventFeedDashboard.this, LandingPageActivity.class));
            }
        });


        profileedit_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventFeedDashboard.this, ProfileActivity.class);
                intent.putExtra("navdashboard", "true");
                startActivity(intent);

            }
        });

        notificationtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventFeedDashboard.this, SettingsNotificationsActivity.class);
                // intent.putExtra("navdashboard", "true");
                startActivity(intent);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });


       /* serviceprovider_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(EventFeedDashboard.this, EventServiceProviderActivity.class);
                intent.putExtra("navdashboard", "true");
                startActivity(intent);
            }
        });
        feeds_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });*/

        profile_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventFeedDashboard.this, ProfileActivity.class);
                intent.putExtra("navdashboard", "true");
                startActivity(intent);
            }
        });

        interest_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventFeedDashboard.this, InterestActivity.class);
                intent.putExtra("navdashboard", "true");
                startActivity(intent);

            }
        });

        settings_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventFeedDashboard.this, SettingsActivity.class);
                overridePendingTransition(R.anim.left_to_right, R.anim.left_to_right);
                intent.putExtra("navdashboard", "true");
                startActivity(intent);

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
                                setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(setIntent);

                                SharedPreferences sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor edit = sharedPrefces.edit();
                                edit.remove("status");
                                edit.clear();
                                edit.commit();
                                finish();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case android.R.id.home:
                // this.finish();

                Intent intent = new Intent(EventFeedDashboard.this, LandingPageActivity.class);
                startActivity(intent);
                finish();
                return true;

            case R.id.action_search:
                // setSearchtollbar();
                return true;
            // break;

            case R.id.action_nav:
                hideSoftKeyboard(EventFeedDashboard.this);
                drawer.openDrawer(Gravity.RIGHT);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }


    }

    private void setupTabIcons() {
        View v2 = tabLayout.getRootView();
        FontsOverride.overrideFonts(EventFeedDashboard.this, v2);
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.customtab_title, null);
        tabOne.setText("Event Feed");
        tabOne.setMinimumWidth(0);
        tabOne.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
        tabOne.setTextSize(13);
        tabOne.setTypeface(lato);
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.eventfeed_tab, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.customtab_title, null);
        tabTwo.setText(" Network");
        tabTwo.setMinimumWidth(0);
        tabTwo.setTypeface(lato);
        tabTwo.setTextSize(13);
        tabTwo.setGravity(View.TEXT_ALIGNMENT_CENTER);
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.network_tab, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.customtab_title, null);
        tabThree.setText("  Wowtag");
        tabThree.setMinimumWidth(0);
        tabThree.setTypeface(lato);
        tabThree.setTextSize(13);
        tabThree.setGravity(View.TEXT_ALIGNMENT_TEXT_END);

        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.wowtag_tab, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

      /*  TextView tabfour = (TextView) LayoutInflater.from(this).inflate(R.layout.customtab_title, null);
        tabfour.setText("   Hubb");
        tabfour.setMinimumWidth(0);
        tabfour.setSingleLine();
        tabfour.setTypeface(lato);
        tabfour.setTextSize(12);
        tabfour.setGravity(View.TEXT_ALIGNMENT_CENTER);
        tabThree.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
        tabfour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.eventhubb_tab, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabfour);*/

        TextView tabfive = (TextView) LayoutInflater.from(this).inflate(R.layout.customtab_title, null);
        tabfive.setText("  Services");
        tabfive.setMinimumWidth(0);
        tabfive.setSingleLine();
        tabfive.setTypeface(lato);
        tabfive.setTextSize(13);

        tabfive.setGravity(View.TEXT_ALIGNMENT_TEXT_END);
        tabfive.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.service_tab, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabfive);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new EventFeeds(), "Event Feed");
        adapter.addFrag(new MyNetworkFragment(), "My Network");
        adapter.addFrag(new WowFragment(), "Wowtag");
        // adapter.addFrag(new EventHubbFragment(), "Event Hubb");
        adapter.addFrag(new EventServiceFragment(), "Services");
        viewPager.setAdapter(adapter);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    public void setSearchtollbar() {
        Log.e("tag", "setSearchtollbar: NULL1111111");
        searchtollbar = (Toolbar) findViewById(R.id.searchtoolbar);
        if (searchtollbar != null) {
            searchtollbar.inflateMenu(R.menu.menu_search);
            search_menu = searchtollbar.getMenu();

            searchtollbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        circleReveal(R.id.searchtoolbar, 1, true, false);
                    else
                        searchtollbar.setVisibility(View.GONE);
                    searchtollbar.setBackgroundColor(getResources().getColor(R.color.white));
                }
            });


            item_search = search_menu.findItem(R.id.action_filter_search);

            MenuItemCompat.setOnActionExpandListener(item_search, new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    // Do something when collapsed
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        circleReveal(R.id.searchtoolbar, 1, true, false);
                    } else
                        searchtollbar.setVisibility(View.GONE);
                    searchtollbar.setBackgroundColor(getResources().getColor(R.color.white));
                    return true;
                }

                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    startActivity(new Intent(EventFeedDashboard.this, LandingPageActivity.class));

                    // Do something when expanded
                    return true;
                }
            });

            initSearchView();


        } else {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.RESULT_HIDDEN);
            Log.e("tag", "setSearchtollbar: NULL");
        }
    }

    //------------------------------ASYN TASK FOR PROFILE GET PROFILE-----------------------------//

    public void initSearchView() {
        final SearchView searchView =
                (SearchView) search_menu.findItem(R.id.action_filter_search).getActionView();
        // Enable/Disable Submit button in the keyboard

        searchView.setSubmitButtonEnabled(false);
        // Change search close button image
        ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);
        closeButton.setImageResource(R.drawable.ic_close);

        // set hint and the text colors
        EditText txtSearch = ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
        txtSearch.setHint("Search..");
        txtSearch.setHintTextColor(Color.DKGRAY);
        txtSearch.setTextColor(getResources().getColor(R.color.colorPrimary));

        // set the cursor

        AutoCompleteTextView searchTextView = (AutoCompleteTextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            //searchTextView.setBackground();
            mCursorDrawableRes.set(searchTextView, R.drawable.search_cursor); //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (Exception e) {
            e.printStackTrace();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callSearch(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                callSearch(newText);
                return true;
            }

            public void callSearch(String query) {
                //Do searching
                Log.e("query", "query-------->" + query);


            }

        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void circleReveal(int viewID, int posFromRight, boolean containsOverflow, final boolean isShow) {
        final View myView = findViewById(viewID);

        int width = myView.getWidth();

        if (posFromRight > 0)
            width -= (posFromRight * getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material)) - (getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) / 2);
        if (containsOverflow)
            width -= getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material);

        int cx = width;
        int cy = myView.getHeight() / 2;

        Animator anim;
        if (isShow)
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, (float) width);
        else
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, (float) width, 0);

        anim.setDuration((long) 220);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isShow) {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.INVISIBLE);
                }
            }
        });

        // make the view visible and start the animation
        if (isShow)
            myView.setVisibility(View.VISIBLE);

        // start the animation
        anim.start();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EventFeedDashboard.this, LandingPageActivity.class);
        startActivity(intent);
        finish();
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

    public class getpersonalprofile extends AsyncTask<String, Void, String> {

        public getpersonalprofile() {
        }

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
                    if (status.equals("true")) {
                        JSONObject message = jo.getJSONObject("message");
                        if (message.has("personalimage")) {
                            personalimage = message.getString("personalimage");
                            if (!personalimage.equals("")) {
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
