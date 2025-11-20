package com.example.a9p.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.a9p.UI.front.MyFishbowlMonitorActivity;
import com.example.a9p.UI.front.SettingsActivity;
import com.example.a9p.databinding.ActivityFrontBinding;

public class FrontActivity extends AppCompatActivity {
    private static final String TAG = "Front";

    private ActivityFrontBinding binding;
    private String NickName, DeviceID, FishSpecies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityFrontBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadUserInfo();
        updateUI();

        setupClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserInfo();
        updateUI();
    }

    private void loadUserInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        NickName = sharedPreferences.getString("NickName", "User");
        DeviceID = sharedPreferences.getString("DeviceID", "Unknown");
        FishSpecies = sharedPreferences.getString("FishSpeciesKO", "(Unknown)");
    }

    private void updateUI() {
        binding.helloSubText.setText(String.format("%s", NickName));
        binding.deviceName.setText(DeviceID);
        binding.fishSpecies.setText(FishSpecies);
    }

    private void setupClickListeners() {
        binding.myFishbowl.setOnClickListener(v -> startNewActivity(MyFishbowlMonitorActivity.class));
        binding.Settings.setOnClickListener(v -> startNewActivity(SettingsActivity.class));
    }

    private void startNewActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }
}