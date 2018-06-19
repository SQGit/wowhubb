package vineture.wowhubb.EventServiceProvider.Fragment;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import vineture.wowhubb.EventServiceProvider.Model.EventListDetailsModel;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapDialogFragment extends DialogFragment implements OnMapReadyCallback {
    private Property propertyData;
    private Context contextData;
    TextView txt_provider_head,txt_shop_name,txt_shop_category,txt_shop_rating,txt_shop_address,txt_shop_mobile,txt_shop_link,txt_open_time,txt_wowsome,txt_call,wowsomes;
    ImageView img_provider_logo,location,close_icon;
    MaterialRatingBar show_ratingbar;
    private SupportMapFragment mapDetail;
    Typeface lato;
    EventListDetailsModel eventListDetailsModel;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        contextData = getContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(vineture.wowhubb.R.layout.map_dialog_fragment, container, false);


        txt_provider_head=view.findViewById(vineture.wowhubb.R.id.txt_provider_head);
        txt_shop_name = (TextView) view.findViewById(vineture.wowhubb.R.id.txt_shop_name);
        txt_shop_category = (TextView) view.findViewById(vineture.wowhubb.R.id.txt_shop_category);
        txt_shop_rating = (TextView) view.findViewById(vineture.wowhubb.R.id.txt_shop_rating);
        txt_shop_address = (TextView) view.findViewById(vineture.wowhubb.R.id.txt_shop_address);
        txt_shop_mobile = (TextView) view.findViewById(vineture.wowhubb.R.id.txt_shop_mobile);
        txt_shop_link = (TextView) view.findViewById(vineture.wowhubb.R.id.txt_shop_link);
        img_provider_logo=(ImageView)view.findViewById(vineture.wowhubb.R.id.img_provider_logo);
        location=(ImageView)view.findViewById(vineture.wowhubb.R.id.location);
        txt_open_time=(TextView)view.findViewById(vineture.wowhubb.R.id.txt_open_time);
        txt_wowsome=(TextView)view.findViewById(vineture.wowhubb.R.id.txt_wowsome);
        txt_call=(TextView)view.findViewById(vineture.wowhubb.R.id.txt_call);
        wowsomes=view.findViewById(vineture.wowhubb.R.id.wowsomes);
        show_ratingbar=view.findViewById(vineture.wowhubb.R.id.show_ratingbar);
        close_icon=view.findViewById(vineture.wowhubb.R.id.close_icon);

        lato = Typeface.createFromAsset(getActivity().getAssets(), "fonts/segoeui.ttf");
        txt_shop_name.setText(eventListDetailsModel.getshopName());
        txt_provider_head.setText(eventListDetailsModel.getshopName());
        txt_shop_category.setText(eventListDetailsModel.getshopCategory());
        txt_shop_address.setText(eventListDetailsModel.getshopAddress());
        txt_open_time.setText("Opens@ "+eventListDetailsModel.getshopOpenTime());
        txt_shop_mobile.setText(eventListDetailsModel.getshopContact());
        txt_shop_rating.setText(eventListDetailsModel.getshopRating());

        //Underline in particular textView:
        String sourceString = "<u>" + eventListDetailsModel.getshopLink() + "</u> ";
        txt_shop_link.setText(Html.fromHtml(sourceString));


        show_ratingbar.setRating(Float.parseFloat(eventListDetailsModel.getshopRating()));

        Glide.with(getActivity()).load("http://104.197.80.225:3010/wow/media/provider/" + eventListDetailsModel.getshopLogo())
                .into(img_provider_logo);

        txt_provider_head.setTypeface(lato);
        txt_shop_name.setTypeface(lato);
        txt_shop_category.setTypeface(lato);
        txt_shop_address.setTypeface(lato);
        txt_open_time.setTypeface(lato);
        txt_shop_mobile.setTypeface(lato);
        txt_shop_link.setTypeface(lato);
        wowsomes.setTypeface(lato);
        txt_shop_rating.setTypeface(lato);
        txt_wowsome.setTypeface(lato);
        txt_call.setTypeface(lato);


        txt_shop_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://"+eventListDetailsModel.getshopLink();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });


      /* close_icon.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(getActivity(), EventListDetailsNewActivity.class));
               getActivity().overridePendingTransition(wow.wowhubb.R.anim.slide_in_up, wow.wowhubb.R.anim.slide_out_up);
               getActivity().finish();

           }
       });*/


     txt_call.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        txt_call.setTextColor(Color.parseColor("#FFFFFF"));
                        txt_call.setBackgroundResource(vineture.wowhubb.R.drawable.fill_square);
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:"+eventListDetailsModel.getshopContact()));
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.

                        }
                        getActivity().startActivity(callIntent);
                        return true;
                    case MotionEvent.ACTION_UP:
                        txt_call.setTextColor(Color.parseColor("#000000"));
                        txt_call.setBackgroundResource(vineture.wowhubb.R.drawable.normalsquare);
                        return true;
                }
                return false;
            }
        });


        txt_wowsome.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        txt_wowsome.setTextColor(Color.parseColor("#FFFFFF"));
                        txt_wowsome.setBackgroundResource(vineture.wowhubb.R.drawable.fill_square);
                        Toast.makeText(getActivity(), "Under Development", Toast.LENGTH_SHORT).show();
                        return true;
                    case MotionEvent.ACTION_UP:
                        txt_wowsome.setTextColor(Color.parseColor("#000000"));
                        txt_wowsome.setBackgroundResource(vineture.wowhubb.R.drawable.normalsquare);
                        return true;
                }
                return false;
            }
        });



        if (Build.VERSION.SDK_INT < 21) {
            mapDetail = (SupportMapFragment) getActivity()
                    .getSupportFragmentManager().findFragmentById(vineture.wowhubb.R.id.map_data);
        } else {
            mapDetail = (SupportMapFragment) getActivity().getSupportFragmentManager()
                    .findFragmentById(vineture.wowhubb.R.id.map_data);
        }
        mapDetail.getMapAsync(this);


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mapDetail != null) {
            //getFragmentManager().beginTransaction().remove(mapDetail).commit();

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(eventListDetailsModel.getshopLatitude(), eventListDetailsModel.getshopLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
        markerOptions.title(eventListDetailsModel.getshopName());

        googleMap.addMarker( markerOptions);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(19)
                .tilt(0)
                .bearing(0)
                .build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    public void setvalues(EventListDetailsModel eventListDetailsModel){
        this.eventListDetailsModel = eventListDetailsModel;
    }


}