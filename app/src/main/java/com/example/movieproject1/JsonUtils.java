package com.example.movieproject1;

import com.example.movieproject1.MovieModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {

    private static final String RESULTS = "results";
    private static final String TITLE = "title";
    private static final String POSTER = "poster_path";
    private static final String OVERVIEW = "overview";
    private static final String RATING = "vote_average";
    private static final String RELEASE = "release_date";

    public static MovieModel[] parseJson(String jsonData) throws JSONException {

        JSONObject jsonRoot = new JSONObject(jsonData);

        JSONArray jsonArray = jsonRoot.getJSONArray(RESULTS);
        MovieModel[] result = new MovieModel[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            MovieModel movie = new MovieModel();
            movie.setTitle(jsonArray.getJSONObject(i).optString(TITLE));
            movie.setPoster(jsonArray.getJSONObject(i).optString(POSTER));
            movie.setOverview(jsonArray.getJSONObject(i).optString(OVERVIEW));
            movie.setRating(jsonArray.getJSONObject(i).optString(RATING));
            movie.setRelease(jsonArray.getJSONObject(i).optString(RELEASE));
            result[i] = movie;
        }
        return result;

    }
}
