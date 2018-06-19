package vineture.wowhubb.wowtag.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.R;
import vineture.wowhubb.Utils.Config;
import vineture.wowhubb.Utils.HttpUtils;
import vineture.wowhubb.wowtag.Activity.PaticularWowtagActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WowFragment extends Fragment {


    AutoCompleteTextView autoCompleteTextView;
    String token,selected_id,selected_name;
    SharedPreferences.Editor editor;
    Typeface segoeui;
    TextView search;
    TextInputLayout til_spinwowtag;
    Map<String,String> wowtag_hash=new HashMap<>();
    Set<String> keys;
    ArrayList<String> list=new ArrayList<>();
    ArrayAdapter<String> adapter;
    TextView txt1,txt2,txt3;


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



        //set font:
        segoeui = Typeface.createFromAsset(getActivity().getAssets(), "fonts/segoeui.ttf");
        FontsOverride.overrideFonts(getActivity(), view);

        //find Add Cast View:
        search=view.findViewById(R.id.search);
        autoCompleteTextView=view.findViewById(R.id.autoCompleteTextView);
        txt1=view.findViewById(R.id.txt_enter);
        txt2=view.findViewById(R.id.txt_code);
        txt3=view.findViewById(R.id.txt3);

        txt1.setTypeface(segoeui);
        txt2.setTypeface(segoeui);
        txt3.setTypeface(segoeui);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");


        //Call My Own Wowtag Id:

        try {
            new callAllWowtagIdAsync().execute();
        }
        catch (NullPointerException e)
        {

        }




        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected_name = autoCompleteTextView.getText().toString();
                selected_id=wowtag_hash.get(selected_name);
            }
        });


        autoCompleteTextView.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
            @Override
            public void onDismiss() {
                InputMethodManager in = (InputMethodManager)getActivity(). getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(getActivity().getCurrentFocus().getApplicationWindowToken(), 0);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent nextpage=new Intent(getActivity(),PaticularWowtagActivity.class);
                    nextpage.putExtra("wowtag_id",selected_id);
                    nextpage.putExtra("wowtag_name",selected_name);
                    startActivity(nextpage);
            }
        });
        return view;
    }


    //Describe Own WOWTAG NAMES and loading into AutoComplete TextView:
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
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL_PARTICULARLIST, json,token);
            } catch (Exception e) {

            }
            return null;
        }


        @SuppressLint("ResourceAsColor")
        @Override
        protected void onPostExecute(String jsonstr) {
            super.onPostExecute(jsonstr);
            if (jsonstr.equals("")) {
                Toast.makeText(getActivity(),"Check Network Connection",Toast.LENGTH_LONG).show();
            } else {

                try {

                    JSONObject jo = new JSONObject(jsonstr);
                    String status = jo.getString("success");

                    if(status.equals("true")) {
                        JSONArray event_service = jo.getJSONArray("message");
                        if (event_service!=null) {
                            wowtag_hash.clear();
                            for (int i1 = 0; i1 < event_service.length(); i1++) {
                                JSONObject jsonObject = event_service.getJSONObject(i1);
                                wowtag_hash.put(jsonObject.getString("eventtitle"),jsonObject.getString("_id"));
                            }


                            keys=wowtag_hash.keySet();
                            list = new ArrayList<>();
                            list.addAll(keys);


                            adapter = new ArrayAdapter<String>(getActivity(), R.layout.samp_text,R.id.txt_auto,list);
                            autoCompleteTextView.setThreshold(1);//will start working from first character
                            autoCompleteTextView.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
                            autoCompleteTextView.setTextColor(getResources().getColor(R.color.textcolr));
                            autoCompleteTextView.setHintTextColor(getResources().getColor(R.color.white));
                            autoCompleteTextView.setDropDownBackgroundResource(R.color.white);

                        } else {
                            Log.e("tag","No Wowtag Id Found..");
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
