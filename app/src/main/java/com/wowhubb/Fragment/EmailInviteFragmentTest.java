package com.wowhubb.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.doodle.android.chips.ChipsView;
import com.doodle.android.chips.model.Contact;
import com.doodle.android.chips.util.Common;
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
import java.util.List;


public class EmailInviteFragmentTest extends Fragment {

    String token, groupId, email;
    Typeface lato;

    TextView sendemail_tv, selectgroup_tv, invitetv;
    Dialog loader_dialog, dialog;
    ListView listview;
    private List<Group> groups;
    private RecyclerView mContacts;
    private ChipsView mChipsView;

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.emailinvitefragment, container, false);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        token = sharedPreferences.getString("token", "");
        View v1 = getActivity().getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(getActivity(), v1);
        lato = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato.ttf");
        mChipsView = (ChipsView) view.findViewById(R.id.cv_contacts);

        mChipsView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato.ttf"));
        sendemail_tv = view.findViewById(R.id.sendemail_tv);
        selectgroup_tv = view.findViewById(R.id.selectgroup_tv);

        groups = new ArrayList<>();

        loader_dialog = new Dialog(getActivity());
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(R.layout.test_loader);
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);

        dialog.setContentView(R.layout.dialog_wowhubb_invitelist);
        listview = (ListView) dialog.findViewById(R.id.listView);
        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        invitetv = dialog.findViewById(R.id.invitetv);
        new fetchGroup().execute();
        sendemail_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    email = mChipsView.getEditText().getText().toString();

                    Log.e("tag", "1111groupIDDDDD-------------------->" + EventInviteActivity.eventId);

                    if (mChipsView.getChips().size() > 0) {
                        new email_invite(EventInviteActivity.eventId).execute();

                    } else if (email != null && Common.isValidEmail(email.trim())) {
                        new email_invite(EventInviteActivity.eventId).execute();
                        Log.e("tag", "eeeeeeeeeee-------------------->");
                    } else {
                        mChipsView.getEditText().setError("Enter Vaild Email");
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
                    new groupemail_invite(EventInviteActivity.eventId).execute();
                } else {
                    Toast.makeText(getActivity(), "Select Group", Toast.LENGTH_LONG).show();
                }
            }
        });


        // change EditText config
        mChipsView.getEditText().setCursorVisible(true);
        mChipsView.setChipsValidator(new ChipsView.ChipValidator() {
            @Override
            public boolean isValid(Contact contact) {
                if (!Common.isValidEmail(contact.getDisplayName().trim())) {
                    return false;
                } else {

                }
                return true;
            }
        });

        mChipsView.setChipsListener(new ChipsView.ChipsListener() {
            @Override
            public void onChipAdded(ChipsView.Chip chip) {
                for (ChipsView.Chip chipItem : mChipsView.getChips()) {
                    Log.e("ChipList", "chip: " + chipItem.toString());
                }
            }

            @Override
            public void onChipDeleted(ChipsView.Chip chip) {

            }

            @Override
            public void onTextChanged(CharSequence text) {
                //mAdapter.filterItems(text);
            }

            @Override
            public boolean onInputNotRecognized(String text) {

                try {
                    if (text.length() > 0 && Common.isValidEmail(text.trim())) {


                    } else {
                        View view = getActivity().getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        Toast.makeText(getActivity(), "Enter Valid Email", Toast.LENGTH_LONG).show();
                        mChipsView.getEditText().setError("Enter Vaild Email");
                    }
                } catch (ClassCastException e) {
                    Log.e("CHIPS", "Error ClassCast", e);
                }
                return false;
            }
        });


        return view;
    }


    public class email_invite extends AsyncTask<String, Void, String> {
        String eventid;

        public email_invite(String eventid) {
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

                Log.e("tag", "Chips-------------->" + mChipsView.getChips().size());
                if (mChipsView.getChips().size() > 0) {
                    for (int i = 0; i < mChipsView.getChips().size(); i++)
                    {
                        jsonObject.accumulate("email", mChipsView.getChips().get(i).getContact().getEmailAddress());
                    }
                } else {
                    Log.e("tag", "Emaillll-------------->" + email);
                    jsonObject.accumulate("email", email);
                }

                jsonObject.accumulate("eventid", eventid);

                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1("http://104.197.80.225:3010/wow/event/emailinvite", json, token);
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

    public class fetchGroup extends AsyncTask<String, Void, String> {
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
            loader_dialog.dismiss();
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


    public class groupemail_invite extends AsyncTask<String, Void, String> {
        String eventid;

        public groupemail_invite(String eventid) {
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
                return jsonStr = HttpUtils.makeRequest1("http://104.197.80.225:3010/wow/event/groupemailinvite", json, token);
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

}
