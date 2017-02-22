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

import java.util.List;

/**
 * Created by arunsoorya on 26/01/17.
 */

public class StoryNameAdapter extends RecyclerView.Adapter<StoryNameHolder> implements RecyclerClickListener {


    private List<StoryVO> stories;
    private String selectedStoryId;
    private Context context;
private RecyclerClickListener recyclerClickListener;
    public StoryNameAdapter(List<StoryVO> stories, Context context, String selectedStoryId ){
        this.context = context;
        this.stories = stories;
        this.selectedStoryId = selectedStoryId;
        recyclerClickListener = (RecyclerClickListener) context;
    }

    @Override
    public StoryNameHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_story_listname_row, parent, false);
        StoryNameHolder storyHolder = new StoryNameHolder(inflatedView, context);
        storyHolder.setOnItemClickListener(this);
        return storyHolder;
    }

    @Override
    public void onBindViewHolder(StoryNameHolder holder, int position) {
        holder.title.setText(stories.get(position).getStoryName());
        if (selectedStoryId != null && selectedStoryId.equalsIgnoreCase(stories.get(position).getStoryId())) {
//            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
//            holder.title.setTextColor(Color.parseColor("#000000"));
        }else{
//            holder.cardView.setCardBackgroundColor(Color.parseColor("#ffffff"));
//            holder.title.setTextColor(Color.parseColor("#000000"));
        }
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }


    @Override
    public void onItemClick(int position, View v) {
        selectedStoryId = stories.get(position).getStoryId();
        notifyDataSetChanged();
        if(recyclerClickListener!= null){
            recyclerClickListener.onItemClick(position,v);
        }

    }

    @Override
    public void onDefaultClick(View v) {
        if(recyclerClickListener!= null){
            recyclerClickListener.onDefaultClick(v);
        }
    }
}
