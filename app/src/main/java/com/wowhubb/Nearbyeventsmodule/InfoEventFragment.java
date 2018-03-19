package com.wowhubb.Nearbyeventsmodule;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;


public class InfoEventFragment extends Fragment {
    String token;
    RecyclerView recyclerView;
    ImageView img_share;
    TextView txt_contactorganizer,txt_interested,txt_attending;

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eventsinfo, container, false);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        token = sharedPreferences.getString("token", "");
        FontsOverride.overrideFonts(getActivity(), view);
        img_share=view.findViewById(R.id.img_share);
        recyclerView = (RecyclerView) view.findViewById(R.id.listview);
        txt_attending=view.findViewById(R.id.txt_attending);
        txt_interested=view.findViewById(R.id.txt_interested);
        txt_contactorganizer=view.findViewById(R.id.txt_contactorganizer);


        txt_contactorganizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Under Development",Toast.LENGTH_LONG).show();
            }
        });


        txt_interested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Under Development",Toast.LENGTH_LONG).show();
            }
        });



        txt_attending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Under Development",Toast.LENGTH_LONG).show();
            }
        });



        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, R.drawable.event1);
                shareIntent.setType("image/jpeg");
                startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.app_name)));
            }
        });




        return view;
    }

    /**
     * Parsing json reponse and passing the data to feed view list adapter
     */






}
