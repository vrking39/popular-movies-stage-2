package com.example.movieproject1.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.movieproject1.data.FavoritesContract.FavoritesEntry;

public class FavoritesProvider extends ContentProvider {
    private static final int MOVIE_DETAIL = 2;
    private static final int MOVIE_LIST = 1;
    static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(FavoritesContract.AUTHORITY, "movies", MOVIE_LIST);
        sUriMatcher.addURI(FavoritesContract.AUTHORITY, "#", MOVIE_DETAIL);
    }

    FavoritesDBHelper DBHelper;
    SQLiteDatabase database;

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (sUriMatcher.match(uri)){
            case MOVIE_LIST: {
                count = database.delete(FavoritesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case MOVIE_DETAIL: {
                count = database.delete(FavoritesEntry.TABLE_NAME, FavoritesEntry.COLUMN_ID + " = ?", selectionArgs);
                break;
            }
            default:
                throw new IllegalArgumentException("Unsupported URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri returnUri;
        long _id = database.insert(FavoritesEntry.TABLE_NAME, null, values);
        if (_id > 0) {
            returnUri = ContentUris.withAppendedId(FavoritesContract.CONTENT_URI, _id);
            getContext().getContentResolver().notifyChange(returnUri, null);
            return returnUri;
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        DBHelper = new FavoritesDBHelper(getContext());
        database = DBHelper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        if (sortOrder == null) sortOrder = FavoritesEntry.COLUMN_ID;
        switch (sUriMatcher.match(uri)){
            case MOVIE_LIST: {
                retCursor = database.query(
                        FavoritesEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            }
            case MOVIE_DETAIL: {
                retCursor = database.query(
                        FavoritesEntry.TABLE_NAME, projection, FavoritesEntry.COLUMN_ID + " = ?",
                        new String[]{uri.getLastPathSegment()}, null, null, sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Not needed");
    }
}
