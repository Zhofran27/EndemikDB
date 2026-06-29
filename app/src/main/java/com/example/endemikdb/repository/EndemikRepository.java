package com.example.endemikdb.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.endemikdb.api.ApiClient;
import com.example.endemikdb.api.ApiService;
import com.example.endemikdb.database.AppDatabase;
import com.example.endemikdb.database.dao.EndemikDao;
import com.example.endemikdb.database.dao.FavoritDao;
import com.example.endemikdb.database.entity.Endemik;
import com.example.endemikdb.database.entity.Favorit;
import com.example.endemikdb.model.EndemikItem;
import com.example.endemikdb.model.EndemikResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EndemikRepository {

    private EndemikDao endemikDao;
    private FavoritDao favoritDao;

    public EndemikRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        endemikDao = db.endemikDao();
        favoritDao = db.favoritDao();
    }

    // --- ENDEMIK ---
    public LiveData<List<Endemik>> getAllEndemik() {
        return endemikDao.getAll();
    }

    public LiveData<List<Endemik>> getByTipe(String tipe) {
        return endemikDao.getByTipe(tipe);
    }

    public LiveData<List<Endemik>> search(String query) {
        return endemikDao.search(query);
    }

    public void fetchFromApi(OnFetchCallback callback) {
        ApiService service = ApiClient.getService();
        service.getEndemik().enqueue(new Callback<EndemikResponse>() {
            @Override
            public void onResponse(Call<EndemikResponse> call, Response<EndemikResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<EndemikItem> items = response.body();
                    List<Endemik> endemikList = new ArrayList<>();
                    for (EndemikItem item : items) {
                        Endemik e = new Endemik();
                        e.setId(item.getId());
                        e.setTipe(item.getTipe());
                        e.setNama(item.getNama());
                        e.setNamaLatin(item.getNamaLatin());
                        e.setDeskripsi(item.getDeskripsi());
                        e.setAsal(item.getAsal());
                        e.setSebaran(item.getSebaran());
                        e.setFoto(item.getFoto());
                        e.setStatus(item.getStatus());
                        endemikList.add(e);
                    }
                    AsyncTask.execute(() -> {
                        endemikDao.insertAll(endemikList);
                        if (callback != null) callback.onSuccess();
                    });
                } else {
                    if (callback != null) callback.onError("Response gagal");
                }
            }

            @Override
            public void onFailure(Call<EndemikResponse> call, Throwable t) {
                if (callback != null) callback.onError(t.getMessage());
            }
        });
    }

    public int getEndemikCount() {
        return endemikDao.getCount();
    }

    // --- FAVORIT ---
    public LiveData<List<Favorit>> getAllFavorit() {
        return favoritDao.getAll();
    }

    public void insertFavorit(Favorit favorit) {
        AsyncTask.execute(() -> favoritDao.insert(favorit));
    }

    public void deleteFavorit(Favorit favorit) {
        AsyncTask.execute(() -> favoritDao.delete(favorit));
    }

    public boolean isFavorit(String id) {
        return favoritDao.isFavorit(id) > 0;
    }

    // --- CALLBACK ---
    public interface OnFetchCallback {
        void onSuccess();
        void onError(String message);
    }
}