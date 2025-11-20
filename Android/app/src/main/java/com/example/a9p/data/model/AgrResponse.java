package com.example.a9p.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AgrResponse {

    @SerializedName("m2m:agr")
    public Agr agr;

    public static class Agr {
        @SerializedName("m2m:rsp")
        public List<RspItem> rsp;
    }

    public static class RspItem {
        public String fr;
        public Pc pc;
    }

    public static class Pc {
        @SerializedName("m2m:cin")
        public Cin cin;
    }

    public static class Cin {
        public float con;   // 값은 number이므로 float
        public String ct;   // timestamp
    }
}
