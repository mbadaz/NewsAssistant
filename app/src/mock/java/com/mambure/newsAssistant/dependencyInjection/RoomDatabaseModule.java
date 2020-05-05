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
    public static LocalDataRepository providesLocalRepositoryDao(Context context) {
        return Room.inMemoryDatabaseBuilder(
                context, AppDatabase.class).build().localDatabaseDAO();
    }

}
