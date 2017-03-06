package com.unqualsevol.moviesproject1.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unqualsevol.moviesproject1.R;
import com.unqualsevol.moviesproject1.adapters.FavoritesAdapter;
import com.unqualsevol.moviesproject1.adapters.PosterAdapter;
import com.unqualsevol.moviesproject1.data.MoviesContract;
import com.unqualsevol.moviesproject1.interfaces.OnRefreshCompleteListener;
import com.unqualsevol.moviesproject1.model.SearchType;

public class MoviesGridFragment extends Fragment
        implements OnRefreshCompleteListener,
        LoaderManager.LoaderCallbacks<Cursor>{

    public static final String[] POSTER_PROJECTION = {
            MoviesContract.MovieEntry.COLUMN_MOVIE_ID,
            MoviesContract.MovieEntry.COLUMN_POSTER,
            MoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE,
    };

    public static final int INDEX_MOVIE_ID = 0;
    public static final int INDEX_POSTER_IMAGE = 1;
    public static final int INDEX_VOTE_AVERAGE = 2;

    public static final int ID_POSTER_LOADER = 491;

    RecyclerView mRecyclerView;

    SwipeRefreshLayout swipeContainer;

    private PosterAdapter mPosterAdapter = new PosterAdapter();

    private FavoritesAdapter mFavoritesAdapter = new FavoritesAdapter();

    private SearchType mSearchType;

    public void setSearchType(SearchType mSearchType) {
        this.mSearchType = mSearchType;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies_grid, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_movies);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), getResources().getInteger(R.integer.number_of_columns));
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("searchType", mSearchType.ordinal());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null && savedInstanceState.containsKey("searchType")) {
            int searchTypeOrdinal = savedInstanceState.getInt("searchType");
            mSearchType = SearchType.values()[searchTypeOrdinal];
        }

        switch (mSearchType) {
            case POPULAR:
            case TOP_RATED:
                mRecyclerView.setAdapter(mPosterAdapter);
                mPosterAdapter.setSearchType(mSearchType);
                mPosterAdapter.registerOnRefreshCompleteListener(this);
                mPosterAdapter.restart();
                swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mPosterAdapter.restart();
                    }
                });
                break;
            case DATABASE:
                mRecyclerView.setAdapter(mFavoritesAdapter);
                getLoaderManager().initLoader(ID_POSTER_LOADER, null, this);
                swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getLoaderManager().restartLoader(ID_POSTER_LOADER, null, MoviesGridFragment.this);
                    }
                });

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if(mSearchType == SearchType.DATABASE) {
            getLoaderManager().restartLoader(ID_POSTER_LOADER, null, this);
        }
    }

    @Override
    public void onRefreshComplete() {
        swipeContainer.setRefreshing(false);
    }

    @Override
    public void onFailedRefresh() {
        swipeContainer.setRefreshing(false);
        Snackbar.make(mRecyclerView, R.string.not_available_error_message, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.action_refresh, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPosterAdapter.restart();
                    }
                }).show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {
            case ID_POSTER_LOADER:
                Uri moviesQueryUri = MoviesContract.MovieEntry.CONTENT_URI;
                String sortOrder = MoviesContract.MovieEntry._ID + " ASC";

                return new CursorLoader(this.getContext(),
                        moviesQueryUri,
                        POSTER_PROJECTION,
                        null,
                        null,
                        sortOrder);
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mFavoritesAdapter.swapCursor(data);
        onRefreshComplete();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFavoritesAdapter.swapCursor(null);
    }
}
