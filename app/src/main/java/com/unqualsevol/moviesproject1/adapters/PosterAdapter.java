package com.unqualsevol.moviesproject1.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unqualsevol.moviesproject1.BuildConfig;
import com.unqualsevol.moviesproject1.R;
import com.unqualsevol.moviesproject1.interfaces.DataReceiver;
import com.unqualsevol.moviesproject1.interfaces.OnRefreshCompleteListener;
import com.unqualsevol.moviesproject1.model.Movie;
import com.unqualsevol.moviesproject1.model.MoviesPage;
import com.unqualsevol.moviesproject1.model.SearchMode;
import com.unqualsevol.moviesproject1.tasks.FetchMoviesTask;
import com.unqualsevol.moviesproject1.viewholders.PosterViewHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class PosterAdapter extends RecyclerView.Adapter<PosterViewHolder> implements DataReceiver<MoviesPage> {

    private String TAG = PosterAdapter.class.getSimpleName();
    //private array of data
    private SparseArray<MoviesPage> moviesPageMap = new SparseArray<>();
    private List<Integer> loadingPages = new ArrayList<>();
    private int totalAmountOfMovies = 0;
    private final String apiKey;
    private final String language;
    private SearchMode searchMode;
    private Set<OnRefreshCompleteListener> listeners = new HashSet<>();

    public PosterAdapter() {
        this.language = Locale.getDefault().getLanguage();
        this.apiKey = BuildConfig.themoviedb_api_key;
    }

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
        MoviesPage currentPage = moviesPageMap.get(page);
        if (currentPage == null) {
            holder.setMovieData(null);
            holder.showLoading();
            loadMoviePage(page, searchMode);
        } else {
            Movie currentMovie = moviesPageMap.get(page).getMovies().get(posInPage);
            holder.setMovieData(currentMovie);
            holder.showData();
        }
    }

    public void loadMoviePage(int page, SearchMode searchMode) {

        if (moviesPageMap.get(page) == null && !loadingPages.contains(page)) {
            loadingPages.add(page);
            new FetchMoviesTask(this).execute(FetchMoviesTask.MOVIES, searchMode.getEntryPoint(), apiKey, language, String.valueOf(page));
        }
    }

    @Override
    public int getItemCount() {
        return totalAmountOfMovies;
    }

    @Override
    public void setData(MoviesPage page) {
        if (page == null) {
            loadingPages.clear();
            for (OnRefreshCompleteListener listener : listeners) {
                listener.onFailedRefresh();
            }
        } else {
            moviesPageMap.put(page.getPage(), page);
            loadingPages.remove(Integer.valueOf(page.getPage()));
            totalAmountOfMovies = page.getTotalResults();
            notifyDataSetChanged();
            for (OnRefreshCompleteListener listener : listeners) {
                listener.onRefreshComplete();
            }
        }
    }

    public SearchMode getSearchMode() {
        return searchMode;
    }

    public void setSearchMode(SearchMode searchMode) {
        this.searchMode = searchMode;
    }

    private void clear() {
        totalAmountOfMovies = 0;
        loadingPages.clear();
        moviesPageMap.clear();
        notifyDataSetChanged();
    }

    public void restart() {
        clear();
        loadMoviePage(FetchMoviesTask.FIRST_PAGE, searchMode);
    }

    public void registerOnRefreshCompleteListener(OnRefreshCompleteListener listener) {
        listeners.add(listener);
    }
}
