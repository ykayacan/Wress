package com.aurora.wress.rest;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * The type Api manager.
 */
public class ApiManager {

    /**
     * Gets client.
     *
     * @return the client
     */
    // Used for logging
    public static OkHttpClient getClient() {
        OkHttpClient okHttpClient = new OkHttpClient();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient.interceptors().add(interceptor);
        return okHttpClient;
    }

    /**
     * Gets api.
     *
     * @param baseUrl the base url
     * @return the api
     */
    public static ApiService getApi(final String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ApiService.class);
    }

}
