package com.example.admin.w5weekendgoogleplaces;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.admin.w5weekendgoogleplaces.model.googleplaces.Result;

import java.util.List;

public class PlacesRecyclerViewAdapter extends RecyclerView.Adapter<PlacesRecyclerViewAdapter.ViewHolder> {

    private final List<Result> mValues;
    private final OnListInteractionListener mListener;
    private int lastPosition = -1;
    Context context;

    public PlacesRecyclerViewAdapter(List<Result> items, OnListInteractionListener listener, Context context) {
        mValues = items;
        mListener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        if (mValues.get(position).getPhotos() != null) {
            //Glide.with(context).load(mValues.get(position).getPhotos().get(0).getHtmlAttributions().get(0)).into(holder.mImageView);
            String pic = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
            pic += mValues.get(position).getPhotos().get(0).getPhotoReference();
            pic += "&key=AIzaSyC6e3sfz0NWoq8_IdWX7FgNo7x013NE3-c";
            Glide.with(context).
                    load(pic).into(holder.mImageView);
        } else
            Glide.with(context).load(mValues.get(position).getIcon()).into(holder.mImageView);
        holder.mNameView.setText(mValues.get(position).getName());
        String open;
        if (mValues.get(position).getOpeningHours() != null) {
            if (mValues.get(position).getOpeningHours().getOpenNow()) {
                open = "Open";
            } else {
                open = "Closed";
            }
        } else {
            open = "";
        }
        holder.mOpenView.setText(open);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListInteraction(holder.mItem);
                }
            }
        });

        setAnimation(holder.itemView, position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mOpenView;
        public final ImageView mImageView;
        public Result mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = view.findViewById(R.id.tvName);
            mOpenView = view.findViewById(R.id.tvOpen);
            mImageView = view.findViewById(R.id.ivIcon);
        }


        @Override
        public String toString() {
            return super.toString() + " '" + mOpenView.getText() + "'";
        }
    }

    public interface OnListInteractionListener {
        void onListInteraction(Result result);
    }
}
