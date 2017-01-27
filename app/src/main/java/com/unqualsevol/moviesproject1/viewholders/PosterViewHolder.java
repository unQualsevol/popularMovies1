package com.unqualsevol.moviesproject1.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.unqualsevol.moviesproject1.R;

/**
 * Created by agimenez on 25/01/17.
 */

public class PosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final ImageView mPosterImageView;

    private final TextView mPosterTitleTextView;

    private final ProgressBar mPosterProgressBar;

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
    }

    public void setMovieData(String posterPath, String title) {
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
