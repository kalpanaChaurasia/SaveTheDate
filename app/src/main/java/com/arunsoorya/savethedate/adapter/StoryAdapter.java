package com.arunsoorya.savethedate.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arunsoorya.savethedate.R;
import com.arunsoorya.savethedate.StoryActivity;
import com.arunsoorya.savethedate.model.StoryVO;
import com.arunsoorya.savethedate.utils.RecyclerClickListener;
import com.arunsoorya.savethedate.utils.Utils;

import java.util.List;

/**
 * Created by arunsoorya on 26/01/17.
 */

public class StoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements RecyclerClickListener {


    private List<StoryVO> stories;
    private Context context;
    private RecyclerClickListener recyclerClickListener;

    public StoryAdapter(List<StoryVO> stories, Context context) {
        this.context = context;
        this.stories = stories;
        if (context instanceof RecyclerClickListener)
            recyclerClickListener = (RecyclerClickListener) context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case Utils.RECYCLE_TYPE_ADD:
                inflatedView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.default_add_row, parent, false);
                DefaultHolder defaultHolder = new DefaultHolder(inflatedView, context);
                defaultHolder.setOnItemClickListener(this);
                holder = defaultHolder;
                break;
            case Utils.RECYCLE_TYPE_NORMAL:

                inflatedView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.content_story_list_row, parent, false);
                StoryHolder storyHolder = new StoryHolder(inflatedView, context);
                storyHolder.setOnItemClickListener(this);

                holder = storyHolder;
                break;

        }

        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        return stories.get(position).getViewType();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case Utils.RECYCLE_TYPE_ADD:
                break;
            case Utils.RECYCLE_TYPE_NORMAL:
                StoryHolder storyHolder = (StoryHolder) holder;
                storyHolder.title.setText(stories.get(position).getStoryName());
                storyHolder.desc.setText(stories.get(position).getStoryDesc());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }


    @Override
    public void onItemClick(int position, View v) {

        recyclerClickListener.onItemClick(position,v);
    }

    @Override
    public void onDefaultClick(View v) {
        recyclerClickListener.onDefaultClick(v);
    }
}
