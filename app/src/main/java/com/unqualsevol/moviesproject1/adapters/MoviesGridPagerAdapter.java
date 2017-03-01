package com.unqualsevol.moviesproject1.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.unqualsevol.moviesproject1.R;
import com.unqualsevol.moviesproject1.fragments.MoviesGridFragment;
import com.unqualsevol.moviesproject1.model.SearchType;

public class MoviesGridPagerAdapter extends FragmentPagerAdapter {

    public static final int MOST_POPULAR_TAB = 0;
    public static final int TOP_RATED_TAB = 1;
    public static final int FAVORITES_TAB = 2;
    public static final int NUMBER_OF_TABS = 3;

    private final String[] tabHeaderNames;

    public MoviesGridPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        tabHeaderNames = context.getResources().getStringArray(R.array.tab_headers);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case MOST_POPULAR_TAB:
                MoviesGridFragment mostPopularGrid =  new MoviesGridFragment();
                mostPopularGrid.setSearchType(SearchType.POPULAR);
                return mostPopularGrid;
            case TOP_RATED_TAB:
                MoviesGridFragment topRatedGrid = new MoviesGridFragment();
                topRatedGrid.setSearchType(SearchType.TOP_RATED);
                return topRatedGrid;
            case FAVORITES_TAB:
                MoviesGridFragment favoritesGrid = new MoviesGridFragment();
                favoritesGrid.setSearchType(SearchType.DATABASE);
                return favoritesGrid;
            default:
                throw new IllegalArgumentException("Invalid tab");
        }
    }

    @Override
    public int getCount() {
        return NUMBER_OF_TABS;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return tabHeaderNames[position];
    }
}
