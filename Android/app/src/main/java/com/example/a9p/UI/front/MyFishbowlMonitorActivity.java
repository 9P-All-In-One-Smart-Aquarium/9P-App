package com.example.a9p.UI.front;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.a9p.UI.FrontActivity;
import com.example.a9p.data.model.AgrMapper;
import com.example.a9p.data.model.AgrResponse;
import com.example.a9p.data.model.AquariumStatus;
import com.example.a9p.data.model.FeedTimeResponse;
import com.example.a9p.data.model.PumpRequest;
import com.example.a9p.data.network.ApiClient;
import com.example.a9p.data.network.ApiService;
import com.example.a9p.databinding.ActivityMyFishbowlMonitorBinding;
import com.example.a9p.databinding.DialogPumpControlBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFishbowlMonitorActivity extends AppCompatActivity {

    private static final String TAG = "FishbowlMonitor";
    private ApiService apiService;
    private ActivityMyFishbowlMonitorBinding binding;
    private String DeviceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        apiService = ApiClient.getApiService(this);

        binding = ActivityMyFishbowlMonitorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupUIListeners();

        loadUserInfo();
        fetchAquariumStatus();
        fetchFeedTime();
    }

    private void setupUIListeners() {
        binding.appLogo.setOnClickListener(v -> goToHomeScreen());
        binding.refundBtn.setOnClickListener(v -> showPumpControlDialog());
    }

    private void loadUserInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        DeviceID = sharedPreferences.getString("DeviceID", "DeviceNONE");

        if (DeviceID == null || DeviceID.isEmpty() || DeviceID.equals("DeviceNONE")) {
            Toast.makeText(this, "No device information found. Returning to the main screen.", Toast.LENGTH_SHORT).show();
            goToHomeScreen();
            return;
        }

        binding.deviceName.setText(DeviceID);
        binding.fishSpecies.setText(sharedPreferences.getString("FishSpeciesKO", "(Unknown)"));

    }

    private void fetchAquariumStatus() {
        apiService.getAquariumStatus().enqueue(new Callback<AgrResponse>() {
            @Override
            public void onResponse(Call<AgrResponse> call, Response<AgrResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    AquariumStatus status = AgrMapper.toAquariumStatus(response.body());

                    updateAquariumUI(status);
                } else {
                    Log.e(TAG, "❌ server responce error: " + response.code());
                    showToast("Server responce error. Please try again.");
                }
            }

            @Override
            public void onFailure(Call<AgrResponse> call, Throwable t) {
                Log.e(TAG, "❌ Network error!", t);
                showToast("Network error! Cannot fetch data.");
            }
        });
    }

    private void fetchFeedTime() {
        apiService.getFeedTime().enqueue(new Callback<FeedTimeResponse>() {
            @Override
            public void onResponse(Call<FeedTimeResponse> call,
                                   Response<FeedTimeResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    int[] times = AgrMapper.mapFeedTime(response.body());

                    if (times != null) {
                        binding.feedingTermdata.setText(times[0] + "H, " + times[1] + "H");
                    } else {
                        binding.feedingTermdata.setText("No Time Data");
                    }
                }
            }

            @Override
            public void onFailure(Call<FeedTimeResponse> call, Throwable t) {
                Log.e(TAG, "Feed-time error", t);
            }
        });
    }

    private void updateAquariumUI(AquariumStatus status) {
        binding.LightSetdata.setText(String.format("%.1f Lux", status.getLight()));
        binding.tempdata.setText(String.format("%.1f °C", status.getTemperature()));
        Log.d(TAG, "✅ Fish Tnk UI update success.");
    }

    private void showPumpControlDialog() {
        DialogPumpControlBinding dialogBinding = DialogPumpControlBinding.inflate(getLayoutInflater());

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogBinding.getRoot())
                .create();

        dialogBinding.pumpControlButtonOn.setOnClickListener(v -> {
            sendPumpState("on");
            dialog.dismiss();
        });

        dialogBinding.pumpControlButtonOff.setOnClickListener(v -> {
            sendPumpState("off");
            dialog.dismiss();
        });

        dialog.show();
    }


    private void sendPumpState(String state) {
        apiService.sendPumpOnRequest(new PumpRequest(state)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    if ("on".equalsIgnoreCase(state)) {
                        showToast("Pump is on!");
                    } else {
                        showToast("Pump is off!");
                    }
                } else {
                    showToast("Pump doesn't work. (Code: " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                showToast("Network error! Failed to send pump state.");
            }
        });
    }

    private void goToHomeScreen() {
        Intent intent = new Intent(this, FrontActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
