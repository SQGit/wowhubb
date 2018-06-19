package vineture.wowhubb.Nearbyeventsmodule;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wang.avi.AVLoadingIndicatorView;
import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.Nearbyeventsmodule.Database_Helper.DatabaseHandler;
import vineture.wowhubb.R;

import java.util.ArrayList;
import java.util.List;


public class HighlightEventFragment extends Fragment {
    String token;
    RecyclerView recycler_eventhighlight;
    AVLoadingIndicatorView av_loader;
    private ArrayList<HighlightEventModelPojo> staticArrayListforHighlights=new ArrayList<>();
    HighlightEventAdapter nearByEventHighlightsAdapter;
    HorizontalScrollView imaqe_scroll;
    TextView guest_name1,guest_name2,guest_info1,guest_info2,txt_speaker_type1,txt_speaker_type2,guest_website1,guest_website2;
    LinearLayout lnr_item1,lnr_item2;
    ImageView img_guestprofile1,img_guestprofile2;


    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eventhighlights, container, false);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        token = sharedPreferences.getString("token", "");
        FontsOverride.overrideFonts(getActivity(), view);
        recycler_eventhighlight = (RecyclerView) view.findViewById(R.id.recycler_eventhighlight);
        guest_name1=view.findViewById(R.id.guest_name1);
        guest_info1=view.findViewById(R.id.guest_info1);
        lnr_item1=view.findViewById(R.id.lnr_item1);
        img_guestprofile1=view.findViewById(R.id.img_guestprofile1);
        txt_speaker_type1=view.findViewById(R.id.txt_speaker_type1);
        guest_website1=view.findViewById(R.id.guest_website1);


        lnr_item1.setVisibility(View.GONE);

        DatabaseHandler db = new DatabaseHandler(getActivity());

        List<HighlightEventModelPojo> list = db.getHighlightEventSchedule();
        for (int i = 0; i < list.size(); i++) {
            HighlightEventModelPojo dbHighlightModelPojo1 = list.get(i);
            String video1=dbHighlightModelPojo1.guestVideo2;

            if(video1!=null)
            {
                lnr_item1.setVisibility(View.VISIBLE);
                guest_name1.setText(dbHighlightModelPojo1.guestName1);
                guest_info1.setText("             "+dbHighlightModelPojo1.guestInfo1);
                String guest_profile1="http://104.197.80.225:3010/wow/media/event/"+dbHighlightModelPojo1.guestProfile1;
                Log.e("tag","view 6 "+guest_profile1);
                txt_speaker_type1.setText(dbHighlightModelPojo1.guestType1);

                Glide.with(getActivity()).load(guest_profile1)
                        .into(img_guestprofile1);


                SpannableString content = new SpannableString(dbHighlightModelPojo1.guestSite1);
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                guest_website1.setText(content);
            }

            String video2=dbHighlightModelPojo1.guestVideo1;
           /* if(video2 !=null)
            {
                guest_name2.setText(dbHighlightModelPojo1.guestName2);
                //guest_info2.setText("             "+dbHighlightModelPojo1.guestInfo2);

                String guest_profile2="http://104.197.80.225:3010/wow/media/event/"+dbHighlightModelPojo1.guestProfile2;
                Log.e("tag","view 6 "+guest_profile2);


                Glide.with(getActivity()).load(guest_profile2)
                        .into(img_guestprofile2);
                txt_speaker_type2.setText(dbHighlightModelPojo1.guestType2);


                SpannableString content = new SpannableString(dbHighlightModelPojo1.guestSite2);
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                guest_website2.setText(content);
            }*/
        }

        return view;
    }

    /**
     * Parsing json reponse and passing the data to feed view list adapter
     */






    private class callEventHighlights extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            av_loader.setVisibility(View.VISIBLE);
        }


        @Override
        protected String doInBackground(String... strings) {
            Log.e("tag","DO IN BACKGROUND");
            return null;
        }


        @Override
        protected void onPostExecute(String jsonstr) {
            av_loader.setVisibility(View.GONE);
            super.onPostExecute(jsonstr);
            Log.e("tag","POST EXECUTE"+staticArrayListforHighlights.size());



            for (int i1 = 0; i1 < staticArrayListforHighlights.size(); i1++) {


                nearByEventHighlightsAdapter = new HighlightEventAdapter(getActivity(), staticArrayListforHighlights);
                recycler_eventhighlight.setAdapter(nearByEventHighlightsAdapter);
                nearByEventHighlightsAdapter.notifyDataSetChanged();
                recycler_eventhighlight.setLayoutManager(new LinearLayoutManager(getActivity()));

            }

        }

    }

}
