package com.arunsoorya.savethedate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.arunsoorya.savethedate.fragment.SplashFragment;
import com.arunsoorya.savethedate.fragment.SplashFragment2;
import com.arunsoorya.savethedate.fragment.SplashFragment3;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreen extends AppCompatActivity {

    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.button)
    Button button;
    private String TAG = "dsd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);

        // primary sections of the activity.
        PagerAdapter mSectionsPagerAdapter = new PagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setPageTransformer(false, new ParallaxPageTransformer());
    }

    public class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return new SplashFragment();
                case 1:
                    return new SplashFragment2();
                case 2:
                    return new SplashFragment3();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

    }

    class ParallaxPageTransformer implements ViewPager.PageTransformer {

        @Override
        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
//                view.setAlpha(1);
            } else if (position <= 1) { // [-1,1]

                long val = (long) ((1-position) * 0.5 * pageWidth);
                Log.d(TAG, "transformPage: " + position+" val"+val);
                button.setTranslationX((float)val );
            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
//                view.setAlpha(1);
            }
        }
    }

}
