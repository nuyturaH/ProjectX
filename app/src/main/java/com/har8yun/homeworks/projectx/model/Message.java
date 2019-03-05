package com.har8yun.homeworks.projectx.model;

public class Message
{
    private String text;
    private User mReciever;
    private User mSender;
    private boolean isImage;

    public User getmSender() {
        return mSender;
    }

    public void setmSender(User mSender) {
        this.mSender = mSender;
    }



    public boolean isImage() {
        return isImage;
    }

    public void setImage(boolean image) {
        isImage = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getmReciever() {
        return mReciever;
    }

    public void setmReciever(User mReciever) {
        this.mReciever = mReciever;
    }
}
