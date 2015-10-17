package com.aurora.wress.intro;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aurora.wress.R;

import su.levenetc.android.textsurface.TextSurface;

public class AnimFragment extends Fragment {

    private TextSurface mTextSurface;

    public AnimFragment() {
        // Required empty public constructor
    }

    public static AnimFragment newInstance() {
        return new AnimFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_anim, container, false);

        mTextSurface = (TextSurface) v.findViewById(R.id.text_surface);

        mTextSurface.postDelayed(new Runnable() {
            @Override
            public void run() {
                show();
            }
        },1000);
        return v;
    }

    private void show() {
        mTextSurface.reset();
        CookieThumperSample.play(mTextSurface, getContext().getAssets());
    }
}
