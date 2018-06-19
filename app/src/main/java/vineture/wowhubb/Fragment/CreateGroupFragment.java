package vineture.wowhubb.Fragment;

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
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import vineture.wowhubb.Activity.CreateGroup;
import vineture.wowhubb.Activity.UpdateGroupActivity;
import vineture.wowhubb.Activity.ViewGroupMembers;
import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.Groups.Adminid;
import vineture.wowhubb.Groups.Group;
import vineture.wowhubb.Groups.GroupData;
import vineture.wowhubb.Groups.User;
import vineture.wowhubb.Groups.Userid;
import vineture.wowhubb.R;
import vineture.wowhubb.Utils.ApiClient;
import vineture.wowhubb.Utils.ApiInterface;
import vineture.wowhubb.Utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


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
   // List<User> userslist;
    List<Adminid> adminidList;
    GroupData groupData;
    List<Group> groupList;
    ApiInterface apiInterface;
    private static Retrofit retrofit = null;
    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.creategroupfragmentlist, container, false);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");
        userId = sharedPreferences.getString("userid", "");
        groupData = new GroupData();
        contactList = new ArrayList<>();
        groupList = new ArrayList<>();
       // userslist = new ArrayList<>();
        adminidList = new ArrayList<>();
        Log.e("tag", "iddddddddd----------->" + userId);
        listView = (ListView) view.findViewById(R.id.listView);

        loader_dialog = new Dialog(getActivity());
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(R.layout.test_loader);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        connectAndGetApiData();

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
    public void connectAndGetApiData() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://104.197.80.225:3010/wow/group/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        ApiInterface movieApiService = retrofit.create(ApiInterface.class);

        Call<GroupData> call = movieApiService.getGroups("application/x-www-form-urlencoded", token);
        call.enqueue(new Callback<GroupData>() {
            @Override
            public void onResponse(Call<GroupData> call, Response<GroupData> response) {
                List<Group> groups = response.body().getGroups();
                Log.d("tag", "Number of movies received: " + groups.size());
                adapter = new groupAdapter(getActivity(), groups);
                listView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<GroupData> call, Throwable throwable) {
                Log.e("tag", throwable.toString());
            }
        });
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
                                User user = new User();
                                Adminid adminid = new Adminid();
                                item.setId(feedObj.getString("_id"));
                                item.setGroupname(feedObj.getString("groupname"));

                                JSONObject adminobj = feedObj.getJSONObject("adminid");
                                {
                                    adminid.setFirstname(adminobj.getString("firstname"));
                                    adminid.setId(adminobj.getString("_id"));
                                }

                                JSONArray users = feedObj.getJSONArray("users");
                                item.setGroupcount("" + users.length());

                                Log.e("tag", "userslength--------------------" + users.length());

                                for (int j = 0; j < users.length(); j++) {
                                    JSONObject feedJson = (JSONObject) users.get(j);
                                    JSONObject userobj = feedJson.getJSONObject("userid");
                                    {
                                        Userid userid = new Userid();
                                        userid.setId(userobj.getString("_id"));
                                        // Log.e("tag", "basda-----" + userobj.getString("firstname"));
                                        userid.setFirstname(userobj.getString("firstname"));
                                        userid.setWowtagid(userobj.getString("wowtagid"));
                                        ///  userid.setPersonalimage(userobj.getString("personalimage"));
                                        // Log.e("tag", "itemmmmmm11-----" + userobj.getString("wowtagid"));
                                        user.setUserid(userid);
                                     //   userslist.add(user);
                                      ///  item.setUsers(userslist);
                                    }

//                    Log.e("tag", "itemmmmmmlisttttt-----" + userslist.size());
                                }


                                item.setAdminid(adminid);
                                groupList.add(item);
                                groupData.setGroups(groupList);
                                Log.e("tag", "aaaaaaaaaa-----" + groupData);
                            }

                            // Log.e("tag", "uttttttttttttttt-----" + groupList);

                            adapter = new groupAdapter(getActivity(), groupList);
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
        public List<Group> feedItems;

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
                viewHolder.name = (TextView) convertView.findViewById(R.id.text1);
                viewHolder.memberscount = (TextView) convertView.findViewById(R.id.members_tv);
                viewHolder.createdname = (TextView) convertView.findViewById(R.id.createdname_tv);
                viewHolder.deleteiv = convertView.findViewById(R.id.delete_iv);
                viewHolder.editiv = convertView.findViewById(R.id.editiv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Group item = feedItems.get(position);
          //  List<Group> mains = groupData.getGroups();

           Adminid adminid=item.getAdminid();

            viewHolder.name.setText(item.getGroupname());
            viewHolder.createdname.setText("Created by "+adminid.getFirstname());
          viewHolder.memberscount.setText(item.getUsers().size() + " Members");


            if (userId.equals(item.getAdminid().getId())) {
                viewHolder.deleteiv.setVisibility(View.VISIBLE);
                viewHolder.editiv.setVisibility(View.VISIBLE);
            } else {
                viewHolder.deleteiv.setVisibility(View.GONE);
                viewHolder.editiv.setVisibility(View.GONE);
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Group item = feedItems.get(position);

                    Log.e("tag", "Gropupname" +item.getUsers());
                    Intent intent = new Intent(activity, ViewGroupMembers.class);
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = prefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(item.getUsers());
                    editor.putString("membersstatus", "group");
                    editor.putString("usergroups", json);
                    editor.apply();
                    getContext().startActivity(intent);

                }
            });

            viewHolder.editiv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Group item = feedItems.get(position);

                    Intent intent=new Intent(activity, UpdateGroupActivity.class);
                    intent.putExtra("groupid",item.getId());
                    intent.putExtra("groupname",item.getGroupname());
                    intent.putExtra("groupprivacy",item.getPrivacy());
                    startActivity(intent);

                   activity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                }
            });


            viewHolder.deleteiv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Group item = feedItems.get(position);
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
            TextView name;
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
                        startActivity(new Intent(getActivity(), CreateGroup.class));
                        getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
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
                groupList.remove(pos);
                Log.e("tag", "111111112222---pos id--------->>>" + pos);

                adapter.notifyDataSetChanged();
            }
        }


    }

}
