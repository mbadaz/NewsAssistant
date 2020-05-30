package com.mambure.newsAssistant.data;

import com.mambure.newsAssistant.data.models.Article;
import com.mambure.newsAssistant.data.models.ArticlesResult;
import com.mambure.newsAssistant.data.models.Source;
import com.mambure.newsAssistant.data.models.SourcesResult;

import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Observer;

public class MockFailingDataRepository implements Repository {

    @Override
    public Maybe<ArticlesResult> getArticles(Map<String, String> params, boolean update) {
        return Maybe.error(new Throwable("No data"));
    }

    @Override
    public Maybe<List<Article>> getSavedArticles() {
        return null;
    }

    @Override
    public Completable saveArticle(Article article) {
        return null;
    }

    @Override
    public Maybe<Article> findSavedArticle(String title) {
        return null;
    }

    @Override
    public Completable deleteSavedArticle(Article article) {
        return null;
    }

    @Override
    public Completable saveSources(List<Source> sources) {
        return null;
    }

    @Override
    public Completable deletePreferredSources(List<Source> sources) {
        return null;
    }

    @Override
    public Maybe<List<Source>> getSavedSources() {
        return null;
    }

    @Override
    public Observable<SourcesResult> getSources() {
        return null;
    }

    @Override
    public void cleanUp() {

    }
}
