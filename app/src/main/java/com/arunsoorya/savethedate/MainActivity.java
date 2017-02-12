package com.arunsoorya.savethedate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.arunsoorya.slideviewpager.SmoothViewPager;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] values = new String[20];
        for (int i = 0; i < 20; i++) {
            values[i] = String.valueOf(2000 + i);
        }
        SmoothViewPager pager = (SmoothViewPager) findViewById(R.id.smooth_pager);
        pager.setUpAdapter(Arrays.asList(values));
    }


}
