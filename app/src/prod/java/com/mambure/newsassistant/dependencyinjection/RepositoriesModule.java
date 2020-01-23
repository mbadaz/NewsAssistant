package com.mambure.newsassistant.dependencyinjection;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import com.mambure.newsassistant.data.LocalDataRepository;
import com.mambure.newsassistant.data.DataRepository;
import com.mambure.newsassistant.data.NewsProvider;
import com.mambure.newsassistant.data.NewsRepository;
import com.mambure.newsassistant.dependencyInjection.AppModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;

@Module(includes = AppModule.class)
public class RepositoriesModule {

    @Provides
    @Singleton
    NewsProvider providesNewsRepository(Application application) {
        return new NewsRepository(application);
    }

    @Provides
    @Singleton
    DataRepository providesNewsDatabase(SharedPreferences preferences, Realm realm) {
        return new LocalDataRepository(preferences, realm);
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return application.getSharedPreferences("app", Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    Realm providesRealm() {
        RealmConfiguration configuration = new RealmConfiguration.Builder().
                name("News Realm").
                schemaVersion(1).
                deleteRealmIfMigrationNeeded().
                build();
        return Realm.getInstance(configuration);
    }
}
