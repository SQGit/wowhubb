package vineture.wowhubb.EventServiceProvider.Fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import vineture.wowhubb.EventServiceProvider.Adapter.CustomGridPhotoGallery;
import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.R;


public class PhotoGalleryFragment extends Fragment {
    String token;
    RecyclerView recyclerView;
    Typeface segoeui;

    GridView grid;
    String[] name = {
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
        View view = inflater.inflate(R.layout.photogallery, container, false);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        token = sharedPreferences.getString("token", "");
        FontsOverride.overrideFonts(getActivity(), view);


        CustomGridPhotoGallery adapter = new CustomGridPhotoGallery(getActivity(), name, imageId,price);
        grid=(GridView)view.findViewById(R.id.grid_view);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(getActivity(), "You Clicked at " +name[+ position], Toast.LENGTH_SHORT).show();
            }
        });




        return view;
    }

    /**
     * Parsing json reponse and passing the data to feed view list adapter
     */






}
