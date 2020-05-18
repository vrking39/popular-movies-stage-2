package com.example.movieproject1.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoritesContract {


    public static final String AUTHORITY = "com.example.movieproject1";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public FavoritesContract(){
    }

    public static final class FavoritesEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_NAME = "display_name";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_RELEASE = "released_date";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_POSTER = "poster_url";
        public static final String COLUMN_ID = "_id";
    }
}
