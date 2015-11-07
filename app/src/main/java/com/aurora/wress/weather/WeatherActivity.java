package com.aurora.wress.weather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.aurora.wress.R;
import com.aurora.wress.rest.ApiManager;
import com.aurora.wress.weather.model.yahoo.Forecast;
import com.aurora.wress.weather.model.yahoo.YahooResponse;
import com.aurora.wress.widget.CustomFontTextView;
import com.github.pwittchen.weathericonview.WeatherIconView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;

import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class WeatherActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = WeatherActivity.class.getSimpleName();

    public static final String PARAM_LATITUDE = "latitude";
    public static final String PARAM_LONGITUDE = "longitude";

    public static final String PARAM_PLACE_ID = "place_id";

    /**
     * GoogleApiClient wraps our service connection to Google Play Services and provides access
     * to the user's sign in state as well as the Google's APIs.
     */
    protected GoogleApiClient mGoogleApiClient;

    private String mLatitude;
    private String mLongitude;

    private String mPlaceId;

    private CustomFontTextView mLabelTodayTextView;
    private CustomFontTextView mLabelTmrwTextView;
    private CustomFontTextView mLabelDay3TextView;
    private CustomFontTextView mLabelDay4TextView;
    private CustomFontTextView mLabelDay5TextView;

    private CustomFontTextView mDegreeTodayTextView;
    private CustomFontTextView mDegreeTmrwTextView;
    private CustomFontTextView mDegreeDay3TextView;
    private CustomFontTextView mDegreeDay4TextView;
    private CustomFontTextView mDegreeDay5TextView;

    private CustomFontTextView mHeroWeatherDegreeTextView;
    private CustomFontTextView mHeroWeatherConditionTextView;

    private WeatherIconView mHeroWeatherIcon;
    private WeatherIconView mWeatherIconToday;
    private WeatherIconView mWeatherIconTmrw;
    private WeatherIconView mWeatherIconDay3;
    private WeatherIconView mWeatherIconDay4;
    private WeatherIconView mWeatherIconDay5;

    private ImageView mBackgroundImageView;

    private CustomFontTextView mLocationTextView;

    private char mCelsiusSymbol = 0x00B0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        setupViews();

        Intent intent = getIntent();
        if (intent != null) {
            mLatitude = intent.getStringExtra(PARAM_LATITUDE);
            mLongitude = intent.getStringExtra(PARAM_LONGITUDE);
            mPlaceId = intent.getStringExtra(PARAM_PLACE_ID);
        }

        Log.i(TAG, "onCreate: \n" + mLatitude + "\n" + mLongitude + "\n" + mPlaceId);

        buildGoogleApiClient();

        placePhotosAsync(mPlaceId);

        String query = String.format("select item from weather.forecast where woeid in" +
                "(select woeid from geo.placefinder where text=\"%s,%s\" and gflags='R') and u='c'", mLatitude, mLongitude);

        final String format = "json";

        getWeather(query, format);

        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent != null) {
            mLatitude = intent.getStringExtra(PARAM_LATITUDE);
            mLongitude = intent.getStringExtra(PARAM_LONGITUDE);
            mPlaceId = intent.getStringExtra(PARAM_PLACE_ID);
        }
    }

    /**
     * Creating google api client object
     */
    private synchronized void buildGoogleApiClient() {
        // Construct a GoogleApiClient for the {@link Places#GEO_DATA_API} using AutoManage
        // functionality, which automatically sets up the API client to handle Activity lifecycle
        // events. If your activity does not extend FragmentActivity, make sure to call connect()
        // and disconnect() explicitly.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();
    }

    private void getWeather(String query, String format) {
        ApiManager.getApi(getString(R.string.base_url_yahoo)).getWeather(query, format).enqueue(new Callback<YahooResponse>() {
            @Override
            public void onResponse(Response<YahooResponse> response, Retrofit retrofit) {
                List<Forecast> forecastList = response.body().getQuery().getResults()
                        .getChannel().getItem().getForecast();

                String mCityName = response.body().getQuery().getResults().getChannel().getItem().getTitle();
                mCityName = mCityName.substring(15, mCityName.indexOf(","));
                mLocationTextView.setText(mCityName);

                mHeroWeatherDegreeTextView.setText(String.format("%s%s", forecastList.get(0).getHigh(), mCelsiusSymbol));
                mHeroWeatherConditionTextView.setText(forecastList.get(0).getText());

                mLabelDay3TextView.setText(forecastList.get(2).getDay());
                mLabelDay4TextView.setText(forecastList.get(3).getDay());
                mLabelDay5TextView.setText(forecastList.get(4).getDay());

                mDegreeTodayTextView.setText(String.format("%s %s", forecastList.get(0).getHigh(), mCelsiusSymbol));
                mDegreeTmrwTextView.setText(String.format("%s %s", forecastList.get(1).getHigh(), mCelsiusSymbol));
                mDegreeDay3TextView.setText(String.format("%s %s", forecastList.get(2).getHigh(), mCelsiusSymbol));
                mDegreeDay4TextView.setText(String.format("%s %s", forecastList.get(3).getHigh(), mCelsiusSymbol));
                mDegreeDay5TextView.setText(String.format("%s %s", forecastList.get(4).getHigh(), mCelsiusSymbol));

                mHeroWeatherIcon.setIconResource(getString(setWeatherIcon(forecastList.get(0).getText())));
                mWeatherIconToday.setIconResource(getString(setWeatherIcon(forecastList.get(0).getText())));
                mWeatherIconTmrw.setIconResource(getString(setWeatherIcon(forecastList.get(1).getText())));
                mWeatherIconDay3.setIconResource(getString(setWeatherIcon(forecastList.get(2).getText())));
                mWeatherIconDay4.setIconResource(getString(setWeatherIcon(forecastList.get(3).getText())));
                mWeatherIconDay5.setIconResource(getString(setWeatherIcon(forecastList.get(4).getText())));
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "onFailure: " + t.getCause());
            }
        });
    }

    private void setupViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mLocationTextView = (CustomFontTextView) findViewById(R.id.weather_location);

        mLabelTodayTextView = (CustomFontTextView) findViewById(R.id.weather_today);
        mLabelTmrwTextView = (CustomFontTextView) findViewById(R.id.weather_tomorrow);
        mLabelDay3TextView = (CustomFontTextView) findViewById(R.id.weather_day3);
        mLabelDay4TextView = (CustomFontTextView) findViewById(R.id.weather_day4);
        mLabelDay5TextView = (CustomFontTextView) findViewById(R.id.weather_day5);

        mDegreeTodayTextView = (CustomFontTextView) findViewById(R.id.weather_today_degree);
        mDegreeTmrwTextView = (CustomFontTextView) findViewById(R.id.weather_tomorrow_degree);
        mDegreeDay3TextView = (CustomFontTextView) findViewById(R.id.weather_day3_degree);
        mDegreeDay4TextView = (CustomFontTextView) findViewById(R.id.weather_day4_degree);
        mDegreeDay5TextView = (CustomFontTextView) findViewById(R.id.weather_day5_degree);

        mHeroWeatherDegreeTextView = (CustomFontTextView) findViewById(R.id.weather_hero_degree);
        mHeroWeatherConditionTextView = (CustomFontTextView) findViewById(R.id.weather_hero_condition);

        mHeroWeatherIcon = (WeatherIconView) findViewById(R.id.weather_hero_icon);
        mWeatherIconToday = (WeatherIconView) findViewById(R.id.weather_todayIV);
        mWeatherIconTmrw = (WeatherIconView) findViewById(R.id.weather_tomorrowIV);
        mWeatherIconDay3 = (WeatherIconView) findViewById(R.id.weather_day3IV);
        mWeatherIconDay4 = (WeatherIconView) findViewById(R.id.weather_day4IV);
        mWeatherIconDay5 = (WeatherIconView) findViewById(R.id.weather_day5IV);

        mBackgroundImageView = (ImageView) findViewById(R.id.weather_bg);
    }

    private int setWeatherIcon(String weatherType) {
        switch (weatherType) {
            case "Tornado":
                return R.string.wi_forecast_io_tornado;
            case "Sunny":
                return R.string.wi_day_sunny;
            case "Cloudy":
                return R.string.wi_day_cloudy;
            case "Hurricane":
                return R.string.wi_hurricane;
            case "Thunderstorms":
                return R.string.wi_forecast_io_thunderstorm;
            case "Mixed Rain And Snow":
                return R.string.wi_rain_mix;
            case "Snow":
                return R.string.wi_forecast_io_snow;
            case "Dust":
                return R.string.wi_dust;
            case "Foggy":
                return R.string.wi_forecast_io_fog;
            case "Windy":
                return R.string.wi_windy;
            case "Cold":
                return R.string.wi_snowflake_cold;
            case "Mostly Cloudy":
                return R.string.wi_wu_mostlycloudy;
            case "Partly Cloudy":
                return R.string.wi_forecast_io_partly_cloudy_day;
            default:
                return R.string.wi_day_cloudy;
        }
    }

    /**
     * Load a bitmap from the photos API asynchronously
     * by using buffers and result callbacks.
     */
    private void placePhotosAsync(String placeId) {
        PendingResult<PlacePhotoMetadataResult> placePhotoMetadataResult = Places.GeoDataApi
                .getPlacePhotos(mGoogleApiClient, placeId);

        placePhotoMetadataResult.setResultCallback(new ResultCallback<PlacePhotoMetadataResult>() {
            @Override
            public void onResult(PlacePhotoMetadataResult photos) {
                if (!photos.getStatus().isSuccess()) {
                    // Request did not complete successfully
                    Log.e(TAG, "Photo query did not complete. Error: " + photos.getStatus().toString());
                }

                PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                Log.i(TAG, "onResult: " + photoMetadataBuffer.getCount());
                if (photoMetadataBuffer.getCount() > 0) {
                    // Display the first bitmap in an ImageView in the size of the view
                    photoMetadataBuffer.get(0)
                            .getScaledPhoto(mGoogleApiClient, mBackgroundImageView.getWidth(),
                                    mBackgroundImageView.getHeight())
                            .setResultCallback(mDisplayPhotoResultCallback);
                }

                photoMetadataBuffer.release();
            }
        });
    }

    private ResultCallback<PlacePhotoResult> mDisplayPhotoResultCallback
            = new ResultCallback<PlacePhotoResult>() {
        @Override
        public void onResult(PlacePhotoResult placePhotoResult) {
            if (!placePhotoResult.getStatus().isSuccess()) {
                return;
            }
            mBackgroundImageView.setImageBitmap(placePhotoResult.getBitmap());
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        finish();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }

}
