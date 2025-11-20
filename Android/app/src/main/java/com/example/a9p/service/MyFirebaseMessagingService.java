package com.example.a9p.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.a9p.R;
import com.example.a9p.UI.FrontActivity;
import com.example.a9p.data.model.NotificationItem;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCMService";
    private static final String PREFS_NAME = "NotificationLog";
    private static final String KEY_NOTIFICATIONS = "notifications";
    private static final SimpleDateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d("FCM", "📩 Push Message Reception: " + remoteMessage.getData());

        String title = "Notificatin";
        String message = "A new message has arrived.";

        if (remoteMessage.getNotification() != null) {
            title = remoteMessage.getNotification().getTitle();
            message = remoteMessage.getNotification().getBody();
            Log.d("FCM", "🔔 Notification Message: " + title + " - " + message);
        }

        if (remoteMessage.getData().size() > 0) {
            try {
                JSONObject jsonData = new JSONObject(remoteMessage.getData());
                Log.d("FCM", "📦 recived JSON data: " + jsonData.toString());

                if (jsonData.has("alerts")) {
                    JSONArray alertsArray = new JSONArray(jsonData.getString("alerts"));
                    StringBuilder alertMessage = new StringBuilder();

                    for (int i = 0; i < alertsArray.length(); i++) {
                        alertMessage.append("- ").append(alertsArray.getString(i)).append("\n");
                    }

                    message += "\n📌 additional notification:\n" + alertMessage.toString();
                }

            } catch (JSONException e) {
                Log.e("FCM", "❌ JSON passing error", e);
            }
        }

        saveNotification(title, message);

        sendUpdateBroadcast();

        sandNotification(title, message);
    }

    private void saveNotification(String title, String message) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();

        NotificationItem item = new NotificationItem(
                title,
                message,
                dateFormat.format(new Date())
        );

        String json = prefs.getString(KEY_NOTIFICATIONS, "");
        Type type = new TypeToken<ArrayList<NotificationItem>>() {}.getType();
        ArrayList<NotificationItem> notificationList = gson.fromJson(json, type);

        if (notificationList == null) {
            notificationList = new ArrayList<>();
        }

        notificationList.add(0, item);

        if (notificationList.size() > 50) {
            notificationList.remove(notificationList.size() - 1);
        }

        SharedPreferences.Editor editor = prefs.edit();
        String serialized = gson.toJson(notificationList);
        editor.putString(KEY_NOTIFICATIONS, serialized);
        editor.apply();

        Log.d(TAG, "✅ Notification saved. (ISO 8601)");
    }

    private void sendUpdateBroadcast() {
        Intent intent = new Intent("com.example.a9p.NOTIFICATION_UPDATED");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        Log.d(TAG, "📡 Notification update Broadcast sended.");
    }

    private void sandNotification(String title, String message) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String channelId = "default_channel";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId, "Push Notification", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("You received this push notification from the app.");
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(this, FrontActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
        Log.d(TAG, "✅ Notification displayed");
    }
}
