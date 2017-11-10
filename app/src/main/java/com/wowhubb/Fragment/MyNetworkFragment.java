package com.wowhubb.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.wowhubb.Activity.EventFeedDashboard;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;


public class MyNetworkFragment extends Fragment {

    TabHost tabHost;

    public MyNetworkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mynetwork,
                container, false);
        FontsOverride.overrideFonts(getActivity(), view);
       // EventFeedDashboard.titletv.setText("Your Events will Show here");
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                view.findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                selectedFragment = MyNetworksTabFragment.newInstance();
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.frame_layout, selectedFragment);
                                transaction.commit();
                                break;
                            case R.id.action_item2:
                               /* selectedFragment = MyNetworkAddContact.newInstance();
                                FragmentTransaction transaction1 = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction1.replace(R.id.frame_layout, selectedFragment);
                                transaction1.commit();
                                break;*/
                                break;
                            case R.id.action_item3:
                                selectedFragment = MyNetworkAddContact.newInstance();
                                FragmentTransaction transaction2 = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction2.replace(R.id.frame_layout, selectedFragment);
                                transaction2.commit();
                                break;
                        }

                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, MyNetworksTabFragment.newInstance());
        transaction.commit();

        //Used to select an item programmatically
        //bottomNavigationView.getMenu().getItem(2).setChecked(true);

        return view;
    }

}
