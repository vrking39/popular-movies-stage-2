package com.example.movieproject1;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private static final String IMAGE_URL = "https://image.tmdb.org/t/p/w185";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView mPoster = findViewById(R.id.iv_movie_poster);
        TextView mTitle = findViewById(R.id.tv_title);
        TextView mOverview = findViewById(R.id.tv_plot);
        TextView mRating = findViewById(R.id.tv_rating);
        TextView mRelease = findViewById(R.id.tv_release_date);

        String title = getIntent().getStringExtra("title");
        String poster = getIntent().getStringExtra("poster");
        String overview = getIntent().getStringExtra("overview");
        String rating = getIntent().getStringExtra("rating");
        String release = getIntent().getStringExtra("release");

        setTitle("Title");
        Picasso.get()
                .load(IMAGE_URL + poster)
                .into(mPoster);
        mTitle.setText(title);
        mOverview.setText(overview);
        mRating.setText(rating);
        mRelease.setText(release);
    }
}
