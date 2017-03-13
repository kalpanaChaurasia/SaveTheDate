package com.arunsoorya.savethedate;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by arunsoorya on 12/03/17.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    /* Enable disk persistence  */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}