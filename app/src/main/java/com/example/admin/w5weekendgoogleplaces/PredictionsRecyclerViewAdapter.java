package com.example.admin.w5weekendgoogleplaces;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.w5weekendgoogleplaces.PredictionsFragment.OnListFragmentInteractionListener;
import com.example.admin.w5weekendgoogleplaces.model.queryautocomplete.Prediction;

import java.util.List;

public class PredictionsRecyclerViewAdapter extends RecyclerView.Adapter<PredictionsRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "prvaTag";
    private final List<Prediction> mValues;
    private final OnListFragmentInteractionListener mListener;

    public PredictionsRecyclerViewAdapter(List<Prediction> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        Log.d(TAG, "PredictionsRecyclerViewAdapter: " + items.size());
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_predictions, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mPredicitonView.setText(mValues.get(position).getStructuredFormatting().getMainText());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mPredicitonView;
        public final TextView mContentView;
        public Prediction mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPredicitonView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
