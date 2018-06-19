package vineture.wowhubb.Fragment;

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

import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.R;


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

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                view.findViewById(R.id.navigation);

        Fragment selectedFragment = MyNetworksTabFragment.newInstance();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, selectedFragment);
        transaction.commit();


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
                                selectedFragment = MyNetworkContact.newInstance();
                                FragmentTransaction transaction1 = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction1.replace(R.id.frame_layout, selectedFragment);
                                transaction1.commit();
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


        return view;
    }

}
