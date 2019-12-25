package com.mambure.newsassistant.dependencyInjection;

import com.mambure.newsassistant.newsActivity.NewsActivity;
import com.mambure.newsassistant.newsActivity.NewsFragment;
import com.mambure.newsassistant.wakthroughActivity.WalkThroughActivity;
import com.mambure.newsassistant.wakthroughActivity.WalkthroughCategorySelectionFragment;
import com.mambure.newsassistant.wakthroughActivity.WalkthroughLanguageSelectionFragment;
import com.mambure.newsassistant.wakthroughActivity.WalkthroughSourcesSelectionFragment;

import javax.inject.Singleton;

import dagger.Component;
@Singleton
@Component(modules = {AppModule.class, ViewModelsModule.class})
public interface AppComponent {
    void inject(WalkThroughActivity activity);
    void inject(NewsActivity activity);
    void inject(WalkthroughCategorySelectionFragment fragment);
    void inject(WalkthroughSourcesSelectionFragment fragment);
    void inject(WalkthroughLanguageSelectionFragment fragment);
    void inject(NewsFragment fragment);
}
