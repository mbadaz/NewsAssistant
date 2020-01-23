package com.mambure.newsassistant.models;

import io.realm.RealmList;
import io.realm.RealmObject;

public class RealmWrapper extends RealmObject {
    public RealmList<String> object;
    public String id;
}
