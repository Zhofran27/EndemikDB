package com.example.endemikdb.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.endemikdb.database.dao.EndemikDao;
import com.example.endemikdb.database.dao.FavoritDao;
import com.example.endemikdb.database.entity.Endemik;
import com.example.endemikdb.database.entity.Favorit;

@Database(entities = {Endemik.class, Favorit.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract EndemikDao endemikDao();
    public abstract FavoritDao favoritDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "endemikdb"
            ).fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}