package com.example.movieproject1;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DetailActivity extends AppCompatActivity {

    private static final String IMAGE_URL = "https://image.tmdb.org/t/p/w185";
    private static String VIDEO_URL;
    private static String REVIEWS_URL;
    public static String title;
    public static String poster;
    public static String overview;
    public static String rating;
    public static String release;
    public static FavoritesDB favoritesDB = new FavoritesDB();
    public static int id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView mPoster = findViewById(R.id.iv_movie_poster);
        TextView mTitle = findViewById(R.id.tv_title);
        TextView mOverview = findViewById(R.id.tv_plot);
        TextView mRating = findViewById(R.id.tv_rating);
        TextView mRelease = findViewById(R.id.tv_release_date);
        Button mTrailer = findViewById(R.id.btn_trailer);
        final Button mFavorites = findViewById(R.id.btn_favorites);


        title = getIntent().getStringExtra("title");
        poster = getIntent().getStringExtra("poster");
        overview = getIntent().getStringExtra("overview");
        rating = getIntent().getStringExtra("rating");
        release = getIntent().getStringExtra("release");

        setTitle("Title");
        Picasso.get()
                .load(IMAGE_URL + poster)
                .into(mPoster);
        mTitle.setText(title);
        mOverview.setText(overview);
        mRating.setText(rating);
        mRelease.setText(release);




        mTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchTrailer().execute(VIDEO_URL);

            }
        });

        id = Integer.parseInt(getIntent().getStringExtra("id"));

        if (favoritesDB.isMovieFavorite(MainActivity.contentResolver, id)) {
            mFavorites.setText((R.string.removeFavorite));
        }

        mFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favoritesDB.isMovieFavorite(MainActivity.contentResolver, id)){
                    favoritesDB.removeMovie(MainActivity.contentResolver, id);
                    mFavorites.setText(R.string.favorites);
                    Snackbar.make(v, "Removed from Favorites", Snackbar.LENGTH_SHORT).show();
                }
                else {
                    favoritesDB.addMovie(MainActivity.contentResolver);
                    mFavorites.setText((R.string.removeFavorite));
                    Snackbar.make(v, "Added to Favorites", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        String id = getIntent().getStringExtra("id");
        VIDEO_URL =  "https://api.themoviedb.org/3/movie/"+id+"/videos?api_key="+getResources().getString(R.string.API_key);
        REVIEWS_URL = "https://api.themoviedb.org/3/movie/"+id+"/reviews?api_key="+getResources().getString(R.string.API_key);
        new FetchReviews().execute(REVIEWS_URL);

    }


    private class FetchReviews extends AsyncTask<String, Void, JSONArray> {


        @Override
        protected JSONArray doInBackground(String... strings) {
            URL url = null;
            JSONArray resultArray = null;

            try {
                url = new URL(strings[0]);
                Log.e("URL", String.valueOf(url));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection httpURLConnection = null;
            String data = "";

            try {
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while (line != null) {
                    line = bufferedReader.readLine();
                    data += line;
                }

                JSONObject videoObject = new JSONObject(data);
                resultArray = videoObject.getJSONArray("results");

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }


            return resultArray;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            String reviewText = "";
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObj = jsonArray.getJSONObject(i);
                    reviewText += jsonObj.getString("author") + ":" + "\n\n" + jsonObj.getString("content") + "\n\n-------------------------------------------------\n\n";
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                TextView mReviews = findViewById(R.id.tv_reviews);
                mReviews.setText(reviewText);
            }
        }
    }

    private class FetchTrailer extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String video = strings[0];
            URL url = null;
            try {
                url = new URL(video);
                Log.e("URL", String.valueOf(url));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


            HttpURLConnection httpURLConnection = null;
            String data = "";
            String key = "";

            try {
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while (line != null) {
                    line = bufferedReader.readLine();
                    data += line;
                }
                JSONObject videoObject = new JSONObject(data);
                JSONArray resultArray = videoObject.getJSONArray("results");
                JSONObject videoKey = resultArray.getJSONObject(0);
                key = videoKey.getString("key");

                httpURLConnection.disconnect();

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return key;
        }

        @Override
        protected void onPostExecute(String key) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+key)));
            Log.e("youtube LINK", "http://www.youtube.com/watch?v="+key);
        }
    }
}
