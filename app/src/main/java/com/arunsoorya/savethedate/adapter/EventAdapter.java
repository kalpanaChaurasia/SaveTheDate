package com.arunsoorya.savethedate.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arunsoorya.savethedate.R;
import com.arunsoorya.savethedate.model.EventVO;
import com.arunsoorya.savethedate.utils.RecyclerClickListener;
import com.arunsoorya.savethedate.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by arunsoorya on 26/01/17.
 */

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements RecyclerClickListener,
        RecyclerClickListener.eventEditListener, RecyclerClickListener.goToStoryListener {


    private RecyclerClickListener recyclerClickListener;
    private RecyclerClickListener.eventEditListener editListener;
    private RecyclerClickListener.goToStoryListener goToStoryListener;
    private List<EventVO> eventVOs;
    private boolean isSquareLayout;
    private Context context;
    private List<EventVO> searchList;

    public EventAdapter(List<EventVO> eventVOs, Context context, boolean isSquareLayout, RecyclerClickListener recyclerClickListener) {
        this.context = context;
        this.eventVOs = eventVOs;
        this.isSquareLayout = isSquareLayout;
        this.recyclerClickListener = recyclerClickListener;
        if (recyclerClickListener instanceof RecyclerClickListener.eventEditListener)
            editListener = (RecyclerClickListener.eventEditListener) recyclerClickListener;
        if (recyclerClickListener instanceof RecyclerClickListener.goToStoryListener)
            goToStoryListener = (RecyclerClickListener.goToStoryListener) recyclerClickListener;
        searchList = new ArrayList<>();
        searchList.addAll(eventVOs);
    }

    @Override
    public int getItemViewType(int position) {
        return eventVOs.get(position).getViewType();
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
                int layout = R.layout.content_event_list_row;
                if (isSquareLayout)
                    layout = R.layout.content_home_event_list_row;

                inflatedView = LayoutInflater.from(parent.getContext())
                        .inflate(layout, parent, false);
                holder = new EventHolder(inflatedView, this);
                break;

        }

        return holder;
    }

    public void updateList(List<EventVO> list){
        searchList.clear();
        searchList.addAll(list);
//        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case Utils.RECYCLE_TYPE_ADD:
                break;
            case Utils.RECYCLE_TYPE_NORMAL:
                EventHolder storyHolder = (EventHolder) holder;
                EventVO eventVO = eventVOs.get(position);
                storyHolder.title.setText(eventVO.getEventName());

                if (storyHolder.desc != null) {
                    storyHolder.desc.setText(eventVO.getEventDesc());
                    storyHolder.date.setText(Utils.getFormatedTime(eventVO.getEventDate()));
                } else {
                    //for home screen layout
                    storyHolder.date.setText(Utils.getFormatedTimeYear(eventVO.getEventDate()));
                }
                if (storyHolder.goToStory!= null && !TextUtils.isEmpty(eventVO.getStoryId())) {
                    storyHolder.goToStory.setVisibility(View.VISIBLE);
                }

                break;

        }

    }

    public void filter(String charText) {
//        if(charText.length()<=2)
//            return;
        charText = charText.toLowerCase(Locale.getDefault());
        eventVOs.clear();
        if (charText.length() == 0) {
            eventVOs.addAll(searchList);
        } else {
            for (EventVO eventVO : searchList) {
                if (eventVO.toString().contains(charText)) {
                    eventVOs.add(eventVO);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return eventVOs.size();
    }


    @Override
    public void onItemClick(int position, View v) {
        recyclerClickListener.onItemClick(position, v);
    }

    @Override
    public void onDefaultClick(View v) {
        recyclerClickListener.onDefaultClick(v);
    }

    @Override
    public void onEventEdit(View v, int position) {
        if (editListener != null)
            editListener.onEventEdit(v, position);
    }

    @Override
    public void goToStory(View v, int position) {
        if (goToStoryListener != null)
            goToStoryListener.goToStory(v, position);
    }
}
