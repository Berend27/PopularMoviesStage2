package com.udacity.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.database.AppExecutors;
import com.udacity.popularmovies.database.FavoritesDatabase;
import com.udacity.popularmovies.database.FavoritesEntry;

public class FavoredDetails extends DetailsActivity {

    public static final String FAVORED_DETAILS_KEY = "favored_details";

    private String movieId;
    private String trailerQuery;
    private FavoritesDatabase mDb;
    private String[] details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
        } catch (NullPointerException npe) { /* ignore this exception */ }
        setContentView(R.layout.movie_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detailsToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent detailsIntent = getIntent();
        details = detailsIntent.getStringArrayExtra(FAVORED_DETAILS_KEY);

        title = (TextView) findViewById(R.id.title);
        title.setText(details[0]);

        date = (TextView) findViewById(R.id.release_date);
        date.setText(details[1]);

        poster = (ImageView) findViewById(R.id.posterPic);
        Picasso.get().load(details[2]).into(poster);

        average = (TextView) findViewById(R.id.voter_average);
        average.setText(details[3]);

        overview = (TextView) findViewById(R.id.synopsis);
        overview.setText(details[4]);

        movieId = details[5];

        videosLabel = (TextView) findViewById(R.id.trailers_label);
        videosLabel.setVisibility(View.INVISIBLE);

        // Get the trailers for the ListView by running the AsyncTask
        trailerQuery = START_OF_TRAILER_QUERY + movieId + END_OF_TRAILER_QUERY;
        fetch.execute(trailerQuery);

        // Initialize member variable for the data base
        mDb = FavoritesDatabase.getInstance(getApplicationContext());

        favoritesButton  = (Button) findViewById(R.id.mark_as_favorite);

    }

    @Override
    protected void onResume()
    {
        alreadyFavored = true;
        favoritesButton.setText(getResources().getString(R.string.removeFromFavorites));
        try {
            super.onResume();
        } catch (NullPointerException npe) {
        } catch (Exception e) {
            e.printStackTrace();
        }

        favoritesButton.setVisibility(View.VISIBLE);

        // Show the view reviews button if there is an internet connection
        Button reviewsButton = (Button) findViewById(R.id.view_reviews);
        if (isConnected())
            reviewsButton.setVisibility(View.VISIBLE);
        else
            reviewsButton.setVisibility(View.GONE);
    }

    @Override
    public void addToFavorites(View view)
    {
        if (doneLoading)
        {
            final FavoritesEntry thisEntry = new FavoritesEntry(details[0], details[1], details[2],
                    details[3], details[4], details[5]);
            if (!alreadyFavored) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.getFavoritesDao().insertFavorite(thisEntry);
                    }
                });
                // the code is not ready for finish();
                favoritesButton.setText(getResources().getString(R.string.removeFromFavorites));
                alreadyFavored = true;
            }
            else
            {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.getFavoritesDao().deleteEntry(thisEntry);
                    }
                });
                favoritesButton.setText(getResources().getString(R.string.add_to_favorites));
                alreadyFavored = false;
            }
        }
    }

    @Override
    public void readReviews(View view)
    {
        if (doneLoading) {
            Intent reviewsIntent = new Intent(this, ReviewsActivity.class);
            reviewsIntent.putExtra(ReviewsActivity.TITLE_KEY, details[0]);
            reviewsIntent.putExtra(ReviewsActivity.ID_KEY, movieId);
            startActivity(reviewsIntent);
        }
    }

    protected boolean isConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
