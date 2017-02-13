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

import java.util.List;

/**
 * Created by arunsoorya on 26/01/17.
 */

public class StoryAdapter extends RecyclerView.Adapter<StoryHolder> implements RecyclerClickListener {


    private List<StoryVO> stories;
    private Context context;

    public StoryAdapter(List<StoryVO> stories, Context context) {
        this.context = context;
        this.stories = stories;
    }

    @Override
    public StoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_story_list_row, parent, false);
        StoryHolder storyHolder = new StoryHolder(inflatedView, context);
        storyHolder.setOnItemClickListener(this);
        return storyHolder;
    }

    @Override
    public void onBindViewHolder(StoryHolder holder, int position) {
        holder.title.setText(stories.get(position).getStoryName());
        holder.desc.setText(stories.get(position).getStoryDesc());
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }


    @Override
    public void onItemClick(int position, View v) {
        Intent intent = new Intent(context, StoryActivity.class);
        intent.putExtra("data", stories.get(position));
        context.startActivity(intent);
    }

    @Override
    public void onDefaultClick(View v) {

    }
}
