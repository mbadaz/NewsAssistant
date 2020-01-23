package com.mambure.newsassistant.dependencyinjection;

import com.mambure.newsassistant.data.MockRemoteNewsRepository;
import com.mambure.newsassistant.data.MockSettingsRepository;
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
    NewsRepository providesRemoteNewsRepository() {
        return new MockRemoteNewsRepository();
    }

    @Provides
    @Singleton
    SettingsRepository providesSettingsRepository() {
        return new MockSettingsRepository();
    }

}
