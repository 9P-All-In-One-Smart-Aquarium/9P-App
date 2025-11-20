package com.example.a9p.data.model;

import com.google.gson.annotations.SerializedName;

public class FishBowlSettings {
    @SerializedName("DeviceID")
    private String deviceID;
    @SerializedName("Light")
    private float light;
    @SerializedName("Temperature")
    private float temperature;
    @SerializedName("Feeding")
    private float feeding;

    public FishBowlSettings() {
    }
    public FishBowlSettings(String deviceID, float light, float temperature) {
        this.deviceID = deviceID;
        this.light = light;
        this.temperature = temperature;
    }

    public String getDeviceID() { return deviceID; }
    public float getLight() { return light; }
    public float getTemperature() { return temperature; }
    public float getFeeding() { return feeding; }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public void setLight(float light) {
        this.light = light;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public void setFeeding(float feeding) {
        this.feeding = feeding;
    }
}