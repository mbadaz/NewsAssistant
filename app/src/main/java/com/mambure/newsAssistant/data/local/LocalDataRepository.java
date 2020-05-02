package com.mambure.newsAssistant.data.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.mambure.newsAssistant.data.models.Article;
import com.mambure.newsAssistant.data.models.Source;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;


@Dao
public interface LocalDataRepository {
    @Query("SELECT * FROM articles")
    Flowable<List<Article>> getAllArticles();

    @Insert
    Single<Long> insertArticle(Article articles);

    @Delete
    Completable deleteArticle(Article article);

    @Query("SELECT * FROM sources")
    Flowable<List<Source>> getAllSources();

    @Insert
    Completable insertSources(List<Source> sources);

    @Delete
    Completable deleteSource(Source source);

}
