package com.mambure.newsAssistant.newsActivity;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mambure.newsAssistant.Constants;
import com.mambure.newsAssistant.Constants.FragmentId;
import com.mambure.newsAssistant.data.Repository;
import com.mambure.newsAssistant.data.models.Article;
import com.mambure.newsAssistant.data.models.ArticlesResult;
import com.mambure.newsAssistant.data.models.SourcesResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.MaybeObserver;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

public class NewsActivityViewModel extends ViewModel {
    private static final String TAG = NewsActivityViewModel.class.getSimpleName();
    private boolean refresh = true;
    private Repository repository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private SharedPreferences sharedPreferences;
    private Map<String, String> params = new HashMap<>();
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

    LiveData<ArticlesResult> getArticlesStream(FragmentId id) {
        if(id == FragmentId.NEW_ARTICLES) return newArticleStream;
        return savedArticleStream;
    }

    void refresh(FragmentId id) {
        refresh = true;
        getArticles(id);
    }

    void getArticles(FragmentId id) {
        if(isBusy) return;
        if (id == FragmentId.NEW_ARTICLES) fetchArticlesFromRemote(refresh);
        else fetchArticlesFromLocal();
    }

    private void fetchArticlesFromLocal() {
        isBusy = true;
        repository.getSavedArticles().
            subscribeOn(Schedulers.io()).
            subscribe(new Observer<List<Article>>() {
                @Override
                public void onSubscribe(Disposable d) {
                    compositeDisposable.add(d);
                }

                @Override
                public void onNext(List<Article> articles) {
                    ArticlesResult result = new ArticlesResult();
                    result.articles = articles;
                    processArticleResult(result);
                    savedArticleStream.postValue(result);
                    isBusy = false;
                    Log.d(TAG, "Fetch articles from local result - " + result);
                }

                @Override
                public void onError(Throwable e) {
                    ArticlesResult result = new ArticlesResult();
                    processArticleResult(result);
                    savedArticleStream.postValue(result);
                    Log.e(TAG, "Fetch articles from local error: ", e);
                    isBusy = false;
                }

                @Override
                public void onComplete() {
                    ArticlesResult result = new ArticlesResult();
                    result.articles = Collections.emptyList();
                    processArticleResult(result);
                    savedArticleStream.postValue(result);
                    Log.d(TAG, "No saved articles to show");
                    isBusy = false;
                }
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
                        if (result.articles.size() > 0) refresh = false;
                        processArticleResult(result);
                        newArticleStream.postValue(result);
                        isBusy = false;
                        // TODO fix this
//               disposable.dispose();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Fetch articles from remote error: ", e);
                        ArticlesResult result = new ArticlesResult();
                        processArticleResult(result);
                        newArticleStream.postValue(result);
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

    private void processArticleResult(ArticlesResult articlesResult) {
        if (articlesResult.articles == null) {
            articlesResult.result = Constants.Result.ERROR;
            return;
        }

        if (articlesResult.articles.size() > 0) {
            articlesResult.result = Constants.Result.OK;
        } else  {
            articlesResult.result = Constants.Result.NO_DATA;
        }
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
        Disposable disposable = repository.deleteSavedArticle(currentArticleToProcess).
                subscribeOn(Schedulers.io()).
                subscribe(() -> {
                    liveData.postValue(true);
                    Log.d(TAG, "Deleted article completed successfully");
        }, throwable -> {
                    liveData.postValue(false);
                    Log.e(TAG, "Delete article Error: ", throwable);;
        });
        compositeDisposable.add(disposable);
        return liveData;
    }

    void cleanUp() {
        isBusy = false;
        compositeDisposable.clear();
        repository.cleanUp();
    }
}
