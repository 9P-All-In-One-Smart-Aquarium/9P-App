package com.example.a9p.data.model;

import com.google.gson.annotations.SerializedName;

public class NotificationItem {
    @SerializedName("title")
    private String title;
    @SerializedName("message")
    private String message;
    @SerializedName("timestamp")
    private String timestamp;

    public NotificationItem(String title, String message, String timestamp) {
        this.title = title;
        this.message = message;
        this.timestamp = timestamp;
    }

    // Getter
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public String getTimestamp() { return timestamp; }
}