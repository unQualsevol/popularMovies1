package com.unqualsevol.moviesproject1.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailersPage {

    @SerializedName("id")
    private int movieId;
    @SerializedName("results")
    private List<Trailer> trailers;

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public List<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
    }
}
