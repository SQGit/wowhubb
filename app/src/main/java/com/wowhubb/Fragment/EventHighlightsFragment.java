package com.wowhubb.Fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import com.wowhubb.Activity.CreateEventActivity;
import com.wowhubb.Adapter.CustomAdapter;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.GetFilePathFromDevice;
import com.wowhubb.R;
import com.wowhubb.Utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static android.app.Activity.RESULT_OK;

public class EventHighlightsFragment extends Fragment {


    private static final int INTENT_REQUEST_GET_HIGHLIGHT1 = 14;
    private static final int INTENT_REQUEST_GET_HIGHLIGHT2 = 15;
    public static TextInputLayout til_speaker, til_intro, til_url;
    public static EditText speaker_et, url_et, intro_et;
    Typeface lato;
    FrameLayout framehighlight1, fraehighlight2;
    ImageView highl1_iv, highl2_iv;
    ImageView h1plus, h2plus, wowtagiv;
    Dialog dialog;
    SharedPreferences.Editor edit;
    SharedPreferences sharedPrefces;
    ArrayList<String> ar_sectionno = new ArrayList<>();
    AutoCompleteTextView wowtag;
    TextView addmore_iv;
    String[] SPINNERLIST = {"Guest Speaker", "Guest Artist", "Comedian", "Event Host", "Event Celebrants", "Others"};
    Set<String> map1;
    String token;
    MaterialBetterSpinner materialDesignSpinner;

