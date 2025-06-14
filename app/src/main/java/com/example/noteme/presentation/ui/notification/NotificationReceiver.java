package com.example.noteme.presentation.ui.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import com.example.noteme.R;
import com.example.noteme.presentation.ui.activity.MainActivity;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Buat Intent untuk membuka aplikasi saat notifikasi diklik
        Intent mainIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);

        // Buat notifikasi
        Notification notification = new NotificationCompat.Builder(context, "noteChannel")
                .setContentTitle("Notifikasi Catatan")
                .setContentText("Waktunya melihat catatan Anda!")
                .setSmallIcon(R.drawable.ic_note) // Ganti dengan ikon Anda
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        // Mengirim notifikasi
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}
