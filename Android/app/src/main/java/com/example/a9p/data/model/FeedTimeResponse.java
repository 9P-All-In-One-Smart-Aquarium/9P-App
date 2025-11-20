package com.example.a9p.data.model;

import com.google.gson.annotations.SerializedName;

public class FeedTimeResponse {
    @SerializedName("m2m:cin")
    public Cin cin;

    public static class Cin {
        public String con;
    }
}