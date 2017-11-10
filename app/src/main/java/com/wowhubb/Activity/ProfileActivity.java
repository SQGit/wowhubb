package com.wowhubb.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gun0912.tedpicker.ImagePickerActivity;
import com.wang.avi.AVLoadingIndicatorView;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Salman on 05-10-2017.
 */

public class ProfileActivity extends Activity {
    public final static int REQUEST_PROFILE = 1;
    public final static int REQUEST_COVERPAGE = 2;
    private static final int INTENT_REQUEST_GET_VIDEO1 = 12;
    private static final int INTENT_REQUEST_GET_VIDEO2 = 11;
    public com.gun0912.tedpicker.Config img_config;
    TextView personal_tv, skiptv, edittv, wowtag2, browse_tv, save_tv;
    ImageView editiv, backiv, coverpageiv, coverpageeditiv, video0_iv, video1plus;
    ArrayList<String> selectedPhotos = new ArrayList<>();
    ArrayList<Uri> image_uris;
    String str_profile_img, str_profile_cover, token, name, lname, str_wowtag, profilepath;
    ImageView profile_iv;
    FrameLayout framevideo1;
    TextView username, lastname, wowtag, professionaltv;
    //String token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1OWQ1ZTkxOTQxYmRjNjE5ZTg0NGRkMjYiLCJmaXJzdG5hbWUiOiJzZ3MiLCJsYXN0bmFtZSI6InNnZWciLCJlbWFpbCI6InJhbXlhQHNxaW5kaWEubmV0IiwicGhvbmUiOiIrOTE5NzkxMDk1MjM4IiwiYmlydGhkYXkiOiIxMC81LzIwMTciLCJpYXQiOjE1MDc2Mzk2OTgsImV4cCI6MTUwNzgxMjQ5OH0.rX71fZqoLoiJPpmbQnoIrcWx8S8QUJzgKCRfKkQUHMo";
    AVLoadingIndicatorView av_loader;
    LinearLayout interest_lvl;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");
        name = sharedPreferences.getString("str_name", "");
        lname = sharedPreferences.getString("str_lname", "");
        str_wowtag = sharedPreferences.getString("wowtagid", "");
        profilepath = sharedPreferences.getString("profilepath", "");
        img_config = new com.gun0912.tedpicker.Config();
        img_config.setCameraHeight(R.dimen.app_camera_height);
        img_config.setSelectedBottomHeight(R.dimen.bottom_height);
        img_config.setCameraBtnBackground(R.drawable.round_rd);
        personal_tv = findViewById(R.id.personal_tv);
        professionaltv = findViewById(R.id.professionaltv);
        editiv = (ImageView) findViewById(R.id.editiv);
        backiv = findViewById(R.id.backiv);
        skiptv = findViewById(R.id.skiptv);
        edittv = findViewById(R.id.edittv);
        save_tv = findViewById(R.id.save_tv);
        browse_tv = findViewById(R.id.browse_tv);
        username = findViewById(R.id.nametv);
        lastname = findViewById(R.id.subname);
        wowtag = findViewById(R.id.wowtagid);
        wowtag2 = findViewById(R.id.wowtag2);
        profile_iv = findViewById(R.id.imageview_profile);
        coverpageiv = findViewById(R.id.coverpage_iv);
        coverpageeditiv = findViewById(R.id.editiv);
        framevideo1 = findViewById(R.id.framespeaker);
        video0_iv = (ImageView) findViewById(R.id.speaker_iv);
        video1plus = findViewById(R.id.speakerplus);
        interest_lvl = findViewById(R.id.interest_lv);
        av_loader = (AVLoadingIndicatorView) findViewById(R.id.avi);
        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(ProfileActivity.this, v1);
        new getpersonalprofile().execute();

        if (name != null) {
            username.setText(name);
        }

        if (lname != null) {
            lastname.setText(lname);

        }

        if (str_wowtag != null) {
            String sa = str_wowtag.substring(0, 1);
            if (sa.equals("!")) {
                wowtag.setText(str_wowtag);
                wowtag2.setText(str_wowtag);
            } else {
                wowtag.setText("!" + str_wowtag);
                wowtag2.setText("!" + str_wowtag);
            }


        }
        if (!profilepath.equals("")) {
            Log.e("tag", "s--------->" + profilepath);

            Glide.with(ProfileActivity.this).load(new File(profilepath)).into(profile_iv);
        }

