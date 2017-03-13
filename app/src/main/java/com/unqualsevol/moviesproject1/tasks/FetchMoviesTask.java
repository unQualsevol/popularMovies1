package com.unqualsevol.moviesproject1.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.unqualsevol.moviesproject1.interfaces.DataReceiver;
import com.unqualsevol.moviesproject1.model.MoviesPage;
import com.unqualsevol.moviesproject1.model.Page;
import com.unqualsevol.moviesproject1.model.ReviewsPage;
import com.unqualsevol.moviesproject1.model.TrailersPage;
import com.unqualsevol.moviesproject1.utils.NetworkUtils;

import java.net.URL;

public class FetchMoviesTask extends AsyncTask<String, Void, Page> {

    public static final String MOVIES = "MOVIES";
    public static final String TRAILERS = "TRAILERS";
    public static final String REVIEWS = "REVIEWS";
    public static final int FIRST_PAGE = 1;

    private static final String TAG = FetchMoviesTask.class.getSimpleName();
    private final DataReceiver dataReceiver;

    public FetchMoviesTask(DataReceiver dataReceiver) {
        this.dataReceiver = dataReceiver;
    }

    @Override
    protected Page doInBackground(String... params) {
        if(params.length < 2) {
            return null;
        }
        URL url;
        Class<? extends Page> clazz;
        switch (params[0]) {
            case MOVIES:
                if(params.length != 5) return null;
                url = NetworkUtils.buildMoviesUrl(params[1], params[2], params[3], params[4]);
                clazz = MoviesPage.class;
                break;
            case TRAILERS:
                if(params.length != 3) return null;
                url = NetworkUtils.buildTrailersUrl(params[1], params[2]);
                clazz = TrailersPage.class;
                break;
            case REVIEWS:
                if(params.length != 4) return null;
                url = NetworkUtils.buildReviewsUrl(params[1], params[2], params[3]);
                clazz = ReviewsPage.class;
                break;
            default:
                throw new IllegalArgumentException("Invalid request type.");
        }


        Page result = null;
        try {
            result = NetworkUtils
                    .getResponseFromHttpUrl(url, clazz);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return result;
    }

    @Override
    protected void onPostExecute(Page page) {
        dataReceiver.setData(page);
    }
}
