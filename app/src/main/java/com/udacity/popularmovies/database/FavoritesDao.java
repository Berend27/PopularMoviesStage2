package com.udacity.popularmovies.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface FavoritesDao {
    @Query("SELECT * FROM favorites ORDER BY title")
    List<FavoritesEntry> loadAllFavorites();

    @Query("SELECT * FROM favorites WHERE id = :id")
    FavoritesEntry getItemById(Long id);

    @Insert
    void insertFavorite(FavoritesEntry favoritesEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateEntry(FavoritesEntry favoritesEntry);

    @Delete
    void deleteEntry(FavoritesEntry favoritesEntry);
}