    public static EventHighlightsFragment newInstance(int page, boolean isLast) {
        Bundle args = new Bundle();
        args.putInt("page", page);
        if (isLast)
            args.putBoolean("isLast", true);
        final EventHighlightsFragment fragment = new EventHighlightsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_eventspeaker, container, false);
        FontsOverride.overrideFonts(getActivity(), view);
        CreateEventActivity.skiptv.setVisibility(View.VISIBLE);
        lato = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato.ttf");
        sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getActivity());
        edit = sharedPrefces.edit();
        token = sharedPrefces.getString("token", "");
        til_speaker = (TextInputLayout) view.findViewById(R.id.til_nameofspeaker);
        til_url = (TextInputLayout) view.findViewById(R.id.til_url);
        til_intro = (TextInputLayout) view.findViewById(R.id.til_intro);

        til_intro.setTypeface(lato);
        til_url.setTypeface(lato);
        til_speaker.setTypeface(lato);

        h1plus = view.findViewById(R.id.highlight1plus_iv);
        h2plus = view.findViewById(R.id.highlight2plus_iv);

        framehighlight1 = view.findViewById(R.id.frame_highlight1);
        fraehighlight2 = view.findViewById(R.id.frame_highlight2);
        addmore_iv = view.findViewById(R.id.addmore_iv);


        highl1_iv = (ImageView) view.findViewById(R.id.highlight1_iv);
        highl2_iv = (ImageView) view.findViewById(R.id.highlight2_iv);
        speaker_et = view.findViewById(R.id.speaker_et);
        url_et = view.findViewById(R.id.url_et);
        intro_et = view.findViewById(R.id.intro_et);
        speaker_et.setHint("Guest Name");
        url_et.setHint("Event Guest URL/Link");
        intro_et.setHint("Guest Introduction");
        wowtagiv = view.findViewById(R.id.wowtag_add);

        wowtagiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                new getWowtag().execute();
            }
        });

        addmore_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speaker_et.setText("");
                url_et.setText("");
                intro_et.setText("");
                highl1_iv.setImageBitmap(null);
                highl1_iv.setImageResource(R.drawable.dotted_square);
                highl2_iv.setImageBitmap(null);
                highl2_iv.setImageResource(R.drawable.dotted_square);
                h1plus.setVisibility(View.VISIBLE);
                h2plus.setVisibility(View.VISIBLE);
            }
        });
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_addwowtag);
        wowtag = (AutoCompleteTextView) dialog.findViewById(R.id.wowtag_ac);
        //Max Screen width
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //   d.show();
        dialog.getWindow().setAttributes(lp);
        ImageView iv = dialog.findViewById(R.id.closeiv);
        final TextView ok_tv = dialog.findViewById(R.id.ok_tv);
        View view1 = dialog.getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(dialog.getContext(), view1);
        wowtag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ok_tv.setVisibility(View.VISIBLE);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                Log.e("tag", "121332");
                String Name = wowtag.getText().toString();
            }
        });
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

        til_url.setTypeface(lato);
        til_speaker.setTypeface(lato);
        til_intro.setTypeface(lato);
        materialDesignSpinner = (MaterialBetterSpinner) view.findViewById(R.id.sp);

        final CustomAdapter arrayAdapter = new CustomAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, SPINNERLIST) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {

                    return true;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTypeface(lato);
                tv.setTextSize(9);
                tv.setPadding(30, 55, 10, 25);
                if (position == 0) {
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }


            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextSize(15);
                tv.setPadding(10, 20, 0, 20);
                tv.setTypeface(lato);
                if (position == 0) {
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        materialDesignSpinner.setAdapter(arrayAdapter);

        materialDesignSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FontsOverride.overrideFonts(getContext(), view);
                String str_type = adapterView.getItemAtPosition(i).toString();
                Log.e("tag", "111111111-------------->" + str_type);

                if (str_type.equals("Speaker"))
                {
                    speaker_et.setText("");
                    url_et.setText("");
                    intro_et.setText("");
                    speaker_et.setHint("");
                    url_et.setHint("");
                    intro_et.setHint("");
                    speaker_et.setHint("Speaker Name");
                    url_et.setHint("Event Speaker URL/Link");
                    intro_et.setHint("Speaker Introduction");
                } else if (str_type.equals("Guest Artist")) {
                    speaker_et.setText("");
                    url_et.setText("");
                    intro_et.setText("");
                    speaker_et.setHint("");
                    url_et.setHint("");
                    intro_et.setHint("");
                    speaker_et.setHint("Guest Name");
                    url_et.setHint("Event Guest URL/Link");
                    intro_et.setHint("Guest Introduction");
                } else if (str_type.equals("Comedian")) {
                    speaker_et.setHint("");
                    url_et.setHint("");
                    intro_et.setHint("");
                    speaker_et.setHint("Comedian Name");
                    url_et.setHint("Event Comedian URL/Link");
                    intro_et.setHint("Comedian Introduction");
                } else if (str_type.equals("Event Host")) {
                    speaker_et.setHint("");
                    url_et.setHint("");
                    intro_et.setHint("");
                    speaker_et.setHint("Event Host Name");
                    url_et.setHint("Event Host URL/Link");
                    intro_et.setHint("Event Host Introduction");
                } else if (str_type.equals("Event Celebrants")) {
                    speaker_et.setHint("");
                    url_et.setHint("");
                    intro_et.setHint("");
                    speaker_et.setHint("Event Celebrants Name");
                    url_et.setHint("Event Celebrants Host URL/Link");
                    intro_et.setHint("Event Celebrants Introduction");
                } else {
                    speaker_et.setHint("");
                    url_et.setHint("");
                    intro_et.setHint("");
                    speaker_et.setHint("Name");
                    url_et.setHint("Event URL/Link");
                    intro_et.setHint("Introduction");

                }

            }
        });

        framehighlight1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission())
                {
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("*/*");
                    pickIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "video/*"});
                    startActivityForResult(pickIntent, INTENT_REQUEST_GET_HIGHLIGHT1);
                }

            }
        });
        fraehighlight2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (checkPermission())
                {
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("*/*");
                    pickIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "video/*"});
                    startActivityForResult(pickIntent, INTENT_REQUEST_GET_HIGHLIGHT2);
                }

            }
        });
        return view;
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);

        if ((result == PackageManager.PERMISSION_GRANTED) && (result1 == PackageManager.PERMISSION_GRANTED)) {
            Log.e("tag", "Permission is granted");
            return true;
        } else {
            Log.e("tag", "Permission is revoked");
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return false;

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            Log.e("tag", "code------------->" + resultCode);
            if (requestCode == INTENT_REQUEST_GET_HIGHLIGHT1) {
                Uri selectedMediaUri = data.getData();

                if (selectedMediaUri.toString().contains("image")) {
                    try {
                        String selectedVideoFilePath1 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                        // Log.d(TAG, String.valueOf(bitmap));
                        highl1_iv.setImageBitmap(bitmap);
                        h1plus.setVisibility(View.GONE);
                        edit.putString("highlight1_status", "image");
                        edit.putString("highlight1", selectedVideoFilePath1);
                        edit.commit();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } else if (selectedMediaUri.toString().contains("video")) {
                    Log.d("tag", "567231546" + selectedMediaUri);
                    //  Uri selectedVideo = data.getData();
                    String selectedVideoFilePath = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                    edit.putString("highlight1_status", "video");
                    edit.putString("highlightsvideo1", selectedVideoFilePath);
                    edit.commit();

                    highl1_iv.setImageBitmap(ThumbnailUtils.createVideoThumbnail(selectedVideoFilePath, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                    h1plus.setImageDrawable(getActivity().getDrawable(R.drawable.video_icon));

                } else {

                }

            } else {
                Uri selectedMediaUri = data.getData();

                if (selectedMediaUri.toString().contains("image")) {
                    try {
                        String selectedVideoFilePath112 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                        highl2_iv.setImageBitmap(bitmap);
                        h2plus.setVisibility(View.GONE);
                        edit.putString("highlight2_status", "image");
                        edit.putString("highlight2", selectedVideoFilePath112);
                        edit.commit();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if (selectedMediaUri.toString().contains("video")) {
                    Log.d("tag", "567231546" + selectedMediaUri);
                    String selectedVideoFilePath112 = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);
                    highl2_iv.setImageBitmap(ThumbnailUtils.createVideoThumbnail(selectedVideoFilePath112, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                    h2plus.setImageDrawable(getActivity().getDrawable(R.drawable.video_icon));
                    edit.putString("highlight2_status", "video");
                    edit.putString("highlightsvideo2", selectedVideoFilePath112);
                    edit.commit();

                }

            }


        }

    }

    public class getWowtag extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                // Log.e("tag", "section_str" + section_str);
                JSONObject jsonObject = new JSONObject();
                // jsonObject.accumulate("regulation", str_regulation);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1("http://104.197.80.225:3010/wow/event/getwowtag", json, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "placeorderrr" + s);
            //  dialog.dismiss();


            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("success");
                    if (status.equals("true")) {
                        JSONArray message = jo.getJSONArray("message");

                        if (message.length() > 0) {

                            map1 = new HashSet<>();

                            for (int i = 0; i < message.length(); i++) {
                                try {
                                    JSONObject jsonObject = message.getJSONObject(i);
                                    String wowtag = jsonObject.getString("wowtagid");
                                    map1.add(wowtag);
                                    ar_sectionno.add(wowtag);
                                } catch (JSONException e) {

                                }

                            }


                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                    getActivity(), android.R.layout.simple_dropdown_item_1line, ar_sectionno);
                            // wowtag= (AutoCompleteTextView) view.findViewById(R.id.wowtagid);.setThreshold(1);
                            wowtag.setAdapter(adapter);
                            wowtag.setThreshold(0);
                            // offence_spn.setVisibility(View.VISIBLE);


                        } else

                        {
                            Log.e("tag", "12132123");
                            String msg = jo.getString("message");
                        }


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                }
            }


        }

    }
}