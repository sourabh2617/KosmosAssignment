package com.kosmos.articles.di.components;

import android.app.Application;

import com.kosmos.articles.KosmosApp;
import com.kosmos.articles.di.builder.ActivityBuilderModule;
import com.kosmos.articles.di.module.AppModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;


@Singleton
@Component(modules = {
        AppModule.class,
        AndroidInjectionModule.class,
        ActivityBuilderModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(KosmosApp nyTimesApp);
}