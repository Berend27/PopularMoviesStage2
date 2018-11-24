package com.udacity.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
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

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.title) TextView title;
    @BindView(R.id.release_date) TextView date;
    @BindView(R.id.posterPic) ImageView poster;
    @BindView(R.id.voter_average) TextView average;
    @BindView(R.id.synopsis) TextView overview;
    @BindView(R.id.trailers_label) TextView videosLabel;

    protected Trailer[] trailers;

    protected FetchTrailersTask fetch = new FetchTrailersTask();

    private static final String TAG = DetailsActivity.class.getSimpleName();

    final String START_OF_TRAILER_QUERY = "https://api.themoviedb.org/3/movie/";
    final String END_OF_TRAILER_QUERY = "/videos?api_key=" + BuildConfig.THE_MOVIE_DB_API_KEY;    // Add your api key

    private String movieId;
    private String trailerQuery;
    private String json;

    private String[] details;

    int place;

    boolean doneLoading = false;
    boolean alreadyFavored = false;

    protected Button favoritesButton;

    private Context context = this;

    private FavoritesDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detailsToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // The launchMode is singleTop, so the activity keeps the intent
        Intent intent = getIntent();
        json = intent.getStringExtra("json");
        place = intent.getIntExtra("place", 0);

        details = JsonUtils.getDetails(json, place);

        title.setText(details[0]);

        date.setText(details[1]);

        Picasso.get().load(details[2]).into(poster);

        average.setText(details[3]);

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
        favoritesButton.setVisibility(View.INVISIBLE);  // made visible in favored()
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
                populateListView();
            }

            doneLoading = true;
        }
    }

    public void readReviews(View view)
    {
        if (doneLoading) {
            Intent reviewsIntent = new Intent(this, ReviewsActivity.class);
            reviewsIntent.putExtra(ReviewsActivity.TITLE_KEY, details[0]);
            reviewsIntent.putExtra(ReviewsActivity.ID_KEY, movieId);
            startActivity(reviewsIntent);
        }
    }

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
                favoritesButton.setText(getResources().getText(R.string.removeFromFavorites));
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
                favoritesButton.setText(getResources().getText(R.string.add_to_favorites));
                alreadyFavored = false;
            }
        }
    }

    // This method checks if a movie has already been favored
    void favored(String id)
    {
        // boolean alreadyFavored is used with a getter method and a setter method
            final String theId = id;
        if (mDb == null)
            return;
        Log.d(TAG, "Actively retrieving the list favored movies from the database");
        FavoritesViewModel viewModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);
        viewModel.getFavorites().observe(this, new Observer<List<FavoritesEntry>>() {
            @Override
            public void onChanged(@Nullable List<FavoritesEntry> favoritesEntries) {
                Log.d(TAG, "Setting alreadyFavored");
                if (favoritesEntries == null)
                    setAlreadyFavored(false);
                else {
                    setAlreadyFavored(false);
                    for (int i = 0; i < favoritesEntries.size(); i++) {
                        if (favoritesEntries.get(i).getId().equals(theId)) {
                            setAlreadyFavored(true);
                        }
                    }
                }
                if (getAlreadyFavored())
                    favoritesButton.setText(getResources().getString(R.string.removeFromFavorites));
                else
                    favoritesButton.setText(getResources().getString(R.string.add_to_favorites));
                favoritesButton.setVisibility(View.VISIBLE);
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

