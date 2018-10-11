package com.udacity.popularmovies;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import static android.content.ContentValues.TAG;

public class NetworkUtils {

    public static URL createURL (String urlString)
    {
        URL url = null;
        try
        {
            url = new URL(urlString);
            return url;
        } catch (MalformedURLException mue) {
            Log.e(TAG, "Problem: ", mue);
            return null;
        }
    }

    public static String getJsonResponseFromHttpUrl(URL url) throws IOException
    {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");    // The scanner reads the whole stream
            boolean hasInput = scanner.hasNext();
            if (hasInput)
                return scanner.next();
            else
                return null;
        } finally {
            urlConnection.disconnect();
        }
    }
}
