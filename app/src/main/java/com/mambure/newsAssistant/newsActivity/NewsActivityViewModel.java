package com.mambure.newsAssistant.newsActivity;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mambure.newsAssistant.Constants;
import com.mambure.newsAssistant.data.DataRepository;
import com.mambure.newsAssistant.data.models.Article;
import com.mambure.newsAssistant.data.models.ArticlesResult;
import com.mambure.newsAssistant.data.models.Source;
import com.mambure.newsAssistant.data.models.SourcesResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

public class NewsActivityViewModel extends ViewModel {
    private static final String TAG = NewsActivityViewModel.class.getSimpleName();
    private DataRepository dataRepository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private SharedPreferences sharedPreferences;
    private Map<String, String> params = new HashMap<>();
    private List<Source> preferredSources = new ArrayList<>();
    private MutableLiveData<SourcesResult> sourcesStream = new MutableLiveData<>();
    private MutableLiveData<ArticlesResult> articleStream = new MutableLiveData<>();
    private LiveData<Boolean> isBusy;

    @Inject
    public NewsActivityViewModel(DataRepository dataRepository, SharedPreferences sharedPreferences) {
        this.dataRepository = dataRepository;
        this.sharedPreferences = sharedPreferences;
        loadData();
    }

    private void loadData() {
        getSources();
    }

    LiveData<ArticlesResult> getArticlesStream() {
        return articleStream;
    }

    void getArticles(String source) {
        if (source.equals(Constants.REMOTE)) {
            compositeDisposable.add(dataRepository.fetchArticlesFromRemote(params).
                    subscribeOn(Schedulers.io()).subscribe(articlesResult -> {
                        articleStream.postValue(articlesResult);
                    }, throwable -> {
                        ArticlesResult result = new ArticlesResult();
                        result.status = Constants.RESULT_OK;
                        articleStream.postValue(result);
                    }));
        } else {
            compositeDisposable.add(
                    dataRepository.fetchArticlesFromLocal().
                            subscribeOn(Schedulers.io()).subscribe(articles -> {
                        ArticlesResult result = new ArticlesResult();
                        result.status = Constants.RESULT_OK;
                        result.articles = articles;
                        articleStream.postValue(result);
                    }, throwable -> {
                        ArticlesResult result = new ArticlesResult();
                        result.status = Constants.RESULT_ERROR;
                        articleStream.postValue(result);
                        throwable.printStackTrace();
                    })
            );
        }
    }

    public void saveArticle(Article article) {
        dataRepository.saveArticle(article);
    }

    public LiveData<String> deleteArticle(Article article) {
        MutableLiveData<String> liveData = new MutableLiveData<>();
        dataRepository.deleteArticle(article).
                subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onComplete() {
                liveData.postValue(Constants.RESULT_ERROR);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                liveData.postValue(Constants.RESULT_ERROR);
            }
        });
        return liveData;
    }

    public LiveData<SourcesResult> getSourcesStream() {
        return sourcesStream;
    }

    void getSources() {
        compositeDisposable.add(
                dataRepository.fetchSourcesFromLocal().
                        subscribeOn(Schedulers.io()).subscribe(sources -> {
                    preferredSources.addAll(sources);
                    SourcesResult result = new SourcesResult();
                    result.status = Constants.RESULT_OK;
                    result.sources = sources;
                    sourcesStream.postValue(result);
                }, throwable -> {
                    SourcesResult result = new SourcesResult();
                    result.status = Constants.RESULT_ERROR;
                    sourcesStream.postValue(result);
                    throwable.printStackTrace();
                })
        );
    }

    void cleanUp() {
        compositeDisposable.clear();
        dataRepository.cleanUp();
    }

    LiveData<Boolean> getIsBusyStatus() {
        return isBusy;
    }
}
