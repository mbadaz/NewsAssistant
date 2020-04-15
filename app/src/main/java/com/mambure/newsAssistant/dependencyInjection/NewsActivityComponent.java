package com.mambure.newsAssistant.dependencyInjection;

import com.mambure.newsAssistant.newsActivity.NewsActivity;

import dagger.Subcomponent;
@Subcomponent
public interface NewsActivityComponent {

    @Subcomponent.Factory
    interface Factory{
        NewsActivityComponent create();
    }

    void inject(NewsActivity activity);
}
