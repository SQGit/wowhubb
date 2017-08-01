package com.wowhubb.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wowhubb.Fragment.PageFragment;
import com.wowhubb.Fragment.WowtagFragment;

public class PagerAdapter extends FragmentPagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public int getCount() {
        return 7;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position + 1, position == getCount() - 1);
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }
}