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

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.unqualsevol.moviesproject1.model.ApplicationContract.INTENT_EXTRA_MOVIE_DATA;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.tv_movie_title) TextView mTitleTextView;

    @BindView(R.id.iv_great_poster) ImageView mGreatPosterImageView;

    @BindView(R.id.tv_original_movie_title) TextView mOriginalTitleTextView;

    @BindView(R.id.tv_movie_overview) TextView mOverviewTextView;

    @BindView(R.id.tv_user_rating) TextView mUserRatingTextView;

    @BindView(R.id.rb_movie_rating) RatingBar mUserRatingRatingBar;

    @BindView(R.id.tv_release_date) TextView mReleaseDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            updateView((Movie) intentThatStartedThisActivity.getParcelableExtra(INTENT_EXTRA_MOVIE_DATA));
        }
    }

    private void updateView(Movie movie) {
        mTitleTextView.setText(movie.getTitle());
        Picasso.with(this)
                .load(NetworkUtils.buildImageUrl(
                        getResources().getString(R.string.detail_size_poster), movie.getPosterPath()))
                .into(mGreatPosterImageView);
        mOriginalTitleTextView.setText(getString(R.string.detail_original_title) + movie.getOriginalTitle());
        mOverviewTextView.setText(getString(R.string.detail_synopsis) + movie.getOverview());
        mUserRatingRatingBar.setRating(movie.getVoteAverage().floatValue()/2);
        mUserRatingTextView.setText("(" + movie.getVoteAverage() + ")");
        mReleaseDateTextView.setText(getString(R.string.detail_release_date) + movie.getReleaseDate());
    }
}
