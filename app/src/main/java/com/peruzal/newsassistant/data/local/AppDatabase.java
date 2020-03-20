package com.peruzal.newsassistant.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.peruzal.newsassistant.data.models.Article;
import com.peruzal.newsassistant.data.models.Source;

@Database(entities = {Article.class, Source.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LocalRepositoryDAO localDatabaseDAO();
}
