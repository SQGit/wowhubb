package com.wowhubb.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.wowhubb.Activity.EventInviteActivity;
import com.wowhubb.Activity.MyEventFeedsActivity;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;
import com.wowhubb.Utils.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collection;
import java.util.StringTokenizer;

import mabbas007.tagsedittext.TagsEditText;


public class SmsInviteFragment extends Fragment implements TagsEditText.TagsEditListener, View.OnClickListener {

    TagsEditText tagsEditText;
    String collectionval,token;
    TextInputLayout til_tags;
    Typeface lato;
    Dialog loader_dialog;
    TextView sendinvite_tv;

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.smsinvitefragment, container, false);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        token = sharedPreferences.getString("token", "");

        View v1 = getActivity().getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(getActivity(), v1);
        lato = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato.ttf");

        tagsEditText = view.findViewById(R.id.tagsEditText);
        til_tags = view.findViewById(R.id.til_tags);
        sendinvite_tv = view.findViewById(R.id.sendinvite_tv);
        tagsEditText.setTagsListener(this);


        sendinvite_tv.setTypeface(lato);
        til_tags.setTypeface(lato);
        tagsEditText.setTypeface(lato);
        loader_dialog = new Dialog(getActivity());
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(R.layout.test_loader);

        sendinvite_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("tag", "122" + EventInviteActivity.eventId);
                new sms_invite(EventInviteActivity.eventId).execute();
            }
        });
        return view;
    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void onTagsChanged(Collection<String> collection) {
        Log.e("tag", "Tags changed: " + collection.toString());
        Log.e("tag", Arrays.toString(collection.toArray()));
        String s = Arrays.toString(collection.toArray());
        StringTokenizer tokens = new StringTokenizer(s, ",");
        for (int i = 0; i < collection.size(); i++) {
            collectionval = tokens.nextToken();
            Log.e("tag", "12ddddvvv" + collectionval);
        }


    }

    @Override
    public void onEditingFinished() {

        Log.e("tag", "OnEditing finished");
    }

    public class sms_invite extends AsyncTask<String, Void, String> {
        String eventid;

        public sms_invite(String eventid) {
            this.eventid = eventid;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader_dialog.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("phone", "+1"+tagsEditText.getText().toString());

                jsonObject.accumulate("eventid", eventid);

                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1("http://104.197.80.225:3010/wow/event/smsinvite", json, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loader_dialog.dismiss();

            Log.e("tag", "aaaaaaaaashjdh------------" + s);

            if (s != null) {
                try {

                    JSONObject jo = new JSONObject(s);
                    if (jo.has("success")) {
                        String status = jo.getString("success");
                        if (status.equals("true")) {
                            String msg = jo.getString("message");
                            Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getActivity(), MyEventFeedsActivity.class);
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                        }
                    } else {
                        String msg = jo.getString("message");
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {

                } catch (NullPointerException e) {

                }

            } else {

            }

        }

    }


}
