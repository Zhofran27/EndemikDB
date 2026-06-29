package com.example.endemikdb.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.endemikdb.database.entity.Endemik;

import java.util.List;

@Dao
public interface EndemikDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Endemik> list);

    @Query("SELECT * FROM endemik")
    LiveData<List<Endemik>> getAll();

    @Query("SELECT * FROM endemik WHERE tipe = :tipe")
    LiveData<List<Endemik>> getByTipe(String tipe);

    @Query("SELECT * FROM endemik WHERE nama LIKE '%' || :query || '%' OR asal LIKE '%' || :query || '%'")
    LiveData<List<Endemik>> search(String query);

    @Query("SELECT COUNT(*) FROM endemik")
    int getCount();

    @Query("DELETE FROM endemik")
    void deleteAll();
}