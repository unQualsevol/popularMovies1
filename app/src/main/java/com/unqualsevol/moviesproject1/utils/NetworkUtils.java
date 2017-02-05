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
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public final class NetworkUtils {

    private final static Gson gson = new Gson();

    private final static OkHttpClient client = new OkHttpClient();

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String THEMOVIEDB_BASE_URL = "https://api.themoviedb.org/3";

    private static final String IMAGEDB_BASE_URL = "http://image.tmdb.org/t/p/";

    final static String API_KEY_PARAM = "api_key";
    final static String LANGUAGE_PARAM = "language";

    final static String PAGE_PARAM = "page";

    public static URL buildUrl(String entryPoint, String apikey, String language, String page) {
        Uri builtUri = Uri.parse(THEMOVIEDB_BASE_URL + entryPoint).buildUpon()
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
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        return gson.fromJson(response.body().string(), type);
    }

    public static Uri buildImageUrl(String size, String posterPath) {
        return Uri.parse(IMAGEDB_BASE_URL + size + posterPath).buildUpon().build();
    }
}