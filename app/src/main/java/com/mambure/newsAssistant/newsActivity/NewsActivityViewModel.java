package com.mambure.newsAssistant.newsActivity;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.mambure.newsAssistant.Constants;
import com.mambure.newsAssistant.data.DataRepository;
import com.mambure.newsAssistant.data.models.Article;
import com.mambure.newsAssistant.data.models.ArticlesResult;
import com.mambure.newsAssistant.data.models.SourcesResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

import javax.inject.Inject;

public class NewsActivityViewModel extends ViewModel {
    private static final String TAG = NewsActivityViewModel.class.getSimpleName();
    DataRepository dataRepository;
    SharedPreferences sharedPreferences;
    Map<String, String> preferredSources = new HashMap<>();

    @Inject
    public NewsActivityViewModel(DataRepository dataRepository, SharedPreferences sharedPreferences) {
        this.dataRepository = dataRepository;
        this.sharedPreferences = sharedPreferences;
    }

    private void loadPreferences() {
        Set<String> sources = sharedPreferences.getStringSet(Constants.SOURCES, null);
        StringJoiner joiner = new StringJoiner(",");
        for (String source : sources) {
            joiner.add(source);
        }
        preferredSources.put(Constants.SOURCES, joiner.toString());
    }


    public LiveData<ArticlesResult> getArticlesStream() {
        return dataRepository.getArticlesStream();
    }



    public void getArticles(String source) {
        dataRepository.fetchArticles(preferredSources, source);
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
}
