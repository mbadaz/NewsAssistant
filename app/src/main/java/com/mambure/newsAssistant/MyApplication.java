package com.mambure.newsAssistant;

import android.app.Application;

import com.mambure.newsAssistant.dependencyInjection.AppComponent;
import com.mambure.newsAssistant.dependencyInjection.AppModule;
import com.mambure.newsAssistant.dependencyInjection.DaggerAppComponent;

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
