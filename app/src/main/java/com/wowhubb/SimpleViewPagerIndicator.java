package com.wowhubb;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class SimpleViewPagerIndicator extends LinearLayout implements
        OnPageChangeListener {

    private Context context;
    private ViewPager pager;
    private OnPageChangeListener onPageChangeListener;
    private LinearLayout itemContainer;
    private List<ImageView> items;
    private TextView currentText;

    // Item click listener for view pager dots
    private OnClickListener itemClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();
            pager.setCurrentItem(position);
        }
    };

    // Constructor
    public SimpleViewPagerIndicator(Context context) {
        super(context);
        this.context = context;
        setup();
    }

    // Constructor
    public SimpleViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setup();
    }

    // Constructor
    public SimpleViewPagerIndicator(Context context, AttributeSet attrs,
                                    int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        setup();
    }

    // init view pager
    private void setup() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (inflater != null) {
            inflater.inflate(R.layout.view_pager_indicator, this);
            itemContainer = (LinearLayout) findViewById(R.id.pager_indicator_container);
            items = new ArrayList<ImageView>();
        }
    }

    /**
     * Notifies the pager indicator that the data set has changed. Be sure to
     * notify the pager as well (though you may wish to place that call in here
     * yourself).
     */
    public void notifyDataSetChanged() {
        if (pager != null && pager.getAdapter() != null) {

            // remove the old items (if any exist)
            itemContainer.removeAllViews();

            // I'm sure this could be optimised a lot more, eg,
            // by reusing existing ImageViews, but it
            // does the job well enough for now.
            items.removeAll(items);

            // now create the new items.
            for (int i = 0; i < pager.getAdapter().getCount(); i++) {

                if (i < 5) {
                    ImageView item = new ImageView(context);
                    LayoutParams lp = new LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 5, 10, 5);
                    item.setLayoutParams(lp);
                    if (i == pager.getCurrentItem()) {
                        item.setImageResource(R.drawable.un_selected_dot);
                    } else {
                        item.setImageResource(R.drawable.selected_dot);
                    }

                    item.setTag(i);
                    items.add(item);
                    itemContainer.addView(item);
                } else {
                    Log.e("Position", "" + i);
                    TextView txt = new TextView(context);
                    LayoutParams lp = new LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 5, 15, 5);
                    txt.setText("Still go :" + (pager.getAdapter().getCount() - i));
                    txt.setLayoutParams(lp);
                    if (i == 5) {
                        currentText = txt;
                        itemContainer.addView(txt);
                    }
                }
            }
        }
    }

    // get viewpager attached with pager indicator
    public ViewPager getViewPager() {
        return pager;
    }

    // set view pager in pager indicator so that in change of pager indicator
    // view pager also gets changed
    public void setViewPager(ViewPager pager) {
        this.pager = pager;
        this.pager.setOnPageChangeListener(this);
    }

    // get page change listener
    public OnPageChangeListener getOnPageChangeListener() {
        return onPageChangeListener;
    }

    // Set page change listener
    public void setOnPageChangeListener(
            OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    // set current item position of view
    private void setCurrentItem(int position) {
        Log.e("Position", "Current Item :" + position);
        try {
            if (position > 4) {
                currentText.setText("still go :" + (pager.getAdapter().getCount() - position));
            }

            if (pager != null && pager.getAdapter() != null) {
                int numberOfItems = pager.getAdapter().getCount();
                for (int i = 0; i < numberOfItems; i++) {
                    ImageView item = items.get(i);
                    if (item != null) {
                        if (i == position) {
                            item.setImageResource(R.drawable.un_selected_dot);
                        } else {
                            item.setImageResource(R.drawable.selected_dot);
                        }
                    }

                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (this.onPageChangeListener != null) {
            this.onPageChangeListener.onPageScrollStateChanged(state);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        if (this.onPageChangeListener != null) {
            this.onPageChangeListener.onPageScrolled(position, positionOffset,
                    positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        setCurrentItem(position);
        if (this.onPageChangeListener != null) {
            this.onPageChangeListener.onPageSelected(position);
        }
    }
}
