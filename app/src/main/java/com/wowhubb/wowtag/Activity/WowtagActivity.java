package com.wowhubb.wowtag.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.wowhubb.R;

/**
 * Created by Guna on 02-03-2018.
 */

public class WowtagActivity extends Activity{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wowtag_adapter);
    }
}
