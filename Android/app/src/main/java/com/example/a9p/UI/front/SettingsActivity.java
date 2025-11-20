package com.example.a9p.UI.front;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.a9p.UI.FrontActivity;
import com.example.a9p.UI.Login.InfoSetActivity;
import com.example.a9p.UI.Login.ModifyInfoActivity;
import com.example.a9p.UI.notification.NotificationLogActivity;
import com.example.a9p.databinding.ActivitySettingsBinding;

import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsLog";
    private ActivitySettingsBinding binding;
    private SharedPreferences sharedPreferences;
    private String DeviceID;
    private String NickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // 다크 모드 비활성화

        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        DeviceID = sharedPreferences.getString("DeviceID", "디바이스 정보 없음");
        NickName = sharedPreferences.getString("NickName", "이메일 정보 없음");

        Log.d(TAG, "DeviceID: " + DeviceID);
        Log.d(TAG, "NickName: " + NickName);

        setupUIListeners();
    }

    private void setupUIListeners() {
        binding.btnNotificationLog.setOnClickListener(v -> startNewActivity(NotificationLogActivity.class));
        binding.btnDeviceReset.setOnClickListener(v -> showDeleteDeviceDialog());
        binding.btnModifyInfoReset.setOnClickListener(v -> startNewActivity(ModifyInfoActivity.class));
        binding.appLogo.setOnClickListener(v -> goToHomeScreen());
    }

    private void showDeleteDeviceDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Reset Device")
                .setMessage("Are you sure you want to reset? The device will be restored to its default settings.\n")
                .setPositiveButton("Reset", (dialog, which) -> deleteDevice())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteDevice() {
        if (NickName.isEmpty() || DeviceID.isEmpty()) {
            showToast("User or device information cannot be found.");
            return;
        }

        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        prefs.edit().clear().apply();
        Log.d(TAG, "✅ [Local] Device reset successful (no server communication)");
        showToast("The device has been reset locally.");
        startNewActivity(InfoSetActivity.class);
    }

    private void goToHomeScreen() {
        Intent intent = new Intent(this, FrontActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private void startNewActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}