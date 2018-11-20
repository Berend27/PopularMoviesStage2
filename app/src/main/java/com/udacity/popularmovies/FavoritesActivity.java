package com.udacity.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.udacity.popularmovies.database.AppExecutors;
import com.udacity.popularmovies.database.FavoritesDatabase;

public class FavoritesActivity extends AppCompatActivity
    implements PosterAdapter.PosterItemClickListener {

    private static final int NUM_COLUMNS = 2;

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
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mAdapter.setFavorites(mDb.getFavoritesDao().loadAllFavorites());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Context context = getApplicationContext();
                        if (started == false)
                        {
                            mPosterGrid.setAdapter(mAdapter);
                            started = true;
                        }
                        /*
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, title, duration);
                        toast.show();
                        */
                    }
                });
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
