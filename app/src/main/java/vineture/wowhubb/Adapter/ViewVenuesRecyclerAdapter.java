package vineture.wowhubb.Adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.podcopic.animationlib.library.AnimationType;
import com.podcopic.animationlib.library.StartSmartAnimation;
import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.Fragment.EventVenueFragment;
import vineture.wowhubb.R;
import vineture.wowhubb.data.AddressVenue;

import java.util.ArrayList;

import sql.DatabaseHelper;

/**
 * Created by delaroy on 5/10/17.
 */
public class ViewVenuesRecyclerAdapter extends RecyclerView.Adapter<ViewVenuesRecyclerAdapter.BeneficiaryViewHolder> {


    private ArrayList<AddressVenue> listBeneficiary;
    private Context mContext;
    private ArrayList<AddressVenue> mFilteredList;
    private DatabaseHelper databaseHelper;

    public ViewVenuesRecyclerAdapter(ArrayList<AddressVenue> listBeneficiary, Context mContext) {
        this.listBeneficiary = listBeneficiary;
        this.mContext = mContext;
        this.listBeneficiary = listBeneficiary;
    }

    @Override
    public BeneficiaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_addressvenue_recycler, parent, false);
        FontsOverride.overrideFonts(mContext, itemView);
        databaseHelper = new DatabaseHelper(mContext);
        return new BeneficiaryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final BeneficiaryViewHolder holder, final int position)

    {
        holder.position = position;
        holder.textViewVenueName.setText(listBeneficiary.get(position).getVenuname());
        holder.textViewCity.setText(listBeneficiary.get(position).getCity());
        holder.textViewAddress.setText(listBeneficiary.get(position).getAddress());
        holder.textViewState.setText(listBeneficiary.get(position).getState());
        holder.textViewZipcode.setText(listBeneficiary.get(position).getZipcode());

        holder.edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AddressVenue item = listBeneficiary.get(holder.position);
                    EventVenueFragment.dialog.dismiss();
                    EventVenueFragment.venuename.setText(item.getVenuname());
                    EventVenueFragment.address.setText(item.getAddress());
                    EventVenueFragment.city.setText(item.getCity());
                    EventVenueFragment.state.setText(item.getState());
                    EventVenueFragment.zipcode.setText(item.getZipcode());
                    Log.e("tag", "22222222222---------------" + item);
                } catch (IndexOutOfBoundsException e) {


                }
            }
        });

        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AddressVenue item = listBeneficiary.get(holder.position);
                    //EventVenueFragment.dialog.dismiss();
                    Log.e("tag", "11111111------------------" + item.getVenuname());
                    // notifyDataSetChanged();
                    boolean result = databaseHelper.deleteProduct(item.getVenuname());

                    if (result) {
                        Log.e("tag", "deleted------------------" + item.getVenuname());

                        StartSmartAnimation.startAnimation(holder.ll, AnimationType.SlideOutRight, 1000, 0, true);
                        listBeneficiary.remove(position);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // listView.setAdapter(mAdapter);
                            }
                        }, 1000);

                    } else {
                        Log.e("tag", "No matches found------------------" + item.getVenuname());
                    }

                } catch (IndexOutOfBoundsException e) {


                }
            }

        });


    }

    @Override
    public int getItemCount() {
        return listBeneficiary.size();
    }

    public class BeneficiaryViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView textViewVenueName;
        public AppCompatTextView textViewAddress;
        public AppCompatTextView textViewCity;
        public AppCompatTextView textViewState, textViewZipcode;
        TextView edit_btn, delete_btn;
        int position;
        CardView cardView;
        LinearLayout ll;


        public BeneficiaryViewHolder(View view) {
            super(view);

            textViewVenueName = view.findViewById(R.id.venuenametv);
            textViewAddress = (AppCompatTextView) view.findViewById(R.id.addresstv);
            textViewCity = (AppCompatTextView) view.findViewById(R.id.citytv);
            textViewState = (AppCompatTextView) view.findViewById(R.id.statetv);
            textViewZipcode = (AppCompatTextView) view.findViewById(R.id.zipcodetv);
            edit_btn = view.findViewById(R.id.edit_btn);
            delete_btn = view.findViewById(R.id.delete_btn);

            ll = view.findViewById(R.id.ll);

        }


    }


}
