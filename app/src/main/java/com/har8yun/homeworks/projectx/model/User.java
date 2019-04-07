package com.har8yun.homeworks.projectx.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class User {

    private String id;
    private String mUsername;
    private UserInfo mUserInfo;
    private String mEmail;
    private Integer mPoints;
    private String mStatus;
    private Map<String, Integer> mSkills;
    private List<String> mImages = new ArrayList<>();
    private List<String> mGoingEvents = new ArrayList<>();

    public List<String> getmGoingEvents() {
        return mGoingEvents;
    }

    public void setmGoingEvents(List<String> mGoingEvents) {
        this.mGoingEvents = mGoingEvents;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        this.mUsername = username;
    }

    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.mUserInfo = userInfo;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public Integer getPoints() {
        return mPoints;
    }

    public void setPoints(Integer points) {
        this.mPoints = points;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        this.mStatus = status;
    }

    public Map<String, Integer> getSkills() {
        return mSkills;
    }

    public void setSkills(Map<String, Integer> skills) {
        mSkills = skills;
    }

    public List<String> getImages() {
        return mImages;
    }

    public void setImages(List<String> mImages) {
        this.mImages = mImages;
    }

    @Override
    public String toString() {
        return this.mUsername+"|"+this.mEmail;
    }

    public User() {
    }

}
