package com.example.movieproject1.utils;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.net.URL;

public class UrlBuilder<getApiKey> {
    private static final String TAG = UrlBuilder.class.getSimpleName();
    private static final String QUERY_API = "api_key";
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie";


    public BufferedReader reader = null;

    public static URL buildUrl(String route, String API_KEY) {

        Uri uri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendPath(route)
                .appendQueryParameter(QUERY_API, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (Exception e) {
            Log.e(TAG, "Error making URL", e);
        }
        return url;
    }


}
