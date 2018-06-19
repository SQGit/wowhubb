package vineture.wowhubb.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.R;
import vineture.wowhubb.data.FeedItem;

import java.util.List;

/**
 * Created by Ramya on 20-09-2017.
 */

public class Test extends Activity {
   // private FeedListAdapter listAdapter;
    private List<FeedItem> feedItems;
    private String URL_FEED = "http://104.197.80.225:3010/wow/event/feed";
    String token;
    TextView addcity_tv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalprofile);

        addcity_tv=findViewById(R.id.addcity_tv);
        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(Test.this, v1);

        final Dialog d = new Dialog(Test.this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
        d.setContentView(R.layout.dialog_profileinfo);
        d.setCanceledOnTouchOutside(false);
        ImageView close=d.findViewById(R.id.closeiv);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });

        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Window view1 = ((Dialog) d).getWindow();
                view1.setBackgroundDrawableResource(R.drawable.border_graybg);
            }
        });

        View v2 = d.getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(Test.this, v2);

        addcity_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.show();
            }
        });
    }


    private void startTest() {
        // specify our test image location
        Uri url = Uri.parse("android.resource://"
                + getPackageName() + "/" + vineture.wowhubb.R.drawable.ic_action_done);

        // set up an intent to share the image
        Intent share_intent = new Intent();
        share_intent.setAction(Intent.ACTION_SEND);
        share_intent.setType("text/plain");
    //    share_intent.putExtra(Intent.EXTRA_STREAM,
             //   Uri.fromFile(new File(url.toString())));
        share_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       /* share_intent.putExtra(Intent.EXTRA_SUBJECT,
                "share an image");*/
        share_intent.putExtra(Intent.EXTRA_TEXT,
                "wowhubb ");

        // start the intent
        try {
            startActivity(Intent.createChooser(share_intent,
                    "ShareThroughChooser Test"));
        } catch (android.content.ActivityNotFoundException ex) {
            (new AlertDialog.Builder(Test.this)
                    .setMessage("Share failed")
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                }
                            }).create()).show();
        }
    }




}
