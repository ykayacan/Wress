package com.aurora.wress.splash;

import com.aurora.wress.R;
import com.aurora.wress.intro.IntroActivity;
import com.aurora.wress.login.LoginActivity;
import com.aurora.wress.utils.PrefUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * The type Splash activity.
 */
public class SplashActivity extends Activity {

    private static final int SPLASH_DURATION = 1250; // ms

    private Handler mHandler;
    private Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Thread stuff
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                // First check if ToS is accepted?
                if (PrefUtil.isTosAccepted(getApplicationContext())) {
                    // Start new activity SplashActivity -> LoginActivity
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    // Remove activity from back stack
                    // This prevents user from get back to SplashActivity
                    finish();
                } else {
                    // Start new activity SplashActivity -> IntroActivity
                    startActivity(new Intent(SplashActivity.this, IntroActivity.class));
                    // Remove activity from back stack
                    // This prevents user from get back to SplashActivity
                    finish();
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Activity is visible in screen
        mHandler.postDelayed(mRunnable, SPLASH_DURATION);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Activity isn't visible in screen, so remove callback of mHandler
        // Prevents memory leak
        mHandler.removeCallbacks(mRunnable);
    }

}
