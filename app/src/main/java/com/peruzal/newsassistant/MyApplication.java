package com.peruzal.newsassistant;

import android.app.Application;

import com.peruzal.newsassistant.dependencyInjection.AppComponent;
import com.peruzal.newsassistant.dependencyInjection.AppModule;
import com.peruzal.newsassistant.dependencyInjection.DaggerAppComponent;

public class MyApplication extends Application {

    AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }

    public AppComponent getComponent() {
        return component;
    }


}
