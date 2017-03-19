package com.arunsoorya.savethedate.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arunsoorya.savethedate.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SplashFragment3 extends Fragment {


    private static final String ARG_POSITION = "ARG_POSITION";

    public SplashFragment3() {
        // Required empty public constructor
    }
    public static SplashFragment3 getInstance(int position) {
        SplashFragment3 splashFragment = new SplashFragment3();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_POSITION, position);
        splashFragment.setArguments(bundle);
        return splashFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_fragment3, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final int position = getArguments().getInt(ARG_POSITION);

        view.setTag(position); // saving index
    }

}
