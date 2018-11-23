package com.udacity.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

import java.net.URL;

public class MoviePosters extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener, PosterAdapter.PosterItemClickListener {

    private static final int NUM_LIST_ITEMS = 20;
    private static final int NUM_COLUMNS = 2;

    private PosterAdapter mAdapter;
    private RecyclerView mPosterGrid;

    private int option = 0;    // For starting with "Most Popular"
    private int beforeFavorites = 0;    // the selected option before "Favorites" was selected

    public boolean started = false;

    static final String POPULAR = "http://api.themoviedb.org/3/movie/popular?api_key=";
    static final String TOP_RATED = "http://api.themoviedb.org/3/movie/top_rated?api_key=";
    // Add your api key to the gradle.properties file for project-wide Gradle settings
    static final String API_KEY = BuildConfig.THE_MOVIE_DB_API_KEY;

    String query = POPULAR + API_KEY;    // starting value

    Toast mToast;

    ProgressBar spinningDialog;

    FetchMoviesTask fetch = new FetchMoviesTask();

    Bundle state;

    Spinner sort;

    boolean favoritesSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_posters);
        state = savedInstanceState;
        if (savedInstanceState != null)
        {
            option = savedInstanceState.getInt("option");
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.posterToolbar);
        setSupportActionBar(toolbar);

        spinningDialog = (ProgressBar) findViewById(R.id.spinning_progress);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sortBy,
                android.R.layout.simple_spinner_dropdown_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.custom_spinner_item);
        // apply the adapter to the spinner
        sort = (Spinner) findViewById(R.id.sort);
        sort.setAdapter(adapter);

        sort.setSelection(beforeFavorites);
        // setting the listener
        sort.setOnItemSelectedListener(this);


        mPosterGrid = (RecyclerView) findViewById(R.id.rv_posters);


        GridLayoutManager layoutManager = new GridLayoutManager(this, NUM_COLUMNS);
        mPosterGrid.setLayoutManager(layoutManager);

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mPosterGrid.setHasFixedSize(true);

        fetch.execute(query);

        mAdapter = new PosterAdapter(NUM_LIST_ITEMS, this);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("option", option);
    }

    @Override
    public void onListItemClicked(int clickedItemIndex) {

        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("key", "http://image.tmdb.org/t/p/w185//3IGbjc5ZC5yxim5W0sFING2kdcz.jpg");
        intent.putExtra("json", mAdapter.getJson());
        intent.putExtra("place", clickedItemIndex);
        startActivity(intent);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id)
    {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

        String selection = parent.getItemAtPosition(pos).toString();
        option = pos;
        if (!favoritesSelected)    // done to prevent an irrelevant toast
            Toast.makeText(parent.getContext(), selection, Toast.LENGTH_LONG).show();
        if (selection.equals("Most Popular")) {
            query = POPULAR + API_KEY;
            beforeFavorites = option;
            favoritesSelected = false;
        }
        else if (selection.equals("Top Rated")) {
            query = TOP_RATED + API_KEY;
            beforeFavorites = option;
            favoritesSelected = false;
        }
        else if (selection.equals("Favorites"))
        {
            Intent favoritesIntent = new Intent(this, FavoritesActivity.class);
            favoritesSelected = true;
            // Make sure that the option isn't still "Favorites" after navigating back
            option = beforeFavorites;
            sort.setSelection(option);
            startActivity(favoritesIntent);
        }
        new FetchMoviesTask().execute(query);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
        // nothing
    }


    public class FetchMoviesTask extends AsyncTask<String, Integer, String>
    {


        @Override
        protected String doInBackground(String... params)
        {
            if (params.length == 0)
                return null;
            String urlString = params[0];
            URL queryURL = NetworkUtils.createURL(urlString);
            publishProgress(1);
            try {
                return NetworkUtils.getJsonResponseFromHttpUrl(queryURL);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.toString());
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            if (!started)
                spinningDialog.setVisibility(spinningDialog.VISIBLE);
        }

        protected void onProgressUpdate(int... values)
        {
            spinningDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String jsonString)
        {
            if (jsonString != null) {
                String json = jsonString;
                mAdapter.setJson(json);
                mAdapter.setPosters();

                if (started == false)
                {
                    mPosterGrid.setAdapter(mAdapter);
                    started = true;
                }

            }
            spinningDialog.setVisibility(spinningDialog.GONE);

        }
    }

}

