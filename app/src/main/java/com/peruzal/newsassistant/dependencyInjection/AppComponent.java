package com.peruzal.newsassistant.dependencyInjection;

import com.peruzal.newsassistant.newsActivity.NewsActivity;
import com.peruzal.newsassistant.newsActivity.NewsFragment;
import com.peruzal.newsassistant.wakthroughActivity.WalkThroughActivity;
import com.peruzal.newsassistant.wakthroughActivity.WalkthroughSourcesFragment;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class, NetworkModule.class, RepositoriesModule.class})
@Singleton
public interface AppComponent {

    void inject(WalkThroughActivity activity);

    void inject(NewsActivity activity);

    void inject(WalkthroughSourcesFragment fragment);

    void inject(NewsFragment fragment);

}
