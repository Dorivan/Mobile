package com.perminov.network.pojo;

import com.google.gson.annotations.SerializedName;

public class ScheduleFromServer {
    @SerializedName("thread")
    private Thread thread;
    @SerializedName("days")
    private String days;
    @SerializedName("departure")
    private String departure;

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }
}
