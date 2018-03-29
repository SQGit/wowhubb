package com.wowhubb.EventServiceProvider.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import com.wowhubb.Adapter.CustomAdapter;
import com.wowhubb.EventServiceProvider.Fragment.PhotoGalleryFragment;
import com.wowhubb.EventServiceProvider.Fragment.VideoGalleryFragment;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guna on 09-02-2018.
 */

public class ViewBusinessPage extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    Typeface segoeui;
    LinearLayout backiv,lnr_enquiry;
    ImageView wowtag_play;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_business_page);
        getSupportActionBar().hide();

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(ViewBusinessPage.this, v1);
        segoeui = Typeface.createFromAsset(getAssets(), "fonts/segoeui.ttf");

        wowtag_play=findViewById(R.id.wowtag_play);
        backiv=findViewById(R.id.backiv);
        lnr_enquiry=findViewById(R.id.lnr_enquiry);

        backiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        lnr_enquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent enquiry=new Intent(getApplicationContext(),EnquiryWithUs.class);
                startActivity(enquiry);
            }
        });


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new PhotoGalleryFragment(), "Photo Gallery");
        adapter.addFrag(new VideoGalleryFragment(), "Video Gallery");
        viewPager.setAdapter(adapter);
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




  private void setupTabIcons() {
        View v2 = tabLayout.getRootView();

        FontsOverride.overrideFonts(ViewBusinessPage.this, v2);
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.customtabgroup_title, null);
        tabOne.setText(" Photo Gallery  ");
        tabOne.setMinimumWidth(0);
        tabOne.setTextSize(15);
        tabOne.setTypeface(segoeui);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.customtabgroup_title, null);
        tabTwo.setText("   Video Gallery ");
        tabTwo.setMinimumWidth(0);
        tabTwo.setTypeface(segoeui);
        tabTwo.setTextSize(15);
        tabTwo.setGravity(View.TEXT_ALIGNMENT_CENTER);
        //tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.network_tab, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
