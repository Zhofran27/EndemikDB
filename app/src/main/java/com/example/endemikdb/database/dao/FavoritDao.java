package com.example.endemikdb.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.endemikdb.database.entity.Favorit;

import java.util.List;

@Dao
public interface FavoritDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Favorit favorit);

    @Delete
    void delete(Favorit favorit);

    @Query("SELECT * FROM favorit")
    LiveData<List<Favorit>> getAll();

    @Query("SELECT * FROM favorit WHERE id = :id")
    Favorit getById(String id);

    @Query("SELECT COUNT(*) FROM favorit WHERE id = :id")
    int isFavorit(String id);
}