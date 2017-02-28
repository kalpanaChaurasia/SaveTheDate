package com.arunsoorya.savethedate.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arunsoorya.savethedate.R;
import com.arunsoorya.savethedate.model.StoryVO;
import com.arunsoorya.savethedate.utils.RecyclerClickListener;
import com.arunsoorya.savethedate.utils.Utils;

import java.util.List;

/**
 * Created by arunsoorya on 26/01/17.
 */

public class StoryNameAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements RecyclerClickListener {


    private List<StoryVO> stories;
    private String selectedStoryId;
    private Context context;
    private RecyclerClickListener recyclerClickListener;

    public StoryNameAdapter(List<StoryVO> stories, Context context, String selectedStoryId) {
        this.context = context;
        this.stories = stories;
        this.selectedStoryId = selectedStoryId;
        recyclerClickListener = (RecyclerClickListener) context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case Utils.RECYCLE_TYPE_ADD:
                inflatedView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.content_story_default_listname_row, parent, false);
                DefaultHolder defaultHolder = new DefaultHolder(inflatedView, context);
                defaultHolder.setOnItemClickListener(this);
                holder = defaultHolder;
                break;
            case Utils.RECYCLE_TYPE_NORMAL:

                inflatedView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.content_story_listname_row, parent, false);
                StoryNameHolder storyHolder = new StoryNameHolder(inflatedView, context);
                storyHolder.setOnItemClickListener(this);

                holder = storyHolder;
                break;

        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case Utils.RECYCLE_TYPE_ADD:
                break;
            case Utils.RECYCLE_TYPE_NORMAL:
                StoryNameHolder storyHolder = (StoryNameHolder) holder;
                storyHolder.title.setText(stories.get(position).getStoryName());
                //selected
                if (selectedStoryId != null && selectedStoryId.equalsIgnoreCase(stories.get(position).getStoryId())) {
                    storyHolder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    storyHolder.title.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    storyHolder.cardView.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    storyHolder.title.setTextColor(Color.parseColor("#000000"));
                }
                break;
        }


    }
    @Override
    public int getItemViewType(int position) {
        return stories.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }


    @Override
    public void onItemClick(int position, View v) {
        selectedStoryId = stories.get(position).getStoryId();
        notifyDataSetChanged();
        if (recyclerClickListener != null) {
            recyclerClickListener.onItemClick(position, v);
        }

    }

    @Override
    public void onDefaultClick(View v) {
        if (recyclerClickListener != null) {
            recyclerClickListener.onDefaultClick(v);
        }
    }
}
