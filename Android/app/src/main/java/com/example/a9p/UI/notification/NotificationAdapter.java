package com.example.a9p.UI.notification;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a9p.R;
import com.example.a9p.data.model.NotificationItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private static final String TAG = "NotificationAdapter";
    private final List<NotificationItem> notificationList;

    private static final SimpleDateFormat inputFormat =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
    private static final SimpleDateFormat outputFormat =
            new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault());

    public NotificationAdapter(List<NotificationItem> notificationList) {
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationItem notification = notificationList.get(position);

        holder.titleTextView.setText(notification.getTitle());
        holder.messageTextView.setText(notification.getMessage());
        holder.timestampTextView.setText(formatTimestamp(notification.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public void removeItem(int position) {
        if (position >= 0 && position < notificationList.size()) {
            notificationList.remove(position);
            notifyItemRemoved(position);
        }
    }

    private String formatTimestamp(String timestamp) {
        try {
            Date date = inputFormat.parse(timestamp);
            return (date != null) ? outputFormat.format(date) : "Timestamp Error";
        } catch (ParseException e) {
            Log.e(TAG, "❌ Failed to parse timestamp: " + timestamp, e);
            return "Timestamp Error";
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, messageTextView, timestampTextView;

        ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.notificationTitle);
            messageTextView = itemView.findViewById(R.id.notificationMessage);
            timestampTextView = itemView.findViewById(R.id.notificationTimestamp);
        }
    }
}