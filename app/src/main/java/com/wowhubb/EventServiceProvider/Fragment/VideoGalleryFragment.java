package com.wowhubb.EventServiceProvider.Fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.wowhubb.EventServiceProvider.Adapter.CustomGridVideoGallery;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;


public class VideoGalleryFragment extends Fragment {
    String token;
    GridView grid;

    String[] web = {
            "Vanilla Wedding Cake",
            "Strawberry Cake",
            "Blackforest Cake",
            "Black Cake",
            "Vanilla Wedding Cake",
            "Strawberry Cake",
            "Blackforest Cake",
            "Black Cake",
    } ;


    String[] price = {
            "$ 50",
            "$ 30",
            "$ 75",
            "$ 27",
            "$ 50",
            "$ 30",
            "$ 75",
            "$ 27",
    } ;

    int[] imageId = {
            R.drawable.cake_1,
            R.drawable.cake_2,
            R.drawable.cake_3,
            R.drawable.cake_4,
            R.drawable.cake_1,
            R.drawable.cake_2,
            R.drawable.cake_3,
            R.drawable.cake_4,
    };


    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_gallery, container, false);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        token = sharedPreferences.getString("token", "");
        FontsOverride.overrideFonts(getActivity(), view);


        CustomGridVideoGallery adapter = new  CustomGridVideoGallery(getActivity(), web,imageId, price);
        grid=(GridView)view.findViewById(R.id.grid_view);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(getActivity(), "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}
