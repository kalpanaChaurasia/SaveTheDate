package com.arunsoorya.savethedate.utils;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by asoor on 2/13/17.
 */

public class Utils {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({RECYCLE_TYPE_NORMAL,RECYCLE_TYPE_ADD})
    public @interface RecycleViewType {}

    // Declare the constants
    public static final int RECYCLE_TYPE_NORMAL = 0;
    public static final int RECYCLE_TYPE_ADD = 1;
}
