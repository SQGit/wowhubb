package com.wowhubb.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;

/**
 * Created by Salman on 21-02-2018.
 */

public class AccountsPrivacyActivity extends Activity {
    TextView backtv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accounts_privacy);

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(AccountsPrivacyActivity.this, v1);
        backtv = findViewById(R.id.backtv);

        backtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
