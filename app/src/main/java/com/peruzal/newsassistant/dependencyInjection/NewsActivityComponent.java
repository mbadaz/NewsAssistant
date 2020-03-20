package com.peruzal.newsassistant.dependencyInjection;

import com.peruzal.newsassistant.newsActivity.NewsActivity;

import dagger.Subcomponent;
@Subcomponent
public interface NewsActivityComponent {

    @Subcomponent.Factory
    interface Factory{
        NewsActivityComponent create();
    }

    void inject(NewsActivity activity);
}
