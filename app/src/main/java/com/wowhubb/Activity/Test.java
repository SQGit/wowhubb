package com.wowhubb.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.wowhubb.R;
import com.wowhubb.data.FeedItem;

import java.util.List;

/**
 * Created by Ramya on 20-09-2017.
 */

public class Test extends Activity {
   // private FeedListAdapter listAdapter;
    private List<FeedItem> feedItems;
    private String URL_FEED = "http://104.197.80.225:3010/wow/event/feed";
    String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);


      startTest();

    }


    private void startTest() {
        // specify our test image location
        Uri url = Uri.parse("android.resource://"
                + getPackageName() + "/" + R.drawable.ic_action_done);

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
