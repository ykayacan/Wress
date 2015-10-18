package com.aurora.wress.intro;

import android.content.Intent;
import android.os.Bundle;

import com.aurora.wress.R;
import com.aurora.wress.login.LoginActivity;
import com.github.paolorotolo.appintro.AppIntro2;

public class IntroActivity extends AppIntro2 {

    @Override
    public void init(Bundle savedInstanceState) {
        addSlide(AnimFragment.newInstance());
        addSlide(SlideFragment.newInstance(R.string.intro_1, R.drawable.intro5));
        addSlide(SlideFragment.newInstance(R.string.intro_1, R.drawable.intro2));
        addSlide(SlideFragment.newInstance(R.string.intro_1, R.drawable.intro3));
    }

    @Override
    public void onDonePressed() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
}
