package com.udacity.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.udacity.popularmovies.database.AppExecutors;
import com.udacity.popularmovies.database.FavoritesDatabase;
import com.udacity.popularmovies.database.FavoritesEntry;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity
    implements PosterAdapter.PosterItemClickListener {

    private static final int NUM_COLUMNS = 2;

    private static final String TAG = FavoritesActivity.class.getSimpleName();

    private FavoritesAdapter mAdapter;
    private FavoritesDatabase mDb;
    private RecyclerView mPosterGrid;
    private String title;
    private boolean started = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        Toolbar toolbar = (Toolbar) findViewById(R.id.favoritesToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mPosterGrid = (RecyclerView) findViewById(R.id.rv_favorites_posters);
        GridLayoutManager layoutManager = new GridLayoutManager(this, NUM_COLUMNS);
        mPosterGrid.setLayoutManager(layoutManager);
        mPosterGrid.setHasFixedSize(true);

        mAdapter = new FavoritesAdapter(this, this);
        mDb = FavoritesDatabase.getInstance(getApplicationContext());

        setupViewModel();
    }

    private void setupViewModel()
    {
        // Instantiate the ViewModel and observe the LiveData from it
        FavoritesViewModel viewModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);
        viewModel.getFavorites().observe(this, new Observer<List<FavoritesEntry>>() {
            @Override
            public void onChanged(@Nullable List<FavoritesEntry> favoritesEntries) {
                Log.d(TAG, "Receiving a database update from LiveData with a ViewModel");
                mAdapter.setFavorites(favoritesEntries);
                if (started == false)
                {
                    mPosterGrid.setAdapter(mAdapter);
                    started = true;
                }
            }
        });
    }

    @Override
    public void onListItemClicked(int clickedItemIndex) {
        String[] details = mAdapter.getMovie(clickedItemIndex);
        Intent intent = new Intent(this, FavoredDetails.class);
        intent.putExtra(FavoredDetails.FAVORED_DETAILS_KEY, details);
        startActivity(intent);
    }
}
