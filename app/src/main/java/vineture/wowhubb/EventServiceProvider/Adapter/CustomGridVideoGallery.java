package vineture.wowhubb.EventServiceProvider.Adapter;

/**
 * Created by Guna on 20-03-2018.
 */

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.devbrackets.android.exomedia.ui.widget.VideoView;

import vineture.wowhubb.R;


public class CustomGridVideoGallery extends BaseAdapter{
    private Context mContext;
    private final String[] web;
    private final String[] price;
    private final int[] Imageid;
    Typeface segoeui;
    private static final String TEST_URL = "http://104.197.80.225:3010/wow/media/personal/chocolate.mp4";

    public CustomGridVideoGallery(Context c, String[] web, int[] Imageid, String[] price) {
        mContext = c;
        this.Imageid = Imageid;
        this.web = web;
        this.price=price;
    }



    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return web.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_videosingle, null);
            TextView item_name = (TextView) grid.findViewById(R.id.grid_text);
            TextView item_price = (TextView) grid.findViewById(R.id.grid_price);
            TextView eventorg = (TextView) grid.findViewById(R.id.eventorg);
            final ImageView player = (ImageView) grid.findViewById(R.id.player);
            FrameLayout framevideo1=grid.findViewById(R.id.framevideo1);

            segoeui = Typeface.createFromAsset(mContext.getAssets(), "fonts/segoeui.ttf");
            item_name.setTypeface(segoeui);
            item_price.setTypeface(segoeui);
            item_name.setText(web[position]);

            segoeui = Typeface.createFromAsset(mContext.getAssets(), "fonts/segoeui.ttf");
            item_name.setTypeface(segoeui);
            item_price.setTypeface(segoeui);
            eventorg.setTypeface(segoeui);
            item_name.setText(web[position]);
            item_price.setText(price[position]);
            player.setImageResource(Imageid[position]);



            framevideo1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final Dialog dialog = new Dialog(mContext, R.style.dialog);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_videoview);
                    View v1 = dialog.getWindow().getDecorView().getRootView();
                    ImageView closeiv = dialog.findViewById(R.id.close_iv);
                    closeiv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    VideoView videoView = dialog.findViewById(R.id.video_view);

                        dialog.show();
                        //prfessionalVH.video3plus.setVisibility(View.VISIBLE);
                        videoView.setVideoURI(Uri.parse(TEST_URL));
                        videoView.start();



                }
            });

        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}