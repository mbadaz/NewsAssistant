package com.peruzal.newsassistant.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.peruzal.newsassistant.models.Article;

@Database(entities = {Article.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ArticlesDAO articlesDAO();
}
