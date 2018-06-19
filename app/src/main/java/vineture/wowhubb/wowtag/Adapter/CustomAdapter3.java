package vineture.wowhubb.wowtag.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import vineture.wowhubb.R;

import java.util.ArrayList;

/**
 * Created by Guna on 05-03-2018.
 */

public class CustomAdapter3  extends ArrayAdapter<String> {
    private Context context;
    private int layout;


    public CustomAdapter3(FragmentActivity activity, int simple_dropdown_item_1line, ArrayList<String> keys) {
        super(activity, R.layout.spinner_item_list,keys);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View view = super.getView(position, convertView, parent);

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/opensans.ttf");

        TextView suggestion = (TextView) view.findViewById(R.id.text);

        return view;
    }


}