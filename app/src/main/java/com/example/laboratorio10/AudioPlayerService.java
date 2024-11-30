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


        mediaPlayer = MediaPlayer.create(this, R.raw.ellaquierebeberanuelaa);
        mediaPlayer.setLooping(false);
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
            }
        }
        return START_NOT_STICKY;
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

    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Audio Player Service",
                NotificationManager.IMPORTANCE_LOW
        );
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.createNotificationChannel(channel);
        }
    }

    private Notification createNotification(String contentText) {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Audio Player")
                .setContentText(contentText)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();
    }
}


