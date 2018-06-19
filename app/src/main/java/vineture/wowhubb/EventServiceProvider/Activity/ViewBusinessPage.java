package vineture.wowhubb.EventServiceProvider.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import vineture.wowhubb.EventServiceProvider.Fragment.PhotoGalleryFragment;
import vineture.wowhubb.EventServiceProvider.Fragment.VideoGalleryFragment;
import vineture.wowhubb.Fonts.FontsOverride;

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
        setContentView(vineture.wowhubb.R.layout.view_business_page);
        getSupportActionBar().hide();

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(ViewBusinessPage.this, v1);
        segoeui = Typeface.createFromAsset(getAssets(), "fonts/segoeui.ttf");

        wowtag_play=findViewById(vineture.wowhubb.R.id.wowtag_play);
        backiv=findViewById(vineture.wowhubb.R.id.backiv);
        lnr_enquiry=findViewById(vineture.wowhubb.R.id.lnr_enquiry);

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


        viewPager = (ViewPager) findViewById(vineture.wowhubb.R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(vineture.wowhubb.R.id.tabs);
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
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(vineture.wowhubb.R.layout.customtabgroup_title, null);
        tabOne.setText(" Photo Gallery  ");
        tabOne.setMinimumWidth(0);
        tabOne.setTextSize(15);
        tabOne.setTypeface(segoeui);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(vineture.wowhubb.R.layout.customtabgroup_title, null);
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
