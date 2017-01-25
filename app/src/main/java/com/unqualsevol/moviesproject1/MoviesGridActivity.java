package com.unqualsevol.moviesproject1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.unqualsevol.moviesproject1.adapters.PosterAdapter;

public class MoviesGridActivity extends AppCompatActivity {

    private static final String TAG = MoviesGridActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;

    private PosterAdapter mPosterAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_grid);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        //TODO: check the number of columns or make it a setting
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mPosterAdapter = new PosterAdapter();

        mRecyclerView.setAdapter(mPosterAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        //loadMovies at least first page
        mPosterAdapter.setData();
        showMoviesDataView();
    }

    private void showMoviesDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }
}
