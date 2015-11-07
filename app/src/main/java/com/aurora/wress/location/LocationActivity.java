package com.aurora.wress.location;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.aurora.wress.R;
import com.aurora.wress.weather.WeatherActivity;
import com.aurora.wress.widget.CustomAutoCompleteTextView;
import com.aurora.wress.widget.CustomFontButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class LocationActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = LocationActivity.class.getSimpleName();

    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));

    /**
     * GoogleApiClient wraps our service connection to Google Play Services and provides access
     * to the user's sign in state as well as the Google's APIs.
     */
    protected GoogleApiClient mGoogleApiClient;

    private PlaceAutocompleteAdapter mAdapter;

    private String mLatitude;
    private String mLongitude;

    private String mPlaceId;

    // UI Widgets
    private ScrollView mScrollView;
    private CustomAutoCompleteTextView mAutocompleteTextView;
    private CustomFontButton mFindCurrLocationButton;
    private CustomFontButton mDoneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        setupViews();

        // Add drawable to right of button
        mFindCurrLocationButton
                .setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.btn_square_navigate, 0);

        buildGoogleApiClient();

        // Register a listener that receives callbacks when a suggestion has been selected
        mAutocompleteTextView.setOnItemClickListener(getAutocompleteClickListener());

        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, BOUNDS_GREATER_SYDNEY, null);
        mAutocompleteTextView.setAdapter(mAdapter);

        mFindCurrLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guessCurrentPlace();
            }
        });

        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WeatherActivity.class);
                intent.putExtra(WeatherActivity.PARAM_LATITUDE, mLatitude);
                intent.putExtra(WeatherActivity.PARAM_LONGITUDE, mLongitude);
                intent.putExtra(WeatherActivity.PARAM_PLACE_ID, mPlaceId);
                startActivity(intent);
            }
        });

        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    /**
     * Setup views for this activity
     */
    private void setupViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAutocompleteTextView = (CustomAutoCompleteTextView) findViewById(R.id.location_choose);
        mFindCurrLocationButton = (CustomFontButton) findViewById(R.id.location_find);
        mDoneButton = (CustomFontButton) findViewById(R.id.location_done);
        mScrollView = (ScrollView) findViewById(R.id.location_scrollView);
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
                .addApi(Places.PLACE_DETECTION_API)
                .build();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        Snackbar.make(mScrollView,
                "Could not connect to Google API Client: Error " +
                        connectionResult.getErrorCode(),
                Snackbar.LENGTH_SHORT).show();
    }

    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> getUpdatePlaceDetailsCallback() {
        return new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(PlaceBuffer places) {
                if (!places.getStatus().isSuccess()) {
                    // Request did not complete successfully
                    Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                    places.release();
                    return;
                }
                // Get the Place object from the buffer.
                final Place place = places.get(0);

                Log.i(TAG, "Place details received: " + place.getName());

                mLatitude = String.valueOf(place.getLatLng().latitude);
                mLongitude = String.valueOf(place.getLatLng().longitude);

                mPlaceId = place.getId();

                places.release();
            }
        };
    }

    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data API
     * to retrieve more details about the place.
     *
     * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(com.google.android.gms.common.api.GoogleApiClient,
     * String...)
     */
    private AdapterView.OnItemClickListener getAutocompleteClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*
                 Retrieve the place ID of the selected item from the Adapter.
                 The adapter stores each Place suggestion in a AutocompletePrediction from which we
                 read the place ID and title.
                */
                final AutocompletePrediction item = mAdapter.getItem(position);
                final String placeId = item.getPlaceId();
                final CharSequence primaryText = item.getPrimaryText(null);

                Log.i(TAG, "Autocomplete item selected: " + primaryText);

                /*
                 Issue a request to the Places Geo Data API to retrieve a Place object with additional
                 details about the place.
                */
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(getUpdatePlaceDetailsCallback());

                Toast.makeText(getApplicationContext(), "Clicked: " + primaryText,
                        Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
            }
        };
    }

    /**
     * Guess current place
     */
    private void guessCurrentPlace() {
        if (mGoogleApiClient.isConnected() && isGPSEnabled()) {
            PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                    .getCurrentPlace(mGoogleApiClient, null);

            result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                @Override
                public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                    // Couldn't find a location
                    if (likelyPlaces.getCount() < 0) {
                        Snackbar.make(mScrollView, R.string.no_address_found, Snackbar.LENGTH_SHORT).show();
                    } else { // Found a location
                        Snackbar.make(mScrollView, R.string.address_found, Snackbar.LENGTH_SHORT).show();
                        // Get the location that has highest possibility
                        LatLng latLng = likelyPlaces.get(0).getPlace().getLatLng();
                        mLatitude = String.valueOf(latLng.latitude);
                        mLongitude = String.valueOf(latLng.longitude);
                        mPlaceId = likelyPlaces.get(0).getPlace().getId();
                    }

                    likelyPlaces.release();
                }
            });
        } else { // Ask device to enable GPS from settings
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }

    /**
     * Check if device GPS is enabled
     *
     * @return isGPSEnabled
     */
    private boolean isGPSEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

}