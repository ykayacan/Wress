package com.aurora.wress;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.aurora.wress.intro.AnimFragment;
import com.aurora.wress.intro.SlideFragment;
import com.github.paolorotolo.appintro.AppIntro2;

public class IntroActivity extends AppIntro2 {

    @Override
    public void init(Bundle savedInstanceState) {
        addSlide(AnimFragment.newInstance());
        addSlide(SlideFragment.newInstance(R.string.intro_1, R.drawable.intro1));
        addSlide(SlideFragment.newInstance(R.string.intro_1, R.drawable.intro2));
        addSlide(SlideFragment.newInstance(R.string.intro_1, R.drawable.intro3));
        addSlide(SlideFragment.newInstance(R.string.intro_1, R.drawable.intro4));
    }

    @Override
    public void onDonePressed() {

    }
}
