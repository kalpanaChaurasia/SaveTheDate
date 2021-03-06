package com.arunsoorya.savethedate.fragment;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by arunsoorya on 18/03/17.
 */

public abstract class BasePageTransformer implements ViewPager.PageTransformer {

    public static boolean inRange(final float position) {
        return position <= 1.0 && position >= -1.0;
    }

    public static boolean isLeftPage(final float position) {
        return position < 0;
    }

    public static boolean isRightPage(final float position) {
        return position > 0;
    }

    @Override
    public void transformPage(final View page, final float position) {
        final int pageIndex = (Integer) page.getTag();
        transformPage(page, pageIndex, position);
    }

    protected abstract void transformPage(final View page, final int pageIndex, final float position);

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
