package com.example.endemikdb.ui.favorit;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.endemikdb.R;
import com.example.endemikdb.database.entity.Favorit;
import com.example.endemikdb.repository.EndemikRepository;
import com.example.endemikdb.ui.detail.DetailActivity;

import java.util.List;

public class FavoritActivity extends AppCompatActivity {

    private FavoritAdapter adapter;
    private EndemikRepository repository;
    private View layoutEmpty;
    private RecyclerView rvFavorit;
    private TextView tvJumlahFavorit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorit);

        repository = new EndemikRepository(getApplication());

        layoutEmpty = findViewById(R.id.layoutEmpty);
        rvFavorit = findViewById(R.id.rvFavorit);
        tvJumlahFavorit = findViewById(R.id.tvJumlahFavorit);

        adapter = new FavoritAdapter(this,
                favorit -> {
                    Intent intent = new Intent(this, DetailActivity.class);
                    intent.putExtra("id", favorit.getId());
                    intent.putExtra("nama", favorit.getNama());
                    intent.putExtra("namaLatin", favorit.getNamaLatin());
                    intent.putExtra("tipe", favorit.getTipe());
                    intent.putExtra("asal", favorit.getAsal());
                    intent.putExtra("sebaran", favorit.getSebaran());
                    intent.putExtra("deskripsi", favorit.getDeskripsi());
                    intent.putExtra("foto", favorit.getFoto());
                    intent.putExtra("status", favorit.getStatus());
                    startActivity(intent);
                },
                favorit -> {
                    AsyncTask.execute(() -> repository.deleteFavorit(favorit));
                }
        );

        rvFavorit.setLayoutManager(new GridLayoutManager(this, 2));
        rvFavorit.setAdapter(adapter);

        repository.getAllFavorit().observe(this, this::updateUI);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnJelajahi).setOnClickListener(v -> finish());
    }

    private void updateUI(List<Favorit> list) {
        if (list == null || list.isEmpty()) {
            tvJumlahFavorit.setText("0 data tersimpan");
            layoutEmpty.setVisibility(View.VISIBLE);
            rvFavorit.setVisibility(View.GONE);
        } else {
            tvJumlahFavorit.setText(list.size() + " data tersimpan");
            layoutEmpty.setVisibility(View.GONE);
            rvFavorit.setVisibility(View.VISIBLE);
            adapter.setData(list);
        }
    }
}