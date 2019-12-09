package com.mbadasoft.newsassistant.dependencyInjection;

import com.mbadasoft.newsassistant.newsActivity.NewsActivity;
import com.mbadasoft.newsassistant.newsActivity.NewsFragment;
import com.mbadasoft.newsassistant.wakthroughActivity.WalkThroughActivity;
import com.mbadasoft.newsassistant.wakthroughActivity.WalkthroughCategorySelectionFragment;
import com.mbadasoft.newsassistant.wakthroughActivity.WalkthroughSourcesSelectionFragment;

import javax.inject.Singleton;

import dagger.Component;
@Singleton
@Component(modules = {AppModule.class, ViewModelsModule.class})
public interface AppComponent {
    void inject(WalkThroughActivity activity);
    void inject(NewsActivity activity);

    void inject(WalkthroughCategorySelectionFragment fragment);
    void inject(WalkthroughSourcesSelectionFragment fragment);

    void inject(NewsFragment fragment);
}
