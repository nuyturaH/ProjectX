package com.har8yun.homeworks.projectx.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class UserInfo implements Parcelable {
    private String firstName;
    private String lastName;
    private String avatar;
    private Date birthDate;
    private Integer gender;
    private Float weight;
    private Float height;

    public UserInfo() {
    }

    private UserInfo(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
//        avatar = in.readParcelable(Uri.class.getClassLoader());
        avatar = in.readString();
        gender = in.readInt();
        weight = in.readFloat();
        height = in.readFloat();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
//        dest.writeParcelable(avatar, flags);
        dest.writeString(avatar);
        dest.writeInt(gender);
        dest.writeFloat(weight);
        dest.writeFloat(height);
    }

//    public Uri getAvatar() {
//        return avatar;
//    }
//
//    public void setAvatar(Uri avatar) {
//        this.avatar = avatar;
//    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }


}
