package com.udacity.popularmovies.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "favorites")
public class FavoritesEntry {

    // title, date, poster, average vote, synopsis, id
    // get reviews for a separate activity
    @PrimaryKey @NonNull
    private String id;
    private String title;
    private String releaseDate;
    private String posterUrlString;
    private String averageRating;
    private String synopsis;



    public FavoritesEntry(String title, String releaseDate,
                          String posterUrlString, String averageRating, String synopsis, String id)
    {
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterUrlString = posterUrlString;
        this.averageRating = averageRating;
        this.synopsis = synopsis;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterUrlString() {
        return posterUrlString;
    }

    public void setPosterUrlString(String posterUrlString) {
        this.posterUrlString = posterUrlString;
    }

    public String getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(String averageRating) {
        this.averageRating = averageRating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }
}
