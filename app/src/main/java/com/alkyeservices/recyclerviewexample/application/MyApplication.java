package com.alkyeservices.recyclerviewexample.application;

import android.app.Application;

public class MyApplication extends Application {
    public static MyApplication myApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;

    }
}