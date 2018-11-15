package com.udacity.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.net.URL;

import static android.content.ContentValues.TAG;

public class ReviewsActivity extends AppCompatActivity {

    protected static final String TITLE_KEY = "title";
    protected static final String ID_KEY = "id";

    final String LABEL_START = "Reviews for ";
    final String START_OF_REVIEWS_QUERY = "https://api.themoviedb.org/3/movie/";
    final String END_OF_REVIEWS_QUERY = "/reviews?api_key=" + BuildConfig.THE_MOVIE_DB_API_KEY;    // Add your api key

    TextView pageTitle;

    String id;

    String[] reviews;

    Context context = this;

    FetchReviewsTask fetch = new FetchReviewsTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        Toolbar toolbar = (Toolbar) findViewById(R.id.reviewsToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent startingIntent = getIntent();
        String title = startingIntent.getStringExtra(TITLE_KEY);
        if (title.length() < 20)
            title = LABEL_START + title;
        else
            title = LABEL_START + "\n" + title;
        pageTitle = (TextView) findViewById(R.id.reviews_label);
        pageTitle.setText(title);

        id = startingIntent.getStringExtra(ID_KEY);
        String query = START_OF_REVIEWS_QUERY + id + END_OF_REVIEWS_QUERY;
        fetch.execute(query);

    }

    private void populateListView()
    {
        // Create the ListView and its ArrayAdapter
        ArrayAdapter<String> reviewsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, reviews);
        ListView reviewsListView = (ListView) findViewById(R.id.reviews_list);
        reviewsListView.setAdapter(reviewsAdapter);

        TextView noReviews = (TextView) findViewById(R.id.no_reviews);
        if (reviews.length == 0)
        {
            reviewsListView.setVisibility(View.GONE);
            noReviews.setVisibility(View.VISIBLE);
        }
        else
        {
            reviewsListView.setVisibility(View.VISIBLE);
            noReviews.setVisibility(View.GONE);
        }
    }

    public class FetchReviewsTask extends AsyncTask<String, Void, String>
    {
        // Gets a JSON String from an HTTP request
        @Override
        protected String doInBackground(String... strings) {
            if (strings.length == 0)
                return null;
            String urlString = strings[0];
            URL queryUrl = NetworkUtils.createURL(urlString);
            try {
                return NetworkUtils.getJsonResponseFromHttpUrl(queryUrl);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, e.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String jsonString)
        {
            reviews = JsonUtils.getReviews(jsonString, context);
            populateListView();
        }
    }


}
