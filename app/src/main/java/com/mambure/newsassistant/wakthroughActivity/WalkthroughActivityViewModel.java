package com.mambure.newsassistant.wakthroughActivity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.mambure.newsassistant.data.DataController;
import com.mambure.newsassistant.models.Tag;
import com.mambure.newsassistant.models.Source;
import com.mambure.newsassistant.models.SourcesResult;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

public class WalkthroughActivityViewModel extends ViewModel {

    private Set<String> selectedSources = new HashSet<>();
    private Set<String> selectedCategories = new HashSet<>();
    private Set<String> selectedLanguages = new HashSet<>();

    private final DataController dataController;

    @Inject
    public WalkthroughActivityViewModel(DataController dataController) {
        this.dataController = dataController;
    }

    LiveData<SourcesResult> getAvailableSources() {
        return dataController.getSources();
    }

    boolean IsFirstTimeLogin() {
        return dataController.IsFirstTimeLogin();
    }

    void setIsFirstTimeLogin(boolean value) {
        dataController.setIsFirstTimeLogin(value);
    }

    void saveUserData() {
        dataController.savePreferredSources(selectedSources);
        dataController.savePreferredCategories(selectedCategories);
        dataController.savePreferredLanguages(selectedLanguages);
    }

    void addSourceToSelection(Source source) {
        selectedSources.add(source.id);
    }

    void removeSourceFromSelection(Source source) {
        selectedSources.remove(source.id);
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
