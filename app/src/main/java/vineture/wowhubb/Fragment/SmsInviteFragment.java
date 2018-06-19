package vineture.wowhubb.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
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
import vineture.wowhubb.Activity.AddContactsInvite;
import vineture.wowhubb.Activity.EventFeedDashboard;
import vineture.wowhubb.Activity.EventInviteActivity;
import vineture.wowhubb.Activity.MyEventFeedsActivity;
import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.Groups.Group;
import vineture.wowhubb.Utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import mabbas007.tagsedittext.TagsEditText;

import static android.Manifest.permission.SEND_SMS;


public class SmsInviteFragment extends Fragment implements TagsEditText.TagsEditListener, View.OnClickListener {
    private static final int REQUEST_SMS = 0;
    TagsEditText tagsEditText;
    String collectionval, token, eventId, eventName,feedstatus, groupId,fname,lname;
    TextInputLayout til_tags;
    Typeface lato;
    Dialog loader_dialog, dialog;
    TextView sendinvite_tv, invitetv;
    CountryCodePicker countryCodePicker;
    ImageView addcontactiv;
    SharedPreferences sharedPrefces;
    SharedPreferences.Editor edit;
    TextView selectgroup_tv;
    ListView listview;
    EditText msg_et;
    private List<Group> groups;
    private BroadcastReceiver sentStatusReceiver, deliveredStatusReceiver;

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(vineture.wowhubb.R.layout.smsinvitefragment, container, false);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        token = sharedPreferences.getString("token", "");
        eventId = sharedPreferences.getString("eventId", "");
        eventName = sharedPreferences.getString("eventName", "");
        feedstatus = sharedPreferences.getString("feedstatus", "");
        fname=sharedPreferences.getString("str_name","");
        lname=sharedPreferences.getString("str_lname","");


        Log.e("tag", "12sdssddsds2-------------->>>" + eventId);
        View v1 = getActivity().getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(getActivity(), v1);
        lato = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato.ttf");
        selectgroup_tv = view.findViewById(vineture.wowhubb.R.id.selectgroup_tv);
        tagsEditText = view.findViewById(vineture.wowhubb.R.id.tagsEditText);
        til_tags = view.findViewById(vineture.wowhubb.R.id.til_tags);
        sendinvite_tv = view.findViewById(vineture.wowhubb.R.id.sendinvite_tv);
        countryCodePicker = view.findViewById(vineture.wowhubb.R.id.ccp);
        addcontactiv = view.findViewById(vineture.wowhubb.R.id.addcontact_iv);
        msg_et = view.findViewById(vineture.wowhubb.R.id.msg_et);
        tagsEditText.setTagsListener(this);
        selectgroup_tv.setTypeface(lato);
        sendinvite_tv.setTypeface(lato);
        til_tags.setTypeface(lato);
        tagsEditText.setTypeface(lato);
        loader_dialog = new Dialog(getActivity());
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(vineture.wowhubb.R.layout.test_loader);

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(vineture.wowhubb.R.layout.dialog_wowhubb_invitelist);
        View v = dialog.getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(getActivity(), v);
        listview = (ListView) dialog.findViewById(vineture.wowhubb.R.id.listView);
        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        invitetv = dialog.findViewById(vineture.wowhubb.R.id.invitetv);
        ImageView close = dialog.findViewById(vineture.wowhubb.R.id.closeiv);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        groups = new ArrayList<>();
        new fetchGroup().execute();
        if (EventInviteActivity.str_number != null)
        {
            Log.e("tag", "122" + EventInviteActivity.str_number);
            tagsEditText.setText("");
            tagsEditText.setText(EventInviteActivity.str_number.trim());
        }

