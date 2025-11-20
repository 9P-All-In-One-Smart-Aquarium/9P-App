package com.example.a9p.data.model;

import com.google.gson.annotations.SerializedName;

public class AquariumStatus {
    private float light;
    private float temperature;
    private float waterLevel;
    private float feeding = 0;

    public void setLight(float light) { this.light = light; }
    public void setTemperature(float temperature) { this.temperature = temperature; }
    public void setWaterLevel(float waterLevel) { this.waterLevel = waterLevel; }
    public float getFeeding() { return feeding; }


    public float getLight() { return light; }
    public float getTemperature() { return temperature; }
    public float getWaterLevel() { return waterLevel; }

    @Override
    public String toString() {
        return "AquariumStatus{" +
                "light=" + light +
                ", temperature=" + temperature +
                ", waterLevel=" + waterLevel +
                '}';
    }
}