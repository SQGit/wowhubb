package com.wowhubb.wowtag.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import com.wowhubb.EventServiceProvider.EventServiceProviderModel;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.R;
import com.wowhubb.Utils.Config;
import com.wowhubb.Utils.HttpUtils;
import com.wowhubb.wowtag.Activity.PaticularWowtagActivity;
import com.wowhubb.wowtag.Adapter.CustomAdapter3;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WowFragment extends Fragment {


    MaterialBetterSpinner wowtagid_spn;
    String token,selected_id,selected_name;
    SharedPreferences.Editor editor;
    Typeface segoeui;
    TextView search;
    TextInputLayout til_spinwowtag;
    Map<String,String> wowtag_hash=new HashMap<>();
    Set<String> keys;
    ArrayList<String> list=new ArrayList<>();
    CustomAdapter3 arrayAdapter;


    public WowFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_wowtag,
                container, false);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");

        //set font:
        segoeui=Typeface.createFromAsset(getActivity().getAssets(),"fonts/segoeui.ttf");
        FontsOverride.overrideFonts(getActivity(), view);

        //find Add Cast View:
        til_spinwowtag=view.findViewById(R.id.til_spinwowtag);
        search=view.findViewById(R.id.search);
        wowtagid_spn = (MaterialBetterSpinner)view. findViewById(R.id.wowtagid_spn);
        wowtagid_spn.setTypeface(segoeui);
        til_spinwowtag.setTypeface(segoeui);

        //call WOWTAG NAMES and loading into Spinner:

        new callAllWowtagIdAsync().execute();


        wowtagid_spn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected_name = list.get(i);
                selected_id=wowtag_hash.get(selected_name);
            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("tag","check"+selected_name);

                if(selected_name!=null)
                {
                    Intent nextpage=new Intent(getActivity(),PaticularWowtagActivity.class);
                    nextpage.putExtra("wowtag_id",selected_id);
                    nextpage.putExtra("wowtag_name",selected_name);
                    startActivity(nextpage);
                }
                else
                {
                    Toast.makeText(getActivity(),"You didnt have any Wowtag videos till yet !! Please Create the Event..",Toast.LENGTH_LONG).show();
                }


            }
        });
        return view;
    }


    //Describe WOWTAG NAMES and loading into Spinner:
    private class callAllWowtagIdAsync extends AsyncTask<String,String,String>{

        protected void onPreExecute() {
            super.onPreExecute();
        }



        @Override
        protected String doInBackground(String... strings) {

            String json = "", jsonStr = "";
            try {
                JSONObject jsonObject = new JSONObject();

                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL_GET_WOWTAGLIST, json,token);
            } catch (Exception e) {

            }
            return null;
        }


        @Override
        protected void onPostExecute(String jsonstr) {
            super.onPostExecute(jsonstr);
            List<EventServiceProviderModel> data=new ArrayList<>();
            if (jsonstr.equals("")) {
                Toast.makeText(getActivity(),"Check Network Connection",Toast.LENGTH_LONG).show();
            } else {

                try {

                    JSONObject jo = new JSONObject(jsonstr);
                    String status = jo.getString("success");

                    if(status.equals("true")) {
                        JSONArray event_service = jo.getJSONArray("message");
                        if (event_service!=null) {
                            for (int i1 = 0; i1 < event_service.length(); i1++) {
                                wowtag_hash.clear();
                                HashMap<String, String> map = new HashMap<String, String>();
                                JSONObject jsonObject = event_service.getJSONObject(i1);
                                wowtag_hash.put(jsonObject.getString("eventtitle"),jsonObject.getString("_id"));
                            }


                            keys=wowtag_hash.keySet();
                            list = new ArrayList<>();
                            list.addAll(keys);

                            //loading into spinner:
                            arrayAdapter = new CustomAdapter3(getActivity(), android.R.layout.simple_dropdown_item_1line, list) {
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
                                    tv.setTypeface(segoeui);
                                    tv.setTextSize(14);
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
                                    tv.setTextSize(14);
                                    tv.setPadding(10, 20, 0, 20);
                                    tv.setTypeface(segoeui);
                                    if (position == 0) {
                                        tv.setTextColor(Color.BLACK);
                                    } else {
                                        tv.setTextColor(Color.BLACK);
                                    }
                                    return view;
                                }
                            };
                            arrayAdapter.notifyDataSetChanged();
                            wowtagid_spn.setAdapter(arrayAdapter);

                        } else {

                            Log.e("tag","No Wowtag Id Found..");
                            //Toast.makeText(getActivity(),"No Wowtag Id Found..",Toast.LENGTH_LONG).show();
                        }

                    }
                    else
                    {

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

}
