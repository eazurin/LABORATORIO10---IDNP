package com.example.laboratorio10;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnStart).setOnClickListener(v -> startServiceAction("START"));
        findViewById(R.id.btnPause).setOnClickListener(v -> startServiceAction("PAUSE"));
        findViewById(R.id.btnResume).setOnClickListener(v -> startServiceAction("RESUME"));
        findViewById(R.id.btnStop).setOnClickListener(v -> startServiceAction("STOP"));
    }

    private void startServiceAction(String action) {
        Intent intent = new Intent(this, AudioPlayerService.class);
        intent.setAction(action);
        startService(intent);
    }
}
