package com.maltedammann.pay2gether.pay2gether.model;

import java.util.List;

/**
 * Created by damma on 26.10.2016.
 */

public class Event {
    private String id;
    private String date;
    private String time;
    private String title;
    private List<User> participants;
    private List<Bill> bills;

    public Event() {
    }

    public Event(String title) {
        this.title = title;
    }

    public Event(String date, String title) {
        this.date = date;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", date=" + date +
                ", time=" + time +
                ", title='" + title + '\'' +
                ", participants=" + participants +
                ", bills=" + bills +
                '}';
    }
}

