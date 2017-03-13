package com.arunsoorya.savethedate;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.arunsoorya.savethedate.model.EventVO;
import com.arunsoorya.savethedate.utils.BottomSheet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.Comparator;

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
    View coordinatorLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
//        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        container = (ViewGroup) findViewById(R.id.container);
        progressLayout = findViewById(R.id.progress_layout);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
    }

    protected void setContentLayout(@LayoutRes int layoutId) {
        View view = LayoutInflater.from(this).inflate(layoutId, null, false);
        container.addView(view);
        progressLayout.setVisibility(View.GONE);
    }

    protected boolean isEmpty(String val) {
        return TextUtils.isEmpty(val);
    }

    protected void showNoInternet() {
        findViewById(R.id.no_internet_layout).setVisibility(View.VISIBLE);
//        boolean connected = snapshot.getValue(Boolean.class);
//        if (connected) {
//            Toast.makeText(this, "Connected", Toask.LENGTH_SHORT);
//        } else {
//            Toast.makeText(this, "Not connected", Toask.LENGTH_SHORT).show();
//        }
    }

    protected void hideNoInternet() {
        findViewById(R.id.no_internet_layout).setVisibility(View.GONE);
    }

    protected void showLoading() {
        container.setVisibility(View.GONE);
        progressLayout.setVisibility(View.VISIBLE);
    }

    protected void dismissLoading() {
        container.setVisibility(View.VISIBLE);
        progressLayout.setVisibility(View.GONE);
    }

    public void showBottomSheet(EventVO eventVO) {
//        MyDialog dialog = (MyDialog) MyDialog.getInstance(eventVOs.get(position));
//        dialog.show(getSupportFragmentManager(), "dia");
        BottomSheet myBottomSheet = BottomSheet.newInstance(eventVO);
        myBottomSheet.show(getSupportFragmentManager(), "show");

//        View llBottomSheet = findViewById(R.id.bottom_sheet);

// init the bottom sheet behavior
//        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
//        bottomSheetBehavior.setSkipCollapsed(true);

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

    public void navigateWithResult(Class clas, int requestCode) {
        Intent intent = new Intent(this, clas);
        startActivityForResult(intent, requestCode);
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
        setDefaultTime(calendar);
        return calendar;

    }

    public Calendar getTodaysDate() {
        Calendar calendar = Calendar.getInstance();
        setDefaultTime(calendar);
        return calendar;
    }

    public Calendar getSelectedDate(Calendar calendarPas) {
        Calendar calendar = Calendar.getInstance();
        if (calendarPas != null)
            calendar.setTimeInMillis(calendarPas.getTimeInMillis());
        setDefaultTime(calendar);
        return calendar;

    }
    private void setDefaultTime(Calendar calendar){
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }


    public String getTimeString() {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return String.valueOf(calendar.getTimeInMillis());
    }

    Comparator sortBasedOnDateComparator = new Comparator<EventVO>() {
        public int compare(EventVO o1, EventVO o2) {
            return o1.getEventDate().compareToIgnoreCase(o2.getEventDate());
        }
    };

    protected void showDeleteAlert(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(message);
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onDeleteConfirm();
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }

    protected void onDeleteConfirm() {
    }

    public void showHeaderLoding() {
        showHeaderLoding("Saving...", null);
    }

    public void showHeaderLoding(View... disableViews) {
        showHeaderLoding("Saving...", disableViews);
    }

    public void showHeaderLoding(String message) {
        showHeaderLoding(message, null);
    }

    public void showHeaderLoding(String message, View... disableViews) {
        findViewById(R.id.header_loading_bar).setVisibility(View.VISIBLE);
        if (message != null)
            ((TextView) findViewById(R.id.header_loading_text)).setText(message);
        if (disableViews != null) {
            for (int i = 0; i < disableViews.length; i++) {
                disableViews[i].setEnabled(false);
            }
        }
    }

    public void hideHeaderLoading() {
        findViewById(R.id.header_loading_bar).setVisibility(View.GONE);
    }


}
