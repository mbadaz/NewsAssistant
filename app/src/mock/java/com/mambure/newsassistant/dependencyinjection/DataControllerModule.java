package com.mambure.newsassistant.dependencyinjection;

import com.mambure.newsassistant.data.DataController;
import com.mambure.newsassistant.data.MockRemoteNewsRepository;
import com.mambure.newsassistant.data.MockSettingsRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DataControllerModule {

    @Provides
    @Singleton
    public static DataController provideDataController() {
        return new DataController(
                new MockRemoteNewsRepository(),
                new MockSettingsRepository()
        );
    }
}
