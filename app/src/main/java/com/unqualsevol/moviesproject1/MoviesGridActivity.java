package com.unqualsevol.moviesproject1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.unqualsevol.moviesproject1.adapters.PosterAdapter;
import com.unqualsevol.moviesproject1.model.SearchType;

import java.util.Locale;

public class MoviesGridActivity extends AppCompatActivity {

    private static final String TAG = MoviesGridActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;

    private PosterAdapter mPosterAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    private MenuItem showMostPopular;

    private MenuItem showTopRated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_grid);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        GridLayoutManager layoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.number_of_columns));

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        //TODO: get the locale and the country from settings default the mobile Locale but able to choose
        String language = Locale.getDefault().getDisplayLanguage();
        String apiKey  = getResources().getString(R.string.themoviedb_api_key);
        mPosterAdapter = new PosterAdapter(language, apiKey);

        mRecyclerView.setAdapter(mPosterAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        //loadMovies at least first page
        mPosterAdapter.setSearchType(SearchType.POPULAR);
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
}
