package com.wowhubb.Activity;

/**
 * Created by Salman on 13-02-2018.
 */

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.ImageView;

import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;

public class ViewEventDiscussions extends Activity {
    ImageView closeiv;
    Typeface lato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vieweventdiscussion);
        closeiv = findViewById(R.id.closeiv);
        lato = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lato.ttf");
        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(ViewEventDiscussions.this, v1);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);


        closeiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}
