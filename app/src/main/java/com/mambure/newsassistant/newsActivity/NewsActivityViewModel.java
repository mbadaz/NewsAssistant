package com.mambure.newsassistant.newsActivity;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.mambure.newsassistant.data.DefaultRepository;
import com.mambure.newsassistant.models.Article;
import com.mambure.newsassistant.models.ArticlesResult;

import java.util.List;

import javax.inject.Inject;

public class NewsActivityViewModel extends ViewModel {
    private static final String TAG = NewsActivityViewModel.class.getSimpleName();
    private DefaultRepository dataDefaultRepository;

    @Inject
    public NewsActivityViewModel(@NonNull DefaultRepository dataDefaultRepository) {
        this.dataDefaultRepository = dataDefaultRepository;
    }


    public LiveData<ArticlesResult> getArticles(String fragmentId) {
        return dataDefaultRepository.getArticles(fragmentId);
    }

    public void saveArticle(Article article) {
        dataDefaultRepository.saveArticle(article);
    }


}
