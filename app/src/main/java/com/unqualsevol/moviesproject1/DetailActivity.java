package com.unqualsevol.moviesproject1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.unqualsevol.moviesproject1.model.Movie;

public class DetailActivity extends AppCompatActivity {

    private TextView mTitleTextView;

    private ImageView mGreatPosterImageViem;

    private TextView mOriginalTitleTextView;

    private TextView mOverviewTextView;

    private TextView mUserRatingTextView;

    private TextView mReleaseDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mTitleTextView = (TextView) findViewById(R.id.tv_movie_title);
        mGreatPosterImageViem = (ImageView) findViewById(R.id.iv_great_poster);
        mOriginalTitleTextView = (TextView) findViewById(R.id.tv_original_movie_title);
        mOverviewTextView = (TextView) findViewById(R.id.tv_movie_overview);
        mUserRatingTextView = (TextView) findViewById(R.id.tv_user_rating);
        mReleaseDateTextView = (TextView) findViewById(R.id.tv_release_date);
        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            updateView((Movie) intentThatStartedThisActivity.getSerializableExtra("movie"));
        }
    }

    private void updateView(Movie movie) {
        mTitleTextView.setText(movie.getTitle());
        //TODO url construction
        Picasso.with(this).load("http://image.tmdb.org/t/p/w780" + movie.getPosterPath()).into(mGreatPosterImageViem);
        mOriginalTitleTextView.setText("Original Title: " + movie.getOriginalTitle());
        mOverviewTextView.setText("Synopsis: " + movie.getOverview());
        mUserRatingTextView.setText("User rating: " + movie.getVoteAverage());
        mReleaseDateTextView.setText("Release date: " + movie.getReleaseDate());
    }
}
