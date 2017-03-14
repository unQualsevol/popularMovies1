package com.unqualsevol.moviesproject1.utils;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public final class NetworkUtils {

    private final static Gson gson = new Gson();

    private final static OkHttpClient client = new OkHttpClient();

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String THEMOVIEDB_BASE_URL = "https://api.themoviedb.org/3";

    private static final String IMAGEDB_BASE_URL = "http://image.tmdb.org/t/p/w500";

    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch";

    final static String PATH_MOVIE = "movie";
    final static String PATH_REVIEWS = "reviews";
    final static String PATH_VIDEOS = "videos";

    final static String API_KEY_PARAM = "api_key";
    final static String LANGUAGE_PARAM = "language";
    final static String PAGE_PARAM = "page";

    public static URL buildMoviesUrl(String entryPoint, String apikey, String language, String page) {
        Uri builtUri = Uri.parse(THEMOVIEDB_BASE_URL + entryPoint).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, apikey)
                .appendQueryParameter(LANGUAGE_PARAM, language)
                .appendQueryParameter(PAGE_PARAM, page)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildTrailersUrl(String movieId, String apikey) {
        Uri builtUri = Uri.parse(THEMOVIEDB_BASE_URL)
                .buildUpon()
                .appendPath(PATH_MOVIE)
                .appendPath(movieId)
                .appendPath(PATH_VIDEOS)
                .appendQueryParameter(API_KEY_PARAM, apikey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildReviewsUrl(String movieId, String apikey, String page) {
        Uri builtUri = Uri.parse(THEMOVIEDB_BASE_URL)
                .buildUpon()
                .appendPath(PATH_MOVIE)
                .appendPath(movieId)
                .appendPath(PATH_REVIEWS)
                .appendQueryParameter(API_KEY_PARAM, apikey)
                .appendQueryParameter(PAGE_PARAM, page)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static <T> T getResponseFromHttpUrl(URL url, Class<T> type) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        return gson.fromJson(response.body().string(), type);
    }

    public static Uri buildImageUri(String posterPath) {
        return Uri.parse(IMAGEDB_BASE_URL + posterPath).buildUpon().build();
    }

    public static Uri buildYoutubeUri(String key) {
        return Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                .appendQueryParameter("v", key)
                .build();
    }
}