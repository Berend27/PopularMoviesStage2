package com.udacity.popularmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

public class DetailsViewModel extends AndroidViewModel {

    private static final String TAG = FavoritesViewModel.class.getSimpleName();

    public DetailsViewModel(@NonNull Application application) {
        super(application);
    }
}
