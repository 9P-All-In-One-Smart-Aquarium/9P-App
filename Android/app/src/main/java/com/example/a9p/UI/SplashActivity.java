package com.example.a9p.UI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a9p.UI.FrontActivity;
import com.example.a9p.UI.Login.InfoSetActivity;
import com.example.a9p.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "Splash";
    private ActivitySplashBinding binding;
    private final Handler handler = new Handler();

    private final Runnable navigate = () -> {
        String fcmToken = getSharedPreferences("AppPrefs", MODE_PRIVATE)
                .getString("FCM_TOKEN", null);

        if (fcmToken != null && !fcmToken.isEmpty()) {
            Log.d(TAG, "✅ Token Access Success → Move to FrontActivity");
            startActivity(new Intent(SplashActivity.this, FrontActivity.class));
        } else {
            Log.d(TAG, "❌ Token Access Failure → Move to InfoSetActivity");
            startActivity(new Intent(SplashActivity.this, InfoSetActivity.class));
        }
        finish();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        handler.postDelayed(navigate, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        handler.removeCallbacks(navigate);
        binding = null;
    }
}