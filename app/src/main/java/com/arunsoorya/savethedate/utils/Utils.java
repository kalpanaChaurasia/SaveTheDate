package com.arunsoorya.savethedate.utils;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by asoor on 2/13/17.
 */

public class Utils {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({RECYCLE_TYPE_NORMAL,RECYCLE_TYPE_ADD})
    public @interface RecycleViewType {}

    // Declare the constants
    public static final int RECYCLE_TYPE_NORMAL = 0;
    public static final int RECYCLE_TYPE_LIST = 2;
    public static final int RECYCLE_TYPE_ADD = 1;


    public static String getFormatedTime(String inMIlls){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(inMIlls));

        return getFormattedDate(calendar);
    }
    public static String getFormatedTimeYear(String inMIlls){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(inMIlls));

        return getFormattedDateYear(calendar);
    }
    public static String getFormattedDate(Calendar calendar) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        return dateFormat.format(calendar.getTime());
    }

    public static String getFormattedDateYear(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        return dateFormat.format(calendar.getTime());
    }
}
