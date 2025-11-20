
package com.example.a9p.UI.Login;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.a9p.UI.notification.NotificationLogActivity;
import com.example.a9p.data.network.ApiClient;
import com.example.a9p.databinding.ActivityModifyInfoBinding;

public class ModifyInfoActivity extends AppCompatActivity {
    private static final String TAG = "ModifyInfoActivity";
    private ActivityModifyInfoBinding binding;
    private SharedPreferences sharedPreferences;
    private String NickName, DeviceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityModifyInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        NickName = sharedPreferences.getString("NickName", "Unknown");
        DeviceID = sharedPreferences.getString("DeviceID", "Unknown");

        updateUI();

        binding.saveButton.setOnClickListener(v -> updateUserInfo());
    }

    private void updateUI() {
        binding.nowUserNickname.setText(NickName);
        binding.nowUserDeviceID.setText(DeviceID);
    }

    private void updateUserInfo() {
        String newNickName = binding.inputNewNickname.getText().toString().trim();
        String newDeviceID = binding.inputDeviceID.getText().toString().trim();

        if (TextUtils.isEmpty(newNickName) && TextUtils.isEmpty(newDeviceID)) {
            showToast("Doesn't need to update.");
            return;
        }

        String finalNickName = TextUtils.isEmpty(newNickName) ? null : newNickName;
        String finalDeviceID = TextUtils.isEmpty(newDeviceID) ? null : newDeviceID;

        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if (finalNickName != null) editor.putString("NickName", finalNickName);
        if (finalDeviceID != null) editor.putString("DeviceID", finalDeviceID);
        editor.apply();

        Log.d("ProfileUpdate", "Success update - NickName: " + finalNickName + ", DeviceID changed: " + (finalDeviceID != null));
        showToast("User information updated.");

        finish();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}