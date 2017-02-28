package com.arunsoorya.savethedate.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.arunsoorya.savethedate.utils.Utils;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by arunsoorya on 19/01/17.
 */
@IgnoreExtraProperties
public class StoryVO implements Parcelable {

    private String storyId;
    private String storyName;
    private String storyDate;
    private int viewType;
    private List<EventVO> eventIds;

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public String getStoryName() {
        return storyName;
    }

    public void setStoryName(String storyName) {
        this.storyName = storyName;
    }

    public String getStoryDesc() {
        return storyDesc;
    }

    public void setStoryDesc(String storyDesc) {
        this.storyDesc = storyDesc;
    }

    private String storyDesc;

    public StoryVO(String storyId, String storyName, String storyDesc, String date) {
        this.storyId = storyId;
        this.storyName = storyName;
        this.storyDesc = storyDesc;
        this.storyDate = date;
    }


    public void setEventIds(List<EventVO> eventIds) {
        this.eventIds = eventIds;
    }

//    public List<EventVO> getEventVOs() {
//        return eventVOs;
//    }
//
//    public void setEventVOs(List<EventVO> eventVOs) {
//        this.eventVOs = eventVOs;
//    }

    public StoryVO() {
    }
    @Exclude
    public Map<String, Object> toMap() {
        return toMap(null);
    }
    @Exclude
    public Map<String, Object> toMap(EventVO newEvent) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("storyId", storyId);
        result.put("storyName", storyName);
        result.put("storyDesc", storyDesc);
        result.put("storyDate", storyDate);
        if (newEvent != null) {
            result.put("eventIds", newEvent.toMap());
        }
        return result;
    }

    public String getStoryDate() {
        return storyDate;
    }

    public void setStoryDate(String storyDate) {
        this.storyDate = storyDate;
    }

    public List<EventVO> getEventIdMap() {
        if (eventIds == null){
            eventIds = new ArrayList<>();
        }
        return eventIds;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.storyId);
        dest.writeString(this.storyName);
        dest.writeString(this.storyDate);
        dest.writeTypedList(this.eventIds);
        dest.writeString(this.storyDesc);
        dest.writeInt(this.viewType);
    }

    protected StoryVO(Parcel in) {
        this.storyId = in.readString();
        this.storyName = in.readString();
        this.storyDate = in.readString();
        this.eventIds = in.createTypedArrayList(EventVO.CREATOR);
        this.storyDesc = in.readString();
        this.viewType = in.readInt();
    }

    public static final Creator<StoryVO> CREATOR = new Creator<StoryVO>() {
        @Override
        public StoryVO createFromParcel(Parcel source) {
            return new StoryVO(source);
        }

        @Override
        public StoryVO[] newArray(int size) {
            return new StoryVO[size];
        }
    };
    @Utils.RecycleViewType
    public int getViewType() {
        return viewType;
    }

    public void setViewType(@Utils.RecycleViewType int viewType) {
        this.viewType = viewType;
    }
}
