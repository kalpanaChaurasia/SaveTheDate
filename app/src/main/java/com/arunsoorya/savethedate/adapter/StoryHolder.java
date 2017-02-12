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

public class StoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView title;
    public TextView desc;
    private Context context;
    private RecyclerClickListener clickListener;

    public StoryHolder(View v, Context context) {
        super(v);

        title = (TextView) v.findViewById(R.id.item_date);
        desc = (TextView) v.findViewById(R.id.item_description);
        this.context = context;
        v.setOnClickListener(this);
    }

    public void setOnItemClickListener(RecyclerClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onClick(View view) {
        clickListener.onItemClick(getAdapterPosition(), view);
    }
}
