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
import android.widget.HorizontalScrollView;

import com.arunsoorya.savethedate.fragment.BasePageTransformer;
import com.arunsoorya.savethedate.fragment.SplashFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreen extends AppCompatActivity {

    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.horizontal_scroll)
    HorizontalScrollView horizontalScroll;
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.button2)
    Button button2;
    @BindView(R.id.first_story)
    View firstStory;
    @BindView(R.id.second_story)
    View secondStory;
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
            return SplashFragment.getInstance(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

    }

    class ParallaxPageTransformer extends BasePageTransformer {

int scrollvalue=0;
        int currentPagePosition;
        @Override
        protected void transformPage(View view, int pageIndex, float position) {
            int horizontalWidth = horizontalScroll.getChildAt(0).getWidth();
            int totalSwipeWidth = view.getWidth() * 4;

            currentPagePosition=0;

            int pageWidth = view.getWidth() / 2;
            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
//                view.setAlpha(1);
                Log.d(TAG, "transformPage: - left end " + pageIndex);
            } else if (position <= 0) { // [-1,0]
                if (pageIndex == 0) {
                    firstStory.setVisibility(View.VISIBLE);
                    button.setX(view.getWidth() / 2 - button.getWidth() / 2);
                    horizontalScroll.smoothScrollTo((int) 0,0);
                }
            } else if (position <= 1) { // (0,1]
                scrollvalue = view.getWidth();


                long scrollValue = (long) Math.abs((pageIndex)* view.getWidth()* (horizontalWidth / totalSwipeWidth));
                Log.d(TAG, "page "+ pageIndex);
//                horizontalScroll.smoothScrollTo((int) scrollValue,0);

                if (pageIndex <= 1) {
                    firstStory.findViewById(R.id.main_event).setVisibility(View.INVISIBLE);
                    button.setVisibility(View.VISIBLE);
                    long val = (long) ((long) (view.getWidth() / 2 - firstStory.getWidth() / 2)
                            + (position * 1.5 * pageWidth));
                    firstStory.setX((float) val);
                    currentPagePosition=1;
                } else if (pageIndex == 2) {
                    if (position == 1.0f) {
                        firstStory.findViewById(R.id.main_event).setVisibility(View.VISIBLE);
                        button.setVisibility(View.GONE);
                    } else {
                        long val = (long) ((long) (firstStory.getWidth() / 3)
                                + (position * pageWidth / 2));
                        firstStory.setX((float) val);
                        button.setX((float) val);

                        secondStory.setVisibility(View.VISIBLE);
                        long valRight = (long) ((long) (view.getWidth() / 2 + secondStory.getWidth() / 2)
                                + (position * 1.5 * pageWidth));
                        secondStory.setX((float) valRight);
                        button2.setX((float) valRight);
                    }
                    currentPagePosition=2;
                } else if (pageIndex == 3) {
                    button2.setVisibility(View.VISIBLE);
                    button.setVisibility(View.VISIBLE);
                    firstStory.setAlpha(position);
//
                    currentPagePosition=3;
                    secondStory.setAlpha(position);
                }

                Log.d(TAG, "transformPage: current"+currentPagePosition);
            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                Log.d(TAG, "transformPage: - right end " + pageIndex);
            }
        }
    }


}
