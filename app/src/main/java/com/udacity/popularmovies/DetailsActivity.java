package com.udacity.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBar;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.database.AppExecutors;
import com.udacity.popularmovies.database.FavoritesDatabase;
import com.udacity.popularmovies.database.FavoritesEntry;

import org.w3c.dom.Text;

import java.net.URL;
import java.util.List;

import static android.content.ContentValues.TAG;

public class DetailsActivity extends AppCompatActivity {

    TextView title;
    TextView date;
    ImageView poster;
    TextView average;
    TextView overview;
    TextView videosLabel;

    Trailer[] trailers;

    FetchTrailersTask fetch = new FetchTrailersTask();

    final String START_OF_TRAILER_QUERY = "https://api.themoviedb.org/3/movie/";
    final String END_OF_TRAILER_QUERY = "/videos?api_key=" + BuildConfig.THE_MOVIE_DB_API_KEY;    // Add your api key

    private String movieId;
    private String trailerQuery;
    private String json;

    private String[] details;

    int place;

    boolean doneLoading = false;
    boolean alreadyFavored = false;

    Button favoritesButton;

    private Context context = this;

    private FavoritesDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detailsToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // The launchMode is singleTop, so the activity keeps the intent
        Intent intent = getIntent();
        json = intent.getStringExtra("json");
        place = intent.getIntExtra("place", 0);

        details = JsonUtils.getDetails(json, place);

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
        favoritesButton.setVisibility(View.INVISIBLE);  // made visible in onResume

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        favored(movieId);
    }

    protected void populateListView()
    {
        // Creating an adapter to populate the ListView
        ArrayAdapter<Trailer> trailerArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, trailers);
        ListView trailersList = (ListView) findViewById(R.id.list_trailers);
        trailersList.setAdapter(trailerArrayAdapter);

        // Creating a Listener for the trailers list
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Play the trailer that was clicked on
                String video_path = "http://www.youtube.com/watch?v="
                        + trailers[position].getYouTubeKey();
                Uri uri = Uri.parse(video_path);
                Intent trailerIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(trailerIntent);
            }
        };

        // Assign the listener to the ListView
        trailersList.setOnItemClickListener(itemClickListener);
    }

    public class FetchTrailersTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            if (params.length == 0)
                return null;
            String urlString = params[0];
            URL queryUrl = NetworkUtils.createURL(urlString);
            try {
                return NetworkUtils.getJsonResponseFromHttpUrl(queryUrl);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String jsonString)
        {
            trailers = JsonUtils.getTrailers(jsonString);
            if (trailers != null)
            {
                videosLabel.setVisibility(View.VISIBLE);
            }
            populateListView();
            doneLoading = true;
        }
    }

    protected void readReviews(View view)
    {
        if (doneLoading) {
            Intent reviewsIntent = new Intent(this, ReviewsActivity.class);
            reviewsIntent.putExtra(ReviewsActivity.TITLE_KEY, details[0]);
            reviewsIntent.putExtra(ReviewsActivity.ID_KEY, movieId);
            startActivity(reviewsIntent);
        }
    }

    protected void addToFavorites(View view)
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
                favoritesButton.setText("Remove from Favorites");
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
                favoritesButton.setText("Add to Favorites");
                alreadyFavored = false;
            }
        }
    }

    void favored(String id)
    {
        // boolean alreadyFavored
            final String theId = id;
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if (mDb == null)
                        return;
                    List<FavoritesEntry> entries = mDb.getFavoritesDao().loadAllFavorites();
                    if (entries == null)
                        setAlreadyFavored(false);
                    else {
                        setAlreadyFavored(false);
                        for (int i = 0; i < entries.size(); i++) {
                            if (entries.get(i).getId().equals(theId)) {
                                setAlreadyFavored(true);
                            }
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (getAlreadyFavored())
                                favoritesButton.setText("Remove from Favorites");
                            else
                                favoritesButton.setText("Add to Favorites");
                            favoritesButton.setVisibility(View.VISIBLE);
                        }
                    });
                }
            });
    }

    void setAlreadyFavored(boolean value)
    {
        alreadyFavored = value;
    }

    boolean getAlreadyFavored()
    {
        return alreadyFavored;
    }

    /*
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("json", json);
        savedInstanceState.putInt("place", place);
    }
    */
}

/*
        String chooserTitle = "Select an app to watch the video in";
        Intent chosenIntent = Intent.createChooser(trailerIntent, chooserTitle);
        startActivity(chosenIntent);
 */