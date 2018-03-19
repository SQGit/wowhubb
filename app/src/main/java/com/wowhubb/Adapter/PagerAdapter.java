package com.wowhubb.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wowhubb.Fragment.DummyFragment;

public class PagerAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private int imagesList[];
    private int screenWidth;
    private int screenHeight;
    private int textList[];

    public PagerAdapter(FragmentManager fm, int screenWidth,
                        int screenHeight) {
        super(fm);
        this.screenWidth = screenWidth / 2;
        this.screenHeight = screenHeight / 2;

    }

    /*public PagerAdapter(FragmentManager fm) {
        super(fm);
    }*/

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DummyFragment();
            case 1:
                return new DummyFragment();
            case 2:
                return new DummyFragment();
            case 3:
                return new DummyFragment();
            case 4:
                return new DummyFragment();
            case 5:
                return new DummyFragment();
            // case 6: return GiftRegistryFragment.newInstance(position + 1, position == getCount() - 1);
            default:
                return new DummyFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

}