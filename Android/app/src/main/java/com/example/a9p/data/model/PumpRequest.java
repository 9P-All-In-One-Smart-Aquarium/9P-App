package com.example.a9p.data.model;

import com.google.gson.annotations.SerializedName;

public class PumpRequest {

    @SerializedName("m2m:cin")
    public Content content;

    public PumpRequest(String pumpState) {
        this.content = new Content(pumpState);
    }

    public static class Content {
        public String con;

        public Content(String con) {
            this.con = con;
        }
    }
}