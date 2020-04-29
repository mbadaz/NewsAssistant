package com.mambure.newsAssistant;

import androidx.test.espresso.idling.CountingIdlingResource;

class MyIdlingResource {

    private static CountingIdlingResource idlingResource;

    static CountingIdlingResource getInstance() {
        if (idlingResource == null) {
            idlingResource = new CountingIdlingResource("APP IDLING RESOURCE");
            return idlingResource;
        }
        return idlingResource;
    }

    private MyIdlingResource() {
    }
}
