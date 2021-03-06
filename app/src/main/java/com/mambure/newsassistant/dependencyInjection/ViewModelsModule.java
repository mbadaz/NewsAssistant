package com.mambure.newsassistant.dependencyInjection;

import androidx.lifecycle.ViewModel;

import com.mambure.newsassistant.data.DefaultRepository;
import com.mambure.newsassistant.newsActivity.NewsActivityViewModel;
import com.mambure.newsassistant.wakthroughActivity.WalkthroughActivityViewModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

import javax.inject.Provider;
import javax.inject.Singleton;

import dagger.MapKey;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module(includes = DataRepositoryModule.class)
public class ViewModelsModule {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @MapKey
    @interface ViewModelKey{
        Class<? extends ViewModel> value();
    }

    @Provides
    @Singleton
    @IntoMap
    @ViewModelKey(WalkthroughActivityViewModel.class)
    ViewModel provideWalkhroughVieModel(DefaultRepository dataDefaultRepository) {
        return new WalkthroughActivityViewModel(dataDefaultRepository);
    }

    @Provides
    @Singleton
    @IntoMap
    @ViewModelKey(NewsActivityViewModel.class)
    ViewModel provideNewsActivityViewModel(DefaultRepository dataDefaultRepository) {
        return new NewsActivityViewModel(dataDefaultRepository);
    }

    @Provides
    @Singleton
    ViewModelsFactory provideViewModelFactory(
            Map<Class<? extends ViewModel>, Provider<ViewModel>> providers) {
        return new ViewModelsFactory(providers);
    }

}
