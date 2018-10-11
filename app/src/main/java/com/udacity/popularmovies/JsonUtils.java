package com.udacity.popularmovies;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class JsonUtils {
    public static String[] getPoster(String json)
    {
        try {
            JSONObject queryResults = new JSONObject(json);
            JSONArray movies = queryResults.getJSONArray("results");
            String[] posters = new String[movies.length()];
            for (int i = 0; i < movies.length(); i++) {
                JSONObject movie = movies.getJSONObject(i);
                posters[i] = "http://image.tmdb.org/t/p/w185/" + movie.getString("poster_path");
            }
            return posters;
        } catch (JSONException je) {
            Log.e(TAG, je.toString());
            return null;
        } catch (NullPointerException ne) {Log.e(TAG, ne.toString()); return null;}

    }

    public static String[] getDetails(String json, int place)
    {
        try {
            JSONObject queryResults = new JSONObject(json);
            JSONArray movies = queryResults.getJSONArray("results");
            String[] details = new String[5];
            JSONObject movie = movies.getJSONObject(place);
            // title, release date, poster, vote average, plot synopsis
            String title = movie.getString("title");
            String releaseDate = movie.getString("release_date");
            String poster = "http://image.tmdb.org/t/p/w185/" + movie.getString("poster_path");
            String average = String.valueOf(movie.getInt("vote_average")) + " out of 10";
            String synopsis = movie.getString("overview");
            details[0] = title;
            details[1] = releaseDate;
            details[2] = poster;
            details[3] = average;
            details[4] = synopsis;
            return details;
        } catch (JSONException je) {
            Log.e(TAG, je.toString());
            return null;
        } catch (Exception e) {
            Log.e(TAG, "Some other problem");
            return null;
        }

    }
}
