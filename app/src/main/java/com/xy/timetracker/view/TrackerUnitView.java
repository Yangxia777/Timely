package com.xy.timetracker.view;

import android.content.Context;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.xy.timetracker.BuildConfig;
import com.xy.timetracker.R;
import com.xy.timetracker.model.TrackerElement;
import com.xy.timetracker.util.TimeUtil;

import java.lang.ref.WeakReference;
import java.util.UUID;

/**
 * this view represent the tracker unit shown in the trackers fragment.
 */
public class TrackerUnitView extends RelativeLayout {
    private UUID trackerId;
    private Chronometer chronometer;
    private TextView titleView;
    private TextView subTitleView;
    private ToggleButton toggleButton;
    private TextView timeCount;
    private View leadingHint;

    public TrackerUnitView(Context context) {
        super(context);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.tracker_unit, this);
        titleView = findViewById(R.id.tracker_unit_title);
        subTitleView = findViewById(R.id.tracker_unit_subtitle);
        toggleButton = findViewById(R.id.tracker_unit_button);
        chronometer = findViewById(R.id.tracker_unit_meter);
        timeCount = findViewById(R.id.tracker_unit_clock_count);
    }

    public void populateTrackerElement(TrackerElement trackerElement) {
        // populate all parts of the tracker initially: title / subtitle / accumulated time
        titleView.setText(trackerElement.getTrackerTitle());
        subTitleView.setText(trackerElement.getTrackerSubTitle());
        chronometer.setVisibility(View.GONE);

        //toggleButton.setChecked(trackerElement.isRunning());
        if (trackerElement.isRunning()) {
            // if it's still running, retrieve last base timestamp, set correct total running time.
            chronometer.setBase(trackerElement.getLastBase());
            chronometer.setVisibility(View.VISIBLE);
            chronometer.start();
            chronometer.setOnChronometerTickListener(new TrackerUnitClockCountTickListener(this, trackerElement));
            toggleButton.setChecked(true);
        }

        toggleButton.setOnCheckedChangeListener(new TrackerUnitStartButtonListener(this, trackerElement));

        timeCount.setText(TimeUtil.timeStringFromMilliSecs(
                trackerElement.getAccumulatedMilliSeconds() + trackerElement.getRunningMilliSecs()));
        timeCount.setBackgroundColor(trackerElement.getTrackerColor());
    }

    public UUID getTrackerId() {
        return trackerId;
    }

    public void setTrackerId(UUID trackerId) {
        this.trackerId = trackerId;
    }

    public long getChronometerRunningMilliSecs() {
        long runningMilliSecs = SystemClock.elapsedRealtime() - chronometer.getBase();
        return runningMilliSecs;
    }

    private class TrackerUnitStartButtonListener implements CompoundButton.OnCheckedChangeListener {

        private final WeakReference<View> trackerViewWeakReference;
        private final WeakReference<TrackerElement> trackerElementWeakReference;

        public TrackerUnitStartButtonListener(View trackerView, TrackerElement trackerElement) {
            this.trackerViewWeakReference = new WeakReference<>(trackerView);
            this.trackerElementWeakReference = new WeakReference<>(trackerElement);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            View tracker = trackerViewWeakReference.get();
            TrackerElement trackerElement = trackerElementWeakReference.get();
            if (tracker == null || trackerElement == null) {
                throw new AssertionError("failed to get tracker or tracker element from weak reference");
            }

            /*
             * based on the toggle of the on/off button (compoundButton), start or stop the tracker.
             * notice : if the chronometer stops, its base has to be set manually to start a new one.
             */
            Chronometer meter = (Chronometer) tracker.findViewById(R.id.tracker_unit_meter);
            if (!isChecked) {
                trackerElement.setIsRunning(false, meter.getBase());
                meter.stop();
                meter.setVisibility(View.GONE);
            } else {
                meter.setBase(SystemClock.elapsedRealtime()); //keep the base of the meter up to current time start from 0.
                meter.setVisibility(View.VISIBLE);
                meter.start();
                trackerElement.setIsRunning(true, meter.getBase());
                meter.setOnChronometerTickListener(new TrackerUnitClockCountTickListener(trackerViewWeakReference, trackerElementWeakReference));
            }
        }
    }

    private class TrackerUnitClockCountTickListener implements Chronometer.OnChronometerTickListener {
        private final WeakReference<TrackerElement> trackerElementWeakReference;
        private final WeakReference<View> trackerViewWeakReference;

        public TrackerUnitClockCountTickListener(WeakReference<View> trackerViewWeakReference, WeakReference<TrackerElement> trackerElementWeakReference) {
            this.trackerViewWeakReference = trackerViewWeakReference;
            this.trackerElementWeakReference = trackerElementWeakReference;
        }

        public TrackerUnitClockCountTickListener(View trackerView, TrackerElement trackerElement) {
            trackerElementWeakReference = new WeakReference<TrackerElement>(trackerElement);
            trackerViewWeakReference = new WeakReference<View>(trackerView);
        }

        @Override
        public void onChronometerTick(Chronometer chronometer) {
            TrackerElement trackerElement = trackerElementWeakReference.get();
            TrackerUnitView trackerView = (TrackerUnitView) trackerViewWeakReference.get();

            if (BuildConfig.DEBUG && (null == trackerElement || null == trackerView)) {
                throw new AssertionError();
            }

            timeCount.setText(TimeUtil.timeStringFromMilliSecs(
                    trackerElement.getAccumulatedMilliSeconds()
                        + trackerElement.getRunningMilliSecs()));
        }
    }
}
