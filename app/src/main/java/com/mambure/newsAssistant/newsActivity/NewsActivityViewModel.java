package com.mambure.newsAssistant.newsActivity;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.mambure.newsAssistant.Constants;
import com.mambure.newsAssistant.data.DataRepository;
import com.mambure.newsAssistant.data.models.Article;
import com.mambure.newsAssistant.data.models.ArticlesResult;
import com.mambure.newsAssistant.data.models.Source;
import com.mambure.newsAssistant.data.models.SourcesResult;
import com.mambure.newsAssistant.dependencyInjection.ActivityScope;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.Future;

import javax.inject.Inject;

public class NewsActivityViewModel extends ViewModel {
    private static final String TAG = NewsActivityViewModel.class.getSimpleName();
    private DataRepository dataRepository;
    private SharedPreferences sharedPreferences;
    private Map<String, String> params = new HashMap<>();
    private MutableLiveData<Source> preferredSources = new MutableLiveData<>();
    private LiveData<Boolean> isBusy;

    @Inject
    public NewsActivityViewModel(DataRepository dataRepository, SharedPreferences sharedPreferences) {
        this.dataRepository = dataRepository;
        this.sharedPreferences = sharedPreferences;
        loadData();
    }

    private void loadData() {
        isBusy = dataRepository.getIsBusyStatusStream();
       // dataRepository.fetchSources(Constants.LOCAL);
    }

    public LiveData<ArticlesResult> getArticlesStream() {
        return dataRepository.getArticlesStream();
    }

    public void getArticles(String source) {
        dataRepository.fetchArticles(source, params);
    }

    public void saveArticle(Article article) {
        dataRepository.saveArticle(article);
    }

    public void deleteArticle(Article article) {
        dataRepository.deleteArticle(article);
    }

    public LiveData<SourcesResult> getSourcesStream() {
        return dataRepository.getSourcesStream();
    }

    public void getSources() {
        final LiveData<SourcesResult> resultLiveData = getSourcesStream();
        resultLiveData.observeForever(new Observer<SourcesResult>() {
            @Override
            public void onChanged(SourcesResult sourcesResult) {
                resultLiveData.removeObserver(this);
            }
        });
        dataRepository.fetchSources(Constants.LOCAL);
    }

    LiveData<Boolean> getIsBusyStatus() {
        return isBusy;
    }
}
