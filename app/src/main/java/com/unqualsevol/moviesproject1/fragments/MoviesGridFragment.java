package com.unqualsevol.moviesproject1.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.unqualsevol.moviesproject1.model.SearchMode;
import com.unqualsevol.moviesproject1.tasks.FetchMoviesTask;

import static com.unqualsevol.moviesproject1.model.ApplicationContract.INTENT_EXTRA_SEARCH_TYPE;
import static com.unqualsevol.moviesproject1.model.ApplicationContract.KEY_STATE_RV_MOVIES_LAYOUT_MANAGER_STATUS;
import static com.unqualsevol.moviesproject1.model.ApplicationContract.KEY_STATE_RV_MOVIES_POSITION;
import static com.unqualsevol.moviesproject1.utils.MoviesUtils.calculateNoOfColumns;

public class MoviesGridFragment extends Fragment
        implements OnRefreshCompleteListener,
        LoaderManager.LoaderCallbacks<Cursor> {

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

    private GridLayoutManager mLayoutManager;

    private SearchMode mSearchMode;

    private Parcelable mPreviousRecyclerViewStatus;

    public void setSearchType(SearchMode mSearchMode) {
        this.mSearchMode = mSearchMode;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies_grid, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_movies);
        mLayoutManager = new GridLayoutManager(getContext(), calculateNoOfColumns(getContext()));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(INTENT_EXTRA_SEARCH_TYPE, mSearchMode.ordinal());
        outState.putInt(KEY_STATE_RV_MOVIES_POSITION, mLayoutManager.findFirstCompletelyVisibleItemPosition());
        outState.putParcelable(KEY_STATE_RV_MOVIES_LAYOUT_MANAGER_STATUS, mLayoutManager.onSaveInstanceState());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int page = FetchMoviesTask.FIRST_PAGE;
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(INTENT_EXTRA_SEARCH_TYPE)) {
                int searchTypeOrdinal = savedInstanceState.getInt(INTENT_EXTRA_SEARCH_TYPE);
                mSearchMode = SearchMode.values()[searchTypeOrdinal];
            }
            if (savedInstanceState.containsKey(KEY_STATE_RV_MOVIES_POSITION)) {
                int position = savedInstanceState.getInt(KEY_STATE_RV_MOVIES_POSITION);
                page = 1 + position/20  ;
            }
            if(savedInstanceState.containsKey(KEY_STATE_RV_MOVIES_LAYOUT_MANAGER_STATUS)) {
                mPreviousRecyclerViewStatus = savedInstanceState.getParcelable(KEY_STATE_RV_MOVIES_LAYOUT_MANAGER_STATUS);
            }
        }

        switch (mSearchMode) {
            case POPULAR:
            case TOP_RATED:
                mRecyclerView.setAdapter(mPosterAdapter);
                mPosterAdapter.setSearchMode(mSearchMode);
                mPosterAdapter.registerOnRefreshCompleteListener(this);
                mPosterAdapter.loadMoviePage(page, mSearchMode);
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
        if (mSearchMode == SearchMode.DATABASE) {
            getLoaderManager().restartLoader(ID_POSTER_LOADER, null, this);
        }
    }

    @Override
    public void onRefreshComplete() {
        swipeContainer.setRefreshing(false);
        if(mPreviousRecyclerViewStatus != null) {
            mLayoutManager.onRestoreInstanceState(mPreviousRecyclerViewStatus);
            mPreviousRecyclerViewStatus = null;
        }
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
