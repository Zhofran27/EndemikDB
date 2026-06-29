package com.example.endemikdb.ui.search;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.endemikdb.R;
import com.example.endemikdb.database.entity.Endemik;
import com.example.endemikdb.repository.EndemikRepository;
import com.example.endemikdb.ui.detail.DetailActivity;
import com.example.endemikdb.ui.home.EndemikAdapter;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText etSearch;
    private ImageButton btnClear;
    private RecyclerView rvSearch;
    private TextView tvHasilLabel;
    private LinearLayout layoutRiwayat;
    private ChipGroup chipGroupRiwayat;
    private EndemikAdapter adapter;
    private EndemikRepository repository;
    private SharedPreferences prefs;
    private static final String PREF_NAME = "search_history";
    private static final String KEY_HISTORY = "history";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        repository = new EndemikRepository(getApplication());

        initViews();
        setupAdapter();
        loadRiwayat();
        setupListeners();
    }

    private void initViews() {
        etSearch = findViewById(R.id.etSearch);
        btnClear = findViewById(R.id.btnClear);
        rvSearch = findViewById(R.id.rvSearch);
        tvHasilLabel = findViewById(R.id.tvHasilLabel);
        layoutRiwayat = findViewById(R.id.layoutRiwayat);
        chipGroupRiwayat = findViewById(R.id.chipGroupRiwayat);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnHapusRiwayat).setOnClickListener(v -> hapusRiwayat());
    }

    private void setupAdapter() {
        adapter = new EndemikAdapter(this,
                endemik -> {
                    Intent intent = new Intent(this, DetailActivity.class);
                    intent.putExtra("id", endemik.getId());
                    intent.putExtra("nama", endemik.getNama());
                    intent.putExtra("namaLatin", endemik.getNamaLatin());
                    intent.putExtra("tipe", endemik.getTipe());
                    intent.putExtra("asal", endemik.getAsal());
                    intent.putExtra("sebaran", endemik.getSebaran());
                    intent.putExtra("deskripsi", endemik.getDeskripsi());
                    intent.putExtra("foto", endemik.getFoto());
                    intent.putExtra("status", endemik.getStatus());
                    startActivity(intent);
                },
                (endemik, btnHeart) -> {}
        );

        rvSearch.setLayoutManager(new GridLayoutManager(this, 2));
        rvSearch.setAdapter(adapter);
    }

    private void setupListeners() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    btnClear.setVisibility(View.VISIBLE);
                    layoutRiwayat.setVisibility(View.GONE);
                    doSearch(s.toString());
                } else {
                    btnClear.setVisibility(View.GONE);
                    tvHasilLabel.setVisibility(View.GONE);
                    rvSearch.setVisibility(View.GONE);
                    layoutRiwayat.setVisibility(View.VISIBLE);
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = etSearch.getText().toString().trim();
                if (!query.isEmpty()) {
                    simpanRiwayat(query);
                    loadRiwayat();
                }
                return true;
            }
            return false;
        });

        btnClear.setOnClickListener(v -> {
            etSearch.setText("");
            etSearch.requestFocus();
        });
    }

    private void doSearch(String query) {
        repository.search(query).observe(this, list -> {
            if (list != null) {
                tvHasilLabel.setVisibility(View.VISIBLE);
                rvSearch.setVisibility(View.VISIBLE);
                adapter.setData(list);
            }
        });
    }

    private void simpanRiwayat(String query) {
        List<String> history = getRiwayatList();
        history.remove(query);
        history.add(0, query);
        if (history.size() > 5) history = history.subList(0, 5);
        prefs.edit().putString(KEY_HISTORY, String.join(",", history)).apply();
    }

    private void hapusRiwayat() {
        prefs.edit().remove(KEY_HISTORY).apply();
        chipGroupRiwayat.removeAllViews();
    }

    private void loadRiwayat() {
        chipGroupRiwayat.removeAllViews();
        List<String> history = getRiwayatList();
        for (String item : history) {
            Chip chip = new Chip(this);
            chip.setText(item);
            chip.setChipBackgroundColorResource(R.color.background);
            chip.setTextColor(getColor(R.color.text_secondary));
            chip.setChipStrokeColorResource(R.color.border);
            chip.setChipStrokeWidth(1f);
            chip.setOnClickListener(v -> {
                etSearch.setText(item);
                etSearch.setSelection(item.length());
            });
            chipGroupRiwayat.addView(chip);
        }
    }

    private List<String> getRiwayatList() {
        String raw = prefs.getString(KEY_HISTORY, "");
        if (raw.isEmpty()) return new ArrayList<>();
        return new ArrayList<>(Arrays.asList(raw.split(",")));
    }
}