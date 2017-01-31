package com.unqualsevol.moviesproject1;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.unqualsevol.moviesproject1.adapters.PosterAdapter;
import com.unqualsevol.moviesproject1.interfaces.OnRefreshCompleteListener;
import com.unqualsevol.moviesproject1.model.SearchType;

import java.util.Locale;

public class MoviesGridActivity extends AppCompatActivity implements OnRefreshCompleteListener {

    private static final String TAG = MoviesGridActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;

    private PosterAdapter mPosterAdapter;

    private SwipeRefreshLayout swipeContainer;

    private MenuItem showMostPopular;

    private MenuItem showTopRated;

    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_grid);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);

        GridLayoutManager layoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.number_of_columns));
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        //TODO: get the locale and the country from settings default the mobile Locale but able to choose
        String language = Locale.getDefault().getDisplayLanguage();
        String apiKey  = getResources().getString(R.string.themoviedb_api_key);
        mPosterAdapter = new PosterAdapter(language, apiKey);

        mRecyclerView.setAdapter(mPosterAdapter);

        //loadMovies at least first page
        mPosterAdapter.setSearchType(SearchType.POPULAR);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
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
        if(mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, R.string.not_available_error_message, Toast.LENGTH_SHORT);
        mToast.show();
    }
}
