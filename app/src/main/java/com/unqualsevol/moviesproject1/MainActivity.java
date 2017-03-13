package com.unqualsevol.moviesproject1;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.unqualsevol.moviesproject1.adapters.MoviesGridPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    private MoviesGridPagerAdapter mMoviesGridPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesGridPagerAdapter = new MoviesGridPagerAdapter(getSupportFragmentManager(), this);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mMoviesGridPagerAdapter);

    }
}
