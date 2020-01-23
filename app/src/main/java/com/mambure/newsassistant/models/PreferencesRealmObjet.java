package com.mambure.newsassistant.models;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;

public class PreferencesRealmObjet extends RealmObject {

    public RealmList<Source> preferedSources;
    public RealmList<String> preferedCategories;
    public RealmList<String> preferedLanguages;


}
