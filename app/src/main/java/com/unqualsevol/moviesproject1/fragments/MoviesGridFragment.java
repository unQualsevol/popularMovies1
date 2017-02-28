package com.unqualsevol.moviesproject1.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unqualsevol.moviesproject1.R;
import com.unqualsevol.moviesproject1.adapters.PosterAdapter;
import com.unqualsevol.moviesproject1.interfaces.OnRefreshCompleteListener;
import com.unqualsevol.moviesproject1.model.SearchType;

public class MoviesGridFragment extends Fragment implements OnRefreshCompleteListener {


    RecyclerView mRecyclerView;

    SwipeRefreshLayout swipeContainer;

    private PosterAdapter mPosterAdapter = new PosterAdapter();

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

        return view;
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
}
