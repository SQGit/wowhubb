package vineture.wowhubb.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.R;

/**
 * Created by Salman on 21-02-2018.
 */

public class SettingsActivity extends Activity {
    TextView accountstv, backtv,notificationtv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(SettingsActivity.this, v1);

        backtv = findViewById(R.id.backtv);
        accountstv = findViewById(R.id.account_tv);


        accountstv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, AccountsActivity.class);
                // intent.putExtra("navdashboard", "true");
                startActivity(intent);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        backtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
