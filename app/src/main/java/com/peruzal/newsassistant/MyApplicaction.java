package com.peruzal.newsassistant;

import android.app.AppComponentFactory;
import android.app.Application;

import com.peruzal.newsassistant.dependencyInjection.AppComponent;
import com.peruzal.newsassistant.dependencyInjection.AppModule;
import com.peruzal.newsassistant.dependencyInjection.DaggerAppComponent;
import com.peruzal.newsassistant.dependencyInjection.LocalRepositoryModule;
import com.peruzal.newsassistant.dependencyInjection.NetworkModule;
import com.peruzal.newsassistant.dependencyInjection.ViewModelsModule;

import dagger.internal.DaggerCollections;

public class MyApplicaction extends Application {

    AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.builder().appModule(new AppModule(this)).
                localRepositoryModule(new LocalRepositoryModule()).
                networkModule(new NetworkModule()).
                viewModelsModule(new ViewModelsModule()).build();
    }

    public AppComponent getComponent() {
        return component;
    }
}
