package com.perminov.network.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SchedulesResponse {
    @SerializedName("event")
    @Expose
    private String event;
    @SerializedName("schedule")
    @Expose
    private List<ScheduleFromServer> schedules = null;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public List<ScheduleFromServer> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<ScheduleFromServer> schedule) {
        this.schedules = schedule;
    }
}