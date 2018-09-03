package com.xy.timetracker.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;

import com.xy.timetracker.BuildConfig;
import com.xy.timetracker.util.ListArrayUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * a TrackerElement represents a tracker.
 * Including its property like title, state like isRunning.
 */
public class TrackerElement implements Parcelable, Serializable {
    private static String TAG = "TrackerElement";
    private boolean isRunning;
    private UUID trackerUUID;
    private String trackerTitle;
    private String trackerSubTitle;     // "context" of this tracker
    private long accumulatedMilliSeconds;    // total seconds that's on this tracker.
    private long lastBase;              // lastBase time for the corresponding chronometer.
    private int trackerColor;
    private boolean isArchived;

    private List<Long> startTime;
    private List<Long> endTime;

    public TrackerElement(UUID uuid, String trackerTitle, String trackerSubTitle, int color) {
        this.trackerUUID = uuid;
        this.trackerTitle = trackerTitle;
        this.trackerSubTitle = trackerSubTitle;
        isRunning = false;
        this.startTime = new ArrayList<>();
        this.endTime = new ArrayList<>();
        this.trackerColor = color;
        this.isArchived = false;
    }

    public TrackerElement(boolean isRunning, UUID trackerUUID, String trackerTitle,
                          String trackerSubTitle, long accumulatedMilliSeconds,
                          long runningMilliSecs, long lastBase, boolean isArchived,
                          long[] startTime, long[] endTime) {
        this.isRunning = isRunning;
        this.trackerUUID = trackerUUID;
        this.trackerTitle = trackerTitle;
        this.trackerSubTitle = trackerSubTitle;
        this.accumulatedMilliSeconds = accumulatedMilliSeconds;
        this.lastBase = lastBase;
        this.startTime = new ArrayList<>();
        this.endTime = new ArrayList<>();
        this.isArchived = isArchived;
        ListArrayUtil.addPrimitivesToList(this.startTime, startTime);
        ListArrayUtil.addPrimitivesToList(this.endTime, endTime);
    }

    public TrackerElement(boolean isRunning, UUID trackerUUID, String trackerTitle,
                          String trackerSubTitle, long accumulatedMilliSeconds,
                          long runningMilliSecs, long lastBase, int trackerColor, boolean isArchived,
                          long[] startTime, long[] endTime) {
        this(isRunning, trackerUUID, trackerTitle, trackerSubTitle, accumulatedMilliSeconds,
                runningMilliSecs, lastBase, isArchived, startTime, endTime);
        this.trackerColor = trackerColor;
    }

    //-------------------------------------------------------------------------
    // Implement Parcelble
    //-------------------------------------------------------------------------
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isRunning ? 1 : 0));
        dest.writeString(trackerUUID.toString());
        dest.writeString(trackerTitle);
        dest.writeString(trackerSubTitle);
        dest.writeLong(accumulatedMilliSeconds);
        dest.writeLong(lastBase);
        dest.writeInt(trackerColor);
        dest.writeByte((byte) (isArchived ? 1 : 0));
        dest.writeInt(startTime.size());
        dest.writeLongArray(ListArrayUtil.toPrimitives(startTime));
        dest.writeInt(endTime.size());
        dest.writeLongArray(ListArrayUtil.toPrimitives(endTime));
    }

    public static final Parcelable.Creator<TrackerElement> CREATOR
            = new Parcelable.Creator<TrackerElement>() {
        public TrackerElement createFromParcel(Parcel in) {
            boolean isRunning = (in.readByte() == 1);
            UUID trackerId = UUID.fromString(in.readString());
            String title = in.readString();
            String subTitle = in.readString();
            long accumulatedMilliSec = in.readLong();
            long lastBase = in.readLong();
            int trackerColor = in.readInt();
            boolean isArchived = (in.readByte() == 1);

            int startTimeLen = in.readInt();
            long[] startTime = new long[startTimeLen];
            in.readLongArray(startTime);
            int endTimeLen = in.readInt();
            long[] endTime = new long[endTimeLen];
            in.readLongArray(endTime);
            return new TrackerElement(isRunning, trackerId, title, subTitle, accumulatedMilliSec,
                    lastBase, trackerColor, isArchived, startTime, endTime);
        }

        public TrackerElement[] newArray(int size) {
            return new TrackerElement[size];
        }
    };

    //-------------------------------------------------------------------------
    // Getter and Setter
    //-------------------------------------------------------------------------
    public UUID getTrackerUUID() {
        return trackerUUID;
    }

    public String getTrackerTitle() {
        return trackerTitle;
    }

    public String getTrackerSubTitle() {
        return trackerSubTitle;
    }

    public long getAccumulatedMilliSeconds() {
        return accumulatedMilliSeconds;
    }

    public long getRunningMilliSecs() {
        if (isRunning) {
            return SystemClock.elapsedRealtime() - getLastBase();
        } else {
            return 0;
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    /**
     * running secs represent the secs since last start.
     * accumulated secs represet the secs for all start-end time log pairs
     * every time the running secs are about to be cleaned, an endTime Should be logged as well.
     */
    public void setIsRunning(boolean isRunning, long oldBase) {
        if (!isRunning) {
            accumulatedMilliSeconds += getRunningMilliSecs();
            endTime.add(System.currentTimeMillis());
        } else {
            startTime.add(System.currentTimeMillis());
            lastBase = oldBase;
        }
        this.isRunning = isRunning;
    }

    public long getLastStartTime() {
        if (BuildConfig.DEBUG && startTime.size() == 0) {
            throw new AssertionError("get last start time while there is none");
        }
        return startTime.get(startTime.size() - 1);
    }

    public int getTrackerColor() {
        return trackerColor;
    }

    public long getLastBase() {
        return lastBase;
    }

    public List<Long> getStartTime() {
        return startTime;
    }

    public List<Long> getEndTime() {
        return endTime;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setIsArchived(boolean isArchived) {
        this.isArchived = isArchived;
    }
}
