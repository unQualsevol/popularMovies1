package com.unqualsevol.moviesproject1.utils;

import android.net.Uri;

import com.unqualsevol.moviesproject1.model.SearchMode;

import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.*;

public class NetworkUtilsTest {

    private static final String dummyMovieId = "movieid";
    private static final String dummyApiKey = "apikey";
    private static final String dummyLanguage = "language";
    private static final String dummyPage = "page";
    private static final String dummyPosterPath = "/posterPath";
    private static final String EXPECTED_MOVIES_URL = "https://api.themoviedb.org/3/movie/top_rated?api_key=apikey&language=language&page=page";
    private static final String EXPECTED_TRAILERS_URL = "https://api.themoviedb.org/3/movie/movieid/videos?api_key=apikey";
    private static final String EXPECTED_REVIEWS_URL = "https://api.themoviedb.org/3/movie/movieid/reviews?api_key=apikey&page=page";
    private static final String EXPECTED_IMAGE_URL = "http://image.tmdb.org/t/p/w780/posterPath";

    @Test
    public void buildMoviesUrl() throws Exception {
        URL actualUrl = NetworkUtils.buildMoviesUrl(SearchMode.TOP_RATED.getEntryPoint(), dummyApiKey, dummyLanguage, dummyPage);
        assertEquals(EXPECTED_MOVIES_URL, actualUrl.toString());
    }

    @Test
    public void buildTrailersUrl() throws Exception {
        URL actualUrl = NetworkUtils.buildTrailersUrl(dummyMovieId, dummyApiKey);
        assertEquals(EXPECTED_TRAILERS_URL, actualUrl.toString());
    }

    @Test
    public void buildReviewsUrl() throws Exception {
        URL actualUrl = NetworkUtils.buildReviewsUrl(dummyMovieId, dummyApiKey, dummyPage);
        assertEquals(EXPECTED_REVIEWS_URL, actualUrl.toString());
    }

    @Test
    public void buildImageUri() throws Exception {
        Uri actualUri = NetworkUtils.buildImageUri(dummyPosterPath);
        assertEquals(EXPECTED_IMAGE_URL, actualUri.toString());
    }

}