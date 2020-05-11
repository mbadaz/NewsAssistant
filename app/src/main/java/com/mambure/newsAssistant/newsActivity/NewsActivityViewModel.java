package com.mambure.newsAssistant.newsActivity;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mambure.newsAssistant.Constants;
import com.mambure.newsAssistant.data.Repository;
import com.mambure.newsAssistant.data.models.Article;
import com.mambure.newsAssistant.data.models.ArticlesResult;
import com.mambure.newsAssistant.data.models.SourcesResult;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.MaybeObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

public class NewsActivityViewModel extends ViewModel {
    private static final String TAG = NewsActivityViewModel.class.getSimpleName();
    private boolean hasData = false;
    private String dataSource = Constants.REMOTE;
    private Repository repository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private SharedPreferences sharedPreferences;
    private Map<String, String> params = new HashMap<>();
    private MutableLiveData<SourcesResult> sourcesStream = new MutableLiveData<>();
    private MutableLiveData<ArticlesResult> articleStream = new MutableLiveData<>();
    private Boolean isBusy = false;
    private Article currentArticleToProcess;

    @Inject
    public NewsActivityViewModel(Repository repository, SharedPreferences sharedPreferences) {
        this.repository = repository;
        this.sharedPreferences = sharedPreferences;
    }

    public boolean hasData() {
        return hasData;
    }

    public void setHasData(boolean hasData) {
        this.hasData = hasData;
    }

    void setDataSource(String id) {
        dataSource = id;
    }

    void setCurrentArticleToProcess(Article currentArticleToProcess) {
        this.currentArticleToProcess = currentArticleToProcess;
    }

    Article getCurrentArticle() {
        return currentArticleToProcess;
    }

    LiveData<ArticlesResult> getArticlesStream() {
        return articleStream;
    }

    void getArticles() {
        if(isBusy) return;
        if (dataSource.equals(Constants.REMOTE)) compositeDisposable.add(fetchArticlesFromRemote());
        else compositeDisposable.add(fetchArticlesFromLocal());
    }

    private Disposable fetchArticlesFromLocal() {
        isBusy = true;
        return  repository.getSavedArticles().
                subscribeOn(Schedulers.io()).
                subscribe(articles -> {
            ArticlesResult result = new ArticlesResult();
            result.status = Constants.RESULT_OK;
            result.articles = articles;
            articleStream.postValue(result);
            isBusy = false;
            Log.d(TAG, "Fetch articles from local result - " + result);
        }, throwable -> {
            processArticleResultError();
            Log.e(TAG, "Fetch articles from local error: ", throwable);
            isBusy = false;
        });
    }

    private Disposable fetchArticlesFromRemote() {
        isBusy = true;
       return repository.getNewArticles(params).
                subscribeOn(Schedulers.io()).subscribe(articlesResult -> {
            articleStream.postValue(articlesResult);
            isBusy = false;
            Log.d(TAG, "Fetch articles from remote result - " + articlesResult);
        }, throwable -> {
            processArticleResultError();
            Log.e(TAG, "Fetch articles from remote error: ", throwable);
            isBusy = false;
        });
    }

    LiveData<Boolean> saveArticle() {
        MutableLiveData<Boolean> savingStatusLiveData = new MutableLiveData<>();

        repository.findSavedArticle(currentArticleToProcess.title).
                subscribeOn(Schedulers.io()).
                subscribe(new MaybeObserver<Article>() {
                    Disposable searchingDisposable;
                    @Override
                    public void onSubscribe(Disposable d) {
                        searchingDisposable = d;
                    }

                    @Override
                    public void onSuccess(Article article) {
                        Log.d(TAG, "Found article in database: " + article);
                        savingStatusLiveData.postValue(true);
                        searchingDisposable.dispose();
                    }

                    @Override
                    public void onError(Throwable e) {
                        savingStatusLiveData.postValue(false);
                        Log.e(TAG, "Error searching Article: " + currentArticleToProcess, e);
                        searchingDisposable.dispose();
                    }

                    @Override
                    public void onComplete() {
                        Disposable saveDisposable = repository.saveArticle(currentArticleToProcess).
                                subscribeOn(Schedulers.io()).
                                subscribe(() -> {
                                    savingStatusLiveData.postValue(true);
                                }, throwable -> {
                                    savingStatusLiveData.postValue(false);
                                    Log.e(TAG, "Error saving Article: " + currentArticleToProcess, throwable);
                                });
                        compositeDisposable.add(saveDisposable);
                        searchingDisposable.dispose();
                    }
                });

        return savingStatusLiveData;
    }

    LiveData<Boolean> deleteArticle() {
        MutableLiveData<Boolean> liveData = new MutableLiveData<>();
        repository.deleteSavedArticle(currentArticleToProcess).
                subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onComplete() {
                liveData.postValue(true);
                Log.d(TAG, "Deleted article completed successfully");
            }

            @Override
            public void onError(@NonNull Throwable e) {
                liveData.postValue(false);
                Log.e(TAG, "Delete article Error: ", e);;
            }
        });
        return liveData;
    }

    private void processArticleResultError() {
        isBusy = false;
        ArticlesResult result = new ArticlesResult();
        result.status = Constants.RESULT_ERROR;
        articleStream.postValue(result);
    }

    private void processSourceResultError() {
        isBusy = false;
        SourcesResult result = new SourcesResult();
        result.status = Constants.RESULT_ERROR;
        sourcesStream.postValue(result);
    }

    void cleanUp() {
        isBusy = false;
        compositeDisposable.clear();
//        articleStream.setValue(null);
        hasData = false;
        repository.cleanUp();
    }
}
