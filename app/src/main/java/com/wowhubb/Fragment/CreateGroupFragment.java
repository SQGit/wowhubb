package com.wowhubb.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.Groups.Group;
import com.wowhubb.R;
import com.wowhubb.Utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CreateGroupFragment extends Fragment {
    public static MaterialBetterSpinner privacy_spn;
    String[] PRIVACYLIST = {"Public Group", "Closed Group", "Secret Group"};
    Typeface lato;
    SharedPreferences.Editor editor;
    String token, userId;
    TextInputLayout groupname_til, people_til, privacy_til;
    List<ContactChip> contactList;
    TextView creategroup_tv;
    Dialog loader_dialog;
    ListView listView;
    groupAdapter adapter;
    View view;
    private List<Group> groups;

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.creategroupfragmentlist, container, false);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");
        userId = sharedPreferences.getString("userid", "");
        contactList = new ArrayList<>();
        groups = new ArrayList<>();
        Log.e("tag", "iddddddddd----------->" + userId);
        listView = (ListView) view.findViewById(R.id.listView);

        loader_dialog = new Dialog(getActivity());
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(R.layout.test_loader);
        new fetchGroup().execute();

       /* Log.e("tag", "Groups---->>>>"+groups.size());
        if(groups.size()>0)
        {
             view = inflater.inflate(R.layout.creategroupfragmentlist, container, false);
            listView = (ListView) view.findViewById(R.id.listView);
              adapter = new groupAdapter(getActivity(), groups);
             listView.setAdapter(adapter);
        }
        else
        {
            view = inflater.inflate(R.layout.creategroup_empty, container, false);
            creategroup_tv=view.findViewById(R.id.creategroup_tv);

            creategroup_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
*/
        return view;
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
                                ///  item.setAdminid(feedObj.getString("adminid"));
                                item.setGroupname(feedObj.getString("groupname"));
                                JSONArray users = feedObj.getJSONArray("users");
                                item.setGroupcount("" + users.length());
                                JSONObject adminobj = feedObj.getJSONObject("adminid");
                                {
                                    item.setFirstname(adminobj.getString("firstname"));
                                }

/*

                               JSONArray users = feedObj.getJSONArray("users
                                Log.e("tag", "val11111-----" + users.toString());
                                   Userid userid = new Userid();
                               JSONObject userobj = (JSONObject) users.get(i);
                                    JSONObject user = userobj.getJSONObject("userid");
                                     Log.e("tag", "val1111122222-----" + user);
                                    userid.setId(user.getString("_id"));
                                    userid.setFirstname(user.getString("firstname"));
                                    userid.setLastname(user.getString("lastname"));
                                    userid.setWowtagid(user.getString("wowtagid"));

*/


                                groups.add(item);

                                Log.e("tag", "basda-----" + groups.toString());
                            }
                            adapter = new groupAdapter(getActivity(), groups);
                            listView.setAdapter(adapter);

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
        String token;
        Dialog dialog;
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
                convertView = inflater.inflate(R.layout.dialog_wowhubb_network, null);
                viewHolder = new ViewHolder();
                viewHolder.position = position;
                FontsOverride.overrideFonts(activity, convertView);
                viewHolder.name = (CheckBox) convertView.findViewById(R.id.text1);
                viewHolder.memberscount = (TextView) convertView.findViewById(R.id.members_tv);
                viewHolder.createdname = (TextView) convertView.findViewById(R.id.createdname_tv);
                viewHolder.deleteiv = convertView.findViewById(R.id.delete_iv);
                viewHolder.editiv = convertView.findViewById(R.id.editiv);

                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Group item = groups.get(position);


            viewHolder.name.setText(item.getGroupname());

            viewHolder.memberscount.setText(item.getGroupcount() + " Members");
            viewHolder.createdname.setText("Created by " + item.getFirstname());
            //   final Doc doc = docs.get(position);


            Log.e("tag", "UID------>" + userId);
            Log.e("tag", "userid1222222332------>" + item.getId());

          /* // String s = String.valueOf(item.getAdminid().getId());
            if (userId==item.getAdminid().getId()) {
                Log.e("tag", "truee------>" + item.getAdminid());
                viewHolder.deleteiv.setVisibility(View.VISIBLE);
            } else {
                viewHolder.deleteiv.setVisibility(View.GONE);
                Log.e("tag", "truee------>" + item.getAdminid());
            }*/


            viewHolder.deleteiv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        Group item = groups.get(position);
                        Log.e("tag", "Gropupname" + position);
                        String groupId = item.getId();
                        Log.e("tag", "Gropupname" + item.getId());
                        new DeleteGroups(groupId, position).execute();
                    } catch (ArrayIndexOutOfBoundsException e) {

                    } catch (IndexOutOfBoundsException e) {

                    }
                }
            });


            return convertView;
        }


        class ViewHolder {
            TextView createdname, memberscount;
            int position;
            ImageView deleteiv, editiv;
            CheckBox name;
        }

    }


    public class DeleteGroups extends AsyncTask<String, Void, String> {
        String groupId;
        int pos;

        public DeleteGroups(String groupId, int pos) {
            this.groupId = groupId;
            this.pos = pos;
            Log.e("tag", "groupId1111111-------->" + groupId);
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
                Log.e("tag", "groupId-------->" + groupId);
                jsonObject.accumulate("groupid", groupId);
                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequest1("http://104.197.80.225:3010/wow/group/deletegroup", json, token);
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "RESSSSSSS-------->" + s.toString());
            loader_dialog.dismiss();

            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("success");
                    if (status.equals("true")) {
                        removegroups(pos);

                    } else {
                        String msg = jo.getString("message");
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {

                }

            } else {

            }

        }

        private void removegroups(int pos) {

            Log.e("tag", "11111111---pos id--------->>>" + pos);

            if (pos > -1) {
                Log.e("tag", "1111111133333333---pos id--------->>>" + pos);
                groups.remove(pos);
                Log.e("tag", "111111112222---pos id--------->>>" + pos);
                adapter.notifyDataSetChanged();

            }
        }


    }

}
