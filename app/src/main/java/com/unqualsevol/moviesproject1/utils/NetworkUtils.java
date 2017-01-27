/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.unqualsevol.moviesproject1.utils;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    private final static Gson gson = new Gson();

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String THEMOVIEDB_BASE_URL = "https://api.themoviedb.org/3";

    private static final String POPULAR_ENTRY_POINT = "/movie/popular";

    private static final String TOP_RATED_ENTRY_POINT = "/movie/top_rated";

    final static String API_KEY_PARAM = "api_key";
    final static String LANGUAGE_PARAM = "language";

    final static String PAGE_PARAM = "page";

    public static URL buildUrl(boolean popular, String apikey, String language, String page) {
        Uri builtUri = Uri.parse(THEMOVIEDB_BASE_URL + (popular ? POPULAR_ENTRY_POINT : TOP_RATED_ENTRY_POINT)).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, apikey)
                .appendQueryParameter(LANGUAGE_PARAM, language)
                .appendQueryParameter(PAGE_PARAM, page)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static <T> T getResponseFromHttpUrl(URL url, Class<T> type) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            return gson.fromJson(new InputStreamReader(in), type);
        } finally {
            urlConnection.disconnect();
        }
    }
}