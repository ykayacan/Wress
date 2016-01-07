package com.aurora.wress.intro;

import com.aurora.wress.R;
import com.bumptech.glide.Glide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * The type Intro fragment.
 */
public class IntroFragment extends Fragment {

    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_IMAGE = "image";

    private int mDescriptionRes;
    private int mBgImageRes;

    /**
     * Instantiates a new Intro fragment.
     */
    public IntroFragment() {
    }

    /**
     * New instance intro fragment.
     *
     * @param description     the description
     * @param backgroundImage the background image
     * @return the intro fragment
     */
    public static IntroFragment newInstance(int description, int backgroundImage) {
        IntroFragment fragment = new IntroFragment();
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

        TextView mDescriptionTv = (TextView) v.findViewById(R.id.intro_description);
        ImageView mBackgroundIV = (ImageView) v.findViewById(R.id.intro_background);

        mDescriptionTv.setText(mDescriptionRes);

        // Image library for handling images
        Glide.with(this)
            .load(mBgImageRes)
            .into(mBackgroundIV);
        return v;
    }

}
