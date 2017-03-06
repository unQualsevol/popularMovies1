package com.unqualsevol.moviesproject1.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unqualsevol.moviesproject1.R;
import com.unqualsevol.moviesproject1.fragments.MoviesGridFragment;
import com.unqualsevol.moviesproject1.utils.MoviesUtils;
import com.unqualsevol.moviesproject1.viewholders.PosterViewHolder;

public class FavoritesAdapter extends RecyclerView.Adapter<PosterViewHolder> {

    private Cursor mCursor;

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.movie_grid_item, parent, false);
        return new PosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PosterViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        int movieId = mCursor.getInt(MoviesGridFragment.INDEX_MOVIE_ID);
        byte[] posterInBytes = mCursor.getBlob(MoviesGridFragment.INDEX_POSTER_IMAGE);
        float voteAverage = mCursor.getFloat(MoviesGridFragment.INDEX_VOTE_AVERAGE) / 2;
        holder.updateViewHolder(movieId, MoviesUtils.getImage(posterInBytes), voteAverage);
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }
}
