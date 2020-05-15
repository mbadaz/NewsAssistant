package com.mambure.newsAssistant.data;

import android.util.Log;

import com.mambure.newsAssistant.Constants;
import com.mambure.newsAssistant.data.local.LocalDataRepository;
import com.mambure.newsAssistant.data.models.Article;
import com.mambure.newsAssistant.data.models.ArticlesResult;
import com.mambure.newsAssistant.data.models.Source;
import com.mambure.newsAssistant.data.models.SourcesResult;
import com.mambure.newsAssistant.data.remote.NewsService;

import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

public interface Repository{

    Maybe<ArticlesResult> getArticles(Map<String, String> params, boolean update);

    Maybe<List<Article>> getSavedArticles();

    Completable saveArticle(Article article);

    Maybe<Article> findSavedArticle(String title);

    Completable deleteSavedArticle(Article article);

    Completable saveSources(List<Source> sources);

    Completable deletePreferredSources(List<Source> sources);

    Maybe<List<Source>> getSavedSources();

    Observable<SourcesResult> getSources();

    void refresh();

    void cleanUp();

}
