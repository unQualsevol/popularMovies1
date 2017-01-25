package com.unqualsevol.moviesproject1.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unqualsevol.moviesproject1.R;
import com.unqualsevol.moviesproject1.viewholders.PosterViewHolder;

/**
 * Created by agimenez on 25/01/17.
 */

public class PosterAdapter extends RecyclerView.Adapter<PosterViewHolder>{

    //private array of data

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.movie_grid_item, parent, false);
        return new PosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PosterViewHolder holder, int position) {
        holder.setMovieData();
    }

    @Override
    public int getItemCount() {
        return 100;
    }

    public void setData(){
        //save data
        notifyDataSetChanged();
    }
}
