package com.example.laboratorio10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView txtTitle, txtArtist;
    private ImageView imgCover;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    private Handler handler;

    private int[] songs = {R.raw.ellaquierebeberanuelaa, R.raw.brotheranuelaa, R.raw.unapalabraanuelaa, R.raw.coroneanuelaa, R.raw.losdiosesozunaanuelaa, R.raw.elosodeldineroozuna, R.raw.unanotiomarcourtz}; // Lista de canciones
    private int currentSongIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTitle = findViewById(R.id.txtTitle);
        txtArtist = findViewById(R.id.txtArtist);
        imgCover = findViewById(R.id.imgCover);
        seekBar = findViewById(R.id.seekBar);
        handler = new Handler();

        initializeMediaPlayer();

        findViewById(R.id.btnStart).setOnClickListener(v -> startPlaying());
        findViewById(R.id.btnPause).setOnClickListener(v -> pausePlaying());
        findViewById(R.id.btnResume).setOnClickListener(v -> resumePlaying());
        findViewById(R.id.btnStop).setOnClickListener(v -> stopPlaying());
        findViewById(R.id.btnNext).setOnClickListener(v -> changeSong(1));
        findViewById(R.id.btnPrevious).setOnClickListener(v -> changeSong(-1));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void initializeMediaPlayer() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }

            mediaPlayer = MediaPlayer.create(this, songs[currentSongIndex]);
            mediaPlayer.setOnCompletionListener(mp -> {
                changeSong(1);
            });

            loadMp3Metadata();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al cargar la canción", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMp3Metadata() {
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/" + songs[currentSongIndex]));

            // Título
            String title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            txtTitle.setText(title != null ? title : "Sin título");

            // Artista
            String artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            txtArtist.setText(artist != null ? artist : "Artista desconocido");

            // Portada
            byte[] art = retriever.getEmbeddedPicture();
            if (art != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
                imgCover.setImageBitmap(bitmap);
            } else {
                imgCover.setImageResource(R.drawable.ic_launcher_background);
            }

            retriever.release();

            seekBar.setMax(mediaPlayer.getDuration());
            updateSeekBar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startPlaying() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            updateSeekBar();
        }
    }

    private void pausePlaying() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    private void resumePlaying() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            updateSeekBar();
        }
    }

    private void stopPlaying() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            initializeMediaPlayer();
        }
    }

    private void changeSong(int direction) {
        if (mediaPlayer != null) {
            currentSongIndex = (currentSongIndex + direction + songs.length) % songs.length;
            initializeMediaPlayer();
            startPlaying();
        }
    }

    private void updateSeekBar() {
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        if (mediaPlayer.isPlaying()) {
            handler.postDelayed(this::updateSeekBar, 1000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacksAndMessages(null);
    }
}