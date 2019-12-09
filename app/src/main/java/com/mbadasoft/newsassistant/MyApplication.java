package com.mbadasoft.newsassistant;

import android.app.Application;
import com.mbadasoft.newsassistant.dependencyInjection.AppComponent;
import com.mbadasoft.newsassistant.dependencyInjection.AppModule;
import com.mbadasoft.newsassistant.dependencyInjection.DaggerAppComponent;


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
