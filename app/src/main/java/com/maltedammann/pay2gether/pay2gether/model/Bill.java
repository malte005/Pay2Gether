package com.maltedammann.pay2gether.pay2gether.model;

import java.util.List;

/**
 * Created by damma on 26.10.2016.
 */

public class Bill {
    private String id;
    private float amount;
    private String title;
    private Event event;
    private User owner;
    private List<User> belongers;

    public Bill() {
    }

    public Bill(float amount, String title, Event event) {
        this.amount = amount;
        this.title = title;
        this.event = event;
    }

    public Bill(float betrag, String title, Event event, User owner) {
        this.amount = betrag;
        this.title = title;
        this.event = event;
        this.owner = owner;
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

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<User> getBelongers() {
        return belongers;
    }

    public void setBelongers(List<User> belongers) {
        this.belongers = belongers;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "id='" + id + '\'' +
                ", amount=" + amount +
                ", title='" + title + '\'' +
                ", event=" + event +
                ", owner=" + owner +
                ", belongers=" + belongers +
                '}';
    }
}

