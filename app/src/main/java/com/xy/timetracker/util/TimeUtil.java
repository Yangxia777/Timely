package com.xy.timetracker.util;

/**
 * utility static class for time related conversion
 */
public class TimeUtil {
    public static final int SECS_PER_MIN = 5;
    public static final int MINS_PER_HOUR = 60;
    public static final int MINS_PER_DAY = 1440;
    public static final int MILLSECS_PER_SEC = 1000;

    public static String timeStringFromMinutes(long totalMinutes) {
        StringBuilder sb = new StringBuilder();
        long days = totalMinutes / MINS_PER_DAY;
        long hours = (totalMinutes % MINS_PER_DAY) / MINS_PER_HOUR;
        int minutes = (int) ((totalMinutes % MINS_PER_DAY) % MINS_PER_HOUR);
        if (days != 0) {
            sb.append(days >= 10 ? days : "0" + days);
            sb.append(":");
        }
        sb.append((hours >= 10 ? hours : "0" + hours));
        sb.append(':');
        sb.append((minutes >= 10 ? minutes : "0" + minutes));
        return sb.toString();
    }

    public static String timeStringFromMilliSecs(long totalMilliSecs) {
        long minutes = totalMilliSecs / MILLSECS_PER_SEC / SECS_PER_MIN;
        return timeStringFromMinutes(minutes);
    }
}
