package vineture.wowhubb.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.R;
import vineture.wowhubb.todayevents.Message;

import java.util.List;
import java.util.StringTokenizer;


public class TodayEventsAdapter extends BaseAdapter {


    SharedPreferences.Editor editor;
    String token, userId;
    Dialog dialog;
    Context context;
    private Activity activity;
    private LayoutInflater inflater;
    private List<Message> feedItems;

    public TodayEventsAdapter(List<Message> feedItems, Activity activity) {

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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.dialog_list_todayevents, null);
            viewHolder = new ViewHolder();
            viewHolder.position = position;
            FontsOverride.overrideFonts(activity, convertView);
            viewHolder.eventname = (TextView) convertView.findViewById(R.id.eventtitle_tv);
            viewHolder.time = (TextView) convertView.findViewById(R.id.runttime_tv);
            viewHolder.profilePic = (ImageView) convertView.findViewById(R.id.imageview_profile);

            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Message item = feedItems.get(position);

        viewHolder.position = position;
        if (feedItems.get(position).getEventtitle() != null) {
            viewHolder.eventname.setText("!" + item.getEventtitle());
        }

        if (feedItems.get(position).getEventstartdate() != null) {

            //2018/05/11 12:03PM
            String datetime = item.getEventstartdate();
            StringTokenizer tk = new StringTokenizer(datetime);
            Log.e("tag", "COUNTTTTTTT-------" + tk.countTokens());
            if (tk.countTokens() > 2) {
                String date = tk.nextToken();  // <---  yyyy-mm-dd
                String time = tk.nextToken();  // <---  hh:mm:ss


                String time1 = tk.nextToken();
                time = time + time1;
                viewHolder.time.setText(time);
            } else {
                String date = tk.nextToken();  // <---  yyyy-mm-dd
                String time = tk.nextToken();  // <---  hh:mm:ss
                viewHolder.time.setText(time);
            }


        }


        if (feedItems.get(position).getUserid().getPersonalimage() != null) {

            // Log.e("tag", "Imagge-------" + item.getImge())

            Glide.with(activity).load("http://104.197.80.225:3010/wow/media/personal/" + feedItems.get(position).getUserid().getPersonalimage()).into(viewHolder.profilePic);
        } else {
            viewHolder.profilePic.setImageResource(R.drawable.profile_img);
        }


        return convertView;
    }

    static class ViewHolder {
        TextView eventname, time;
        int position;
        ImageView profilePic;

    }


}
