package com.mambure.newsassistant;

import android.app.Application;
import com.mambure.newsassistant.dependencyInjection.AppComponent;
import com.mambure.newsassistant.dependencyInjection.AppModule;
import com.mambure.newsassistant.dependencyInjection.DaggerAppComponent;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;


public class MyApplication extends Application {

    AppComponent appComponent;

    @Override
    public void onCreate() {
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        Realm.init(this);
        super.onCreate();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }




}
