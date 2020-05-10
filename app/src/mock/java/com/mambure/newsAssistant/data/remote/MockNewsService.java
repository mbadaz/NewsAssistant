package com.mambure.newsAssistant.data.remote;

import com.mambure.newsAssistant.Constants;
import com.mambure.newsAssistant.data.MockingUtils;
import com.mambure.newsAssistant.data.models.ArticlesResult;
import com.mambure.newsAssistant.data.models.SourcesResult;

import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import kotlinx.coroutines.flow.Flow;

public class MockNewsService implements NewsService {
    @Override
    public Observable<SourcesResult> getSources() {
        return new Observable<SourcesResult>() {
            @Override
            protected void subscribeActual(Observer<? super SourcesResult> observer) {
                SourcesResult result = new SourcesResult();
                result.sources = MockingUtils.generateMockSources();
                result.status = Constants.RESULT_OK;
                observer.onNext(result);
            }
        };
    }

    @Override
    public Observable<ArticlesResult> getArticles(Map<String, Object> params) {
        return new Observable<ArticlesResult>() {
            @Override
            protected void subscribeActual(Observer<? super ArticlesResult> observer) {
                ArticlesResult result = new ArticlesResult();
                result.articles = MockingUtils.generateMockArticles();
                result.status = Constants.RESULT_OK;
                observer.onNext(result);
            }
        };
    }
}