        sendinvite_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String str_country = countryCodePicker.getSelectedCountryCodeWithPlus();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    int hasSMSPermission = getActivity().checkSelfPermission(Manifest.permission.SEND_SMS);
                    if (hasSMSPermission != PackageManager.PERMISSION_GRANTED) {
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)) {
                            showMessageOKCancel("You need to allow access to Send SMS",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(new String[]{Manifest.permission.SEND_SMS},
                                                        REQUEST_SMS);
                                            }
                                        }
                                    });
                            return;
                        }
                        requestPermissions(new String[]{Manifest.permission.SEND_SMS},
                                REQUEST_SMS);
                        return;
                    }
                    Log.e("tag", "smseditvalue-------->>" + str_country + tagsEditText.getText().toString().trim());
                    sendMySMS();
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
                SharedPreferences sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor edit = sharedPrefces.edit();
                if(msg_et.getText().toString()!=null)
                {
                    edit.putString("eventMessage", msg_et.getText().toString());
                    edit.commit();

                }
                getActivity().overridePendingTransition(vineture.wowhubb.R.anim.left_to_right, vineture.wowhubb.R.anim.right_to_left);

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

    public void sendMySMS() {
        String str_country = countryCodePicker.getSelectedCountryCodeWithPlus();
        String phone = str_country + tagsEditText.getText().toString().trim();
        String msg1=msg_et.getText().toString();
        String message = fname+" "+lname+" has invited you for " +eventName +" " + msg1+", "+
                " Click on this link to RSVP "+"http://104.197.80.225:8080/wowhubb/event/getparticularevent/"+eventId;

        //Check if the phoneNumber is empty
        if (!phone.isEmpty()) {


                SmsManager sms = SmsManager.getDefault();
                // if message length is too long messages are divided
                ArrayList<String> messages = sms.divideMessage(message);
            sms.sendMultipartTextMessage(phone, null, messages, null, null);

            /*for (String msg : messages)
                {

                    sms.sendMultipartTextMessage(phone, null, messages, null, null);
                   *//* PendingIntent sentIntent = PendingIntent.getBroadcast(getActivity(), 0, new Intent("SMS_SENT"), 0);
                    PendingIntent deliveredIntent = PendingIntent.getBroadcast(getActivity(), 0, new Intent("SMS_DELIVERED"), 0);
                    sms.sendTextMessage(phone, null, msg, sentIntent, deliveredIntent);*//*

                }*/

        } else {
            Toast.makeText(getActivity(), "Please Enter a Valid Phone Number", Toast.LENGTH_SHORT).show();

        }
    }

    private boolean checkPermission() {
        return (ActivityCompat.checkSelfPermission(getActivity(), SEND_SMS) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{SEND_SMS}, REQUEST_SMS);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_SMS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Permission Granted, Now you can access sms", Toast.LENGTH_SHORT).show();

                    sendMySMS();

                } else {
                    Toast.makeText(getActivity(), "Permission Denied, You cannot access and sms", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(SEND_SMS)) {
                            showMessageOKCancel("You need to allow access to both the permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(new String[]{SEND_SMS},
                                                        REQUEST_SMS);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public void onResume() {
        super.onResume();
        sentStatusReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent arg1) {
                String s = "Unknown Error";
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        s = "Message Sent Successfully !!";
                        tagsEditText.setText("");
                        msg_et.setText("");
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        s = "Generic Failure Error";
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        s = "Error : No Service Available";
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        s = "Error : Null PDU";
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        s = "Error : Radio is off";
                        break;
                    default:
                        break;
                }
                Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();

            }
        };
        deliveredStatusReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent arg1) {
                String s = "Message Not Delivered";
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        s = "Message Delivered Successfully";
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
              //  Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
                tagsEditText.setText("");
                msg_et.setText("");
            }
        };
        getActivity().registerReceiver(sentStatusReceiver, new IntentFilter("SMS_SENT"));
        getActivity().registerReceiver(deliveredStatusReceiver, new IntentFilter("SMS_DELIVERED"));
    }

    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(sentStatusReceiver);
        getActivity().unregisterReceiver(deliveredStatusReceiver);
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
                if (!msg_et.getText().toString().trim().equalsIgnoreCase("")) {
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
                                getActivity().overridePendingTransition(vineture.wowhubb.R.anim.left_to_right, vineture.wowhubb.R.anim.right_to_left);
                                getActivity().finish();
                            } else {
                                Intent intent = new Intent(getActivity(), EventFeedDashboard.class);
                                startActivity(intent);
                                getActivity().overridePendingTransition(vineture.wowhubb.R.anim.left_to_right, vineture.wowhubb.R.anim.right_to_left);
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
                            if (feedstatus.equals("myevents")) {
                                Intent intent = new Intent(getActivity(), MyEventFeedsActivity.class);
                                startActivity(intent);
                                getActivity().overridePendingTransition(vineture.wowhubb.R.anim.left_to_right, vineture.wowhubb.R.anim.right_to_left);
                                getActivity().finish();
                            } else {
                                Intent intent = new Intent(getActivity(), EventFeedDashboard.class);
                                startActivity(intent);
                                getActivity().overridePendingTransition(vineture.wowhubb.R.anim.left_to_right, vineture.wowhubb.R.anim.right_to_left);
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
                convertView = inflater.inflate(vineture.wowhubb.R.layout.dialog_viewgroup, null);
                viewHolder = new groupAdapter.ViewHolder();
                viewHolder.position = position;
                FontsOverride.overrideFonts(activity, convertView);
                viewHolder.name = (CheckBox) convertView.findViewById(vineture.wowhubb.R.id.text1);
                viewHolder.memberscount = (TextView) convertView.findViewById(vineture.wowhubb.R.id.members_tv);
                viewHolder.createdname_tv = convertView.findViewById(vineture.wowhubb.R.id.createdname_tv);

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

}
