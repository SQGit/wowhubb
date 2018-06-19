package vineture.wowhubb.Activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.Fragment.BusinessEventsFragment;

import java.util.ArrayList;


/**
 * Created by Ramya on 24-07-2017.
 */

public class InterestActivity extends Activity {
    TextView head_tv, skip_tv;
    Typeface latoheading, lato;
    ImageView backiv;
    String navdashboard;
    Bundle extras;
    public static ArrayList<String> list;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(vineture.wowhubb.R.layout.fragment_interest);
         extras = getIntent().getExtras();
        //-----------------------------------------SNACKBAR----------------------------------------//
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(InterestActivity.this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String firstname = sharedPreferences.getString("firsttime", "");
        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(InterestActivity.this, v1);
        latoheading = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/latoheading.ttf");
        lato = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lato.ttf");

        head_tv = (TextView) findViewById(vineture.wowhubb.R.id.head_tv);
        backiv=findViewById(vineture.wowhubb.R.id.backiv);
        skip_tv = findViewById(vineture.wowhubb.R.id.skiptv);
        head_tv.setTypeface(latoheading);
        list = new ArrayList<String>();

        if(firstname.equals("true"))
        {
            list.clear();
        }

        backiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (extras != null) {
                    navdashboard = extras.getString("navdashboard");
                    if (navdashboard.equals("true")) {
                        finish();
                    }
                }
                else
                {
                    startActivity(new Intent(InterestActivity.this, LoginActivity.class));
                    overridePendingTransition(vineture.wowhubb.R.anim.slide_in_up, vineture.wowhubb.R.anim.slide_out_up);
                }

            }
        });

        skip_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InterestActivity.this, ProfileActivity.class));
                overridePendingTransition(vineture.wowhubb.R.anim.left_to_right, vineture.wowhubb.R.anim.right_to_left);
                finish();
            }
        });

        //-----------------------------BusinessEventsFragment--------------------------------------//

        BusinessEventsFragment bef = new BusinessEventsFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(vineture.wowhubb.R.id.fragment_container, bef);
        ft.commit();
        overridePendingTransition(vineture.wowhubb.R.anim.left_to_right, vineture.wowhubb.R.anim.right_to_left);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
