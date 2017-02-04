package com.unqualsevol.moviesproject1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.unqualsevol.moviesproject1.model.Movie;
import com.unqualsevol.moviesproject1.utils.NetworkUtils;

import static com.unqualsevol.moviesproject1.model.ApplicationContract.INTENT_EXTRA_MOVIE_DATA;

public class DetailActivity extends AppCompatActivity {

    private TextView mTitleTextView;

    private ImageView mGreatPosterImageViem;

    private TextView mOriginalTitleTextView;

    private TextView mOverviewTextView;

    private TextView mUserRatingTextView;

    private RatingBar mUserRatingRatingBar;

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
        mUserRatingRatingBar = (RatingBar) findViewById(R.id.rb_movie_rating);
        mReleaseDateTextView = (TextView) findViewById(R.id.tv_release_date);
        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            updateView((Movie) intentThatStartedThisActivity.getSerializableExtra(INTENT_EXTRA_MOVIE_DATA));
        }
    }

    private void updateView(Movie movie) {
        mTitleTextView.setText(movie.getTitle());
        Picasso.with(this)
                .load(NetworkUtils.buildImageUrl(
                        getResources().getString(R.string.detail_size_poster), movie.getPosterPath()))
                .into(mGreatPosterImageViem);
        mOriginalTitleTextView.setText(getString(R.string.detail_original_title) + movie.getOriginalTitle());
        mOverviewTextView.setText(getString(R.string.detail_synopsis) + movie.getOverview());
        mUserRatingRatingBar.setRating(movie.getVoteAverage().floatValue()/2);
        mUserRatingTextView.setText("(" + movie.getVoteAverage() + ")");
        mReleaseDateTextView.setText(getString(R.string.detail_release_date) + movie.getReleaseDate());
    }
}
