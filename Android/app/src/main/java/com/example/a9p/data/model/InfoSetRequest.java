package com.example.a9p.data.model;

import com.google.gson.annotations.SerializedName;

public class InfoSetRequest {

    @SerializedName("m2m:cin")
    public Content content;

    public InfoSetRequest(String nickname, String deviceId) {
        String json = "{ \"nickname\": \"" + nickname + "\", \"deviceId\": \"" + deviceId + "\" }";
        this.content = new Content(json);
    }

    public static class Content {
        public String con;

        public Content(String con) {
            this.con = con;
        }
    }
}