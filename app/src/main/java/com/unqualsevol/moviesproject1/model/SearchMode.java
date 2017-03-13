package com.unqualsevol.moviesproject1.model;

public enum SearchMode {
    POPULAR("/movie/popular"), TOP_RATED("/movie/top_rated"), DATABASE("");

    private String entryPoint;

    SearchMode(String entryPoint) {
        this.entryPoint = entryPoint;
    }

    public String getEntryPoint() {
        return entryPoint;
    }
}
