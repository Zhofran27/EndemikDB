package com.example.endemikdb.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.endemikdb.R;
import com.example.endemikdb.ui.favorit.FavoritActivity;
import com.example.endemikdb.ui.profile.ProfileFragment;
import com.example.endemikdb.ui.search.SearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences("settings", MODE_PRIVATE);

        // Terapkan dark mode sebelum setContentView
        boolean isDark = prefs.getBoolean("dark_mode", false);
        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        setContentView(R.layout.activity_home);

        bottomNav = findViewById(R.id.bottomNav);

        if (savedInstanceState == null) {
            loadFragment(new HewanFragment());
            bottomNav.setSelectedItemId(R.id.nav_hewan);
        }

        setupBottomNav();
        setupButtons();
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    private void setupBottomNav() {
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_hewan) {
                loadFragment(new HewanFragment());
                return true;
            } else if (id == R.id.nav_tumbuhan) {
                loadFragment(new TumbuhanFragment());
                return true;
            } else if (id == R.id.nav_profile) {
                loadFragment(new ProfileFragment());
                return true;
            }
            return false;
        });
    }

    private void setupButtons() {
        findViewById(R.id.btnSearch).setOnClickListener(v ->
                startActivity(new Intent(this, SearchActivity.class)));

        findViewById(R.id.btnFavorit).setOnClickListener(v ->
                startActivity(new Intent(this, FavoritActivity.class)));

        findViewById(R.id.btnTheme).setOnClickListener(v -> {
            boolean currentDark = prefs.getBoolean("dark_mode", false);
            if (currentDark) {
                prefs.edit().putBoolean("dark_mode", false).apply();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                prefs.edit().putBoolean("dark_mode", true).apply();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        });
    }
}