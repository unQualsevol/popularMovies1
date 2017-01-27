package com.unqualsevol.moviesproject1.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by agimenez on 27/01/17.
 */

public class MoviesPage {

    private int page;
    private int total_results;
    private int total_pages;
    @SerializedName("results")
    private List<Movie> movies;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
