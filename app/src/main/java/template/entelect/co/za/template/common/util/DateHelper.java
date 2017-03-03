package template.entelect.co.za.template.common.util;

import android.content.Context;
import android.text.format.DateUtils;

/**
 * Created by hennie.brink on 2017/03/02.
 */

public class DateHelper {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    private static final int WEEK_MILLIS = 7 * DAY_MILLIS;

    public static String getTimeAgo(long time, Context context, long minResolutions) {
        return DateUtils.getRelativeTimeSpanString(
                time,
                System.currentTimeMillis(),
                minResolutions,
                DateUtils.FORMAT_ABBREV_RELATIVE).toString();
    }

    public static String getTimeAgo(long time, Context context) {
        return getTimeAgo(time, context, DateUtils.HOUR_IN_MILLIS);
    }
}
