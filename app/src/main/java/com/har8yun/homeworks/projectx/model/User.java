package com.har8yun.homeworks.projectx.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String id;
    private String mUsername;
    private UserInfo mUserInfo;
    private String mEmail;
    private Integer mPoints;
    private String mStatus;
    private List<Skill> mSkills;

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

    public List getSkills() {
        return mSkills;
    }

    public void setSkills(ArrayList skills) {
        mSkills = skills;
    }

    @Override
    public String toString() {
        return this.mUsername+"|"+this.mEmail;
    }

    public User() {
    }

}
