package com.mbadasoft.newsassistant.dependencyinjection;

import android.app.Application;
import android.content.Context;

import com.mbadasoft.newsassistant.data.AppNewsRepository;
import com.mbadasoft.newsassistant.data.AppPreferencesRepository;
import com.mbadasoft.newsassistant.data.DataController;
import com.mbadasoft.newsassistant.data.NewsRepository;
import com.mbadasoft.newsassistant.data.PreferencesRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DataControllerModule {

    @Provides
    @Singleton
    public static DataController provideDataController(Application application) {
        return new DataController(
                new AppNewsRepository(application),
                new AppPreferencesRepository(application)
        );
    }
}
