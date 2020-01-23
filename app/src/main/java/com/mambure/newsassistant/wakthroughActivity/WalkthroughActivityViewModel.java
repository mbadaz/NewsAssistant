package com.mambure.newsassistant.wakthroughActivity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.mambure.newsassistant.data.DefaultRepository;
import com.mambure.newsassistant.models.Tag;
import com.mambure.newsassistant.models.Source;
import com.mambure.newsassistant.models.SourcesResult;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

public class WalkthroughActivityViewModel extends ViewModel {

    private Set<Source> selectedSources = new HashSet<>();
    private Set<String> selectedCategories = new HashSet<>();
    private Set<String> selectedLanguages = new HashSet<>();

    private final DefaultRepository dataDefaultRepository;

    @Inject
    public WalkthroughActivityViewModel(DefaultRepository dataDefaultRepository) {
        this.dataDefaultRepository = dataDefaultRepository;
    }

    LiveData<SourcesResult> getAvailableSources() {
        return dataDefaultRepository.getSources();
    }

    boolean IsFirstTimeLogin() {
        return dataDefaultRepository.getIsFirstTimeLaunch();
    }

    void setIsFirstTimeLogin(boolean value) {
        dataDefaultRepository.isFirstTimeLaunch(value);
    }

    void saveUserData() {
        dataDefaultRepository.savePreferredSources(selectedSources);
        dataDefaultRepository.savePreferredCategories(selectedCategories);
        dataDefaultRepository.savePreferredLanguages(selectedLanguages);
    }

    void addSourceToSelection(Source source) {
        selectedSources.add(source);
    }

    void removeSourceFromSelection(Source source) {
        selectedSources.remove(source);
    }

    void addCategoryToSelection(Tag tag) {
        selectedCategories.add(tag.name);
    }

    void removeCategoryToSelected(Tag tag) {
        selectedCategories.remove(tag.name);
    }

    void addLanguageToSelection(String language) {
        selectedLanguages.add(language);
    }

    void removeLanguageFromSelection(String language) {
        selectedLanguages.remove(language);
    }

}
