package com.har8yun.homeworks.projectx.model;

public class MyLatLng
{
    private Double latitude;
    private Double longitude;

    public MyLatLng() {}

    public MyLatLng(double v1,double v2)
    {
        latitude = v1;
        longitude = v2;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
