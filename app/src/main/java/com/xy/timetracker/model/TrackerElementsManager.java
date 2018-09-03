package com.xy.timetracker.model;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class TrackerElementsManager {

    public interface OnTrackerAddedListener {
        void onTrackerAdded(TrackerElement tracker);
    }

    private static TrackerElementsManager mTrackerElementsManager;
    private LinkedList<OnTrackerAddedListener> onTrackerAddedListenerList;
    private ArrayList<TrackerElement> trackers;

    private TrackerElementsManager() {
        trackers = new ArrayList<>();
        onTrackerAddedListenerList = new LinkedList<>();
    }

    public static TrackerElementsManager getInstance() {
        if (mTrackerElementsManager == null) {
            mTrackerElementsManager = new TrackerElementsManager();
        }

        return mTrackerElementsManager;
    }

    public ArrayList<TrackerElement> getTrackers() {
        return trackers;
    }

    public void setTrackers(ArrayList<TrackerElement> trackers) {
        this.trackers = trackers;
    }

    public List<TrackerElement> filterOutArchivedTrackerElements() {
        assert trackers != null;
        List<TrackerElement> res = new ArrayList<>();
        for (TrackerElement tracker : trackers) {
            if (!tracker.isArchived()) {
                res.add(tracker);
            }
        }
        return res;
    }

    public List<WeekViewEvent> filterOutEventsByYearMonth(int newYear, int newMonth) {
        List<WeekViewEvent> result = new ArrayList<>();

        for (TrackerElement tracker : trackers) {
            List<Long> startTimes = tracker.getStartTime();
            List<Long> endTimes = tracker.getEndTime();
            for (int i = 0; i < endTimes.size(); i++) {
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                start.setTimeInMillis(startTimes.get(i));
                end.setTimeInMillis(endTimes.get(i));

                if ((start.get(Calendar.YEAR) == newYear && start.get(Calendar.MONTH) == newMonth) || (end.get(Calendar.YEAR) == newYear && end.get(Calendar.MONTH) == newMonth)) {
                    WeekViewEvent event = new WeekViewEvent();
                    event.setName(tracker.getTrackerTitle());
                    event.setColor(tracker.getTrackerColor());
                    event.setStartTime(start);
                    event.setEndTime(end);
                    result.add(event);
                }
            }
        }

        return result;
    }

    public void addOnTrackerAddedListener(OnTrackerAddedListener listener) {
        onTrackerAddedListenerList.add(listener);
    }

    public void removeOnTrackerAddedListener(OnTrackerAddedListener listener) {
        onTrackerAddedListenerList.remove(listener);
    }

    public void addTracker(TrackerElement trackerElement) {
        trackers.add(trackerElement);
        for (OnTrackerAddedListener listener : onTrackerAddedListenerList) {
            listener.onTrackerAdded(trackerElement);
        }
    }
}

