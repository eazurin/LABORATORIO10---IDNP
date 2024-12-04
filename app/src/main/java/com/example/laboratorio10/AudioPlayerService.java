package com.example.laboratorio10;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class AudioPlayerService extends Service {
    private static final String CHANNEL_ID = "AudioPlayerChannel";
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
        mediaPlayer = new MediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();

        if (action != null) {
            switch (action) {
                case "START":
                    startForeground(1, createNotification("Reproduciendo..."));
                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                    }
                    break;

                case "PAUSE":
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                    break;

                case "RESUME":
                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                    }
                    break;

                case "STOP":
                    mediaPlayer.stop();
                    stopForeground(true);
                    stopSelf();
                    break;

                case "START_FOREGROUND":
                    startForeground(1, createNotification("Reproduciendo en segundo plano..."));
                    break;

                case "STOP_FOREGROUND":
                    stopForeground(true);
                    break;
            }
        }
        return START_NOT_STICKY;
    }

    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "AudioPlayer Channel";
            String description = "Canal de notificaciones para el reproductor de audio";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private Notification createNotification(String contentText) {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Reproductor de Audio")
                .setContentText(contentText)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}