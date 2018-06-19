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
import java.util.List;

import vineture.wowhubb.Profile.College;
import vineture.wowhubb.R;
import vineture.wowhubb.app.ProfileModel;

public class ProfileCollegeAdapter extends BaseAdapter {

    Context context;
    ArrayList<ProfileModel> modelList;
    ArrayList<String> skillList;
    List<College> collegeList;

    public ProfileCollegeAdapter(Context context, ArrayList<ProfileModel> modelList, ArrayList<String> skillList) {
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
            convertView = mInflater.inflate(R.layout.professionalcolleges_itemlist, null);

            TextView collegetv = (TextView) convertView.findViewById(R.id.collegetv);
            TextView yearstv = (TextView) convertView.findViewById(R.id.yearstv);
            ImageView rm_btn = (ImageView) convertView.findViewById(R.id.removeiv);

            ProfileModel m = modelList.get(position);
            collegetv.setText(m.getCollege());
            yearstv.setText(m.getFrom()+" - "+m.getTo());


            // click listiner for remove button

            rm_btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    modelList.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
        return convertView;
    }
}
