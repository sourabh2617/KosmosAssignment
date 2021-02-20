package com.kosmos.articles;

import android.app.Activity;
import android.app.Application;

import com.kosmos.articles.di.components.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class KosmosApp extends Application implements HasActivityInjector {

    private static KosmosApp sInstance;
    public static KosmosApp getAppContext() {
        return sInstance;
    }
    private static synchronized void setInstance(KosmosApp app) {
        sInstance = app;
    }

    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeComponent();
        setInstance(this);
    }

    private void initializeComponent() {
        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this);
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return activityDispatchingInjector;
    }
}
