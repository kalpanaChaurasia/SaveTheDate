package com.arunsoorya.slideviewpager;


import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SmoothViewPager extends HorizontalScrollView {

    private List<String> values = new ArrayList<>();
    private Context context;
    private LinearLayout rootLayout;

    public SmoothViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
//        setOrientation(LinearLayout.HORIZONTAL);
//        setGravity(Gravity.CENTER_VERTICAL);
//        LayoutInflater.from(context).inflate(R.layout.pager_layout, this,true);
        rootLayout = new LinearLayout(context);
        rootLayout.setOrientation(LinearLayout.HORIZONTAL);
        rootLayout.setLayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        this.addView(rootLayout);
    }

    public void setUpAdapter(List values) {
        this.values = values;
        createScrollingChild();

    }

    private void createScrollingChild() {
        TextView text;
        for (String name : values) {
            ViewGroup layout = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.pager_row, null, false);
            layout.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layout.setBackgroundColor(Color.RED);
            text = (TextView) layout.findViewById(R.id.textview);
            text.setText(name);
            rootLayout.addView(layout);
        }


    }


    private void initViewPager(Context context) {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        CustomPagerAdapter adapter = new CustomPagerAdapter(context);
        viewPager.setAdapter(adapter);
//        adapter.notifyDataSetChanged();

    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//
//        int desiredWidth = 100;
//        int desiredHeight = 100;
//
//        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//
//        int width;
//        int height;
//        //Measure Width
//        if (widthMode == MeasureSpec.EXACTLY) {
//            //Must be this size
//            width = widthSize;
//        } else if (widthMode == MeasureSpec.AT_MOST) {
//            //Can't be bigger than...
//            width = Math.min(desiredWidth, widthSize);
//        } else {
//            //Be whatever you want
//            width = desiredWidth;
//        }
//
//        //Measure Height
//        if (heightMode == MeasureSpec.EXACTLY) {
//            //Must be this size
//            height = heightSize;
//        } else if (heightMode == MeasureSpec.AT_MOST) {
//            //Can't be bigger than...
//            height = Math.min(desiredHeight, heightSize);
//        } else {
//            //Be whatever you want
//            height = desiredHeight;
//        }
//
//        //MUST CALL THIS
//        setMeasuredDimension(width, height);
//
////        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }

    public class CustomPagerAdapter extends PagerAdapter {

        private Context mContext;
        LayoutInflater inflater;

        public CustomPagerAdapter(Context context) {
            mContext = context;
            inflater = LayoutInflater.from(mContext);
        }

        @Override
        public float getPageWidth(int position) {
            float nbPages = 6; // You could display partial pages using a float value
            return (1 / nbPages);
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {

            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.pager_row, collection, false);
            layout.setLayoutParams(new LayoutParams(200, 50));
            TextView text = (TextView) layout.findViewById(R.id.textview);
            text.setText(values.get(position));
            collection.addView(layout);
            return layout;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return values.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


    }
}