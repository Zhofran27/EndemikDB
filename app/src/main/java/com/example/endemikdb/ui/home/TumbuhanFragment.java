package com.example.endemikdb.ui.home;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.endemikdb.R;
import com.example.endemikdb.database.entity.Endemik;
import com.example.endemikdb.database.entity.Favorit;
import com.example.endemikdb.repository.EndemikRepository;
import com.example.endemikdb.ui.detail.DetailActivity;

public class TumbuhanFragment extends Fragment {

    private EndemikRepository repository;
    private EndemikAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tumbuhan, container, false);

        repository = new EndemikRepository(requireActivity().getApplication());

        RecyclerView rvTumbuhan = view.findViewById(R.id.rvTumbuhan);
        rvTumbuhan.setLayoutManager(new GridLayoutManager(getContext(), 2));

        adapter = new EndemikAdapter(getContext(),
                endemik -> openDetail(endemik),
                (endemik, btnHeart) -> toggleFavorit(endemik, btnHeart)
        );

        rvTumbuhan.setAdapter(adapter);

        repository.getByTipe("Tumbuhan").observe(getViewLifecycleOwner(), list -> {
            if (list != null) adapter.setData(list);
        });

        return view;
    }

    private void openDetail(Endemik endemik) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
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
    }

    private void toggleFavorit(Endemik endemik, ImageButton btnHeart) {
        AsyncTask.execute(() -> {
            boolean isFav = repository.isFavorit(endemik.getId());
            if (isFav) {
                Favorit fav = new Favorit();
                fav.setId(endemik.getId());
                repository.deleteFavorit(fav);
                requireActivity().runOnUiThread(() ->
                        btnHeart.setImageResource(R.drawable.ic_heart));
            } else {
                Favorit fav = new Favorit();
                fav.setId(endemik.getId());
                fav.setNama(endemik.getNama());
                fav.setNamaLatin(endemik.getNamaLatin());
                fav.setTipe(endemik.getTipe());
                fav.setAsal(endemik.getAsal());
                fav.setSebaran(endemik.getSebaran());
                fav.setDeskripsi(endemik.getDeskripsi());
                fav.setFoto(endemik.getFoto());
                fav.setStatus(endemik.getStatus());
                repository.insertFavorit(fav);
                requireActivity().runOnUiThread(() ->
                        btnHeart.setImageResource(R.drawable.ic_heart_filled));
            }
        });
    }
}