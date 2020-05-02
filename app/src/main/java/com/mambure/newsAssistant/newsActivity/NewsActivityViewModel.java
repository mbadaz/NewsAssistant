package com.mambure.newsAssistant.newsActivity;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mambure.newsAssistant.Constants;
import com.mambure.newsAssistant.data.DataManager;
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
    private DataManager dataManager;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private SharedPreferences sharedPreferences;
    private Map<String, String> params = new HashMap<>();
    private List<Source> preferredSources = new ArrayList<>();
    private MutableLiveData<SourcesResult> sourcesStream = new MutableLiveData<>();
    private MutableLiveData<ArticlesResult> articleStream = new MutableLiveData<>();
    private Boolean isBusy;

    @Inject
    public NewsActivityViewModel(DataManager dataManager, SharedPreferences sharedPreferences) {
        this.dataManager = dataManager;
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

        if(isBusy) return;

        if (source.equals(Constants.REMOTE)) {
            compositeDisposable.add(dataManager.fetchArticlesFromRemote(params).
                    subscribeOn(Schedulers.io()).subscribe(articlesResult -> {
                        articleStream.postValue(articlesResult);
                        isBusy = false;
                        Log.d(TAG, "Fetch articles from remote result - " + articlesResult);
                    }, throwable -> {
                            isBusy = false;
                            ArticlesResult result = new ArticlesResult();
                            result.status = Constants.RESULT_OK;
                            articleStream.postValue(result);
                            Log.d(TAG, "Fetch articles from remote error");
                            throwable.printStackTrace();
                    }));
        } else {
            compositeDisposable.add(
                    dataManager.fetchArticlesFromLocal().
                            subscribeOn(Schedulers.io()).subscribe(articles -> {
                        ArticlesResult result = new ArticlesResult();
                        result.status = Constants.RESULT_OK;
                        result.articles = articles;
                        articleStream.postValue(result);
                        isBusy = false;
                        Log.d(TAG, "Fetch articles from local result - " + result);
                    }, throwable -> {
                            isBusy = false;
                            ArticlesResult result = new ArticlesResult();
                            result.status = Constants.RESULT_ERROR;
                            articleStream.postValue(result);
                            Log.d(TAG, "Fetch articles from local error");
                            throwable.printStackTrace();
                    })
            );
        }
        isBusy = true;
    }

    public void saveArticle(Article article) {
        dataManager.saveArticle(article);
    }

    public LiveData<String> deleteArticle(Article article) {
        MutableLiveData<String> liveData = new MutableLiveData<>();
        dataManager.deleteArticle(article).
                subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onComplete() {
                liveData.postValue(Constants.RESULT_ERROR);
                Log.d(TAG, "Deleted article completed successfully");
            }

            @Override
            public void onError(@NonNull Throwable e) {
                liveData.postValue(Constants.RESULT_ERROR);
                e.printStackTrace();
            }
        });
        return liveData;
    }

    public LiveData<SourcesResult> getSourcesStream() {
        return sourcesStream;
    }

    void getSources() {
        if(isBusy) return;

        compositeDisposable.add(
                dataManager.fetchSourcesFromLocal().
                        subscribeOn(Schedulers.io()).subscribe(sources -> {
                    preferredSources.addAll(sources);
                    SourcesResult result = new SourcesResult();
                    result.status = Constants.RESULT_OK;
                    result.sources = sources;
                    sourcesStream.postValue(result);
                    isBusy = false;
                    Log.d(TAG, "Fetch sources from local result: " + result);
                }, throwable -> {
                        isBusy = false;
                        SourcesResult result = new SourcesResult();
                        result.status = Constants.RESULT_ERROR;
                        sourcesStream.postValue(result);
                        Log.d(TAG, "Fetch sources from local error");
                        throwable.printStackTrace();
                })
        );
        isBusy = true;
    }

    void cleanUp() {
        compositeDisposable.clear();
        dataManager.cleanUp();
    }
}
