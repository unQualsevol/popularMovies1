package com.unqualsevol.moviesproject1.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unqualsevol.moviesproject1.R;
import com.unqualsevol.moviesproject1.model.Movie;
import com.unqualsevol.moviesproject1.model.MoviesPage;
import com.unqualsevol.moviesproject1.tasks.FetchMoviesTask;
import com.unqualsevol.moviesproject1.viewholders.PosterViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PosterAdapter extends RecyclerView.Adapter<PosterViewHolder>{

    private String TAG = PosterAdapter.class.getSimpleName();
    //private array of data
    private SparseArray<MoviesPage> moviesPageMap = new SparseArray<>();
    private List<Integer> loadingPages = new ArrayList<>();
    private int totalAmountOfMovies = 0;
    private String theMovieDb_api_key;

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        theMovieDb_api_key = parent.getResources().getString(R.string.themoviedb_api_key);
        View view = LayoutInflater.from(context).inflate(R.layout.movie_grid_item, parent, false);
        return new PosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PosterViewHolder holder, int position) {
        int page = 1 + position / 20;
        int posInPage = position % 20;
        MoviesPage currentPage = moviesPageMap.get(page);
        if(currentPage == null) {
            holder.showLoading();
            if(!loadingPages.contains(page)) {
                loadingPages.add(page);
                new FetchMoviesTask(this).execute("true", theMovieDb_api_key, "ca-ES", String.valueOf(page));
            }
        } else {
            Movie currentMovie = moviesPageMap.get(page).getMovies().get(posInPage);
            holder.setMovieData(currentMovie);
            holder.showData();
        }
    }

    @Override
    public int getItemCount() {
        return totalAmountOfMovies;
    }

    public void setData(MoviesPage page){
        //save data
        moviesPageMap.put(page.getPage(), page);
        loadingPages.remove(new Integer(page.getPage()));
        totalAmountOfMovies = page.getTotalResults();
        notifyDataSetChanged();
    }
}
