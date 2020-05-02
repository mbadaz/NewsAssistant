package com.mambure.newsAssistant;

import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.idling.CountingIdlingResource;

public class MyIdlingResource implements IdlingResource {

    private static final String APP_IDLING_RESOURCE = "APP IDLING RESOURCE";
    private static MyIdlingResource myIdlingResource;

    private MyIdlingResource() {
    }

    public static MyIdlingResource getInstance() {
        if (myIdlingResource == null) {
            myIdlingResource = new MyIdlingResource();
            return myIdlingResource;
        }
        return myIdlingResource;
    }

    public void countDown() {
        // NO OP
    }

    public void increment() {
        // NO OPP
    }

    @Override
    public String getName() {
        return APP_IDLING_RESOURCE;
    }

    @Override
    public boolean isIdleNow() {
        return false;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        // NO OP
    }
}
