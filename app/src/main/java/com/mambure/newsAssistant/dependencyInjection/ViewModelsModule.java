package com.mambure.newsAssistant.dependencyInjection;

import androidx.lifecycle.ViewModel;

import com.mambure.newsAssistant.newsActivity.NewsActivityViewModel;
import com.mambure.newsAssistant.wakthroughActivity.WalkthroughActivityViewModel;

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

@Module
public class ViewModelsModule {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @MapKey
    @interface ViewModelKey {
        Class<? extends ViewModel> value();
    }

    @Provides
    @Singleton
    ViewModelsFactory providesViewModelsFactory(Map<Class<?extends ViewModel>, Provider<ViewModel>> providerMap) {
        return new ViewModelsFactory(providerMap);
    }

    @Provides
    @IntoMap
    @ViewModelKey(WalkthroughActivityViewModel.class)
    ViewModel providesWalkthroughViewModel(WalkthroughActivityViewModel viewModel) {
        return viewModel;
    }

    @Provides
    @IntoMap
    @ViewModelKey(NewsActivityViewModel.class)
    ViewModel providesNewsActivityViewModel(NewsActivityViewModel viewModel) {
        return viewModel;
    }
}
