package com.udacity.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import static android.content.ContentValues.TAG;

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterViewHolder> {
    private int mNumberItems;

    final private PosterItemClickListener mOnClickListener;

    protected String json;

    private String[] posters = JsonUtils.getPoster(json);

    public interface PosterItemClickListener{
        void onListItemClicked(int clickedItemIndex);
    }

    public PosterAdapter(int numberOfItems, PosterItemClickListener listener)
    {
        mNumberItems = numberOfItems;
        mOnClickListener = listener;
    }

    protected void setPosters()
    {
        posters = JsonUtils.getPoster(json);
    }

    protected void setJson(String jsonString)
    {
        json = jsonString;
        notifyDataSetChanged();
    }



    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        Context context = viewGroup.getContext();
        int layoutIdForItem = R.layout.poster_grid_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForItem, viewGroup, shouldAttachToParentImmediately);
        PosterViewHolder viewHolder = new PosterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PosterViewHolder holder, int number)
    {
        Log.d(TAG, " " + number + " " + posters[number]);
        holder.bind(posters[number]);
    }

    @Override
    public int getItemCount() { return mNumberItems; }

    public void selectCriteria(String option)
    {
        json = option;
        notifyDataSetChanged();
        setPosters();
    }

    // getter method
    String getJson()
    {
        return json;
    }

    class PosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView gridItemPosterView;

        public PosterViewHolder(View itemView)
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
