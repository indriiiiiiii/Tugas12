package com.example.noteme.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.noteme.R;

public class NotificationUtils {

    private static final String CHANNEL_ID = "note_channel";
    private static final int NOTIFICATION_ID = 1;

    public static void createNotification(Context context, String title, String message) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "NoteMe Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.setLightColor(Color.parseColor("#800000"));
            channel.enableVibration(true);
            nm.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_note)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true);

        nm.notify(NOTIFICATION_ID, builder.build());
    }
}
