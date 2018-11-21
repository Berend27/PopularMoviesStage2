package com.udacity.popularmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.udacity.popularmovies.database.FavoritesDatabase;
import com.udacity.popularmovies.database.FavoritesEntry;

import java.util.List;

public class FavoritesViewModel extends AndroidViewModel {

    private static final String TAG = FavoritesViewModel.class.getSimpleName();

    private LiveData<List<FavoritesEntry>> favorites;

    public FavoritesViewModel(@NonNull Application application) {
        super(application);
        FavoritesDatabase database = FavoritesDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the favorites from the Database with a ViewModel");
        favorites = database.getFavoritesDao().loadAllFavorites();
    }

    public LiveData<List<FavoritesEntry>> getFavorites()
    {
        return favorites;
    }
}
