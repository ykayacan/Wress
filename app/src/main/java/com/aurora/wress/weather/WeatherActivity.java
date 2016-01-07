package com.aurora.wress.weather;

import com.aurora.wress.R;
import com.aurora.wress.base.BaseActivity;
import com.aurora.wress.outfit.OutfitActivity;
import com.aurora.wress.rest.ApiManager;
import com.aurora.wress.weather.model.Currently;
import com.aurora.wress.weather.model.ForecastIoResponse;
import com.aurora.wress.weather.model.Weather;
import com.github.pwittchen.weathericonview.WeatherIconView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * The type Weather activity.
 */
public class WeatherActivity extends BaseActivity {

    /**
     * The constant PARAM_LATITUDE.
     */
    public static final String PARAM_LATITUDE = "latitude";
    /**
     * The constant PARAM_LONGITUDE.
     */
    public static final String PARAM_LONGITUDE = "longitude";
    /**
     * The constant PARAM_PLACE_NAME.
     */
    public static final String PARAM_PLACE_NAME = "place_name";

    private static final String TAG = "WeatherActivity";

    private final char mCelsiusSymbol = 0x00B0;

    private String mWeatherType;

    private String mLatitude;
    private String mLongitude;
    private String mPlaceName;

    private TextView mHeroWeatherDegreeTextView;
    private TextView mHeroWeatherConditionTextView;
    private WeatherIconView mHeroWeatherIconImageView;

    private TextView mLabelDay1TextView;
    private TextView mLabelDay2TextView;
    private TextView mLabelDay3TextView;
    private TextView mLabelDay4TextView;
    private TextView mLabelDay5TextView;

    private TextView mDegreeDay1HighTextView;
    private TextView mDegreeDay2HighTextView;
    private TextView mDegreeDay3HighTextView;
    private TextView mDegreeDay4HighTextView;
    private TextView mDegreeDay5HighTextView;

    private TextView mDegreeDay1LowTextView;
    private TextView mDegreeDay2LowTextView;
    private TextView mDegreeDay3LowTextView;
    private TextView mDegreeDay4LowTextView;
    private TextView mDegreeDay5LowTextView;

    private WeatherIconView mWeatherIconDay1;
    private WeatherIconView mWeatherIconDay2;
    private WeatherIconView mWeatherIconDay3;
    private WeatherIconView mWeatherIconDay4;
    private WeatherIconView mWeatherIconDay5;

    private TextView mFeelsLikeTextView;
    private TextView mHumidityTextView;
    private TextView mDaylightTimeTextView;
    private TextView mWindSpeedTextView;

    private ImageView mBackgroundImageView;
    private ProgressBar mProgressBar;
    private ScrollView mInnerContent;
    private Button mWhatToWearButton;

    private int mDegree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        setupViews();

        Intent intent = getIntent();
        if (intent != null) {
            mLatitude = intent.getStringExtra(PARAM_LATITUDE);
            mLongitude = intent.getStringExtra(PARAM_LONGITUDE);
            mPlaceName = intent.getStringExtra(PARAM_PLACE_NAME);
        }

        // set place name
        getToolbar().setTitle(mPlaceName);

        getWeather();

        mWhatToWearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), OutfitActivity.class);
                i.putExtra(OutfitActivity.PARAM_WEATHER_TYPE, mWeatherType);
                i.putExtra(OutfitActivity.PARAM_DEGREE, mDegree);
                startActivity(i);
            }
        });

        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent != null) {
            mLatitude = intent.getStringExtra(PARAM_LATITUDE);
            mLongitude = intent.getStringExtra(PARAM_LONGITUDE);
        }
    }

    /**
     * Make request to forecast.io
     */
    private void getWeather() {
        Map<String, Object> params = new HashMap<>();
        params.put("units", "si");
        params.put("exclude", "minutely,flags,alerts");

        mProgressBar.setVisibility(View.VISIBLE);
        mInnerContent.setVisibility(View.GONE);

        ApiManager.getApi(getString(R.string.base_url_forecast_io))
                .getWeather(mLatitude, mLongitude, params)
                .enqueue(new Callback<ForecastIoResponse>() {
                    @Override
                    public void onResponse(Response<ForecastIoResponse> response, Retrofit retrofit) {
                        mProgressBar.setVisibility(View.GONE);
                        mInnerContent.setVisibility(View.VISIBLE);

                        setHeroWeatherInfo(response);
                        setWeatherContentInfo(response);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e(TAG, "onFailure: " + t.getCause());
                    }
                });
    }

    private void setHeroWeatherInfo(Response<ForecastIoResponse> response) {
        // get currently weather info
        Currently c = response.body().getCurrently();
        // get hourly weather info
        Weather w = response.body().getHourly().getData().get(2);

        // set weather info for current
        mHeroWeatherDegreeTextView.setText(String.format("%s%s", String.valueOf((int) c.getTemperature()), mCelsiusSymbol));
        mDegree = (int) c.getTemperature();
        // set weather condition text for current, i.e cloudy
        mHeroWeatherConditionTextView.setText(w.getSummary());
        // set weather condition icon for current
        mHeroWeatherIconImageView.setIconResource(getString(setWeatherIcon(w.getIcon())));
        mWeatherType = w.getIcon();

        mFeelsLikeTextView.setText(String.format("%s%s", String.valueOf((int) c.getApparentTemperature()), mCelsiusSymbol));
        mHumidityTextView.setText(String.valueOf((int) (c.getHumidity() * 100)));

        mWindSpeedTextView.setText(String.format("%s m/h", String.valueOf(c.getWindSpeed())));

        switch (w.getIcon()) {
            case "clear-day":
                setBackgroundDrawable(R.drawable.clear_day5);
                break;
            case "clear-night":
                setBackgroundDrawable(R.drawable.clear_night1);
                break;
            case "rain":
                setBackgroundDrawable(R.drawable.rain_day2);
                break;
            case "snow":
                setBackgroundDrawable(R.drawable.snow_day2);
                break;
            case "sleet":
                setBackgroundDrawable(R.drawable.sleet);
                break;
            case "wind":
                setBackgroundDrawable(R.drawable.wind);
                break;
            case "fog":
                setBackgroundDrawable(R.drawable.fog_day3);
                break;
            case "cloudy":
                setBackgroundDrawable(R.drawable.cloudy_night1);
                break;
            case "partly-cloudy-day":
                setBackgroundDrawable(R.drawable.cloudy_night1);
                break;
            case "partly-cloudy-night":
                setBackgroundDrawable(R.drawable.cloudy_night1);
                break;
            default:
                setBackgroundDrawable(R.drawable.cloudy_night1);
                break;
        }
    }

    private void setWeatherContentInfo(Response<ForecastIoResponse> response) {
        // get all weather list
        List<Weather> weatherList = response.body().getDaily().getData();

        Calendar cal = Calendar.getInstance();

        mLabelDay1TextView.setText(cal.getTime().toString().substring(0, 3));
        // next day, add 1 to today
        cal.add(Calendar.DATE, 1);
        mLabelDay2TextView.setText(cal.getTime().toString().substring(0, 3));
        cal.add(Calendar.DATE, 1);
        mLabelDay3TextView.setText(cal.getTime().toString().substring(0, 3));
        cal.add(Calendar.DATE, 1);
        mLabelDay4TextView.setText(cal.getTime().toString().substring(0, 3));
        cal.add(Calendar.DATE, 1);
        mLabelDay5TextView.setText(cal.getTime().toString().substring(0, 3));

        mWeatherIconDay1.setIconResource(getString(setWeatherIcon(weatherList.get(0).getIcon())));
        mWeatherIconDay2.setIconResource(getString(setWeatherIcon(weatherList.get(1).getIcon())));
        mWeatherIconDay3.setIconResource(getString(setWeatherIcon(weatherList.get(2).getIcon())));
        mWeatherIconDay4.setIconResource(getString(setWeatherIcon(weatherList.get(3).getIcon())));
        mWeatherIconDay5.setIconResource(getString(setWeatherIcon(weatherList.get(4).getIcon())));

        mDegreeDay1HighTextView.setText(String.format("%s %s", (int) weatherList.get(0).getTemperatureMax(), mCelsiusSymbol));
        mDegreeDay2HighTextView.setText(String.format("%s %s", (int) weatherList.get(1).getTemperatureMax(), mCelsiusSymbol));
        mDegreeDay3HighTextView.setText(String.format("%s %s", (int) weatherList.get(2).getTemperatureMax(), mCelsiusSymbol));
        mDegreeDay4HighTextView.setText(String.format("%s %s", (int) weatherList.get(3).getTemperatureMax(), mCelsiusSymbol));
        mDegreeDay5HighTextView.setText(String.format("%s %s", (int) weatherList.get(4).getTemperatureMax(), mCelsiusSymbol));

        mDegreeDay1LowTextView.setText(String.format("%s %s", (int) weatherList.get(0).getTemperatureMin(), mCelsiusSymbol));
        mDegreeDay2LowTextView.setText(String.format("%s %s", (int) weatherList.get(1).getTemperatureMin(), mCelsiusSymbol));
        mDegreeDay3LowTextView.setText(String.format("%s %s", (int) weatherList.get(2).getTemperatureMin(), mCelsiusSymbol));
        mDegreeDay4LowTextView.setText(String.format("%s %s", (int) weatherList.get(3).getTemperatureMin(), mCelsiusSymbol));
        mDegreeDay5LowTextView.setText(String.format("%s %s", (int) weatherList.get(4).getTemperatureMin(), mCelsiusSymbol));
    }

    private void setupViews() {
        mProgressBar = (ProgressBar) findViewById(R.id.loading);
        mInnerContent = (ScrollView) findViewById(R.id.inner_content);

        mHeroWeatherDegreeTextView = (TextView) findViewById(R.id.weather_hero_degree);
        mHeroWeatherConditionTextView = (TextView) findViewById(R.id.weather_hero_condition);
        mHeroWeatherIconImageView = (WeatherIconView) findViewById(R.id.weather_hero_icon);

        mLabelDay1TextView = (TextView) findViewById(R.id.weather_day1_text);
        mLabelDay2TextView = (TextView) findViewById(R.id.weather_day2_text);
        mLabelDay3TextView = (TextView) findViewById(R.id.weather_day3_text);
        mLabelDay4TextView = (TextView) findViewById(R.id.weather_day4_text);
        mLabelDay5TextView = (TextView) findViewById(R.id.weather_day5_text);

        mDegreeDay1HighTextView = (TextView) findViewById(R.id.weather_day1_degree_high);
        mDegreeDay2HighTextView = (TextView) findViewById(R.id.weather_day2_degree_high);
        mDegreeDay3HighTextView = (TextView) findViewById(R.id.weather_day3_degree_high);
        mDegreeDay4HighTextView = (TextView) findViewById(R.id.weather_day4_degree_high);
        mDegreeDay5HighTextView = (TextView) findViewById(R.id.weather_day5_degree_high);

        mDegreeDay1LowTextView = (TextView) findViewById(R.id.weather_day1_degree_low);
        mDegreeDay2LowTextView = (TextView) findViewById(R.id.weather_day2_degree_low);
        mDegreeDay3LowTextView = (TextView) findViewById(R.id.weather_day3_degree_low);
        mDegreeDay4LowTextView = (TextView) findViewById(R.id.weather_day4_degree_low);
        mDegreeDay5LowTextView = (TextView) findViewById(R.id.weather_day5_degree_low);

        mWeatherIconDay1 = (WeatherIconView) findViewById(R.id.weather_day1_icon);
        mWeatherIconDay2 = (WeatherIconView) findViewById(R.id.weather_day2_icon);
        mWeatherIconDay3 = (WeatherIconView) findViewById(R.id.weather_day3_icon);
        mWeatherIconDay4 = (WeatherIconView) findViewById(R.id.weather_day4_icon);
        mWeatherIconDay5 = (WeatherIconView) findViewById(R.id.weather_day5_icon);

        mFeelsLikeTextView = (TextView) findViewById(R.id.weather_feels_like);
        mHumidityTextView = (TextView) findViewById(R.id.weather_humidity);
        mDaylightTimeTextView = (TextView) findViewById(R.id.weather_daylight);
        mWindSpeedTextView = (TextView) findViewById(R.id.weather_wind_speed);

        mBackgroundImageView = (ImageView) findViewById(R.id.weather_bg);
        mWhatToWearButton = (Button) findViewById(R.id.weather_wearBtn);
    }

    private int setWeatherIcon(String icon) {
        switch (icon) {
            case "clear-day":
                return R.string.wi_forecast_io_clear_day;
            case "clear-night":
                return R.string.wi_forecast_io_clear_night;
            case "rain":
                return R.string.wi_forecast_io_rain;
            case "snow":
                return R.string.wi_forecast_io_snow;
            case "sleet":
                return R.string.wi_forecast_io_sleet;
            case "wind":
                return R.string.wi_forecast_io_wind;
            case "fog":
                return R.string.wi_forecast_io_fog;
            case "cloudy":
                return R.string.wi_forecast_io_cloudy;
            case "partly-cloudy-day":
                return R.string.wi_forecast_io_partly_cloudy_day;
            case "partly-cloudy-night":
                return R.string.wi_forecast_io_partly_cloudy_night;
            default:
                return R.string.wi_forecast_io_clear_day;
        }
    }

    /**
     * Sets background image
     *
     * @param drawable background image
     */
    private void setBackgroundDrawable(int drawable) {
        mBackgroundImageView.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), drawable));
    }

    @Override
    public void onBackPressed() {
        if (getDrawer().isDrawerOpen(GravityCompat.START)) {
            getDrawer().closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            finish();
        }
    }

}
