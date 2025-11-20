package com.example.a9p.data.model;

import com.google.gson.annotations.SerializedName;

public class FeedTimeUpdateRequest {

    @SerializedName("m2m:cin")
    public Content content;

    public FeedTimeUpdateRequest(String feedTimeJson) {
        this.content = new Content(feedTimeJson);
    }

    public static class Content {
        public String con;   // Mobius에 저장되는 실제 값

        public Content(String con) {
            this.con = con;
        }
    }
}