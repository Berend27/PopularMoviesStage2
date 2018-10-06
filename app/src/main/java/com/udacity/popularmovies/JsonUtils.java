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
            String[] posters = null;
            return posters;
        }

    }
}
