package com.arunsoorya.savethedate.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.arunsoorya.savethedate.R;
import com.arunsoorya.savethedate.utils.RecyclerClickListener;

/**
 * Created by arunsoorya on 26/01/17.
 */

public class StoryNameHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView title;
    public CardView cardView;
    private RecyclerClickListener clickListener;

    public StoryNameHolder(View v, Context context) {
        super(v);
        title = (TextView) v.findViewById(R.id.item_name);
        cardView = (CardView) v.findViewById(R.id.card_view);
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
