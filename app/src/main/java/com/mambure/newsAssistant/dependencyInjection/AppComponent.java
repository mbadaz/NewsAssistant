package com.mambure.newsAssistant.dependencyInjection;

import com.mambure.newsAssistant.newsActivity.NewsActivity;
import com.mambure.newsAssistant.newsActivity.NewsFragment;
import com.mambure.newsAssistant.wakthroughActivity.WalkThroughActivity;
import com.mambure.newsAssistant.wakthroughActivity.WalkthroughSourcesFragment;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class, NetworkModule.class, DatabaseModule.class})
@Singleton
public interface AppComponent {

    void inject(WalkThroughActivity activity);

    void inject(NewsActivity activity);

    void inject(WalkthroughSourcesFragment fragment);

    void inject(NewsFragment fragment);

}
