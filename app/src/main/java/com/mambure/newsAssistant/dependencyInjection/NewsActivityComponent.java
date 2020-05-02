package com.mambure.newsAssistant.dependencyInjection;

import com.mambure.newsAssistant.newsActivity.NewsActivity;
import com.mambure.newsAssistant.newsActivity.NewsListFragment;

import dagger.Subcomponent;
@Subcomponent
@ActivityScope
public interface NewsActivityComponent {

    @Subcomponent.Factory
    interface Factory{
        NewsActivityComponent create();
    }

    void inject(NewsActivity activity);
    void inject(NewsListFragment fragment);
}
