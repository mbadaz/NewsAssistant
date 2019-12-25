package com.mambure.newsassistant.dependencyInjection;

import android.app.Application;
import com.mambure.newsassistant.data.AppRemoteNewsRepository;
import com.mambure.newsassistant.data.AppSettingsRepository;
import com.mambure.newsassistant.data.DataController;
import com.mambure.newsassistant.data.NewsRepository;
import com.mambure.newsassistant.data.SettingsRepository;
import com.mambure.newsassistant.dependencyinjection.RepositoriesModule;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module (includes = RepositoriesModule.class)
public class DataControllerModule {

    @Provides
    @Singleton
    public static DataController provideDataController(NewsRepository newsRepository, SettingsRepository settingsRepository) {

        return new DataController(
                newsRepository,
                settingsRepository
        );
    }
}
