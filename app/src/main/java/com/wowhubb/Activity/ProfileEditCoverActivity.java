package com.wowhubb.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;

/**
 * Created by Salman on 05-10-2017.
 */

public class ProfileEditCoverActivity extends Activity {
    ImageView backiv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_editcoverpage);

        backiv = findViewById(R.id.backiv);
        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(ProfileEditCoverActivity.this, v1);
        backiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileEditCoverActivity.this.finish();
            }
        });

    }
}
