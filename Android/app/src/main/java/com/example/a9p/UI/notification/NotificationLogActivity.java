package com.example.a9p.UI.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a9p.R;
import com.example.a9p.UI.FrontActivity;
import com.example.a9p.data.model.NotificationItem;
import com.example.a9p.databinding.ActivityNotificationLogBinding;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationLogActivity extends AppCompatActivity {

    private static final String TAG = "NotificationLogActivity";
    private static final String PREFS_NAME = "NotificationLog";
    private static final String KEY_NOTIFICATIONS = "notifications";

    private ActivityNotificationLogBinding binding;
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private Spinner spinnerSort;
    private Spinner spinnerDateFilter;
    private SearchView searchView;
    private Button btnDeleteAll;
    private final Gson gson = new Gson();

    private List<NotificationItem> notificationList = new ArrayList<>();
    private List<NotificationItem> filteredList = new ArrayList<>();

    private static final SimpleDateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());

    private final BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "📡 NotificationReceiver received");
            loadNotifications();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNotificationLogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeUI();

        loadNotifications();

        LocalBroadcastManager.getInstance(this).registerReceiver(notificationReceiver,
                new IntentFilter("com.example.a9p.NOTIFICATION_UPDATED"));

        binding.appLogo.setOnClickListener(v -> goToHomeScreen());
    }

    private void initializeUI() {
        recyclerView = binding.recyclerView;
        searchView = binding.searchView;
        btnDeleteAll = binding.btnDeleteAll;
        spinnerDateFilter = binding.spinnerDateFilter;
        spinnerSort = binding.spinnerSort;

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        enableSwipeToDelete();

        setupSpinners();
        setupSearchView();
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> dateAdapter = ArrayAdapter.createFromResource(
                this, R.array.date_filter_options, android.R.layout.simple_spinner_item);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDateFilter.setAdapter(dateAdapter);

        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(
                this, R.array.sort_options, android.R.layout.simple_spinner_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(sortAdapter);

        spinnerDateFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyAllFilters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean isFirstSelection = true;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirstSelection) {
                    isFirstSelection = false;
                    return;
                }
                applyAllFilters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnDeleteAll.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                applyAllFilters();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                applyAllFilters();
                return true;
            }
        });
    }

    private Date parseTimestamp(String timestamp) {
        try {
            return dateFormat.parse(timestamp);
        } catch (ParseException e) {
            Log.e(TAG, "❌ Timestamp parsing error: " + timestamp, e);
            return null;
        }
    }

    private void loadNotifications() {
        Log.d(TAG, "📥 Start loading notifications...");
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String json = prefs.getString(KEY_NOTIFICATIONS, "");

        if (!json.isEmpty()) {
            Type type = new TypeToken<ArrayList<NotificationItem>>() {}.getType();
            try {
                notificationList = gson.fromJson(json, type);
            } catch (JsonSyntaxException e) {
                Log.e(TAG, "🚨 JSON parsing error: " + e.getMessage(), e);
                notificationList = new ArrayList<>();
            }
        } else {
            Toast.makeText(this, "No notifications", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "⚠️ No notifications");
        }

        filteredList = new ArrayList<>(notificationList);
        adapter = new NotificationAdapter(filteredList);
        recyclerView.setAdapter(adapter);
    }

    private void applyAllFilters() {
        filterNotifications(searchView.getQuery().toString());
        filterByDate(spinnerDateFilter.getSelectedItemPosition());
        sortNotifications(spinnerSort.getSelectedItemPosition());
        adapter.notifyDataSetChanged();
    }

    private void filterByDate(int filterOption) {
        filteredList.clear();
        Date startDate = getDateFilter();
        Date now = new Date();

        for (NotificationItem item : notificationList) {
            Date itemDate = parseTimestamp(item.getTimestamp());
            if (itemDate != null && (startDate == null || (!itemDate.before(startDate) && !itemDate.after(now)))) {
                filteredList.add(item);
            }
        }
    }

    private Date getDateFilter() {
        long currentTime = System.currentTimeMillis();
        switch (spinnerDateFilter.getSelectedItemPosition()) {
            case 1: return new Date(currentTime - 24 * 60 * 60 * 1000);
            case 2: return new Date(currentTime - 7 * 24 * 60 * 60 * 1000);
            case 3: return new Date(currentTime - 30 * 24 * 60 * 60 * 1000);
            default: return null;
        }
    }

    private void sortNotifications(int sortOption) {
        Collections.sort(filteredList, (a, b) -> {
            Date dateA = parseTimestamp(a.getTimestamp());
            Date dateB = parseTimestamp(b.getTimestamp());

            if (dateA == null || dateB == null) return 0;
            return sortOption == 0 ? dateB.compareTo(dateA) : dateA.compareTo(dateB);
        });
    }

    private void filterNotifications(String query) {
        filteredList.clear();
        for (NotificationItem item : notificationList) {
            if (item.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    item.getMessage().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete All")
                .setMessage("Are you sure you want to delete all notifications?")
                .setPositiveButton("Delete", (dialogInterface, which) -> deleteAllNotifications())
                .setNegativeButton("Cancel", null)
                .create().show();
    }

    private void deleteAllNotifications() {
        Log.d(TAG, "🗑 Deleting all notifications...");
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_NOTIFICATIONS);
        editor.apply();

        notificationList.clear();
        filteredList.clear();
        adapter.notifyDataSetChanged();

        Toast.makeText(this, "Deleted all notifications", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "✅ Deleste all notifications");
    }

    private void enableSwipeToDelete() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        adapter.removeItem(position);
                        saveNotifications();
                        Toast.makeText(NotificationLogActivity.this, "Notification deleted", Toast.LENGTH_SHORT).show();
                    }
                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void saveNotifications() {
        Log.d(TAG, "💾 Saving notifications...");
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_NOTIFICATIONS, gson.toJson(notificationList));
        editor.apply();
        Log.d(TAG, "✅ Successfully saved notifications");
    }

    private void goToHomeScreen() {
        Intent intent = new Intent(this, FrontActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(notificationReceiver);
        super.onDestroy();
    }
}
