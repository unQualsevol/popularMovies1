package com.unqualsevol.moviesproject1.utils;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.unqualsevol.moviesproject1.data.MoviesContract;
import com.unqualsevol.moviesproject1.model.Movie;

import java.io.ByteArrayOutputStream;

public final class MoviesUtils {

    private MoviesUtils() {
        super();
    }

    public static ContentValues buildMovieContentValues(Movie movie, Bitmap poster) {
        ContentValues result = new ContentValues();
        result.put(MoviesContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        result.put(MoviesContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        result.put(MoviesContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        result.put(MoviesContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        result.put(MoviesContract.MovieEntry.COLUMN_POSTER, getBitmapAsByteArray(poster));
        result.put(MoviesContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        result.put(MoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage().floatValue());
        result.put(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        return result;
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        if(bitmap == null) {
            return new byte[0];
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
