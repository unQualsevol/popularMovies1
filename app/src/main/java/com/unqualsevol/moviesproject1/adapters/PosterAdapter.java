package com.unqualsevol.moviesproject1.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

/**
 * Created by agimenez on 25/01/17.
 */

public class PosterAdapter extends RecyclerView.Adapter<PosterViewHolder>{

    private String TAG = PosterAdapter.class.getSimpleName();
    //private array of data
    private Map<Integer, MoviesPage> moviesPageMap = new HashMap<>();
    private List<Integer> loadingPages = new ArrayList<>();
    private int totalAmountOfMovies = 0;

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.movie_grid_item, parent, false);
        return new PosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PosterViewHolder holder, int position) {
        int page = 1 + position / 20;
        int posInPage = position % 20;
        if(moviesPageMap.containsKey(page)) {
            Movie currentMovie = moviesPageMap.get(page).getMovies().get(posInPage);
            holder.setMovieData(currentMovie.getPoster_path(), currentMovie.getTitle());
            holder.showData();
        } else {
            holder.showLoading();
            if(!loadingPages.contains(page)) {
                loadingPages.add(page);
                new FetchMoviesTask(this).execute("true", "2a00000d535e284f8685741dbd16b5c6", "ca_ES", String.valueOf(page));
            }
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
        totalAmountOfMovies = page.getTotal_results();
        notifyDataSetChanged();
    }
}
