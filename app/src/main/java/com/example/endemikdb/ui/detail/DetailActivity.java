package com.example.endemikdb.ui.detail;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.endemikdb.R;
import com.example.endemikdb.database.entity.Favorit;
import com.example.endemikdb.repository.EndemikRepository;

public class DetailActivity extends AppCompatActivity {

    private EndemikRepository repository;
    private boolean isFavorit = false;
    private String itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        repository = new EndemikRepository(getApplication());

        itemId = getIntent().getStringExtra("id");
        String nama = getIntent().getStringExtra("nama");
        String namaLatin = getIntent().getStringExtra("namaLatin");
        String tipe = getIntent().getStringExtra("tipe");
        String asal = getIntent().getStringExtra("asal");
        String sebaran = getIntent().getStringExtra("sebaran");
        String deskripsi = getIntent().getStringExtra("deskripsi");
        String foto = getIntent().getStringExtra("foto");
        String status = getIntent().getStringExtra("status");

        ImageView imgHero = findViewById(R.id.imgHero);
        TextView tvName = findViewById(R.id.tvName);
        TextView tvLatin = findViewById(R.id.tvLatin);
        TextView tvCategory = findViewById(R.id.tvCategory);
        TextView tvStatus = findViewById(R.id.tvStatus);
        TextView tvHabitat = findViewById(R.id.tvHabitat);
        TextView tvSize = findViewById(R.id.tvSize);
        TextView tvStatusInfo = findViewById(R.id.tvStatusInfo);
        TextView tvDescription = findViewById(R.id.tvDescription);
        LinearLayout btnBack = findViewById(R.id.btnBack);
        LinearLayout btnFavorit = findViewById(R.id.btnFavorit);
        ImageView imgHeart = findViewById(R.id.imgHeart);

        tvName.setText(nama);
        tvLatin.setText(namaLatin != null ? namaLatin : "");
        tvCategory.setText(tipe);
        tvStatus.setText(status);
        tvHabitat.setText(sebaran != null ? sebaran : "-");
        tvSize.setText(asal != null ? asal : "-");
        tvStatusInfo.setText(status);
        tvDescription.setText(deskripsi);

        Glide.with(this)
                .load(foto)
                .centerCrop()
                .into(imgHero);

        AsyncTask.execute(() -> {
            isFavorit = repository.isFavorit(itemId);
            runOnUiThread(() -> {
                if (isFavorit) {
                    imgHeart.setImageResource(R.drawable.ic_heart_filled);
                } else {
                    imgHeart.setImageResource(R.drawable.ic_heart);
                }
            });
        });

        btnBack.setOnClickListener(v -> finish());

        btnFavorit.setOnClickListener(v -> {
            AsyncTask.execute(() -> {
                if (isFavorit) {
                    Favorit fav = new Favorit();
                    fav.setId(itemId);
                    repository.deleteFavorit(fav);
                    isFavorit = false;
                    runOnUiThread(() -> imgHeart.setImageResource(R.drawable.ic_heart));
                } else {
                    Favorit fav = new Favorit();
                    fav.setId(itemId);
                    fav.setNama(nama);
                    fav.setNamaLatin(namaLatin);
                    fav.setTipe(tipe);
                    fav.setAsal(asal);
                    fav.setSebaran(sebaran);
                    fav.setDeskripsi(deskripsi);
                    fav.setFoto(foto);
                    fav.setStatus(status);
                    repository.insertFavorit(fav);
                    isFavorit = true;
                    runOnUiThread(() -> imgHeart.setImageResource(R.drawable.ic_heart_filled));
                }
            });
        });
    }
}