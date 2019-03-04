package com.har8yun.homeworks.projectx.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class Sport implements Parcelable {
    private String mName;
    private HashMap mSkills;

    private Sport(Parcel in) {
        mName = in.readString();
        mSkills = in.readHashMap(String.class.getClassLoader());
    }

    public static final Creator<Sport> CREATOR = new Creator<Sport>() {
        @Override
        public Sport createFromParcel(Parcel in) {
            return new Sport(in);
        }

        @Override
        public Sport[] newArray(int size) {
            return new Sport[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeMap(mSkills);
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public HashMap getSkills() {
        return mSkills;
    }

    public void setSkills(HashMap skills) {
        mSkills = skills;
    }
}
