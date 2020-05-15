package com.mambure.newsAssistant.dependencyInjection;

import com.mambure.newsAssistant.newsActivity.NewsActivity;
import com.mambure.newsAssistant.newsActivity.NewsArticleListFragment;
import com.mambure.newsAssistant.newsActivity.SavedArticleListFragment;

import dagger.Subcomponent;
@Subcomponent
@ActivityScope
public interface NewsActivityComponent {

    @Subcomponent.Factory
    interface Factory{
        NewsActivityComponent create();
    }

    void inject(NewsActivity activity);
    void inject(SavedArticleListFragment fragment);

    void inject(NewsArticleListFragment fragment);
}
