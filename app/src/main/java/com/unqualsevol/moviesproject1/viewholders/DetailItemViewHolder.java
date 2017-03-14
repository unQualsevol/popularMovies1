package com.unqualsevol.moviesproject1.viewholders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.unqualsevol.moviesproject1.R;
import com.unqualsevol.moviesproject1.model.Review;
import com.unqualsevol.moviesproject1.model.Trailer;
import com.unqualsevol.moviesproject1.utils.NetworkUtils;


public class DetailItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final ImageView mIconImageView;

    private final TextView mContentTextView;

    private TextView mAuthorTextView;

    private final ProgressBar mProgressBar;

    private String mTrailerKey;

    public DetailItemViewHolder(View view) {
        super(view);
        mIconImageView = (ImageView) view.findViewById(R.id.iv_detail_item_icon);
        mContentTextView = (TextView) view.findViewById(R.id.tv_detail_item_content);
        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_loading_details);
        if (R.id.vh_labeled_review == view.getId()
                || R.id.vh_review == view.getId()) {
            mAuthorTextView = (TextView) view.findViewById(R.id.tv_review_author);
        }
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mTrailerKey != null) {
            Context context = itemView.getContext();
            Intent trailerIntent = new Intent(Intent.ACTION_VIEW, NetworkUtils.buildYoutubeUri(mTrailerKey));
            if (trailerIntent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(trailerIntent);
            }
        }
    }

    public void updateViewHolder(Trailer trailer) {
        mIconImageView.setImageResource(R.drawable.trailer);
        mContentTextView.setText(trailer.getName());
        mTrailerKey = trailer.getKey();
    }

    public void updateViewHolder(Review review) {
        mIconImageView.setImageResource(R.drawable.review);
        mContentTextView.setText(review.getContent());
        mAuthorTextView.setText(review.getAuthor());
    }

    public void showData() {
        mIconImageView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    public void showLoading() {
        mIconImageView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }
}
