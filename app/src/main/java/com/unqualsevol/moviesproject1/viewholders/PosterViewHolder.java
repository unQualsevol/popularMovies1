package com.unqualsevol.moviesproject1.viewholders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.unqualsevol.moviesproject1.DetailActivity;
import com.unqualsevol.moviesproject1.R;
import com.unqualsevol.moviesproject1.model.Movie;

public class PosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final ImageView mPosterImageView;

    private final TextView mPosterTitleTextView;

    private final ProgressBar mPosterProgressBar;

    private Movie currentMovie;

    public PosterViewHolder(View view) {
        super(view);
        mPosterImageView = (ImageView) view.findViewById(R.id.iv_movie_poster);
        mPosterTitleTextView = (TextView) view.findViewById(R.id.tv_movie_poster_title);
        mPosterProgressBar = (ProgressBar) view.findViewById(R.id.pb_loading_movie_poster);
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //TODO: how to call the click handler
        if(currentMovie != null) {
            Context context = itemView.getContext();
            Intent intentToStartDetailActivity = new Intent(context, DetailActivity.class);
            intentToStartDetailActivity.putExtra("movie", currentMovie);
            context.startActivity(intentToStartDetailActivity);
        }
    }

    public void setMovieData(Movie data) {
        currentMovie = data;
        if(data == null) {
            showLoading();
        } else {
            updateViewHolder(data.getPosterPath(), data.getTitle());
            showData();
        }
    }

    public void updateViewHolder(String posterPath, String title) {
        //TODO: extract url construction
        Picasso.with(itemView.getContext()).load("http://image.tmdb.org/t/p/w342"+posterPath).into(mPosterImageView);
        mPosterTitleTextView.setText(title);
    }

    public void showData() {
        mPosterImageView.setVisibility(View.VISIBLE);
        mPosterTitleTextView.setVisibility(View.VISIBLE);
        mPosterProgressBar.setVisibility(View.INVISIBLE);
    }

    public void showLoading() {
        mPosterImageView.setVisibility(View.INVISIBLE);
        mPosterTitleTextView.setVisibility(View.INVISIBLE);
        mPosterProgressBar.setVisibility(View.VISIBLE);
    }
}
