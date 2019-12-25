package com.mambure.newsassistant.dependencyinjection;

import android.app.Application;

import com.mambure.newsassistant.data.AppRemoteNewsRepository;
import com.mambure.newsassistant.data.AppSettingsRepository;
import com.mambure.newsassistant.data.NewsRepository;
import com.mambure.newsassistant.data.SettingsRepository;
import com.mambure.newsassistant.dependencyInjection.AppModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = AppModule.class)
public class RepositoriesModule {

    @Provides
    @Singleton
    NewsRepository providesRemoteNewsRepository(Application application) {
        return new AppRemoteNewsRepository(application);
    }

    @Provides
    @Singleton
    SettingsRepository providesSettingsRepository(Application application) {
        return new AppSettingsRepository(application);
    }
}
