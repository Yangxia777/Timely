package com.xy.timetracker.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.xy.timetracker.model.TrackerElement;
import com.xy.timetracker.model.TrackerElementsManager;
import com.xy.timetracker.view.TrackerUnitView;

import java.util.Collections;
import java.util.List;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class TrackerRecyclerViewAdapter extends RecyclerView.Adapter<TrackerRecyclerViewAdapter
        .ViewHolder> implements ItemTouchHelperAdapter, TrackerElementsManager.OnTrackerAddedListener {
    private static final String TAG = "CustomAdapter";

    private List<TrackerElement> mDataSet;

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TrackerUnitView trackerView;
        public ViewHolder(View v) {
            super(v);
            trackerView = (TrackerUnitView) v;
        }

        public TrackerUnitView getView() {
            return trackerView;
        }

    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        TrackerElementsManager trackersManager = TrackerElementsManager.getInstance();
        trackersManager.removeOnTrackerAddedListener(this);
        //TODO is this necessary?
        mDataSet = null;
    }

    public TrackerRecyclerViewAdapter(List<TrackerElement> trackers) {
        mDataSet = trackers;
        TrackerElementsManager trackersManager = TrackerElementsManager.getInstance();
        trackersManager.addOnTrackerAddedListener(this);
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        TrackerUnitView v = new TrackerUnitView(viewGroup.getContext());

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getView().populateTrackerElement(mDataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


    //ItemTouchHelperAdapter
    @Override
    public void onItemDismiss(int position) {
        mDataSet.get(position).setIsArchived(true);
        mDataSet.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mDataSet, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mDataSet, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }


    //TrackerElementsManager.OnTrackerAddedListener
    @Override
    public void onTrackerAdded(TrackerElement tracker) {
        mDataSet.add(tracker);
        notifyDataSetChanged();
    }
}