        personal_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, ProfilePersonalActivity.class));

            }
        });

        professionaltv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  startActivity(new Intent(ProfileActivity.this, ProfilePersonalActivity.class));
                //overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });
        save_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, LandingPageActivity.class));
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

            }
        });
        backiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileActivity.this.finish();
            }
        });
        skiptv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, LandingPageActivity.class));
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                finish();
            }
        });
        interest_lvl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, InterestActivity.class));
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                finish();
            }
        });
        edittv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    img_config.setSelectionMin(1);
                    img_config.setSelectionLimit(1);
                    img_config.setToolbarTitleRes(R.string.img_profile);
                    ImagePickerActivity.setConfig(img_config);
                    Intent intent = new Intent(ProfileActivity.this, com.gun0912.tedpicker.ImagePickerActivity.class);
                    startActivityForResult(intent, REQUEST_PROFILE);
                }
            }
        });

        coverpageeditiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    img_config.setSelectionMin(1);
                    img_config.setSelectionLimit(1);
                    img_config.setToolbarTitleRes(R.string.img_coverprofile);
                    ImagePickerActivity.setConfig(img_config);
                    Intent intent = new Intent(ProfileActivity.this, com.gun0912.tedpicker.ImagePickerActivity.class);
                    startActivityForResult(intent, REQUEST_COVERPAGE);
                }
            }
        });

      /*  framevideo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    Intent intent = new Intent();
                    intent.setType("video*//*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Video"), INTENT_REQUEST_GET_VIDEO1);
                }

            }
        });*/

        browse_tv.setOnClickListener(new View.OnClickListener() {
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<String> photos = null;
        if (requestCode == REQUEST_PROFILE && resultCode == Activity.RESULT_OK) {
            try {
                image_uris = data.getParcelableArrayListExtra(com.gun0912.tedpicker.ImagePickerActivity.EXTRA_IMAGE_URIS);
                Log.e("tag", "12345----------->>>>>>" + image_uris.get(0).toString());
            } catch (IndexOutOfBoundsException e) {
                Log.e("tag", "xgdfdg" + e.toString());
            }
            // selectedPhotos.clear();
            if (image_uris != null) {
                str_profile_img = image_uris.get(0).toString();
                Glide.with(ProfileActivity.this).load(new File(str_profile_img)).into(profile_iv);


                new profileupload_task().execute();
            }
        } else if (requestCode == REQUEST_COVERPAGE && resultCode == Activity.RESULT_OK) {
            try {
                image_uris = data.getParcelableArrayListExtra(com.gun0912.tedpicker.ImagePickerActivity.EXTRA_IMAGE_URIS);
                Log.e("tag", "12345" + image_uris.get(0).toString());
            } catch (IndexOutOfBoundsException e) {
                Log.e("tag", "xgdfdg" + e.toString());
            }


            // selectedPhotos.clear();
            if (image_uris != null) {
                str_profile_cover = image_uris.get(0).toString();
                Glide.with(ProfileActivity.this).load(new File(str_profile_cover)).into(coverpageiv);
                new profilecoverupload_task().execute();
            }
        } else if (requestCode == INTENT_REQUEST_GET_VIDEO1) {

            try {
                Uri selectedMediaUri = data.getData();
                Log.d("tag", "567231546" + selectedMediaUri);
                //  Uri selectedVideo = data.getData();
                String selectedVideoFilePath1 = GetFilePathFromDevice.getPath(ProfileActivity.this, selectedMediaUri);

                video0_iv.setImageBitmap(ThumbnailUtils.createVideoThumbnail(selectedVideoFilePath1, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                video1plus.setImageDrawable(ProfileActivity.this.getDrawable(R.drawable.video_icon));
            } catch (NullPointerException e) {

            }


        }


    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.RECORD_AUDIO);

        if ((result == PackageManager.PERMISSION_GRANTED) && (result1 == PackageManager.PERMISSION_GRANTED)) {
            Log.e("tag", "Permission is granted");
            return true;


        } else {
            Log.e("tag", "Permission is revoked");
            ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return false;

        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);


        }
        return super.dispatchTouchEvent(ev);
    }

    public class profileupload_task extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            av_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                String responseString = null;
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Config.WEB_URL_PROFILE_IMG);
                httppost.setHeader("token", token);
                HttpResponse response = null;
                HttpEntity r_entity = null;

                if (str_profile_img != null) {
                    Log.e("tag", "strt111" + str_profile_img);
                    MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                    entity.addPart("personalimage", new FileBody(new File(str_profile_img), "image/jpeg"));
                    httppost.setEntity(entity);
                }

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
                        JSONObject jo = new JSONObject(responseString);
                        String success = jo.getString("success");
                        editor.putString("profilepath", str_profile_img);
                        editor.commit();
                        return success;


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
            av_loader.setVisibility(View.GONE);
            //Toast.makeText(getActivity(),"Event Created Successfully",Toast.LENGTH_LONG).show();

        }

    }

    public class profilecoverupload_task extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            av_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                String responseString = null;
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Config.WEB_URL_COVER_IMG);
                httppost.setHeader("token", token);
                HttpResponse response = null;
                HttpEntity r_entity = null;

                if (str_profile_cover != null) {
                    Log.e("tag", "strt111" + str_profile_cover);
                    MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                    entity.addPart("personalcover", new FileBody(new File(str_profile_cover), "image/jpeg"));
                    httppost.setEntity(entity);
                }

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
                        JSONObject jo = new JSONObject(responseString);
                        String success = jo.getString("success");

                        return success;


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
            av_loader.setVisibility(View.GONE);
            //Toast.makeText(getActivity(),"Event Created Successfully",Toast.LENGTH_LONG).show();

        }

    }

    public class getpersonalprofile extends AsyncTask<String, Void, String> {
        String phone_str, str_pwd;

        public getpersonalprofile() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // av_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();

                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL_GETPERSONAL, json, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "getprofile------->" + s);
            // av_loader.setVisibility(View.GONE);
            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("success");
                    if (status.equals("true")) {
                        JSONObject message = jo.getJSONObject("message");
                        // String abotme = message.getString("");

                        String personalimage = message.getString("personalimage");
                        String personalcover = message.getString("personalcover");
                        //  Log.e("tag", "getprofile------->" + personalimage);

                        if (personalimage.equals("")) {

                        } else {
                            Glide.with(ProfileActivity.this).load(new File("http://104.197.80.225:3010/wow/media/personal/" + personalimage)).into(profile_iv);
                        }

                        if (personalcover.equals("")) {

                        } else {
                            Glide.with(ProfileActivity.this).load(new File("http://104.197.80.225:3010/wow/media/personal/" + personalcover)).into(coverpageiv);
                        }


                    }

                } catch (JSONException e) {

                }

            } else {

            }

        }

    }
}
