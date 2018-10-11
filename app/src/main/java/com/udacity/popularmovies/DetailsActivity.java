package com.udacity.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBar;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class DetailsActivity extends AppCompatActivity {

    TextView title;
    TextView date;
    ImageView poster;
    TextView average;
    TextView overview;

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
    }
}
