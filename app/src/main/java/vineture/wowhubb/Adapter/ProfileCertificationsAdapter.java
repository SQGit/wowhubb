package vineture.wowhubb.Adapter;

/**
 * Created by Salman on 11-06-2018.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import vineture.wowhubb.R;
import vineture.wowhubb.app.ProfileModel;

public class ProfileCertificationsAdapter extends BaseAdapter {

    Context context;
    ArrayList<ProfileModel> modelList;
    ArrayList<String> skillList;

    public ProfileCertificationsAdapter(Context context, ArrayList<ProfileModel> modelList, ArrayList<String> skillList) {
        this.context = context;
        this.modelList = modelList;
        this.skillList = skillList;
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = null;

        if (convertView == null) {

            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.professionalcertification_itemlist, null);

            TextView certificate_tv = (TextView) convertView.findViewById(R.id.certificate_tv);
            TextView years_tv = (TextView) convertView.findViewById(R.id.years_tv);

            ImageView rm_btn = (ImageView) convertView.findViewById(R.id.removeiv);

            ProfileModel m = modelList.get(position);
            certificate_tv.setText(m.getCertication());
            years_tv.setText(m.getCertificationYear());

            // click listiner for remove button
            rm_btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    modelList.remove(position);
                    //skillList.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
        return convertView;
    }
}
