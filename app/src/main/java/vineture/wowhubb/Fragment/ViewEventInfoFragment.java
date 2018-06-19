package vineture.wowhubb.Fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import vineture.wowhubb.Activity.ViewMoreDetailspage;
import vineture.wowhubb.Fonts.FontsOverride;


public class ViewEventInfoFragment extends Fragment {
    String token;
    RecyclerView recyclerView;
    ImageView img_share;
    TextView txt_contactorganizer, txt_interested, txt_attending;

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(vineture.wowhubb.R.layout.fragment_vieweventsinfo, container, false);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        token = sharedPreferences.getString("token", "");
        FontsOverride.overrideFonts(getActivity(), view);
        TextView timestamp = (TextView) view.findViewById(vineture.wowhubb.R.id.timestamp);
        TextView desc = (TextView) view.findViewById(vineture.wowhubb.R.id.desc_tv);
        TextView datetv = (TextView) view.findViewById(vineture.wowhubb.R.id.datetv);
        TextView timetv = (TextView) view.findViewById(vineture.wowhubb.R.id.timetv);
        TextView ticketurltv = (TextView) view.findViewById(vineture.wowhubb.R.id.ticketurl_tv);
        TextView donatiionurltv = (TextView) view.findViewById(vineture.wowhubb.R.id.donationurl_tv);
        TextView addresstv = (TextView) view.findViewById(vineture.wowhubb.R.id.address_tv);
        TextView ticketrurl_tv = (TextView) view.findViewById(vineture.wowhubb.R.id.ticketurl_desc_tv);
        TextView donationurl_tv = (TextView) view.findViewById(vineture.wowhubb.R.id.donationturl_desc_tv);

        if (ViewMoreDetailspage.description != null && !ViewMoreDetailspage.description.equals("null")) {
            desc.setText(ViewMoreDetailspage.description);
        }

        if (ViewMoreDetailspage.eventdatedetails != null && !ViewMoreDetailspage.eventdatedetails.equals("null")) {
            datetv.setText(ViewMoreDetailspage.eventdatedetails);
        }

        if (ViewMoreDetailspage.eventtimedetails != null && !ViewMoreDetailspage.eventtimedetails.equals("null")) {
            timetv.setText(ViewMoreDetailspage.eventtimedetails);
        }

        if (ViewMoreDetailspage.eventvenueaddress != null && !ViewMoreDetailspage.eventvenueaddress.equals("null")) {
            addresstv.setText(ViewMoreDetailspage.eventvenueaddress);
        }

        if (ViewMoreDetailspage.eventtype != null && !ViewMoreDetailspage.eventtype.equals("null")) {

            if (ViewMoreDetailspage.eventtype.equals("personal_event"))
            {
                if (ViewMoreDetailspage.donationurl != null && !ViewMoreDetailspage.donationurl.equals(""))
                {
                    donationurl_tv.setVisibility(View.VISIBLE);
                    donatiionurltv.setVisibility(View.VISIBLE);
                    donationurl_tv.setText(ViewMoreDetailspage.donationurl);
                }
                else
                {
                    donationurl_tv.setVisibility(View.GONE);
                    donatiionurltv.setVisibility(View.GONE);
                }

                if (ViewMoreDetailspage.gifturl != null && !ViewMoreDetailspage.gifturl.equals(""))
                {
                    ticketrurl_tv.setVisibility(View.VISIBLE);
                    ticketurltv.setVisibility(View.VISIBLE);
                    ticketrurl_tv.setText(ViewMoreDetailspage.gifturl);
                }
                else
                {
                    ticketrurl_tv.setVisibility(View.GONE);
                    ticketurltv.setVisibility(View.GONE);
                }
            }
            else
            {
                donationurl_tv.setVisibility(View.GONE);
                donatiionurltv.setVisibility(View.GONE);
                ticketrurl_tv.setVisibility(View.GONE);
                ticketurltv.setVisibility(View.GONE);
            }
        }
        return view;
    }


}
