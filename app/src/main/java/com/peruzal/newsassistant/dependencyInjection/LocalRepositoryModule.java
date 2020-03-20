package com.peruzal.newsassistant.dependencyInjection;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.peruzal.newsassistant.data.local.AppDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = AppModule.class)
public class LocalRepositoryModule {

    @Provides
    @Singleton
    AppDatabase providesRoomDatabase(Context context) {
        return Room.databaseBuilder(
                context, AppDatabase.class, "App-database.db").build();
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Context context) {
        return context.getSharedPreferences("app preferences", Context.MODE_PRIVATE);
    }
}
