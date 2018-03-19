package com.wowhubb.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wowhubb.R;

/**
 * Created by Salman on 26-01-2018.
 */

public class DummyFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dummyfragment, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


    }
}
