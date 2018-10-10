package com.udacity.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

public class MoviePosters extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener, PosterAdapter.PosterItemClickListener {

    private static final int NUM_LIST_ITEMS = 20;
    private static final int NUM_COLUMNS = 2;

    private PosterAdapter mAdapter;
    private RecyclerView mPosterGrid;

    Toast mToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_posters);
        Toolbar toolbar = (Toolbar) findViewById(R.id.posterToolbar);
        setSupportActionBar(toolbar);


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sortBy,
                android.R.layout.simple_spinner_dropdown_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.custom_spinner_item);
        // apply the adapter to the spinner
        Spinner sort = (Spinner) findViewById(R.id.sort);
        sort.setAdapter(adapter);
        // setting the listener
        sort.setOnItemSelectedListener(this);

        /*
        Spinner sort = (Spinner) findViewById(R.id.sort);
        String sortedBy = String.valueOf(sort.getSelectedItem());
        */

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
        mAdapter = new PosterAdapter(NUM_LIST_ITEMS, this);

        mPosterGrid.setAdapter(mAdapter);

    }

    @Override
    public void onListItemClicked(int clickedItemIndex) {
        // getting rid of old toast
        if (mToast != null)
            mToast.cancel();

        /*
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
        Toast.makeText(parent.getContext(), parent.getItemAtPosition(pos).toString() , Toast.LENGTH_LONG).show();
        mAdapter.selectCriteria(parent.getItemAtPosition(pos).toString());
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


    /* spinner adapter? inflater?
    public void select(View view)
    {
        Spinner sort = (Spinner) findViewById(R.id.sort);
        String sortedBy = String.valueOf(sort.getSelectedItem());
        mAdapter = new PosterAdapter(NUM_LIST_ITEMS, sortedBy);
        mPosterGrid.setAdapter(mAdapter);
    }
    */
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