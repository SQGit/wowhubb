package com.wowhubb.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.leocardz.link.preview.library.LinkPreviewCallback;
import com.leocardz.link.preview.library.SourceContent;
import com.leocardz.link.preview.library.TextCrawler;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;

import org.apache.commons.io.output.ByteArrayOutputStream;
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
import java.util.List;

/**
 * Created by Salman on 21-02-2018.
 */

public class ShareThoughtsUrlActivity extends Activity {
    private static final int INTENT_REQUEST_GET_COVERIMAGES = 11;
    private static final int INTENT_REQUEST_GET_VIDEO1 = 12;
    TextView backtv, profilenametv, userdesignationtv, publishtv, submittv;
    ImageView linkiv, profile_iv;
    VideoView videoview;
    String selectedCoverFilePath, selectedVideoFilePath1, token, personalimage, str_name, str_lname, userdesignation;
    FrameLayout videoframe;
    String thoughtsvideo, thoughtsimage, thoughtstext, urltext;
    EditText thoughts_et, url_et;
    Dialog loader_dialog;
    TextInputLayout url_link;
    private Context context;
    private String currentTitle, currentUrl, currentCannonicalUrl,
            currentDescription;
    private TextCrawler textCrawler;
    private Bitmap[] currentImageSet;
    private Bitmap currentImage;
    private int currentItem = 0;
    private int countBigImages = 0;
    private boolean noThumb;
    private ViewGroup dropPreview, dropPost;
    ImageView imageSet;
    private LinkPreviewCallback callback = new LinkPreviewCallback() {
        /**
         * This view is used to be updated or added in the layout after getting
         * the result
         */
        private View mainView;
        private LinearLayout linearLayout;
        private View loading;
        private ImageView imageView;

        @Override
        public void onPre() {
            hideSoftKeyboard();

            currentImageSet = null;
            currentItem = 0;

            // postButton.setVisibility(View.GONE);
            //  previewAreaTitle.setVisibility(View.VISIBLE);

            currentImage = null;
            noThumb = false;
            currentTitle = currentDescription = currentUrl = currentCannonicalUrl = "";

            //submitButton.setEnabled(false);

            /** Inflating the preview layout */
            mainView = getLayoutInflater().inflate(R.layout.main_view, null);

            linearLayout = (LinearLayout) mainView.findViewById(R.id.external);

            /**
             * Inflating a loading layout into Main View LinearLayout
             */
            loading = getLayoutInflater().inflate(R.layout.loading,
                    linearLayout);

            dropPreview.addView(mainView);
        }

        @Override
        public void onPos(final SourceContent sourceContent, boolean isNull) {

            /** Removing the loading layout */
            linearLayout.removeAllViews();

            if (isNull || sourceContent.getFinalUrl().equals("")) {
                /**
                 * Inflating the content layout into Main View LinearLayout
                 */
                View failed = getLayoutInflater().inflate(R.layout.failed,
                        linearLayout);

                TextView titleTextView = (TextView) failed
                        .findViewById(R.id.text);
                titleTextView.setText(getString(R.string.failed_preview) + "\n"
                        + sourceContent.getFinalUrl());

                failed.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        releasePreviewArea();
                    }
                });

            } else {
                //  postButton.setVisibility(View.VISIBLE);

                currentImageSet = new Bitmap[sourceContent.getImages().size()];

                /**
                 * Inflating the content layout into Main View LinearLayout
                 */
                final View content = getLayoutInflater().inflate(
                        R.layout.preview_content, linearLayout);

                /** Fullfilling the content layout */
                final LinearLayout infoWrap = (LinearLayout) content
                        .findViewById(R.id.info_wrap);
                final LinearLayout titleWrap = (LinearLayout) infoWrap
                        .findViewById(R.id.title_wrap);
                final LinearLayout thumbnailOptions = (LinearLayout) content
                        .findViewById(R.id.thumbnail_options);
                final LinearLayout noThumbnailOptions = (LinearLayout) content
                        .findViewById(R.id.no_thumbnail_options);

                  imageSet = (ImageView) content
                        .findViewById(R.id.image_post_set);

                final TextView close = (TextView) titleWrap
                        .findViewById(R.id.close);
                final TextView titleTextView = (TextView) titleWrap
                        .findViewById(R.id.title);
                final EditText titleEditText = (EditText) titleWrap
                        .findViewById(R.id.input_title);
                final TextView urlTextView = (TextView) content
                        .findViewById(R.id.url);
                final TextView descriptionTextView = (TextView) content
                        .findViewById(R.id.description);
                final EditText descriptionEditText = (EditText) content
                        .findViewById(R.id.input_description);
                final TextView countTextView = (TextView) thumbnailOptions
                        .findViewById(R.id.count);
                final CheckBox noThumbCheckBox = (CheckBox) noThumbnailOptions
                        .findViewById(R.id.no_thumbnail_checkbox);
                final Button previousButton = (Button) thumbnailOptions
                        .findViewById(R.id.post_previous);
                final Button forwardButton = (Button) thumbnailOptions
                        .findViewById(R.id.post_forward);

                //  editTextTitlePost = titleEditText;
                // editTextDescriptionPost = descriptionEditText;
                publishtv.setVisibility(View.VISIBLE);
                submittv.setVisibility(View.GONE);
                titleTextView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        titleTextView.setVisibility(View.GONE);

                        titleEditText.setText(TextCrawler
                                .extendedTrim(titleTextView.getText()
                                        .toString()));
                        titleEditText.setVisibility(View.VISIBLE);
                    }
                });
                titleEditText
                        .setOnEditorActionListener(new TextView.OnEditorActionListener() {

                            @Override
                            public boolean onEditorAction(TextView arg0,
                                                          int arg1, KeyEvent arg2) {

                                if (arg2.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                                    titleEditText.setVisibility(View.GONE);

                                    currentTitle = TextCrawler
                                            .extendedTrim(titleEditText
                                                    .getText().toString());

                                    titleTextView.setText(currentTitle);
                                    titleTextView.setVisibility(View.VISIBLE);

                                    hideSoftKeyboard();
                                }

                                return false;
                            }
                        });
                descriptionTextView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        descriptionTextView.setVisibility(View.GONE);

                        descriptionEditText.setText(TextCrawler
                                .extendedTrim(descriptionTextView.getText()
                                        .toString()));
                        descriptionEditText.setVisibility(View.VISIBLE);
                    }
                });
                descriptionEditText
                        .setOnEditorActionListener(new TextView.OnEditorActionListener() {

                            @Override
                            public boolean onEditorAction(TextView arg0,
                                                          int arg1, KeyEvent arg2) {

                                if (arg2.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                                    descriptionEditText
                                            .setVisibility(View.GONE);

                                    currentDescription = TextCrawler
                                            .extendedTrim(descriptionEditText
                                                    .getText().toString());

                                    descriptionTextView
                                            .setText(currentDescription);
                                    descriptionTextView
                                            .setVisibility(View.VISIBLE);

                                    hideSoftKeyboard();
                                }

                                return false;
                            }
                        });

                close.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        releasePreviewArea();
                    }
                });

                noThumbCheckBox
                        .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                            @Override
                            public void onCheckedChanged(CompoundButton arg0,
                                                         boolean arg1) {
                                noThumb = arg1;

                                if (sourceContent.getImages().size() > 1)
                                    if (noThumb)
                                        thumbnailOptions
                                                .setVisibility(View.GONE);
                                    else
                                        thumbnailOptions
                                                .setVisibility(View.VISIBLE);

                                showHideImage(imageSet, infoWrap, !noThumb);
                            }
                        });

                previousButton.setEnabled(false);
                previousButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        if (currentItem > 0)
                            changeImage(previousButton, forwardButton,
                                    currentItem - 1, sourceContent,
                                    countTextView, imageSet, sourceContent
                                            .getImages().get(currentItem - 1),
                                    currentItem);
                    }
                });
                forwardButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        if (currentItem < sourceContent.getImages().size() - 1)
                            changeImage(previousButton, forwardButton,
                                    currentItem + 1, sourceContent,
                                    countTextView, imageSet, sourceContent
                                            .getImages().get(currentItem + 1),
                                    currentItem);
                    }
                });

                if (sourceContent.getImages().size() > 0) {

                    if (sourceContent.getImages().size() > 1) {
                        countTextView.setText("1 " + getString(R.string.failed_preview)
                                + " " + sourceContent.getImages().size());

                        thumbnailOptions.setVisibility(View.VISIBLE);
                    }
                    noThumbnailOptions.setVisibility(View.VISIBLE);

                    UrlImageViewHelper.setUrlDrawable(imageSet, sourceContent
                            .getImages().get(0), new UrlImageViewCallback() {

                        @Override
                        public void onLoaded(ImageView imageView,
                                             Bitmap loadedBitmap, String url,
                                             boolean loadedFromCache) {
                            if (loadedBitmap != null) {
                                currentImage = loadedBitmap;
                                currentImageSet[0] = loadedBitmap;
                            }
                        }
                    });

                } else {
                    showHideImage(imageSet, infoWrap, false);
                }

                if (sourceContent.getTitle().equals(""))
                    sourceContent.setTitle(getString(R.string.enter_title));
                if (sourceContent.getDescription().equals(""))
                    sourceContent
                            .setDescription(getString(R.string.enter_description));

                titleTextView.setText(sourceContent.getTitle());
                urlTextView.setText(sourceContent.getCannonicalUrl());
                descriptionTextView.setText(sourceContent.getDescription());

                //  postButton.setVisibility(View.VISIBLE);
            }

            currentTitle = sourceContent.getTitle();
            currentDescription = sourceContent.getDescription();
            currentUrl = sourceContent.getUrl();
            currentCannonicalUrl = sourceContent.getCannonicalUrl();
            Log.e("tag", "1111122222222223333333333------------->>>" + currentImage);
          //  BitmapToString(imageSet);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.dialog_urlsharethoughts);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ShareThoughtsUrlActivity.this);
        token = sharedPreferences.getString("token", "");
        personalimage = sharedPreferences.getString("profilepath", "");
        str_name = sharedPreferences.getString("str_name", "");
        str_lname = sharedPreferences.getString("str_lname", "");
        userdesignation = sharedPreferences.getString("userdesignation", "");
        Typeface lato = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lato.ttf");
        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(ShareThoughtsUrlActivity.this, v1);

        Log.e("tag", "code3------------->" + userdesignation);

        loader_dialog = new Dialog(ShareThoughtsUrlActivity.this);
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(R.layout.test_loader);

        backtv = findViewById(R.id.backtv);

        videoview = findViewById(R.id.video_view);
        profilenametv = findViewById(R.id.hoster_name);

        videoframe = findViewById(R.id.videoframe);
        profile_iv = findViewById(R.id.imageview_profile);
        userdesignationtv = findViewById(R.id.hoster_designation);
        publishtv = findViewById(R.id.publishtv);
        submittv = findViewById(R.id.submittv);
        thoughts_et = findViewById(R.id.thoughts_et);
        url_et = findViewById(R.id.link_et);
        url_link = findViewById(R.id.til_url);
        url_link.setTypeface(lato);
        dropPreview = (ViewGroup) findViewById(R.id.drop_preview);
        Bundle extras = getIntent().getExtras();

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

        submittv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textCrawler.makePreview(callback, url_et.getText().toString());

           /*     thoughtstext = thoughts_et.getText().toString().trim();
                urltext = url_et.getText().toString().trim();


                if (!thoughts_et.getText().toString().trim().equalsIgnoreCase("") || !url_et.getText().toString().trim().equalsIgnoreCase("")|| selectedCoverFilePath != null || selectedVideoFilePath1 != null) {
                    new publishthoughts().execute();
                } else {
                    Toast.makeText(ShareThoughtsActivity.this, "Please publish some data", Toast.LENGTH_LONG).show();
                }*/

            }
        });
        publishtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //thoughtstext = thoughts_et.getText().toString().trim();
                //   urltext = url_et.getText().toString().trim();

                new publishthoughts().execute();


            }
        });


        backtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //------------------- Share thought details get from URL---------------------------------//

        if (getIntent().getAction() == Intent.ACTION_VIEW) {
            Uri data = getIntent().getData();
            String scheme = data.getScheme();
            String host = data.getHost();
            List<String> params = data.getPathSegments();
            String builded = scheme + "://" + host + "/";

            for (String string : params) {
                builded += string + "/";
            }

            if (data.getQuery() != null && !data.getQuery().equals("")) {
                builded = builded.substring(0, builded.length() - 1);
                builded += "?" + data.getQuery();
            }

            System.out.println(builded);

            url_et.setText(builded);

        }
        textCrawler = new TextCrawler();


    }

    /**
     * Change the current image in image set
     */
    private void changeImage(Button previousButton, Button forwardButton,
                             final int index, SourceContent sourceContent,
                             TextView countTextView, ImageView imageSet, String url,
                             final int current) {

        if (currentImageSet[index] != null) {
            currentImage = currentImageSet[index];
            imageSet.setImageBitmap(currentImage);
        } else {
            UrlImageViewHelper.setUrlDrawable(imageSet, url,
                    new UrlImageViewCallback() {

                        @Override
                        public void onLoaded(ImageView imageView,
                                             Bitmap loadedBitmap, String url,
                                             boolean loadedFromCache) {
                            if (loadedBitmap != null) {
                                currentImage = loadedBitmap;
                                currentImageSet[index] = loadedBitmap;
                            }
                        }
                    });

        }

        currentItem = index;

        if (index == 0)
            previousButton.setEnabled(false);
        else
            previousButton.setEnabled(true);

        if (index == sourceContent.getImages().size() - 1)
            forwardButton.setEnabled(false);
        else
            forwardButton.setEnabled(true);

        countTextView.setText((index + 1) + " " + getString(R.string.failed_preview) + " "
                + sourceContent.getImages().size());
    }

    /**
     * Show or hide the image layout according to the "No Thumbnail" ckeckbox
     */
    private void showHideImage(View image, View parent, boolean show) {
        if (show) {
            image.setVisibility(View.VISIBLE);
            parent.setPadding(5, 5, 5, 5);
            parent.setLayoutParams(new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 2f));
        } else {
            image.setVisibility(View.GONE);
            parent.setPadding(5, 5, 5, 5);
            parent.setLayoutParams(new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 3f));
        }
    }

    /**
     * Hide keyboard
     */
    private void hideSoftKeyboard() {
        hideSoftKeyboard(url_et);

      /*  if (editTextTitlePost != null)
            hideSoftKeyboard(editTextTitlePost);
        if (editTextDescriptionPost != null)
            hideSoftKeyboard(editTextDescriptionPost);*/
    }

    private void hideSoftKeyboard(EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void releasePreviewArea() {

        dropPreview.removeAllViews();
        publishtv.setVisibility(View.GONE);
        submittv.setVisibility(View.VISIBLE);
    }

    public String BitmapToString(Bitmap bitmap) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            thoughtsimage = Base64.encodeToString(b, Base64.DEFAULT);
            Log.e("tag", "GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG--------" + thoughtsimage);
            return thoughtsimage;
        } catch (NullPointerException e) {
            return null;
        } catch (OutOfMemoryError e) {
            return null;
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


                if (currentDescription != null && !currentDescription.equals("null")) {
                    Log.e("tag", "texttttttttttttttttttttttt:" + thoughtstext);
                    httppost.setHeader("thoughtstext", currentDescription);
                }
                if (currentUrl != null && !currentUrl.equals("null")) {
                    Log.e("tag", "texttttttttttttttttttttttt:" + urltext);
                    httppost.setHeader("urllink", currentUrl);
                }
                HttpResponse response = null;
                HttpEntity r_entity = null;
                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                Log.e("tag", "image:------------------>......" + thoughtsimage);


                if (thoughtsimage != null) {
                    Log.e("tag", "PPPPPPPPPPP------------------>......" + thoughtsimage);
                    entity.addPart("thoughtsimage", new FileBody(new File(thoughtsimage), "image/jpeg"));
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
                            Intent intent = new Intent(ShareThoughtsUrlActivity.this, EventFeedDashboard.class);
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
