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

    private String json = null;

    public boolean started = false;

    static final String POPULAR = "http://api.themoviedb.org/3/movie/popular?api_key=";
    static final String TOP_RATED = "http://api.themoviedb.org/3/movie/top_rated?api_key=";
    static final String API_KEY = "";    // Add your api key

    String query = POPULAR + API_KEY;    // starting value

    Toast mToast;

    ProgressBar spinningDialog;

    FetchMoviesTask fetch = new FetchMoviesTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_posters);
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
        Spinner sort = (Spinner) findViewById(R.id.sort);
        sort.setAdapter(adapter);

        sort.setSelection(option);
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
        // getting rid of old toast
        /*
        if (mToast != null)
            mToast.cancel();

        String toastMessage = String.valueOf(clickedItemIndex);
        mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT);
        mToast.show();
        */


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
        Toast.makeText(parent.getContext(), selection, Toast.LENGTH_LONG).show();
        if (selection.equals("Most Popular"))
            query = POPULAR + API_KEY;
        else if (selection.equals("Top Rated"))
            query = TOP_RATED + API_KEY;
        new FetchMoviesTask().execute(query);

        /*
        mAdapter = new PosterAdapter(NUM_LIST_ITEMS, this, json);
        mAdapter.setJson(json);
        mPosterGrid.setAdapter(mAdapter);
        */

        // new FetchMoviesTask().execute(query);

        //mAdapter.selectCriteria(json);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
        // nothing
    }


    //  @Override
  //  public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
    //    MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
    //    inflater.inflate(R.menu.sort, menu);
        /* Return true so that the menu is displayed in the Toolbar */
      //  Spinner s = (Spinner) menu.findItem(R.id.sort).getActionView(); // find the spinner

        /*SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(getActivity().getActionBar()
                .getThemedContext(), R.array.sortBy, android.R.layout.simple_spinner_dropdown_item); //  create the adapter from a StringArray
        s.setAdapter(mSpinnerAdapter); // set the adapter
        */

    //   return true;
  // }

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
                json = jsonString;
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


     //  Picasso.get().load("https://scontent-dfw5-2.xx.fbcdn.net/v/t31.0-8/21427310_10210605307356571_2524072672159646116_o.jpg?_nc_cat=110&oh=c45c1b78f877829bcf42789c750f10b4&oe=5C4E0247").into(imageView);

/* add the spinner as a menu item?
<item android:id="@+id/spinner"
        android:title="@string/sort"
        android:orderInCategory="1"
        app:showAsAction="ifRoom"
        app:actionViewClass="android.widget.Spinner"
        android:entries="@array/sortBy"
        />
 */