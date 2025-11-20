package com.example.a9p.data.model;

import com.google.gson.annotations.SerializedName;

public class TokenUpdateRequest {

    @SerializedName("m2m:cin")
    public Content content;

    public TokenUpdateRequest(String token) {
        this.content = new Content(token);
    }

    public static class Content {
        public String con;

        public Content(String con) {
            this.con = con;
        }
    }
}