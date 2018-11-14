package com.udacity.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBar;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.net.URL;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detailsToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String json = intent.getStringExtra("json");
        int place = intent.getIntExtra("place", 0);

        String[] details = JsonUtils.getDetails(json, place);

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
        }
    }
}

/*
        String chooserTitle = "Select an app to watch the video in";
        Intent chosenIntent = Intent.createChooser(trailerIntent, chooserTitle);
        startActivity(chosenIntent);
 */