package com.example.a9p.data.network;

import com.example.a9p.data.model.AgrResponse;
import com.example.a9p.data.model.FeedTimeResponse;
import com.example.a9p.data.model.FeedTimeUpdateRequest;
import com.example.a9p.data.model.InfoSetRequest;
import com.example.a9p.data.model.PumpRequest;
import com.example.a9p.data.model.TokenUpdateRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    @Headers({
            "Accept: application/json",
            "X-M2M-Origin: C-App",
            "X-M2M-RI: 1001",
            "Content-Type: application/json;ty=4"
    })
    @POST("/Mobius/AE-App/fcm-token")
    Call<Void> updateFcmToken(
            @Body TokenUpdateRequest request);

    @Headers({
            "Accept: application/json",
            "X-M2M-Origin: C-App",
            "X-M2M-RI: 1001",
            "Content-Type: application/json;ty=4"
    })
    @POST("/Mobius/AE-App/userinfo")
    Call<Void> sendUserInfo(
            @Body InfoSetRequest request);

    @Headers({
            "Accept: application/json",
            "X-M2M-Origin: C-App",
            "X-M2M-RI: 1001",
    })
    @GET("/Mobius/AE-Sensor/grp-Sensor-latest/fopt")
    Call<AgrResponse> getAquariumStatus();

    @Headers({
            "Accept: application/json",
            "X-M2M-Origin: C-App",
            "X-M2M-RI: 1001"
    })
    @GET("/Mobius/AE-App/feed-time/la")
    Call<FeedTimeResponse> getFeedTime();

    @Headers({
            "Accept: application/json",
            "X-M2M-Origin: C-App",
            "X-M2M-RI: 1001",
            "Content-Type: application/json;ty=4"
    })
    @POST("/Mobius/AE-Actuator/pump")
    Call<Void> sendPumpOnRequest(@Body PumpRequest request);
}

