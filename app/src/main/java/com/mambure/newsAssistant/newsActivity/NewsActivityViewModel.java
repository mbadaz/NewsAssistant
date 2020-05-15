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

import java.util.Collections;
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
    private Repository repository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private SharedPreferences sharedPreferences;
    private Map<String, String> params = new HashMap<>();
    private MutableLiveData<SourcesResult> sourcesStream = new MutableLiveData<>();
    private MutableLiveData<ArticlesResult> newArticleStream = new MutableLiveData<>();
    private MutableLiveData<ArticlesResult> savedArticleStream = new MutableLiveData<>();
    private Boolean isBusy = false;
    private Article currentArticleToProcess;

    @Inject
    public NewsActivityViewModel(Repository repository, SharedPreferences sharedPreferences) {
        this.repository = repository;
        this.sharedPreferences = sharedPreferences;
    }

    void setCurrentArticleToProcess(Article currentArticleToProcess) {
        this.currentArticleToProcess = currentArticleToProcess;
    }

    Article getCurrentArticle() {
        return currentArticleToProcess;
    }

    LiveData<ArticlesResult> getSavedArticlesStream() {
       return savedArticleStream;
    }

    LiveData<ArticlesResult> getNewArticlesStream() {
       return newArticleStream;
    }

    void getArticles(String id, boolean update) {
        if(isBusy) return;
        if (id.equals(Constants.REMOTE)) fetchArticlesFromRemote(update);
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
            savedArticleStream.postValue(result);
            isBusy = false;
            Log.d(TAG, "Fetch articles from local result - " + result);
        }, throwable -> {
            processArticleResultError();
            Log.e(TAG, "Fetch articles from local error: ", throwable);
            isBusy = false;
        });
    }

    private void fetchArticlesFromRemote(boolean update) {
        isBusy = true;
       repository.getArticles(params, update).
                subscribe(new MaybeObserver<ArticlesResult>() {
            private Disposable disposable;
           @Override
           public void onSubscribe(Disposable d) {
               disposable = d;
           }

           @Override
           public void onSuccess(ArticlesResult result) {
               newArticleStream.postValue(result);
               isBusy = false;
//               disposable.dispose();
           }

           @Override
           public void onError(Throwable e) {
               Log.e(TAG, "Fetch articles from remote error: ", e);
               processArticleResultError();
               isBusy = false;
               disposable.dispose();
           }

           @Override
           public void onComplete() {
               ArticlesResult result = new ArticlesResult();
               result.status = Constants.RESULT_OK;
               result.articles = Collections.emptyList();
               newArticleStream.postValue(result);
               Log.d(TAG, "Empty response fetching data");
               isBusy = false;
               disposable.dispose();
           }
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
                        Log.d(TAG, "Skipping saving, article already saved: " + article);
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
        newArticleStream.postValue(result);
    }

    private void processSourceResultError() {
        isBusy = false;
        SourcesResult result = new SourcesResult();
        result.status = Constants.RESULT_ERROR;
        sourcesStream.postValue(result);
    }

    void refreshArticles() {
        repository.refresh();
    }

    void cleanUp() {
        isBusy = false;
        compositeDisposable.clear();
        repository.cleanUp();
    }
}
