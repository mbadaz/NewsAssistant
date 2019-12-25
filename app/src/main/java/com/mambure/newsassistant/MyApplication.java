package com.mambure.newsassistant;

import android.app.Application;
import com.mambure.newsassistant.dependencyInjection.AppComponent;
import com.mambure.newsassistant.dependencyInjection.AppModule;
import com.mambure.newsassistant.dependencyInjection.DaggerAppComponent;


public class MyApplication extends Application {

    AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }


}
