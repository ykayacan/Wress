package com.aurora.wress.rest;

import com.aurora.wress.outfit.model.OutfitResponse;
import com.aurora.wress.weather.model.ForecastIoResponse;

import java.util.Map;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;

/**
 * Created by duygu on 1.11.2015.
 *
 * This interface is an abstract representation of urls
 */
public interface ApiService {

    /**
     * Gets weather.
     *
     * @param latitude   the latitude
     * @param longitude  the longitude
     * @param parameters the parameters
     * @return the weather
     */
    @GET("{latitude},{longitude}")
    Call<ForecastIoResponse> getWeather(
            @Path("latitude") String latitude,
            @Path("longitude") String longitude,
            @QueryMap Map<String, Object> parameters);

    /**
     * Gets outfit.
     *
     * @param weatherType the weather type
     * @return the outfit
     */
    @GET("wress.php")
    Call<OutfitResponse> getOutfit(@Query("weatherType") String weatherType);

}
