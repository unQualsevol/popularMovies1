package com.unqualsevol.moviesproject1.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.unqualsevol.moviesproject1.interfaces.DataReceiver;
import com.unqualsevol.moviesproject1.model.MoviesPage;
import com.unqualsevol.moviesproject1.utils.NetworkUtils;

import java.net.URL;

public class FetchMoviesTask extends AsyncTask<String, Void, MoviesPage> {

    private static final String TAG = FetchMoviesTask.class.getSimpleName();
    private final DataReceiver dataReceiver;

    public FetchMoviesTask(DataReceiver dataReceiver) {
        this.dataReceiver = dataReceiver;
    }

    @Override
    protected MoviesPage doInBackground(String... params) {
        if(params.length != 4) {
            return null;
        }
        URL url = NetworkUtils.buildMoviesUrl(params[0], params[1], params[2], params[3]);
        MoviesPage result = null;
        try {
            result = NetworkUtils
                    .getResponseFromHttpUrl(url, MoviesPage.class);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return result;
    }

    @Override
    protected void onPostExecute(MoviesPage page) {
        dataReceiver.setData(page);
    }
}
