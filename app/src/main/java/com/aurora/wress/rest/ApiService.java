package com.aurora.wress.rest;

import com.aurora.wress.weather.model.yahoo.YahooResponse;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by duygu on 1.11.2015.
 * <p/>
 * This interface is an abstract representation of urls
 */
public interface ApiService {

    @GET("yql")
    Call<YahooResponse> getWeather(@Query("q") String query,
                                   @Query("format") String format);

}
