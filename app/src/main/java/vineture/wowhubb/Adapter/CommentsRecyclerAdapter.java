package vineture.wowhubb.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import vineture.wowhubb.Fonts.FontsOverride;
import vineture.wowhubb.R;
import vineture.wowhubb.data.FeedItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by delaroy on 5/10/17.
 */
public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsRecyclerAdapter.BeneficiaryViewHolder> {
    private ArrayList<FeedItem> commentsList;
    private Context mContext;
    private List<FeedItem> messages;

    public CommentsRecyclerAdapter(ArrayList<FeedItem> commentsList, Context mContext) {
        this.commentsList = commentsList;
        Log.e("tag", "commentslist------------------->" + commentsList);
        this.mContext = mContext;
    }

    @Override
    public BeneficiaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(vineture.wowhubb.R.layout.comment_list_item, parent, false);
        FontsOverride.overrideFonts(mContext, itemView);

        return new BeneficiaryViewHolder(itemView);
    }

    public void addMessage(ArrayList<FeedItem> aa) {

        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final BeneficiaryViewHolder holder, int position) {

        FeedItem feedItem = commentsList.get(position);
        holder.userName.setText(feedItem.getUserfname());
        holder.userComments.setText(feedItem.getComment());
        holder.timpedisplay.setText(feedItem.getDisplaytime());
        //--------------------------------View Profile Image----------------------------//
        if (feedItem.getUserimage() != null) {
            // Log.e("tag", "Imagge-------" + item.getImge());
            Glide.with(mContext).load("http://104.197.80.225:3010/wow/media/personal/" + feedItem.getUserimage()).into(holder.profilePic);
        } else {
            holder.profilePic.setImageResource(vineture.wowhubb.R.drawable.profile_img);
        }

    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    public class BeneficiaryViewHolder extends RecyclerView.ViewHolder {

        public TextView userName, userComments,timpedisplay;
        int position;
        ImageView profilePic;

        public BeneficiaryViewHolder(View view) {
            super(view);
            userName = view.findViewById(vineture.wowhubb.R.id.username);
            userComments = view.findViewById(vineture.wowhubb.R.id.usercomments);
            timpedisplay=view.findViewById(R.id.timpedisplay);
            profilePic = (ImageView) view.findViewById(vineture.wowhubb.R.id.imageview_profile);
        }


    }

}
