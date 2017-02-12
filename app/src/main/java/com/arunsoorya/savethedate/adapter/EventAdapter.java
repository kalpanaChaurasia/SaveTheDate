package com.arunsoorya.savethedate.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arunsoorya.savethedate.R;
import com.arunsoorya.savethedate.model.EventVO;
import com.arunsoorya.savethedate.utils.RecyclerClickListener;

import java.util.List;

/**
 * Created by arunsoorya on 26/01/17.
 */

public class EventAdapter extends RecyclerView.Adapter<StoryHolder> implements RecyclerClickListener {


    private List<EventVO> eventVOs;
    private Context context;

    public EventAdapter(List<EventVO> eventVOs, Context context) {
        this.context = context;
        this.eventVOs = eventVOs;
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
        holder.title.setText(eventVOs.get(position).getEventName());
        holder.desc.setText(eventVOs.get(position).getEventDesc());
    }

    @Override
    public int getItemCount() {
        return eventVOs.size();
    }


    @Override
    public void onItemClick(int position, View v) {
//        Intent intent = new Intent(context, E.class);
//        intent.putExtra("data", eventVOs.get(position));
//        context.startActivity(intent);
    }
}
