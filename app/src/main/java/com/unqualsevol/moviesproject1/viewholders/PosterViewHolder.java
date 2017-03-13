package com.unqualsevol.moviesproject1.viewholders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;

import com.squareup.picasso.Picasso;
import com.unqualsevol.moviesproject1.DetailActivity;
import com.unqualsevol.moviesproject1.R;
import com.unqualsevol.moviesproject1.model.Movie;
import com.unqualsevol.moviesproject1.utils.NetworkUtils;

import static com.unqualsevol.moviesproject1.model.ApplicationContract.INTENT_EXTRA_MOVIE_DATA;
import static com.unqualsevol.moviesproject1.model.ApplicationContract.INTENT_EXTRA_MOVIE_ID;

public class PosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    private final ImageView mPosterImageView;

    private final RatingBar mRatingRatingBar;

    private final ProgressBar mPosterProgressBar;

    private Movie mMovie;

    private int mMovieId;

    public PosterViewHolder(View view) {
        super(view);
        mPosterImageView = (ImageView) view.findViewById(R.id.iv_movie_poster);
        mRatingRatingBar = (RatingBar) view.findViewById(R.id.rb_small_movie_rating);
        mPosterProgressBar = (ProgressBar) view.findViewById(R.id.pb_loading_movie_poster);
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Context context = itemView.getContext();
        if (mMovie != null) {
            Intent intentToStartDetailActivity = new Intent(context, DetailActivity.class);
            intentToStartDetailActivity.putExtra(INTENT_EXTRA_MOVIE_DATA, mMovie);
            context.startActivity(intentToStartDetailActivity);
        } else if (mMovieId != 0) {
            Intent intentToStartDetailActivity = new Intent(context, DetailActivity.class);
            intentToStartDetailActivity.putExtra(INTENT_EXTRA_MOVIE_ID, mMovieId);
            context.startActivity(intentToStartDetailActivity);
        }
    }

    public void setMovieData(Movie data) {
        mMovie = data;
        if (data == null) {
            showLoading();
        } else {
            updateViewHolder(data.getPosterPath(), data.getVoteAverage().floatValue() / 2);
            showData();
        }
    }

    public void updateViewHolder(String posterPath, float rating) {
        Picasso.with(itemView.getContext())
                .load(NetworkUtils.buildImageUri(posterPath))
                .into(mPosterImageView);
        mRatingRatingBar.setRating(rating);
    }

    public void updateViewHolder(int movieId, Bitmap poster, float rating) {
        mMovieId = movieId;
        mPosterImageView.setImageBitmap(poster);
        mRatingRatingBar.setRating(rating);
    }

    public void showData() {
        mPosterImageView.setVisibility(View.VISIBLE);
        mRatingRatingBar.setVisibility(View.VISIBLE);
        mPosterProgressBar.setVisibility(View.INVISIBLE);
    }

    public void showLoading() {
        mPosterImageView.setVisibility(View.INVISIBLE);
        mRatingRatingBar.setVisibility(View.INVISIBLE);
        mPosterProgressBar.setVisibility(View.VISIBLE);
    }
}
