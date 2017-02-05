package com.unqualsevol.moviesproject1;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.unqualsevol.moviesproject1.adapters.PosterAdapter;
import com.unqualsevol.moviesproject1.interfaces.OnRefreshCompleteListener;
import com.unqualsevol.moviesproject1.model.SearchType;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesGridActivity extends AppCompatActivity implements OnRefreshCompleteListener {

    private static final String TAG = MoviesGridActivity.class.getSimpleName();

    @BindView(R.id.recyclerview_movies) RecyclerView mRecyclerView;

    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeContainer;

    private PosterAdapter mPosterAdapter;

    private MenuItem showMostPopular;

    private MenuItem showTopRated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_grid);
        ButterKnife.bind(this);

        GridLayoutManager layoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.number_of_columns));
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mPosterAdapter = new PosterAdapter();

        mRecyclerView.setAdapter(mPosterAdapter);

        //loadMovies at least first page
        mPosterAdapter.setSearchType(SearchType.POPULAR);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPosterAdapter.restart();
            }
        });
        mPosterAdapter.registerOnRefreshCompleteListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.moviesgrid, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        showMostPopular = menu.findItem(R.id.action_show_most_popular);
        showTopRated = menu.findItem(R.id.action_show_top_rated);
        SearchType searchType = mPosterAdapter.getSearchType();
        showTopRated.setVisible(searchType != SearchType.TOP_RATED);
        showMostPopular.setVisible(searchType != SearchType.POPULAR);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_show_most_popular) {
            mPosterAdapter.setSearchType(SearchType.POPULAR);
            return true;
        } else if (id == R.id.action_show_top_rated) {
            mPosterAdapter.setSearchType(SearchType.TOP_RATED);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
