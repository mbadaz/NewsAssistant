package com.peruzal.newsassistant.dependencyInjection;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.room.Room;

import com.peruzal.newsassistant.data.local.AppDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Application app;

    public AppModule(Application app) {
        this.app = app;
    }

    @Provides
    Application providesApplication() {
        return app;
    }

    @Provides
    Context providesContext() {
       return app.getApplicationContext();
    }
}
