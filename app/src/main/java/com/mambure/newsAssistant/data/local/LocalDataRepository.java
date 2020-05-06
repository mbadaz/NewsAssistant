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
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;


@Dao
public interface LocalDataRepository {
    @Query("SELECT * FROM articles")
    Flowable<List<Article>> getAllArticles();

    @Query("SELECT * FROM articles WHERE title=:articleTitle")
    Maybe<Article> getArticleByTitle(String articleTitle);

    @Insert
    Completable saveArticle(Article articles);

    @Delete
    Completable deleteArticle(Article article);

    @Query("SELECT * FROM sources")
    Maybe<List<Source>> getAllSources();

    @Insert
    Completable insertSources(List<Source> sources);

    @Delete
    Completable deleteSources(List<Source> sources);

}
