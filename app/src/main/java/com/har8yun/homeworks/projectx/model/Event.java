package com.har8yun.homeworks.projectx.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.List;

public class Event
{
    private String uid;
    private String title;
    private String description;
    private LatLng position;
    private String place;
    private User creator;
    private List<User> participants;
    private Date date;

    public Event() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }


    @Override
    public String toString() {
        return position +
                creator.getUsername()
                + title;
    }
}
