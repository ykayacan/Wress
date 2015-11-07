package com.aurora.wress.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.aurora.wress.R;
import com.aurora.wress.intro.IntroActivity;
import com.aurora.wress.location.LocationActivity;
import com.aurora.wress.utils.PrefUtils;
import com.aurora.wress.widget.CustomFontTextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        // Make activity fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        // Check if the EULA has been accepted; if not, show it.
        if (!PrefUtils.isTosAccepted(this)) {
            Intent intent = new Intent(this, IntroActivity.class);
            startActivity(intent);
            finish();
        }

        CustomFontTextView skipTV = (CustomFontTextView) findViewById(R.id.login_skip);

        skipTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LocationActivity.class));
                finish();
            }
        });
    }

}
