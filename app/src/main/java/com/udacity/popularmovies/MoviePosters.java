package com.udacity.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MoviePosters extends AppCompatActivity {

    private static final int NUM_LIST_ITEMS = 20;
    private static final int NUM_COLUMNS = 2;

    private PosterAdapter mAdapter;
    private RecyclerView mPosterGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_posters);

        mPosterGrid = (RecyclerView) findViewById(R.id.rv_posters);

        /*
         * A LinearLayoutManager is responsible for measuring and positioning item views within a
         * RecyclerView into a linear list. This means that it can produce either a horizontal or
         * vertical list depending on which parameter you pass in to the LinearLayoutManager
         * constructor. By default, if you don't specify an orientation, you get a vertical list.
         * In our case, we want a vertical list, so we don't need to pass in an orientation flag to
         * the LinearLayoutManager constructor.
         *
         * There are other LayoutManagers available to display your data in uniform grids,
         * staggered grids, and more! See the developer documentation for more details.
         */
        GridLayoutManager layoutManager = new GridLayoutManager(this, NUM_COLUMNS);
        mPosterGrid.setLayoutManager(layoutManager);

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mPosterGrid.setHasFixedSize(true);

        /*
         * The GreenAdapter is responsible for displaying each item in the list.
         */
        mAdapter = new PosterAdapter(NUM_LIST_ITEMS);

        mPosterGrid.setAdapter(mAdapter);

    }
}


     //  Picasso.get().load("https://scontent-dfw5-2.xx.fbcdn.net/v/t31.0-8/21427310_10210605307356571_2524072672159646116_o.jpg?_nc_cat=110&oh=c45c1b78f877829bcf42789c750f10b4&oe=5C4E0247").into(imageView);