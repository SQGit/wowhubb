package com.wowhubb;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;


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
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

/**
 * Created by Ramya on 28-07-2017.
 */

public class Test extends Activity {
    String profilepath;
    Button btn;
    FrameLayout framevideo1, framevideo2, framecoverpage, framehighlight1, fraehighlight2;
    ImageView video0_iv, video1_iv, cover_iv, highl1_iv, highl2_iv;
    private static final int INTENT_REQUEST_GET_VIDEO1 = 12;
    private static final int INTENT_REQUEST_GET_VIDEO2 = 13;
    ImageView video1plus, video2plus, coverplus, h1plus, h2plus, tickiv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);









    }


    public class videoupload_task extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("tag", "reg_preexe");
            //  av_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";

            // if (selectedPhotos.size() > 0) {
            try {
                String responseString = null;
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://104.197.80.225:3010/wow/createevent");
                // mobileno,email,password,username,licence_number
              /*  httppost.setHeader("user_name", user_name);
                httppost.setHeader("user_mobile", user_mobile);
                httppost.setHeader("user_email", user_email);
                httppost.setHeader("user_address", user_address);
                httppost.setHeader("id", str_id);
                httppost.setHeader("sessiontoken", token);*/


                HttpResponse response = null;
                HttpEntity r_entity = null;

                if (profilepath != null) {
                    Log.e("tag", "strt111" + profilepath);
                    MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                    entity.addPart("eventimage", new FileBody(new File(profilepath), "video/mp4"));
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
                        String status = jo.getString("status");
                        // message = jo.getString("message");
                        return status;
                        // Toast.makeText(getApplicationContext(),"You have been successfully registered with Medikally",Toast.LENGTH_LONG).show();

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

            // }
            // return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            Log.e("tag", "code------------->" + resultCode);
        if (requestCode == INTENT_REQUEST_GET_VIDEO1) {
                Uri selectedMediaUri = data.getData();
                Log.d("tag", "567231546" + selectedMediaUri);
                //  Uri selectedVideo = data.getData();
            profilepath= GetFilePathFromDevice.getPath(getApplicationContext(), selectedMediaUri);
            Log.e("tag", "<--------selectedVideoFilePath----------->" + profilepath);
                video0_iv.setImageBitmap(ThumbnailUtils.createVideoThumbnail(profilepath, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                video1plus.setImageDrawable(getApplicationContext().getDrawable(R.drawable.video_icon));
            } else if (requestCode == INTENT_REQUEST_GET_VIDEO2) {
                Uri selectedMediaUri = data.getData();
                Log.d("tag", "567231546" + selectedMediaUri);
                //  Uri selectedVideo = data.getData();
                String selectedVideoFilePath = GetFilePathFromDevice.getPath(getApplicationContext(), selectedMediaUri);

                video1_iv.setImageBitmap(ThumbnailUtils.createVideoThumbnail(selectedVideoFilePath, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                video2plus.setImageDrawable(getApplicationContext().getDrawable(R.drawable.video_icon));
            }  else {

                }

            } else {
                Uri selectedMediaUri = data.getData();

                if (selectedMediaUri.toString().contains("image")) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), selectedMediaUri);
                        highl2_iv.setImageBitmap(bitmap);
                        h2plus.setVisibility(View.GONE);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } else if (selectedMediaUri.toString().contains("video")) {
                    Log.d("tag", "567231546" + selectedMediaUri);

                    String selectedVideoFilePath = GetFilePathFromDevice.getPath(getApplicationContext(), selectedMediaUri);
                    highl2_iv.setImageBitmap(ThumbnailUtils.createVideoThumbnail(selectedVideoFilePath, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                    h2plus.setImageDrawable(getApplicationContext().getDrawable(R.drawable.video_icon));

                }

            }


        }
    }






