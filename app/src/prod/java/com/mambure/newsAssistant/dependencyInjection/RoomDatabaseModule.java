package com.mambure.newsAssistant.dependencyInjection;

import android.content.Context;

import androidx.room.Room;

import com.mambure.newsAssistant.data.local.AppDatabase;
import com.mambure.newsAssistant.data.local.LocalDataRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RoomDatabaseModule {

    @Provides
    @Singleton
    LocalDataRepository providesLocalRepositoryDao(Context context) {
        return Room.databaseBuilder(
                context, AppDatabase.class, "App-database.db").
                fallbackToDestructiveMigration().
                build().localDatabaseDAO();
    }

}
