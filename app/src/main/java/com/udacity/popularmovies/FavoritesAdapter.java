package com.udacity.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.database.FavoritesEntry;

import java.util.List;

import static android.content.ContentValues.TAG;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>{

    private List<FavoritesEntry> entries;
    private Context context;
    private int numberOfFavorites;
    // Member variable to handle item clicks
    final private PosterAdapter.PosterItemClickListener mOnClickListener;

    private String[] posters;

    public FavoritesAdapter(Context context, PosterAdapter.PosterItemClickListener listener)
    {

        this.context = context;
        mOnClickListener = listener;

    }

    public void setFavorites(final List<FavoritesEntry> favorites)
    {
        entries = favorites;
        posters = new String[entries.size()];
        getPosters();
    }

    public String getTitleTest()
    {
        if (entries != null)
            return entries.get(0).getTitle();
        else
            return "Null";
    }

    protected void getPosters()
    {
        if (entries != null) {
            for (int i = 0; i < entries.size(); i++) {
                posters[i] = entries.get(i).getPosterUrlString();
            }
        }
    }

    protected String[] getMovie(int index)
    {
        String[] movie = new String[6];
        if (entries != null)
        {
            // title, release date, poster, average vote, overview, id
            movie[0] = entries.get(index).getTitle();
            movie[1] = entries.get(index).getReleaseDate();
            movie[2] = entries.get(index).getPosterUrlString();
            movie[3] = entries.get(index).getAverageRating();
            movie[4] = entries.get(index).getSynopsis();
            movie[5] = entries.get(index).getId();
        }
        return movie;
    }

    public boolean favorited(String id)
    {
        if (entries == null)
            return false;
        for (int i = 0; i < entries.size(); i++)
        {
            if (entries.get(i).getId().equals(id))
                return true;
        }
        return false;
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutIdForItem = R.layout.poster_grid_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForItem, parent, shouldAttachToParentImmediately);
        return new FavoritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {
        getPosters();
        Log.d(TAG, " " + position + " " + posters[position]);
        holder.bind(posters[position]);
    }

    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (entries == null) {
            return 0;
        }
        return entries.size();
    }

    class FavoritesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView gridItemPosterView;

        public FavoritesViewHolder(View itemView)
        {
            super(itemView);
            gridItemPosterView = (ImageView) itemView.findViewById(R.id.poster_image);
            itemView.setOnClickListener(this);
        }


        void bind(String imageAddress) {
            Picasso.get().load(imageAddress).error(R.mipmap.ic_launcher).into(gridItemPosterView);

            /*
            int picture = R.drawable.peanut_day;
            gridItemPosterView.setImageResource(picture);
            */
        }

        @Override
        public void onClick(View v)
        {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClicked(clickedPosition);
        }
    }
}
