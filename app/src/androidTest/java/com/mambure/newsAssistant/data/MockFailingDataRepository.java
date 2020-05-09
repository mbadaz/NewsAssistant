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
    public Maybe<ArticlesResult> fetchArticlesFromRemote(Map<String, String> params) {
        return null;
    }

    @Override
    public Flowable<List<Article>> fetchArticlesFromLocal() {
        return null;
    }

    @Override
    public void saveArticle(Article article) {

    }

    @Override
    public Completable deleteSavedArticle(Article article) {
        return null;
    }

    @Override
    public Flowable<List<Source>> fetchSourcesFromLocal() {
        return null;
    }

    @Override
    public Observable<SourcesResult> fetchSourcesFromRemote() {
        return new Observable<SourcesResult>() {
            @Override
            protected void subscribeActual(Observer<? super SourcesResult> observer) {
                observer.onError(new Exception());
            }
        };
    }

    @Override
    public Completable saveSources(List<Source> sources) {
        return null;
    }

    @Override
    public Completable deleteSources(Source source) {
        return null;
    }

    @Override
    public void cleanUp() {

    }
}
