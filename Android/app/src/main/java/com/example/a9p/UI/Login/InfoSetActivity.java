package com.example.a9p.UI.Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.a9p.UI.FrontActivity;
import com.example.a9p.data.model.InfoSetRequest;
import com.example.a9p.data.model.TokenUpdateRequest;
import com.example.a9p.data.network.ApiClient;
import com.example.a9p.data.network.ApiService;
import com.example.a9p.databinding.ActivityInfosetBinding;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoSetActivity extends AppCompatActivity {
    private static final String TAG = "InfoSetActivity";
    private ApiService apiService;

    private ActivityInfosetBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityInfosetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        apiService = ApiClient.getApiService(this);

        binding.StartButton.setOnClickListener(v -> attemptSetInfo());
    }

    private void attemptSetInfo() {
        String nickname = binding.inputNickname.getText().toString().trim();
        String devicename = binding.inputDeviceName.getText().toString().trim();

        if (TextUtils.isEmpty(nickname) || TextUtils.isEmpty(devicename)) {
            showToast("Please fill in all fields.");
            return;
        }

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String fcmToken = task.getResult();
                Log.d(TAG, "▶ Generated FCM token: " + fcmToken);

                // ✅ 로컬 저장
                getSharedPreferences("AppPrefs", MODE_PRIVATE)
                        .edit()
                        .putString("FCM_TOKEN", fcmToken)
                        .putString("NickName", nickname)
                        .putString("DeviceID", devicename)
                        .apply();

                // ✅ Mobius 전송
                sendFcmTokenToMobius(fcmToken, nickname, devicename);

            } else {
                showToast("Failed to get FCM token: " + task.getException().getMessage());
            }
        });
    }

    private void sendFcmTokenToMobius(String fcmToken, String nickname, String devicename) {
        TokenUpdateRequest request = new TokenUpdateRequest(fcmToken);

        apiService.updateFcmToken(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "✅ Send FCM token to Mobius (" + fcmToken.length() + " length)");
                    showToast("Mobius registration completed");
                    sendUserInfoToMobius(nickname, devicename);
                    goToFrontActivity();
                } else {
                    Log.e(TAG, "❌ Mobius response error: " + response.code());
                    showToast("Mobius error code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "❌ Failed to send FCM token to Mobius", t);
                showToast("Failed to send FCM token: " + t.getMessage());
            }
        });
    }

    private void sendUserInfoToMobius(String nickname, String deviceId) {
        InfoSetRequest request = new InfoSetRequest(nickname, deviceId);

        apiService.sendUserInfo(request).enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "▶ 사용자 정보 Mobius 등록 성공");
                    showToast("사용자 정보 등록 완료");
                    goToFrontActivity();
                } else {
                    Log.e(TAG, "❌ 사용자 정보 전송 실패 - 코드: " + response.code());
                    showToast("서버 오류: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "❌ Failed to send user info to Mobius", t);
                showToast("Failed to send user info: " + t.getMessage());
            }
        });
    }

    private void goToFrontActivity() {
        startActivity(new Intent(InfoSetActivity.this, FrontActivity.class));
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}