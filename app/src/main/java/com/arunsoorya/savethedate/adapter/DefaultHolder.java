package com.arunsoorya.savethedate.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.arunsoorya.savethedate.R;
import com.arunsoorya.savethedate.utils.RecyclerClickListener;

/**
 * Created by arunsoorya on 26/01/17.
 */

public class DefaultHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private RecyclerClickListener clickListener;

    public DefaultHolder(View v, Context context) {
        super(v);
        v.setOnClickListener(this);
    }

    public void setOnItemClickListener(RecyclerClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onClick(View view) {
        clickListener.onDefaultClick(view);
    }
}
