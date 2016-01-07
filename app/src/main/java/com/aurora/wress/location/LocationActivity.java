package com.aurora.wress.location;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;

import com.aurora.wress.R;
import com.aurora.wress.weather.WeatherActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/**
 * The type Location activity.
 */
public class LocationActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    /**
     * The constant TAG.
     */
    public static final String TAG = "LocationActivity";

    /**
     * GoogleApiClient wraps our service connection to Google Play Services and provides access
     * to the user's sign in state as well as the Google's APIs.
     */
    private GoogleApiClient mGoogleApiClient;

    // UI Widgets
    private CoordinatorLayout mCoordinatorLayout;
    private Button mFindMyLocationButton;
    private Button mWhereAreYouGoingButton;
    private ProgressBar mProgressBar;
    private RelativeLayout mInnerContent;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        setupViews();

        buildGoogleApiClient();

        mWhereAreYouGoingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
            }
        });

        mFindMyLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guessCurrentPlace();
            }
        });

        // Override activity loading animation
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    /**
     * Setup views for this activity
     */
    private void setupViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFindMyLocationButton = (Button) findViewById(R.id.find_my_location);
        mWhereAreYouGoingButton = (Button) findViewById(R.id.where_are_you_going);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        mProgressBar = (ProgressBar) findViewById(R.id.loading);
        mInnerContent = (RelativeLayout) findViewById(R.id.inner_content);
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
                .addApi(Places.PLACE_DETECTION_API)
                .build();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        Snackbar.make(mCoordinatorLayout,
                "Could not connect to Google API Client: Error " +
                        connectionResult.getErrorCode(),
                Snackbar.LENGTH_SHORT).show();
    }

    /**
     * Guess current place
     */
    private void guessCurrentPlace() {
        Dexter.checkPermission(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                if (mGoogleApiClient.isConnected() && isGPSEnabled()) {
                    // Show progressbar until a location is found
                    mProgressBar.setVisibility(View.VISIBLE);
                    mInnerContent.setVisibility(View.GONE);

                    PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                            .getCurrentPlace(mGoogleApiClient, null);

                    result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                        @Override
                        public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                            // Couldn't find a location
                            if (likelyPlaces.getCount() < 0) {
                                Snackbar.make(mCoordinatorLayout, R.string.no_address_found, Snackbar.LENGTH_SHORT).show();
                            } else { // Found a location
                                // Get the location that has highest possibility
                                LatLng latLng = likelyPlaces.get(0).getPlace().getLatLng();

                                String latitude = String.valueOf(latLng.latitude);
                                String longitude = String.valueOf(latLng.longitude);
                                String placeName = likelyPlaces.get(0).getPlace().getName().toString();

                                // Hide progressbar
                                mProgressBar.setVisibility(View.GONE);
                                mInnerContent.setVisibility(View.VISIBLE);

                                Intent intent = new Intent(getApplicationContext(), WeatherActivity.class);
                                intent.putExtra(WeatherActivity.PARAM_LATITUDE, latitude);
                                intent.putExtra(WeatherActivity.PARAM_LONGITUDE, longitude);
                                intent.putExtra(WeatherActivity.PARAM_PLACE_NAME, placeName);
                                startActivity(intent);
                            }

                            likelyPlaces.release();
                        }
                    });
                } else { // Ask device to enable GPS from settings
                    mProgressBar.setVisibility(View.GONE);
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                PermissionListener dialogPermissionListener =
                        DialogOnDeniedPermissionListener.Builder
                                .withContext(getApplicationContext())
                                .withTitle("Camera permission")
                                .withMessage("Camera permission is needed to take pictures of your cat")
                                .withButtonText(android.R.string.ok)
                                .withIcon(R.mipmap.ic_launcher)
                                .build();
                Dexter.checkPermission(dialogPermissionListener, Manifest.permission.ACCESS_FINE_LOCATION);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

            }
        }, Manifest.permission.ACCESS_FINE_LOCATION);
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