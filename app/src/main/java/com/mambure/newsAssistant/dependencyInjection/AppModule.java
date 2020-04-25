package com.mambure.newsAssistant.dependencyInjection;

import android.app.Application;
import android.content.Context;

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
