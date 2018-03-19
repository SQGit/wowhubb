package com.wowhubb.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.GetFilePathFromDevice;
import com.wowhubb.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

/**
 * Created by Salman on 21-02-2018.
 */

public class ShareThoughtsActivity extends Activity {
    private static final int INTENT_REQUEST_GET_COVERIMAGES = 11;
    private static final int INTENT_REQUEST_GET_VIDEO1 = 12;
    TextView backtv, profilenametv, userdesignationtv, publishtv;
    ImageView photoview, piciv, videoiv, linkiv, photocloseiv, videocloseiv, profile_iv;
    VideoView videoview;
    String selectedCoverFilePath, selectedVideoFilePath1, token, personalimage, str_name, str_lname, userdesignation;
    FrameLayout videoframe;
    String thoughtsvideo, thoughtsimage, thoughtstext, urltext;
    EditText thoughts_et, url_et;
    Dialog loader_dialog;
    TextInputLayout url_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sharethoughts);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ShareThoughtsActivity.this);
        token = sharedPreferences.getString("token", "");
        personalimage = sharedPreferences.getString("profilepath", "");
        str_name = sharedPreferences.getString("str_name", "");
        str_lname = sharedPreferences.getString("str_lname", "");
        userdesignation = sharedPreferences.getString("userdesignation", "");
        Typeface lato = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lato.ttf");
        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(ShareThoughtsActivity.this, v1);

        Log.e("tag", "code3------------->" + userdesignation);
        loader_dialog = new Dialog(ShareThoughtsActivity.this);
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(R.layout.test_loader);
        backtv = findViewById(R.id.backtv);
        photoview = findViewById(R.id.photo_view);
        piciv = findViewById(R.id.pic_iv);
        videoiv = findViewById(R.id.video_iv);
        videoview = findViewById(R.id.video_view);
        profilenametv = findViewById(R.id.hoster_name);
        photocloseiv = findViewById(R.id.close_iv);
        videocloseiv = findViewById(R.id.videoclose_iv);
        videoframe = findViewById(R.id.videoframe);
        profile_iv = findViewById(R.id.imageview_profile);
        userdesignationtv = findViewById(R.id.hoster_designation);
        publishtv = findViewById(R.id.publishtv);
        thoughts_et = findViewById(R.id.thoughts_et);
        url_et = findViewById(R.id.link_et);
        url_link = findViewById(R.id.til_url);
        url_link.setTypeface(lato);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            selectedCoverFilePath = extras.getString("selectedCoverFilePath");
        }
        if (personalimage != null) {
            Glide.with(this).load("http://104.197.80.225:3010/wow/media/personal/" + personalimage).into(profile_iv);
        } else {
            profile_iv.setImageResource(R.drawable.profile_img);
        }
        if (str_name != null) {
            profilenametv.setText(str_name);
        }

        if (str_lname != null) {
            profilenametv.append(" " + str_lname);
        }

        if (userdesignation != null) {
            userdesignationtv.setText(userdesignation);
        }
        photocloseiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoview.setVisibility(View.GONE);
            }
        });
        videocloseiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoview.setVisibility(View.GONE);
            }
        });

        publishtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thoughtstext = thoughts_et.getText().toString().trim();
                urltext = url_et.getText().toString().trim();


                if (!thoughts_et.getText().toString().trim().equalsIgnoreCase("") || !url_et.getText().toString().trim().equalsIgnoreCase("")|| selectedCoverFilePath != null || selectedVideoFilePath1 != null) {
                    new publishthoughts().execute();
                } else {
                    Toast.makeText(ShareThoughtsActivity.this, "Please publish some data", Toast.LENGTH_LONG).show();
                }

            }
        });

        videoiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    Intent intent = new Intent();
                    intent.setType("video/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Video"), INTENT_REQUEST_GET_VIDEO1);
                }
            }
        });


        piciv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("image/*");
                    startActivityForResult(pickIntent, INTENT_REQUEST_GET_COVERIMAGES);
                }

            }
        });
        if (selectedCoverFilePath != null && !selectedCoverFilePath.equals("null")) {
            try {
                photoview.setVisibility(View.VISIBLE);
                photoview.setImageURI(Uri.parse(selectedCoverFilePath));
                photoview.requestFocus();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }

        backtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(ShareThoughtsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(ShareThoughtsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(ShareThoughtsActivity.this, Manifest.permission.CAMERA);
        if ((result == PackageManager.PERMISSION_GRANTED) && (result1 == PackageManager.PERMISSION_GRANTED) && (result2 == PackageManager.PERMISSION_GRANTED)) {
            return true;
        } else {
            ActivityCompat.requestPermissions(ShareThoughtsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
            return false;

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            Log.e("tag", "code------------->" + resultCode);
            if (requestCode == INTENT_REQUEST_GET_COVERIMAGES) {
                Uri selectedMediaUri = data.getData();
                selectedCoverFilePath = GetFilePathFromDevice.getPath(ShareThoughtsActivity.this, selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(ShareThoughtsActivity.this.getContentResolver(), selectedMediaUri);
                    // Log.d(TAG, String.valueOf(bitmap));
                    photoview.setVisibility(View.VISIBLE);
                    photoview.setImageBitmap(bitmap);
                    photoview.requestFocus();
                    Log.e("tag", "code------------->" + selectedCoverFilePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (requestCode == INTENT_REQUEST_GET_VIDEO1) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Uri selectedMediaUri = data.getData();
                    selectedVideoFilePath1 = GetFilePathFromDevice.getPath(ShareThoughtsActivity.this, selectedMediaUri);
                    Log.e("tag", "qqqqqqqqqqqqqqqqqqnougggggggg------------" + selectedVideoFilePath1);
                    videoview.setVisibility(View.VISIBLE);
                    videoview.setVideoURI(Uri.parse(selectedVideoFilePath1));
                    videoframe.requestFocus();

                } else {
                    Uri selectedMediaUri = data.getData();
                    selectedVideoFilePath1 = GetFilePathFromDevice.getPath(ShareThoughtsActivity.this, selectedMediaUri);
                    Log.e("tag", "qqqqqqqqqqqqqqqqqq------------" + selectedVideoFilePath1);
                    videoview.setVisibility(View.VISIBLE);
                    videoview.setVideoURI(Uri.parse(selectedVideoFilePath1));
                    videoframe.requestFocus();
                }

            }

        }

    }

    public class publishthoughts extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader_dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                String responseString = null;
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://104.197.80.225:3010/wow/event/thoughts");
                httppost.setHeader("token", token);
                httppost.setHeader("eventtype", "thought");
                Log.e("tag", "texttttttttttttttttttttttt:" + thoughtstext + token);


                if (thoughtstext != null && !thoughtstext.equals("null")) {
                    Log.e("tag", "texttttttttttttttttttttttt:" + thoughtstext);
                    httppost.setHeader("thoughtstext", thoughts_et.getText().toString().trim());
                }
                if (urltext != null && !urltext.equals("null")) {
                    Log.e("tag", "texttttttttttttttttttttttt:" + urltext);
                    httppost.setHeader("urllink", url_et.getText().toString().trim());
                }
                HttpResponse response = null;
                HttpEntity r_entity = null;
                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                if (selectedVideoFilePath1 != null) {
                    Log.e("tag", "videooo1111:");
                    entity.addPart("thoughtsvideo", new FileBody(new File(selectedVideoFilePath1), "video/mp4"));
                }

                if (selectedCoverFilePath != null) {
                    Log.e("tag", "image:");
                    entity.addPart("thoughtsimage", new FileBody(new File(selectedCoverFilePath), "image/jpeg"));
                }
                httppost.setEntity(entity);

                try {
                    try {
                        response = httpclient.execute(httppost);
                    } catch (Exception e) {
                        Log.e("tag", "ds:" + e.toString());
                    }

                    try {
                        r_entity = response.getEntity();
                    } catch (Exception e) {
                        Log.e("tag", "dsa:" + e.toString());
                    }

                    int statusCode = response.getStatusLine().getStatusCode();
                    Log.e("tag", response.getStatusLine().toString());
                    if (statusCode == 200) {
                        responseString = EntityUtils.toString(r_entity);
                        Log.e("tag", "rssss" + responseString);
                        // return success;

                    } else {
                        responseString = "Error occurred! Http Status Code: "
                                + statusCode;
                        Log.e("tag3", responseString);
                    }
                } catch (ClientProtocolException e) {
                    responseString = e.toString();
                    Log.e("tag44", responseString);
                } catch (IOException e) {
                    responseString = e.toString();
                    Log.e("tag45", responseString);
                }
                return responseString;
            } catch (Exception e) {
                Log.e("tag_InputStream0", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e("tag", "wwwwwwwwwwwwwww----------------->>>>>>>" + s);
            if (s != null) {

                if (s.length() > 0) {
                    try {
                        JSONObject jo = new JSONObject(s.toString());
                        String success = jo.getString("success");
                        if (success.equals("true")) {
                            loader_dialog.dismiss();
                            Intent intent = new Intent(ShareThoughtsActivity.this, EventFeedDashboard.class);
                            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();


                        } else {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

    }


}
