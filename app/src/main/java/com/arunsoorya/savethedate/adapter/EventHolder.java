package com.arunsoorya.savethedate.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arunsoorya.savethedate.R;
import com.arunsoorya.savethedate.utils.RecyclerClickListener;

/**
 * Created by arunsoorya on 26/01/17.
 */

public class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public View goToStory;
    public TextView title;
    public TextView date;
    public TextView desc;
    public ImageView edit;
    private View goToNext;
    private RecyclerClickListener clickListener;
    private RecyclerClickListener.eventEditListener eventEditListener;
    private RecyclerClickListener.goToStoryListener goToStoryListener;

    public EventHolder(View v, RecyclerClickListener clickListener) {
        super(v);
        this.clickListener = clickListener;
        if (clickListener instanceof RecyclerClickListener.eventEditListener) {
            this.eventEditListener = (RecyclerClickListener.eventEditListener) clickListener;
        }
        if (clickListener instanceof RecyclerClickListener.goToStoryListener) {
            this.goToStoryListener = (RecyclerClickListener.goToStoryListener) clickListener;
        }

        title = (TextView) v.findViewById(R.id.item_title);
        date = (TextView) v.findViewById(R.id.item_date);
        desc = (TextView) v.findViewById(R.id.item_description);
        edit = (ImageView) v.findViewById(R.id.edit);
        goToNext = v.findViewById(R.id.goToDetail);
        goToStory = v.findViewById(R.id.go_to_story);
        if (goToNext != null) {
            goToNext.setOnClickListener(this);
        }
        if (eventEditListener != null) {
            edit.setOnClickListener(this);
        }
        if (goToStory != null && goToStoryListener != null) {
            goToStory.setOnClickListener(this);
        }
        v.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit:
            case R.id.goToDetail:
                eventEditListener.onEventEdit(view, getAdapterPosition());
                break;
            case R.id.go_to_story:
                goToStoryListener.goToStory(view, getAdapterPosition());
                break;
            default:
                clickListener.onItemClick(getAdapterPosition(), view);
                break;
        }

    }
}
