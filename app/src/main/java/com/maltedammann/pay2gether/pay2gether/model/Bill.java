package com.maltedammann.pay2gether.pay2gether.model;

import java.util.List;

/**
 * Created by damma on 26.10.2016.
 */

public class Bill {
    private String id;
    private String title;
    private float amount;
    private String eventId;
    private String ownerId;
    private List<String> belongersId;

    public Bill() {
    }

    public Bill(String title, String eventid) {
        this.title = title;
        this.eventId = eventid;
    }

    public Bill(float amount, String title, String eventid) {
        this.amount = amount;
        this.title = title;
        this.eventId = eventid;
    }

    public Bill(float amount, String title, String eventid, User owner) {
        this.amount = amount;
        this.title = title;
        this.eventId = eventid;
        this.ownerId = owner.getId();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float betrag) {
        this.amount = betrag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String event) {
        this.eventId = event;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(User owner) {
        this.ownerId = owner.getId();
    }

    public List<String> getBelongersId() {
        return belongersId;
    }

    public void setBelongersId(List<String> belongers) {
        this.belongersId = belongers;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "id='" + id + '\'' +
                ", amount=" + amount +
                ", title='" + title + '\'' +
                ", event=" + eventId +
                ", owner=" + ownerId +
                ", belongers=" + belongersId +
                '}';
    }
}

