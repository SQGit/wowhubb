package vineture.wowhubb.Activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import vineture.wowhubb.FeedsData.Programschedule;
import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.Fragment.ViewEventInfoFragment;
import vineture.wowhubb.Fragment.ViewHighlightFragment;

/**
 * Created by Guna on 09-02-2018.
 */

public class ViewMoreDetailspage extends AppCompatActivity {
    public static String eventdatedetails, eventtimedetails, fulladdress, profilepicture, timestamp_str, description, eventname, eventdate, status, coverimage, str_position;
    public static String eventtype, wowtagvideo, highligh1, highligh2, gifturl, donationurl, eventvenueaddress, eventspeakername1, eventguesttype1, eventspeakeractivities1, eventspeakerlink1;
    public static int eventdayscount;
    public static ArrayList<Programschedule> programschedules = new ArrayList<>();
    Typeface segoeui;
    ImageView wowtag_play, closeiv, feediv;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(vineture.wowhubb.R.layout.event_individual_page);
        getSupportActionBar().hide();

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(ViewMoreDetailspage.this, v1);
        segoeui = Typeface.createFromAsset(getAssets(), "fonts/segoeui.ttf");
        Bundle extras = getIntent().getExtras();
        feediv = findViewById(vineture.wowhubb.R.id.feedImage1);
        closeiv = findViewById(vineture.wowhubb.R.id.close_iv);
        try {
            programschedules = getIntent().getParcelableArrayListExtra("program");
            Log.e("tag", "programshedule-------------->>>" + programschedules.size());

            Log.e("tag", "programshedule111-------------->>>" + programschedules.get(0).getItyendtime());
        } catch (NullPointerException e) {

        } catch (RuntimeException e) {

        }
        if (extras != null) {
            eventvenueaddress = extras.getString("eventvenueaddress");
            description = extras.getString("description");
            status = extras.getString("status");
            eventname = extras.getString("eventname");
            str_position = extras.getString("str_position");
            eventdate = extras.getString("eventstartdate");
            coverimage = extras.getString("coverpage");
            highligh1 = extras.getString("highlight1");
            highligh2 = extras.getString("highlight2");
            wowtagvideo = extras.getString("wowtagvideo");
            gifturl = extras.getString("gifturl");
            donationurl = extras.getString("donationurl");
            eventdayscount = extras.getInt("eventdayscount");

            eventtype = extras.getString("eventtype");

            eventdatedetails = extras.getString("eventdate");
            eventtimedetails = extras.getString("eventtime");

            eventspeakername1 = extras.getString("eventspeakername1");
            eventguesttype1 = extras.getString("eventguesttype1");
            eventspeakeractivities1 = extras.getString("eventspeakeractivities1");
            eventspeakerlink1 = extras.getString("eventspeakerlink1");
            Log.e("tag", "wowtagvideo-------" + wowtagvideo);
            Log.e("tag", "Imagge22222222-------" + gifturl + highligh1);


        }

        if (coverimage != null && !coverimage.equals("null")) {
            Log.e("tag", "coverrrr------->");
            Glide.with(ViewMoreDetailspage.this).load(coverimage)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .crossFade()
                    .into(feediv);
        }


        closeiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        viewPager = (ViewPager) findViewById(vineture.wowhubb.R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(vineture.wowhubb.R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();


    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        if (eventtype.equals("business_event")) {
            adapter.addFrag(new ViewEventInfoFragment(), "Event Info");
        } else {


            adapter.addFrag(new ViewEventInfoFragment(), "Event Info");
            adapter.addFrag(new ViewProgramSchedule(), "Event Schedule");
            adapter.addFrag(new ViewHighlightFragment(), "Event Highlights");
        }
        //adapter.addFrag(new EventDiscussionFragment(), "Event Discussions");

        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        if (eventtype.equals("business_event")) {
            View v2 = tabLayout.getRootView();
            FontsOverride.overrideFonts(ViewMoreDetailspage.this, v2);
            TextView tabOne = (TextView) LayoutInflater.from(this).inflate(vineture.wowhubb.R.layout.customtab_title, null);
            tabOne.setText("Event Info");
            tabOne.setMinimumWidth(0);
            tabOne.setTextSize(15);
            tabOne.setTypeface(segoeui);
            //tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.eventfeed_tab, 0, 0);
            tabLayout.getTabAt(0).setCustomView(tabOne);
        } else {
            View v2 = tabLayout.getRootView();
            FontsOverride.overrideFonts(ViewMoreDetailspage.this, v2);
            TextView tabOne = (TextView) LayoutInflater.from(this).inflate(vineture.wowhubb.R.layout.customtab_title, null);
            tabOne.setText("Event Info");
            tabOne.setMinimumWidth(0);
            tabOne.setTextSize(15);
            tabOne.setTypeface(segoeui);
            //tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.eventfeed_tab, 0, 0);
            tabLayout.getTabAt(0).setCustomView(tabOne);

            TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(vineture.wowhubb.R.layout.customtab_title, null);
            tabTwo.setText(" Event Schedule");
            tabTwo.setMinimumWidth(0);
            tabTwo.setTypeface(segoeui);
            tabTwo.setTextSize(15);
            tabTwo.setGravity(View.TEXT_ALIGNMENT_CENTER);
            //tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.network_tab, 0, 0);
            tabLayout.getTabAt(1).setCustomView(tabTwo);

            TextView tabThree = (TextView) LayoutInflater.from(this).inflate(vineture.wowhubb.R.layout.customtab_title, null);
            tabThree.setText(" Event Highlights");
            tabThree.setMinimumWidth(0);
            tabThree.setTypeface(segoeui);
            tabThree.setTextSize(15);
            tabThree.setGravity(View.TEXT_ALIGNMENT_CENTER);

            //tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.wowtag_tab, 0, 0);
            tabLayout.getTabAt(2).setCustomView(tabThree);

            TextView tabfour = (TextView) LayoutInflater.from(this).inflate(vineture.wowhubb.R.layout.customtab_title, null);
            tabfour.setText(" Event Discussions");
            tabfour.setMinimumWidth(0);
            tabfour.setSingleLine();
            tabfour.setTypeface(segoeui);
            tabfour.setTextSize(15);

            tabfour.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
            //tabfour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.eventhubb_tab, 0, 0);
            //   tabLayout.getTabAt(2).setCustomView(tabfour);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

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


}
