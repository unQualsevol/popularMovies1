package com.unqualsevol.moviesproject1.model;

public enum SearchType {
    POPULAR("/movie/popular"), TOP_RATED("/movie/top_rated");

    private String entryPoint;

    SearchType(String entryPoint) {
        this.entryPoint = entryPoint;
    }

    public String getEntryPoint() {
        return entryPoint;
    }
}
