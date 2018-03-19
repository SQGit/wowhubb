package com.wowhubb.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;

/**
 * Created by Salman on 19-03-2018.
 */

public class LandingActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        final View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(LandingActivity.this, v1);
    }

}
