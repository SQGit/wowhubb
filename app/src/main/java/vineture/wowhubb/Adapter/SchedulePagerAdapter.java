package vineture.wowhubb.Adapter;

/**
 * Created by Salman on 13-02-2018.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import vineture.wowhubb.FeedsData.Programschedule;
import vineture.wowhubb.Fragment.DayPS1;

import java.util.ArrayList;

public class SchedulePagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public static ArrayList<ArrayList<Programschedule>> ps = new ArrayList<>();
    Bundle b;

    public SchedulePagerAdapter(FragmentManager fm, int NumOfTabs, ArrayList<ArrayList<Programschedule>> ps) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.ps = ps;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                b = new Bundle();
                b.putParcelableArrayList("program", ps.get(0));
                DayPS1 tab1 = new DayPS1();
                tab1.setArguments(b);
                return tab1;
            case 1:

                b = new Bundle();
                b.putParcelableArrayList("program", ps.get(1));
                DayPS1 tab2 = new DayPS1();
                tab2.setArguments(b);
                return tab2;
            case 2:
                b = new Bundle();
                b.putParcelableArrayList("program", ps.get(2));
                DayPS1 tab3 = new DayPS1();
                tab3.setArguments(b);
                return tab3;
            case 3:

                b = new Bundle();
                b.putParcelableArrayList("program", ps.get(3));
                DayPS1 tab4 = new DayPS1();
                tab4.setArguments(b);

                return tab4;
            case 4:
                b = new Bundle();
                b.putParcelableArrayList("program", ps.get(4));
                DayPS1 tab5 = new DayPS1();
               tab5.setArguments(b);
                return tab5;
            case 5:
                Bundle b = new Bundle();
                b.putParcelableArrayList("program", ps.get(5));
                DayPS1 tab6 = new DayPS1();
               tab6.setArguments(b);
                return tab6;
            case 6:
                b = new Bundle();
                b.putParcelableArrayList("program", ps.get(6));
                DayPS1 tab7 = new DayPS1();
              tab7.setArguments(b);

                return tab7;

            default:
                b = new Bundle();
                b.putParcelableArrayList("program", ps.get(0));
                DayPS1 tab11 = new DayPS1();
                tab11.setArguments(b);
                return tab11;
        }
    }

    @Override
    public int getCount() {
        return ps.size();
    }


}