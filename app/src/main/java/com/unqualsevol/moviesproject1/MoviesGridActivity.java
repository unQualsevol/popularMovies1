package com.unqualsevol.moviesproject1;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.unqualsevol.moviesproject1.adapters.MoviesGridPagerAdapter;

import butterknife.ButterKnife;

public class MoviesGridActivity extends AppCompatActivity {

    private static final String TAG = MoviesGridActivity.class.getSimpleName();


    private MenuItem showMostPopular;

    private MenuItem showTopRated;
    private ViewPager mViewPager;

    private MoviesGridPagerAdapter mMoviesGridPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_grid);
        ButterKnife.bind(this);

        mMoviesGridPagerAdapter = new MoviesGridPagerAdapter(getSupportFragmentManager(), this);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mMoviesGridPagerAdapter);

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
//        SearchType searchType = mPosterAdapter.getSearchType();
        int current = mViewPager.getCurrentItem();
        showTopRated.setVisible(current != MoviesGridPagerAdapter.TOP_RATED_TAB);
        showMostPopular.setVisible(current != MoviesGridPagerAdapter.MOST_POPULAR_TAB);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_show_most_popular) {
            //mPosterAdapter.setSearchType(SearchType.POPULAR);
            mViewPager.setCurrentItem(0);
            return true;
        } else if (id == R.id.action_show_top_rated) {
            //mPosterAdapter.setSearchType(SearchType.TOP_RATED);
            mViewPager.setCurrentItem(1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
