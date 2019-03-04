package com.har8yun.homeworks.projectx.model;


import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private Long id;
    private String mUsername;
    private UserInfo mUserInfo;
    private String mEmail;
    //private String password;
    private Integer mPoints;
    private String mStatus;
    private Sport mSport;//sport12345

    public Long getId() {
        return id;
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

//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }


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

    public Sport getSport() {
        return mSport;
    }

    public void setSport(Sport sport) {
        this.mSport = sport;
    }


    @Override
    public String toString() {
        return this.mUsername;
    }

    public User() {
    }

    private User(Parcel in) {
//        id = in.readLong();
        mSport = in.readParcelable(Sport.class.getClassLoader());
        mUserInfo = in.readParcelable(UserInfo.class.getClassLoader());
        mUsername = in.readString();
        mEmail = in.readString();
        mStatus = in.readString();
        mPoints = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeLong(id);
        dest.writeParcelable(mSport, flags);
        dest.writeParcelable(mUserInfo, flags);
        dest.writeString(mUsername);
        dest.writeString(mEmail);
        dest.writeString(mStatus);
        dest.writeInt(mPoints);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
