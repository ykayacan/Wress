package com.aurora.wress.intro;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aurora.wress.R;
import com.bumptech.glide.Glide;

public class SlideFragment extends Fragment {

    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_IMAGE = "image";

    private int mDescriptionRes;
    private int mBgImageRes;

    private TextView mDescTv;
    private ImageView mBgIV;

    public SlideFragment() {
    }

    public static SlideFragment newInstance(@StringRes int description,
                                            @DrawableRes int backgroundImage) {
        SlideFragment fragment = new SlideFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_DESCRIPTION, description);
        args.putInt(ARG_IMAGE, backgroundImage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDescriptionRes = getArguments().getInt(ARG_DESCRIPTION);
            mBgImageRes = getArguments().getInt(ARG_IMAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_slide, container, false);

        mDescTv = (TextView) v.findViewById(R.id.intro_description);
        mBgIV = (ImageView) v.findViewById(R.id.intro_background);

        mDescTv.setText(mDescriptionRes);
        Glide.with(this)
                .load(mBgImageRes)
                .crossFade()
                .centerCrop()
                .into(mBgIV);
        return v;
    }

}
