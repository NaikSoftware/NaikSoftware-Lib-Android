package ua.naiksoftware.android.util;

import org.joda.time.DateTimeZone;

import java.util.Date;

/**
 * Provide net.danlew:android.joda dependency for use this
 */
public class DateTimeUtil {

    /**
     * Provide net.danlew:android.joda dependency for use this
     */
    public static Date toUTC(Date localDate) {
        if (localDate == null) return null;
        return new Date(DateTimeZone.getDefault().convertLocalToUTC(localDate.getTime(), false));
    }

    /**
     * Provide net.danlew:android.joda dependency for use this
     */
    public static Date fromUTC(Date utcDate) {
        if (utcDate == null) return null;
        return new Date(DateTimeZone.getDefault().convertUTCToLocal(utcDate.getTime()));
    }
}
