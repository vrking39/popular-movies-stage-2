package com.example.movieproject1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieproject1.utils.UrlBuilder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MainAdapter.ClickListener{
    
    private static final String CALLBACK_QUERY = "callbackQuery";
    private static final String CALLBACK_NAMESORT = "callbackNamesort";

    private RecyclerView mRecyclerView;

    private TextView mErrorMessage;
    private ProgressBar mLoading;
    
    private String queryMovie = "popular";
    private String nameSort = "Popular Movies";

    private MovieModel[] mMovie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.rv_main);
        LinearLayoutManager layoutManager
                = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mErrorMessage = findViewById(R.id.tv_error_message);
        mLoading = findViewById(R.id.pd_loading);

        if (isInternetAvailable() != false){
            mErrorMessage.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
        }
        else {

        setTitle(nameSort);

        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(CALLBACK_QUERY) || savedInstanceState.containsKey(CALLBACK_NAMESORT)){
                queryMovie = savedInstanceState.getString(CALLBACK_QUERY);
                nameSort = savedInstanceState.getString(CALLBACK_NAMESORT);
                setTitle(nameSort);
                loadMovieData();
                return;
            }
        }

        loadMovieData();}

    }

    private void loadMovieData(){
        new FetchMovieTask().execute(queryMovie);
        showMovieDataView();
    }

    private void showMovieDataView() {
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.popularity:
                queryMovie = "popular";
                new FetchMovieTask().execute(queryMovie);
                nameSort = "Popular Movies";
                setTitle(nameSort);
                loadMovieData();
                break;
            case R.id.top_rated:
                queryMovie = "top_rated";
                nameSort = "Top-Rated Movies";
                setTitle(nameSort);
                loadMovieData();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(int position) {

        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("title", mMovie[position].getTitle());
        intent.putExtra("poster", mMovie[position].getPoster());
        intent.putExtra("overview", mMovie[position].getOverview());
        intent.putExtra("rating", mMovie[position].getRating());
        intent.putExtra("release", mMovie[position].getRelease());
        startActivity(intent);

    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchMovieTask extends AsyncTask<String, Void, MovieModel[]> {
        public String data = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mRecyclerView.setVisibility(View.INVISIBLE);
            mLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected MovieModel[] doInBackground(String... strings) {
            URL url = UrlBuilder.buildUrl(strings[0]);

            try {

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while(line != null){
                    line = bufferedReader.readLine();
                    data = data + line;
                }

                mMovie = JsonUtils.parseJson(data);

                httpURLConnection.disconnect();
            } catch (Exception e) {
                Log.e("HELLOOOOO", "Problems create url", e);
            }
            return mMovie;
        }

        @Override
        protected void onPostExecute(MovieModel[] movieModels) {
            if (movieModels != null) {
                mLoading.setVisibility(View.INVISIBLE);
                mErrorMessage.setVisibility(View.INVISIBLE);
                mMovie = movieModels;
                MainAdapter adapter = new MainAdapter(mMovie, MainActivity.this);
                mRecyclerView.setAdapter(adapter);
                mRecyclerView.setVisibility(View.VISIBLE);
            } else {
                mErrorMessage.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.INVISIBLE);
                mLoading.setVisibility(View.INVISIBLE);
            }
        }
    }
}
