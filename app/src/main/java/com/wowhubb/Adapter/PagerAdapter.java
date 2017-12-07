package com.wowhubb.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.wowhubb.Fragment.EventContactFragment;
import com.wowhubb.Fragment.EventHighlightsFragment;
import com.wowhubb.Fragment.EventTypeFragment;
import com.wowhubb.Fragment.EventVenueFragment;
import com.wowhubb.Fragment.GiftRegistryFragment;
import com.wowhubb.Fragment.PageFragment;
import com.wowhubb.Fragment.ProgramScheduleFragment;
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
    public Fragment getItem(int position)
    {
        switch(position)
        {
            case 0:return EventTypeFragment.newInstance(position + 1, position == getCount() - 1);
            case 1: return WowtagFragment.newInstance(position + 1, position == getCount() - 1);
            case 2: return EventVenueFragment.newInstance(position + 1, position == getCount() - 1);
            case 3: return ProgramScheduleFragment.newInstance(position + 1, position == getCount() - 1);
            case 4: return EventHighlightsFragment.newInstance(position + 1, position == getCount() - 1);
            case 5: return EventContactFragment.newInstance(position + 1, position == getCount() - 1);
            case 6: return GiftRegistryFragment.newInstance(position + 1, position == getCount() - 1);
            default: return PageFragment.newInstance(position + 1, position == getCount() - 1);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

}