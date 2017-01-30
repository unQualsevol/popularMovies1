package com.unqualsevol.moviesproject1.tasks;

import android.os.AsyncTask;

import com.unqualsevol.moviesproject1.adapters.PosterAdapter;
import com.unqualsevol.moviesproject1.model.MoviesPage;
import com.unqualsevol.moviesproject1.utils.NetworkUtils;

import java.net.URL;
import java.net.UnknownHostException;

public class FetchMoviesTask extends AsyncTask<String, Void, MoviesPage> {

    private final PosterAdapter adapter;
    //TODO: change poster adapter for an interface
    public FetchMoviesTask(PosterAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    protected MoviesPage doInBackground(String... params) {
        //TODO: validate size of params
        URL url = NetworkUtils.buildUrl(params[0], params[1], params[2], params[3]);
        MoviesPage result;
        try {
            result = NetworkUtils
                    .getResponseFromHttpUrl(url, MoviesPage.class);
        } catch (UnknownHostException e) {
            //TODO show some useful error
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    @Override
    protected void onPostExecute(MoviesPage page) {
        adapter.setData(page);
    }
}
