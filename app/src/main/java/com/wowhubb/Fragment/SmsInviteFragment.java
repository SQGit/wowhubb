package com.wowhubb.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Telephony;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.wowhubb.Activity.AddContactsInvite;
import com.wowhubb.Activity.EventFeedDashboard;
import com.wowhubb.Activity.EventInviteActivity;
import com.wowhubb.Activity.MyEventFeedsActivity;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.Groups.Group;
import com.wowhubb.R;
import com.wowhubb.Utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import mabbas007.tagsedittext.TagsEditText;


public class SmsInviteFragment extends Fragment implements TagsEditText.TagsEditListener, View.OnClickListener {

    TagsEditText tagsEditText;
    String collectionval, token, eventId, feedstatus,groupId;
    TextInputLayout til_tags;
    Typeface lato;
    Dialog loader_dialog, dialog;
    TextView sendinvite_tv,invitetv;
    CountryCodePicker countryCodePicker;
    ImageView addcontactiv;
    SharedPreferences sharedPrefces;
    SharedPreferences.Editor edit;
    TextView selectgroup_tv;
    ListView listview;
    private List<Group> groups;
    EditText msg_et;
    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.smsinvitefragment, container, false);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        token = sharedPreferences.getString("token", "");
        eventId = sharedPreferences.getString("eventId", "");
        feedstatus = sharedPreferences.getString("feedstatus", "");
        Log.e("tag", "12sdssddsds2-------------->>>" + eventId);
        View v1 = getActivity().getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(getActivity(), v1);
        lato = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato.ttf");
        selectgroup_tv = view.findViewById(R.id.selectgroup_tv);
        tagsEditText = view.findViewById(R.id.tagsEditText);
        til_tags = view.findViewById(R.id.til_tags);
        sendinvite_tv = view.findViewById(R.id.sendinvite_tv);
        countryCodePicker = view.findViewById(R.id.ccp);
        addcontactiv = view.findViewById(R.id.addcontact_iv);
        msg_et = view.findViewById(R.id.msg_et);
        tagsEditText.setTagsListener(this);
        selectgroup_tv.setTypeface(lato);
        sendinvite_tv.setTypeface(lato);
        til_tags.setTypeface(lato);
        tagsEditText.setTypeface(lato);
        loader_dialog = new Dialog(getActivity());
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(R.layout.test_loader);

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_wowhubb_invitelist);
        View v = dialog.getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(getActivity(), v);
        listview = (ListView) dialog.findViewById(R.id.listView);
        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        invitetv = dialog.findViewById(R.id.invitetv);
        ImageView close=dialog.findViewById(R.id.closeiv);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        groups = new ArrayList<>();
        new fetchGroup().execute();
        if (EventInviteActivity.str_number != null) {
            Log.e("tag", "122" + EventInviteActivity.str_number);
            tagsEditText.setText("");
            tagsEditText.setText(EventInviteActivity.str_number.trim());
        }

        sendinvite_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("tag", "122" + EventInviteActivity.eventId);
                String str_country = countryCodePicker.getSelectedCountryCodeWithPlus();
                try {
                    String number = tagsEditText.getText().toString().trim();

                    if (number.length() > 0) {
                        //new sms_invite(eventId, str_country).execute();

                     sendSMS();

                     /*   Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_APP_MESSAGING);
                        startActivity(intent);
*/

                    } else {
                        tagsEditText.setError("Enter Valid PhoneNumber");
                    }


                } catch (IndexOutOfBoundsException e) {

                }


            }
        });
        selectgroup_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
        invitetv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (groupId != null) {
                    new groupsms_invite(eventId).execute();
                } else {
                    Toast.makeText(getActivity(), "Select Group", Toast.LENGTH_LONG).show();
                }
            }
        });
        addcontactiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), AddContactsInvite.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

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
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }

    public class sms_invite extends AsyncTask<String, Void, String> {
        String eventid, code;

        public sms_invite(String eventid, String code) {
            this.eventid = eventid;
            this.code = code;

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

                Log.e("tag", "111111111------->>" + eventid);

                String str_number = tagsEditText.getText().toString().trim();
                str_number = str_number.replace(" ", "");
                jsonObject.accumulate("phone", code + str_number);
                jsonObject.accumulate("eventid", eventid);
                if (!msg_et.getText().toString().trim().equalsIgnoreCase(""))
                {
                    jsonObject.accumulate("mesage", msg_et.getText().toString().trim());
                }
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

                            if (feedstatus.equals("myevents")) {
                                Intent intent = new Intent(getActivity(), MyEventFeedsActivity.class);
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                getActivity().finish();
                            } else {
                                Intent intent = new Intent(getActivity(), EventFeedDashboard.class);
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                getActivity().finish();
                            }


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
    public class groupsms_invite extends AsyncTask<String, Void, String> {
        String eventid;

        public groupsms_invite(String eventid) {
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
                jsonObject.accumulate("groupid", groupId);
                jsonObject.accumulate("eventid", eventid);

                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1("http://104.197.80.225:3010/wow/event/groupsmsinvite", json, token);
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
                            dialog.dismiss();
                            if(feedstatus.equals("myevents"))
                            {
                                Intent intent = new Intent(getActivity(), MyEventFeedsActivity.class);
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                getActivity().finish();
                            }
                            else
                            {
                                Intent intent = new Intent(getActivity(), EventFeedDashboard.class);
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                getActivity().finish();
                            }

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
    public class fetchGroup extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //    loader_dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1("http://104.197.80.225:3010/wow/group/fetchgroups", json, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "tag" + s);
            // loader_dialog.dismiss();
            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("success");
                    if (status.equals("true")) {
                        try {
                            // av_loader.setVisibility(View.GONE);
                            JSONArray feedArray = jo.getJSONArray("groups");

                            for (int i = 0; i < feedArray.length(); i++) {
                                JSONObject feedObj = (JSONObject) feedArray.get(i);
                                Group item = new Group();
                                item.setId(feedObj.getString("_id"));
                                item.setGroupname(feedObj.getString("groupname"));
                                JSONArray users = feedObj.getJSONArray("users");
                                item.setGroupcount("" + users.length());

                                JSONObject adminobj = feedObj.getJSONObject("adminid");
                                {
                                    item.setFirstname(adminobj.getString("firstname"));
                                }

                                // item.setAdminid(feedObj.getJSONObject(""));

                              /*  JSONArray users = feedObj.getJSONArray("users");

                                //  Log.e("tag", "val11111-----" + users.toString());

                                for (int j = 0; j < users.length(); i++) {
                                    Userid userid = new Userid();
                                    JSONObject userobj = (JSONObject) users.get(j);
                                    JSONObject user = userobj.getJSONObject("userid");
                                    //   Log.e("tag", "val11111-----" + user);
                                    userid.setId(user.getString("_id"));
                                    userid.setFirstname(user.getString("firstname"));
                                    userid.setLastname(user.getString("lastname"));
                                    userid.setWowtagid(user.getString("wowtagid")
                                     }*/


                                groups.add(item);

                                Log.e("tag", "basda-----" + groups.toString());
                            }

                            groupAdapter adapter = new groupAdapter(getActivity(), groups);
                            listview.setAdapter(adapter);
                            listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                }
            } else {

            }

        }

    }
    class groupAdapter extends BaseAdapter {
        SharedPreferences.Editor editor;
        String token, userId;
        Dialog dialog;
        int selectedIndex = -1;
        private Activity activity;
        private LayoutInflater inflater;
        private List<Group> feedItems;

        public groupAdapter(Activity activity, List<Group> feedItems) {
            this.activity = activity;
            this.feedItems = feedItems;
        }

        @Override
        public int getCount() {
            return feedItems.size();
        }

        @Override
        public Object getItem(int location) {
            return feedItems.get(location);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final groupAdapter.ViewHolder viewHolder;

            if (inflater == null)
                inflater = (LayoutInflater) activity
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.dialog_viewgroup, null);
                viewHolder = new groupAdapter.ViewHolder();
                viewHolder.position = position;
                FontsOverride.overrideFonts(activity, convertView);
                viewHolder.name = (CheckBox) convertView.findViewById(R.id.text1);
                viewHolder.memberscount = (TextView) convertView.findViewById(R.id.members_tv);
                viewHolder.createdname_tv = convertView.findViewById(R.id.createdname_tv);

                convertView.setTag(viewHolder);


            } else {
                viewHolder = (groupAdapter.ViewHolder) convertView.getTag();
            }

            Group item = groups.get(position);
            viewHolder.name.setText(item.getGroupname());
            Log.e("tag", "Gropupname" + item.getGroupname());
            viewHolder.memberscount.setText(item.getGroupcount() + " Members");

            viewHolder.createdname_tv.setText("Created by " + item.getFirstname());
            viewHolder.name.setChecked(false);

            if (position == selectedIndex) {
                viewHolder.name.setChecked(true);
            } else {
                viewHolder.name.setChecked(false);
            }

            viewHolder.name.setOnClickListener(onStateChangedListener(viewHolder.name, position));


            return convertView;
        }

        private View.OnClickListener onStateChangedListener(final CheckBox checkBox, final int position) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {
                        selectedIndex = position;
                        Group item = groups.get(position);
                        groupId = item.getId();
                    } else {
                        selectedIndex = -1;
                    }
                    notifyDataSetChanged();
                }
            };
        }

        class ViewHolder {
            TextView memberscount, createdname_tv;
            int position;
            CheckBox name;


        }


    }

    private void sendSMS() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // At least KitKat
        {
            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(getActivity()); // Need to change the build to API 19

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, "text");
            if (defaultSmsPackageName != null)
            {
                sendIntent.setPackage(defaultSmsPackageName);
            }
            startActivity(sendIntent);

        }
        else // For early versions, do what worked for you before.
        {
            Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("address","+919791095238");
            smsIntent.putExtra("sms_body","message");
            startActivity(smsIntent);
        }
    }
}
