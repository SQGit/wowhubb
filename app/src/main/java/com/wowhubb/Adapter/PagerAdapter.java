package com.wowhubb.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wowhubb.Fragment.EventContactFragment;
import com.wowhubb.Fragment.EventSpeakerFragment;
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
        return 6;
    }

    @Override
    public Fragment getItem(int position)
    {
       // return PageFragment.newInstance(position + 1, position == getCount() - 1);


        switch(position) {

            case 0: return WowtagFragment.newInstance(position + 1, position == getCount() - 1);
            case 1: return EventVenueFragment.newInstance(position + 1, position == getCount() - 1);
            case 2: return ProgramScheduleFragment.newInstance(position + 1, position == getCount() - 1);
            case 3: return EventSpeakerFragment.newInstance(position + 1, position == getCount() - 1);
            case 4: return EventContactFragment.newInstance(position + 1, position == getCount() - 1);
            case 5: return GiftRegistryFragment.newInstance(position + 1, position == getCount() - 1);

            default: return PageFragment.newInstance(position + 1, position == getCount() - 1);
        }





    }


    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }
}