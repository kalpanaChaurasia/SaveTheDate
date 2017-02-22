package com.arunsoorya.savethedate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

/**
 * Created by arunsoorya on 19/01/17.
 */

public class BaseActivity extends AppCompatActivity {
    public static String USER = "/User/";
    public static String STORIES = "/Stories/";
    public static String EVENTS = "/Events/";
    public static String EVENTIDS = "eventIds";

    //    @BindView()
    ViewGroup container;
    //    @BindView(R.id.progress_layout)
    View progressLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
//        ButterKnife.bind(this);

        container = (ViewGroup) findViewById(R.id.container);
        progressLayout = findViewById(R.id.progress_layout);
    }

    protected void setContentLayout(@LayoutRes int layoutId) {
        View view = LayoutInflater.from(this).inflate(layoutId, null, false);
        container.addView(view);
        progressLayout.setVisibility(View.GONE);
    }

    protected boolean isEmpty(String val) {
        return TextUtils.isEmpty(val);
    }

    protected void showLoading() {
        container.setVisibility(View.GONE);
        progressLayout.setVisibility(View.VISIBLE);
    }

    protected void dismissLoading() {
        container.setVisibility(View.VISIBLE);
        progressLayout.setVisibility(View.GONE);
    }

    public String getStoryEventPath(String storyId) {
        return getStoryPath().concat(storyId).concat("/").concat(EVENTIDS);
    }

    public void showToast(String message) {
        Toast.makeText(this, message,
                Toast.LENGTH_SHORT).show();
    }

    public String getLoggedInUserPath() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return USER.concat(user.getUid());
        }
        return "";
    }

    public String getStoryPath() {
        return getLoggedInUserPath().concat(STORIES);
    }

    public String getEventsPath() {
        return getLoggedInUserPath().concat(EVENTS);
    }


    public void navigate(Class clas) {
        navigate(clas, null);
    }

    public void navigate(Class clas, String params) {
        Intent intent = new Intent(this, clas);
        if (params != null) {
            intent.putExtra(params, true);
        }
        startActivity(intent);
    }

    public void navigateWithData(Class clas, Bundle params) {
        Intent intent = new Intent(this, clas);
        if (params != null) {
            intent.putExtras(params);
        }
        startActivityForResult(intent, 101);
    }

    public Calendar getSelectedDate(int... ymd) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(ymd[0], ymd[1], ymd[2]);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;

    }

    public String getTimeString() {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return String.valueOf(calendar.getTimeInMillis());
    }
}
