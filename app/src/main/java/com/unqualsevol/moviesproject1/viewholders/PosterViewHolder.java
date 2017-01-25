package com.unqualsevol.moviesproject1.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.unqualsevol.moviesproject1.R;

/**
 * Created by agimenez on 25/01/17.
 */

public class PosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final ImageView mPosterImageView;

    private final TextView mPosterTitleTextView;

    public PosterViewHolder(View view) {
        super(view);
        mPosterImageView = (ImageView) view.findViewById(R.id.iv_movie_poster);
        mPosterTitleTextView = (TextView) view.findViewById(R.id.tv_movie_poster_title);
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //TODO: how to call the click handler
    }

    public void setMovieData() {
        Picasso.with(itemView.getContext()).load("http://image.tmdb.org/t/p/w342//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg").into(mPosterImageView);
        mPosterTitleTextView.setText("Interstellar");
    }
}
