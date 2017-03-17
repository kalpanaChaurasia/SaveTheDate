package com.arunsoorya.savethedate.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.arunsoorya.savethedate.utils.Utils;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by arunsoorya on 19/01/17.
 */

public class EventVO implements Parcelable {

    private String eventId;

    public EventVO(String storyId, String eventName, String eventDesc, String eventDate) {
        this.storyId = storyId;
        this.eventName = eventName;
        this.eventDesc = eventDesc;
        this.eventDate = eventDate;
    }

    private String storyId;
    private String eventName;
    private String eventKey;
    private String eventDesc;
    private String eventDate;
    private String eventDateWithoutYear;
    private String eventImage;
    private int viewType;

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("storyId", storyId);
        result.put("eventName", eventName);
        result.put("eventDesc", eventDesc);
        result.put("eventDate", eventDate);
        result.put("eventKey", eventKey);
        result.put("eventDateWithoutYear", eventDateWithoutYear);
        return result;
    }
    public String getEventDateWithoutYear() {
        return eventDateWithoutYear;
    }

    public void setEventDateWithoutYear(String eventDateWithoutYear) {
        this.eventDateWithoutYear = eventDateWithoutYear;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventImage() {
        return eventImage;
    }

    public void setEventImage(String eventImage) {
        this.eventImage = eventImage;
    }

    public EventVO() {
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    @Utils.RecycleViewType
    public int getViewType() {
        return viewType;
    }

    public void setViewType(@Utils.RecycleViewType int viewType) {
        this.viewType = viewType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.eventId);
        dest.writeString(this.storyId);
        dest.writeString(this.eventName);
        dest.writeString(this.eventDesc);
        dest.writeString(this.eventDate);
        dest.writeString(this.eventDate);
        dest.writeString(this.eventKey);
        dest.writeString(this.eventDateWithoutYear);
        dest.writeInt(this.viewType);
    }

    protected EventVO(Parcel in) {
        this.eventId = in.readString();
        this.storyId = in.readString();
        this.eventName = in.readString();
        this.eventDesc = in.readString();
        this.eventDate = in.readString();
        this.eventImage = in.readString();
        this.eventKey = in.readString();
        this.eventDateWithoutYear = in.readString();
        this.viewType = in.readInt();
    }

    public static final Creator<EventVO> CREATOR = new Creator<EventVO>() {
        @Override
        public EventVO createFromParcel(Parcel source) {
            return new EventVO(source);
        }

        @Override
        public EventVO[] newArray(int size) {
            return new EventVO[size];
        }
    };

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    @Override
    public String toString() {
        return eventName.concat(" ").concat(eventDesc);
    }
}
