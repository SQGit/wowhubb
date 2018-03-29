package com.wowhubb.wowtag.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.wowhubb.Activity.LandingPageActivity;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.GetFilePathFromDevice;
import com.wowhubb.R;
import com.wowhubb.Utils.Config;
import com.wowhubb.Utils.HttpUtils;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.wowhubb.Activity.ProfileActivity.getFilePathFromURI;

/**
 * Created by Guna on 06-03-2018.
 */

public class PaticularWowtagActivity extends Activity {
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int INTENT_REQUEST_GET_VIDEO1 = 12;
    private static final int INTENT_REQUEST_GET_VIDEO11 = 13;
    private static final int INTENT_REQUEST_GET_COVERIMAGES = 11;
    public static String selectedVideoFilePath1, selectedCoverFilePath;
    String get_wowid, get_wowname, wowtag_video;
    SharedPreferences.Editor editor;
    String token, str_msg;
    TextView txt_provider_head, wowtagid, wowtag_name, wowtag_venue, wowtag_address, wowtag_eventdatetime, wowtag_time, wowtag_description, wowtag_description_cont, wowtag_runtime, wow_shares, wow_attending, wow_views, wowtag_edit;
    TextView txt_fromgallery, txt_takevideo;
    ImageView no_video, del_img;
    VideoView wow_videoview;
    Button wowtag_register;
    LinearLayout del_video, img_back;
    Animation animShake;
    Boolean flag = false;
    Dialog dialog, loader_dialog;
    private Uri fileUri, videoUri;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.particular_wowtag_activity);

        //set FONT for whole class:
        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(getApplicationContext(), v1);

        //find Add Cast:
        txt_provider_head = findViewById(R.id.txt_provider_head);
        wowtagid = findViewById(R.id.wowtagid);
        wowtag_name = findViewById(R.id.wowtag_name);
        wowtag_venue = findViewById(R.id.wowtag_venue);
        wowtag_address = findViewById(R.id.wowtag_address);
        wowtag_eventdatetime = findViewById(R.id.wowtag_eventdatetime);
        wowtag_time = findViewById(R.id.wowtag_time);
        wowtag_description = findViewById(R.id.wowtag_description);
        wowtag_description_cont = findViewById(R.id.wowtag_description_cont);
        wowtag_runtime = findViewById(R.id.wowtag_runtime);
        wow_shares = findViewById(R.id.wow_shares);
        wow_attending = findViewById(R.id.wow_attending);
        wow_views = findViewById(R.id.wow_views);
        wow_videoview = findViewById(R.id.video_view);
        img_back = findViewById(R.id.img_back);
        wowtag_register = findViewById(R.id.wowtag_register);
        del_video = (LinearLayout) findViewById(R.id.del_video);
        no_video = findViewById(R.id.no_video);
        del_img = findViewById(R.id.del_img);
        wowtag_edit = findViewById(R.id.wowtag_edit);

        //token get from Login Activity:
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");


        //This intent get from WowFragment Class:
        Intent intent = getIntent();
        get_wowid = intent.getStringExtra("wowtag_id");
        get_wowname = intent.getStringExtra("wowtag_name");
        txt_provider_head.setText("Wowtag");
        no_video.setVisibility(View.GONE);
        wow_videoview.setVisibility(View.VISIBLE);
        wowtagid.setText(get_wowname);

        //set default Loader:
        loader_dialog = new Dialog(PaticularWowtagActivity.this);
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(R.layout.test_loader);

        //call Particular WOWTAG List:
        new callParticularWowtag().execute();


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(getApplicationContext(), LandingPageActivity.class);
                startActivity(back);
                finish();
            }
        });

        wowtag_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedVideoFilePath1 != null) {
                    new editWowtagVideoAsync().execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Please Select or Capture New Video", Toast.LENGTH_LONG).show();
                }
            }
        });

        del_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = false;
                animShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                del_img.startAnimation(animShake);
                new callParticularWowtagVideoDelete().execute();
            }
        });

        wowtag_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = true;
                wow_videoview.setVisibility(View.GONE);
                no_video.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext()).load(R.drawable.capture_video)
                        .into(no_video);
            }
        });


        no_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == true) {
                    dialog = new Dialog(PaticularWowtagActivity.this);
                    dialog.setContentView(R.layout.dialog_takevideo);
                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialogInterface) {
                            Window view1 = ((Dialog) dialog).getWindow();
                            view1.setBackgroundDrawableResource(R.drawable.border_bg);
                        }
                    });
                    txt_fromgallery = (TextView) dialog.findViewById(R.id.txt_fromgallery);
                    txt_takevideo = (TextView) dialog.findViewById(R.id.txt_takevideo);
                    ImageView image = (ImageView) dialog.findViewById(R.id.imageDialog);
                    final LinearLayout lnr_video = (LinearLayout) dialog.findViewById(R.id.lnr_video);
                    final LinearLayout lnr_gallery = (LinearLayout) dialog.findViewById(R.id.lnr_gallery);

                    txt_fromgallery.setTextColor(Color.parseColor("#3c3c3c"));
                    txt_takevideo.setTextColor(Color.parseColor("#3c3c3c"));

                    txt_fromgallery.setText("From Gallery");
                    txt_takevideo.setText("Take Video   ");

                    lnr_gallery.setBackgroundResource(R.drawable.btn_bg_white);
                    lnr_video.setBackgroundResource(R.drawable.btn_bg_white);

                    lnr_gallery.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            lnr_gallery.setBackgroundResource(R.drawable.btnbg);
                            txt_fromgallery.setTextColor(Color.parseColor("#FFFFFF"));

                            lnr_video.setBackgroundResource(R.drawable.btn_bg_white);
                            txt_takevideo.setTextColor(Color.parseColor("#3c3c3c"));

                            if (checkPermission()) {
                                Intent intent = new Intent();
                                intent.setType("video/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select Video"), INTENT_REQUEST_GET_VIDEO1);
                                Toast.makeText(getApplicationContext(), "Please choose less than 2 mb video", Toast.LENGTH_LONG).show();
                            }
                        }
                    });


                    lnr_video.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                if (checkPermission()) {
                                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                                    videoUri = getOutputMediaFileUri2(MEDIA_TYPE_VIDEO);
                                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri); // set the image file
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    startActivityForResult(intent, INTENT_REQUEST_GET_VIDEO11);
                                    dialog.dismiss();
                                }

                            } else {
                                lnr_video.setBackgroundResource(R.drawable.btnbg);
                                txt_takevideo.setTextColor(Color.parseColor("#FFFFFF"));
                                lnr_gallery.setBackgroundResource(R.drawable.btn_bg_white);
                                txt_fromgallery.setTextColor(Color.parseColor("#3c3c3c"));

                                if (checkPermission()) {
                                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                                    fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
                                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    // start the video capture Intent
                                    startActivityForResult(intent, INTENT_REQUEST_GET_VIDEO1);
                                    dialog.dismiss();
                                }
                            }

                        }
                    });

                    dialog.show();
                } else if (flag == false) {
                    Log.e("tag", "Dont Capture");
                }
            }
        });


        wow_videoview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wow_videoview.start();
            }
        });
    }

    private Uri getOutputMediaFileUri(int type) {
        Uri selectedMediaUri = Uri.fromFile(getOutputMediaFile(type));
        selectedVideoFilePath1 = GetFilePathFromDevice.getPath(getApplicationContext(), selectedMediaUri);
        no_video.setVisibility(View.VISIBLE);
        no_video.setImageBitmap(ThumbnailUtils.createVideoThumbnail(selectedVideoFilePath1, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
        return selectedMediaUri;


    }

    private Uri getOutputMediaFileUri2(int type) {
        File mediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/myvideo.mp4");
        //videoUri = Uri.fromFile(mediaFile);
        String authorities = getApplicationContext().getPackageName() + ".fileprovider";
        videoUri = FileProvider.getUriForFile(getApplicationContext(), authorities, getOutputMediaFile(type));
        return videoUri;


    }


    private File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Config.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("tag", "Oops! Failed create "
                        + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");

            //Make sure you close all streams.

        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");

        } else {
            return null;
        }

        return mediaFile;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == INTENT_REQUEST_GET_COVERIMAGES) {
                Uri selectedMediaUri = data.getData();
                selectedCoverFilePath = GetFilePathFromDevice.getPath(getApplicationContext(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), selectedMediaUri);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (requestCode == INTENT_REQUEST_GET_VIDEO1) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    try {
                        Uri selectedMediaUri = data.getData();
                        selectedVideoFilePath1 = GetFilePathFromDevice.getPath(getApplicationContext(), selectedMediaUri);
                        no_video.setImageBitmap(ThumbnailUtils.createVideoThumbnail(selectedVideoFilePath1, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                    } catch (NullPointerException e) {

                    }
                } else {

                    selectedVideoFilePath1 = fileUri.getPath();
                    no_video.setImageBitmap(ThumbnailUtils.createVideoThumbnail(selectedVideoFilePath1, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));


                }


            } else if (requestCode == INTENT_REQUEST_GET_VIDEO11) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Uri selectedMediaUri = videoUri;
                    selectedVideoFilePath1 = getFilePathFromURI(getApplicationContext(), selectedMediaUri);
                    no_video.setImageBitmap(ThumbnailUtils.createVideoThumbnail(selectedVideoFilePath1, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));

                } else {
                    Uri selectedMediaUri = data.getData();
                    selectedVideoFilePath1 = GetFilePathFromDevice.getPath(getApplicationContext(), selectedMediaUri);
                    no_video.setImageBitmap(ThumbnailUtils.createVideoThumbnail(selectedVideoFilePath1, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));

                }
            } else {
                Uri selectedMediaUri = data.getData();
                if (selectedMediaUri.toString().contains("image")) {
                    try {
                        String selectedVideoFilePath1 = GetFilePathFromDevice.getPath(getApplicationContext(), selectedMediaUri);
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), selectedMediaUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (selectedMediaUri.toString().contains("video")) {
                    String selectedVideoFilePath = GetFilePathFromDevice.getPath(getApplicationContext(), selectedMediaUri);
                }

            }

        }

    }


    //check Camera Permission Method:
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        if ((result == PackageManager.PERMISSION_GRANTED) && (result1 == PackageManager.PERMISSION_GRANTED) && (result2 == PackageManager.PERMISSION_GRANTED)) {
            return true;
        } else {
            ActivityCompat.requestPermissions(PaticularWowtagActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
            return false;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back = new Intent(getApplicationContext(), LandingPageActivity.class);
        startActivity(back);
        finish();
    }

    //Describe Asynctask for Particular WOWTAG List:
    private class callParticularWowtag extends AsyncTask<String, String, String> {


        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... strings) {

            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("eventid", get_wowid);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL_GET_PARTICULAR_WOWTAGLIST, json, token);
            } catch (Exception e) {

            }
            return null;
        }


        @Override
        protected void onPostExecute(String jsonstr) {
            super.onPostExecute(jsonstr);
            if (jsonstr.equals("")) {
                Toast.makeText(getApplicationContext(), "Check Network Connection", Toast.LENGTH_LONG).show();
            } else {

                try {

                    JSONObject jo = new JSONObject(jsonstr);
                    String status = jo.getString("success");
                    if (status.equals("true")) {

                        JSONObject jsonObject = jo.getJSONObject("message");
                        wowtag_name.setText(jsonObject.getString("eventname"));
                        String e_start_date = jsonObject.getString("eventstartdate");
                        String e_end_date = jsonObject.getString("eventenddate");
                        wowtag_description_cont.setText(jsonObject.getString("eventdescription"));
                        wowtag_video = jsonObject.getString("wowtagvideo");
                        String wowtag_runtimefrom = jsonObject.getString("runtimefrom");
                        String wowtag_runtimeto = jsonObject.getString("runtimeto");

                        wowtag_runtime.setText("Wowtag Runtime From " + wowtag_runtimefrom + " to " + wowtag_runtimeto);

                        if (wowtag_video == null) {
                            no_video.setVisibility(View.VISIBLE);
                            wow_videoview.setVisibility(View.GONE);
                            Glide.with(getApplicationContext()).load(R.drawable.no_video)
                                    .into(no_video);

                        }
                        if (wowtag_video.equals("null")) {
                            no_video.setVisibility(View.VISIBLE);
                            wow_videoview.setVisibility(View.GONE);

                            Glide.with(getApplicationContext()).load(R.drawable.no_video)
                                    .into(no_video);
                        } else {

                            no_video.setVisibility(View.GONE);
                            wow_videoview.setVisibility(View.VISIBLE);
                            wow_videoview.setVideoPath("http://104.197.80.225:3010/wow/media/event/" + wowtag_video);
                        }

                        try {
                            String[] splited_start = e_start_date.split("\\s+");
                            String date1 = splited_start[0];
                            String time1 = splited_start[1];
                            String ampm1 = splited_start[2];

                            String[] splited_end = e_end_date.split("\\s+");
                            String date2 = splited_end[0];
                            String time2 = splited_end[1];
                            String ampm2 = splited_end[2];

                            wowtag_eventdatetime.setText(date1 + " to " + date2);
                            wowtag_time.setText(time1 + " " + ampm1 + " - " + time2 + " " + ampm2);

                        } catch (ArrayIndexOutOfBoundsException e) {

                        }


                    } else {

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    //Describe Asynctask for DELETE VIDEO:
    private class callParticularWowtagVideoDelete extends AsyncTask<String, String, String> {


        protected void onPreExecute() {
            super.onPreExecute();

            //av_loader.setVisibility(View.VISIBLE);
        }


        @Override
        protected String doInBackground(String... strings) {

            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("eventid", get_wowid);

                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL_GET_PARTICULAR_WOWTAGLIST_DELETE, json, token);
            } catch (Exception e) {

            }
            return null;
        }


        @Override
        protected void onPostExecute(String jsonstr) {
            //av_loader.setVisibility(View.GONE);
            super.onPostExecute(jsonstr);

            if (jsonstr.equals("")) {
                Toast.makeText(getApplicationContext(), "Check Network Connection", Toast.LENGTH_LONG).show();
            } else {

                try {

                    JSONObject jo = new JSONObject(jsonstr);
                    String status = jo.getString("success");
                    if (status.equals("true")) {

                        str_msg = jo.getString("message");

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {


                                no_video.setVisibility(View.VISIBLE);
                                wow_videoview.setVisibility(View.GONE);

                                Glide.with(getApplicationContext()).load(R.drawable.no_video)
                                        .into(no_video);
                                Toast.makeText(getApplicationContext(), str_msg, Toast.LENGTH_LONG).show();
                                animShake.cancel();
                            }
                        }, 3000);


                    } else {
                        String str_msg = jo.getString("message");
                        Glide.with(getApplicationContext()).load(R.drawable.no_video)
                                .into(no_video);
                        Toast.makeText(getApplicationContext(), str_msg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    //Describe Asynctask for EDIT VIDEO:
    public class editWowtagVideoAsync extends AsyncTask<String, Void, String> {
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
                HttpPost httppost = new HttpPost("http://104.197.80.225:3010/wow/event/editwowtagvideo");
                httppost.setHeader("token", token);
                httppost.setHeader("eventid", get_wowid);
                HttpResponse response = null;
                HttpEntity r_entity = null;


                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                if (!selectedVideoFilePath1.equals("")) {
                    entity.addPart("wowtagvideo", new FileBody(new File(selectedVideoFilePath1), "image/jpeg"));
                }

                httppost.setEntity(entity);
                try {

                    try {
                        response = httpclient.execute(httppost);
                    } catch (Exception e) {
                    }

                    try {
                        r_entity = response.getEntity();
                    } catch (Exception e) {
                    }

                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        responseString = EntityUtils.toString(r_entity);

                    } else {
                        responseString = "Error occurred! Http Status Code: "
                                + statusCode;
                    }
                } catch (ClientProtocolException e) {
                    responseString = e.toString();
                } catch (IOException e) {
                    responseString = e.toString();
                }
                return responseString;
            } catch (Exception e) {
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loader_dialog.dismiss();
            if (s.length() > 0) {
                try {
                    JSONObject jo = new JSONObject(s.toString());
                    String success = jo.getString("success");
                    if (success.equals("true")) {
                        String msg = jo.getString("message");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        new callParticularWowtag().execute();

                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
