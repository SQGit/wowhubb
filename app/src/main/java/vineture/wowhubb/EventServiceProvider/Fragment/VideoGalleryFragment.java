package vineture.wowhubb.EventServiceProvider.Fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import vineture.wowhubb.EventServiceProvider.Adapter.CustomGridVideoGallery;
import vineture.wowhubb.Fonts.FontsOverride;


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
            vineture.wowhubb.R.drawable.cake_1,
            vineture.wowhubb.R.drawable.cake_2,
            vineture.wowhubb.R.drawable.cake_3,
            vineture.wowhubb.R.drawable.cake_4,
            vineture.wowhubb.R.drawable.cake_1,
            vineture.wowhubb.R.drawable.cake_2,
            vineture.wowhubb.R.drawable.cake_3,
            vineture.wowhubb.R.drawable.cake_4,
    };


    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(vineture.wowhubb.R.layout.video_gallery, container, false);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        token = sharedPreferences.getString("token", "");
        FontsOverride.overrideFonts(getActivity(), view);


        CustomGridVideoGallery adapter = new  CustomGridVideoGallery(getActivity(), web,imageId, price);
        grid=(GridView)view.findViewById(vineture.wowhubb.R.id.grid_view);
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
