package com.example.movieproject1;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.example.movieproject1.data.FavoritesContract;
import com.example.movieproject1.data.FavoritesContract.FavoritesEntry;


public class FavoritesDB {
    public static String[] fav_movies;
    public static String[] fav_dates;
    public static String[] fav_summary;
    public static String[] fav_votes;
    public static String[] fav_poster;
    public static String[] fav_id;
    static final String AUTHORITY_Uri = "content://" + FavoritesContract.AUTHORITY;

    public boolean isMovieFavorite(ContentResolver contentResolver, int id){
        boolean ret = false;
        Cursor cursor = contentResolver.query(Uri.parse(AUTHORITY_Uri + "/" + id), null, null, null, null, null);
        if (cursor != null && cursor.moveToNext()){
            ret = true;
            cursor.close();
        }
        return ret;
    }

    public void addMovie(ContentResolver contentResolver){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoritesEntry.COLUMN_ID, DetailActivity.id);
        contentValues.put(FavoritesEntry.COLUMN_NAME, DetailActivity.title);
        contentValues.put(FavoritesEntry.COLUMN_OVERVIEW, DetailActivity.overview);
        contentValues.put(FavoritesEntry.COLUMN_POSTER, DetailActivity.poster);
        contentValues.put(FavoritesEntry.COLUMN_RATING, DetailActivity.rating);
        contentValues.put(FavoritesEntry.COLUMN_RELEASE, DetailActivity.release);
        contentResolver.insert(Uri.parse(AUTHORITY_Uri + "/movies"), contentValues);
    }

    public void removeMovie(ContentResolver contentResolver, int id){
        Uri uri = Uri.parse(AUTHORITY_Uri + "/" + id);
        contentResolver.delete(uri, null, new String[]{id + ""});
    }

    public void getFavoriteMovies(ContentResolver contentResolver){
        Uri uri = Uri.parse(AUTHORITY_Uri + "/movies");
        Cursor cursor = contentResolver.query(uri, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()){

            fav_id=new String[cursor.getCount()];
            fav_dates=new String[cursor.getCount()];
            fav_movies=new String[cursor.getCount()];
            fav_poster=new String[cursor.getCount()];
            fav_summary=new String[cursor.getCount()];
            fav_votes=new String[cursor.getCount()];

            for(int i=0;i<cursor.getCount();i++){
                fav_id[i] = cursor.getInt(cursor.getColumnIndex(FavoritesEntry.COLUMN_ID))+"";
                fav_movies[i] = cursor.getString(cursor.getColumnIndex(FavoritesEntry.COLUMN_NAME));
                fav_summary[i] = cursor.getString(cursor.getColumnIndex(FavoritesEntry.COLUMN_OVERVIEW));
                fav_votes[i] = cursor.getString(cursor.getColumnIndex(FavoritesEntry.COLUMN_RATING));
                fav_poster[i] = cursor.getString(cursor.getColumnIndex(FavoritesEntry.COLUMN_POSTER));
                fav_dates[i]= cursor.getString(cursor.getColumnIndex(FavoritesEntry.COLUMN_RELEASE));
                cursor.moveToNext();
            }
            cursor.close();
        }else{
            fav_id=null;
            fav_dates=null;
            fav_movies=null;
            fav_poster=null;
            fav_summary=null;
            fav_votes=null;
        }
    }

    public static MovieModel[] GetFavoritesAsMovieModels() {
        MovieModel[] result = new MovieModel[fav_id.length];
        for (int i = 0; i < fav_id.length; i++) {
            MovieModel favMovie = new MovieModel();
            favMovie.setId(Integer.parseInt(fav_id[i]));
            favMovie.setTitle(fav_movies[i]);
            favMovie.setPoster(fav_poster[i]);
            favMovie.setOverview(fav_summary[i]);
            favMovie.setRelease(fav_dates[i]);
            favMovie.setRating(fav_votes[i]);
            result[i] = favMovie;
        }

        return result;
    }



}
