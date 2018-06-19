package vineture.wowhubb.Activity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;import android.view.animation.AnimationUtils;

import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.R;
import vineture.wowhubb.wowtag.Activity.WowtagRsvp;
import java.util.ArrayList;
import java.util.HashMap;


public class LandingWowSearch extends Activity {
    LinearLayout img_back;
    ListView listview_wow;
    LandingWowSearch lws;
    EditText myFilter;
    Typeface segoeui;
    ArrayAdapter<String> dataAdapter = null;
    String token,selected_name,selected_id;
    SharedPreferences.Editor editor;
    public ArrayList<String> list = new ArrayList<>();
    HashMap<String, String> getwowtag_hash = new HashMap<>();
    Animation animation;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_wow_search);

        final View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(LandingWowSearch.this, v1);
        img_back=findViewById(R.id.img_back);
        listview_wow=findViewById(R.id.listview_wow);
        myFilter=findViewById(R.id.myFilter);

        segoeui = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/segoeui.ttf");

        //token get from Login Activity:
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");
        Intent intent = getIntent();
        list=intent.getStringArrayListExtra("list_value");
        Log.e("tag","zerox2->"+list);
        getwowtag_hash = (HashMap<String, String>)intent.getSerializableExtra("hashmap");
        Log.e("tag","zerox1->"+getwowtag_hash);


        animation = AnimationUtils.loadAnimation(this, R.anim.shake);
        img_back.startAnimation(animation);

        // Initialize an array adapter
        dataAdapter = new ArrayAdapter<String>(this, R.layout.wowtag_list_row,list){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Cast the list view each item as text view
                TextView item = (TextView) super.getView(position,convertView,parent);

                // Set the typeface/font for the current item
                item.setTypeface(segoeui);

                // Set the list view item's text color
                item.setTextColor(Color.parseColor("#3c3c3c"));

                // Change the item text size
                item.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);

                // return the view
                return item;
            }
        };

        listview_wow.setAdapter(dataAdapter);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent wow_back=new Intent(getApplicationContext(),LandingPageActivity.class);
                startActivity(wow_back);
                finish();
            }
        });


        listview_wow.setTextFilterEnabled(true);
        listview_wow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                selected_name=dataAdapter.getItem(i);//check this value is comin correct or not
                selected_id = getwowtag_hash.get(selected_name);
                Log.e("tag", "name-------->" + selected_name);
                Log.e("tag", "id--------->" + selected_id);

                Intent rsvp = new Intent(getApplicationContext(), WowtagRsvp.class);
                rsvp.putExtra("wowtag_id",selected_id);
                rsvp.putExtra("wowtag_name",selected_name);
                startActivity(rsvp);
                finish();
            }
        });


        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String get_wowtagid=s.toString();
                dataAdapter.getFilter().filter(s);
                dataAdapter.notifyDataSetChanged();
            }
        });

    }

        @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent wow_back=new Intent(getApplicationContext(),LandingPageActivity.class);
        startActivity(wow_back);
        finish();
    }



}
