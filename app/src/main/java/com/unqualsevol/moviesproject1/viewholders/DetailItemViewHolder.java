package com.unqualsevol.moviesproject1.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.unqualsevol.moviesproject1.R;


public class DetailItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final ImageView mIconImageView;

    private final TextView mContentTextView;

    private TextView mAuthorTextView;

    private final ProgressBar mProgressBar;


    public DetailItemViewHolder(View view) {
        super(view);
        mIconImageView = (ImageView) view.findViewById(R.id.iv_detail_item_icon);
        mContentTextView = (TextView) view.findViewById(R.id.tv_detail_item_content);
        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_loading_details);
        if(R.id.vh_labeled_review == view.getId()
                || R.id.vh_review == view.getId()) {
            mAuthorTextView = (TextView) view.findViewById(R.id.tv_review_author);
        }
    }

    @Override
    public void onClick(View v) {

    }

    public void updateViewHolder(int icon, String content, String author) {
        mIconImageView.setImageResource(icon);
        mContentTextView.setText(content);
        if(mAuthorTextView != null) {
            mAuthorTextView.setText(author);
        }
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
