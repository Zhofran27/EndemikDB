package com.example.endemikdb.ui.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.endemikdb.R;
import com.example.endemikdb.repository.EndemikRepository;
import com.example.endemikdb.ui.home.HomeActivity;

public class SplashActivity extends AppCompatActivity {

    private TextView tvStatus;
    private EndemikRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Apply saved theme sebelum setContentView
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("dark_mode", false);
        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        setContentView(R.layout.activity_splash);

        tvStatus = findViewById(R.id.tvStatus);
        repository = new EndemikRepository(getApplication());

        checkDataAndLoad();
    }

    private void checkDataAndLoad() {
        AsyncTask.execute(() -> {
            int count = repository.getEndemikCount();
            if (count > 0) {
                runOnUiThread(() -> goToHome());
            } else {
                runOnUiThread(() -> tvStatus.setText("Mengambil data dari server..."));
                repository.fetchFromApi(new EndemikRepository.OnFetchCallback() {
                    @Override
                    public void onSuccess() {
                        runOnUiThread(() -> goToHome());
                    }

                    @Override
                    public void onError(String message) {
                        runOnUiThread(() -> tvStatus.setText("Gagal: " + message));
                    }
                });
            }
        });
    }

    private void goToHome() {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}