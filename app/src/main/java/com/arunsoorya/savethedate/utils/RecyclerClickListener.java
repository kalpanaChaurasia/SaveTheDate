package com.arunsoorya.savethedate.utils;

import android.view.View;

/**
 * Created by arunsoorya on 27/01/17.
 */

public interface RecyclerClickListener {
    interface eventEditListener{
        void onEventEdit(View v, int position);
    }
    void onItemClick(int position, View v);
    void onDefaultClick(View v);
}
