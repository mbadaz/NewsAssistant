package com.peruzal.newsassistant.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.peruzal.newsassistant.data.models.Article;
import com.peruzal.newsassistant.data.models.Source;

import java.util.List;

@Dao
public interface LocalRepositoryDAO {
    @Query("SELECT * FROM articles")
    LiveData<List<Article>> getAllArticles();

    @Insert
    void insertArticle(Article articles);

    @Delete
    void deleteArticle(Article article);

    @Query("SELECT * FROM sources")
    LiveData<List<Source>> getAllSources();

    @Insert
    void insertSources(List<Source> sources);

    @Delete
    void deleteSource(Source source);
}
