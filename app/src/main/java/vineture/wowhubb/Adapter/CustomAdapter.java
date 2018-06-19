package vineture.wowhubb.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.List;

/**
 * Created by Admin on 07-10-2016.
 */
public class CustomAdapter extends ArrayAdapter<String> {

    private Context context;
    private int layout;
    List<String> source;

    public CustomAdapter(Context context, int layout , String[] source) {
        super(context, vineture.wowhubb.R.layout.spinner_item_list,source);
        this.context = context;
        this.layout = layout;

    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View view = super.getView(position, convertView, parent);

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/opensans.ttf");

        TextView suggestion = (TextView) view.findViewById(vineture.wowhubb.R.id.text);

        return view;
    }


}
