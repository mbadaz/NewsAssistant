package com.mambure.newsAssistant;

import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.idling.CountingIdlingResource;

public class MyIdlingResource implements IdlingResource {

    private static final String APP_IDLING_RESOURCE = "APP IDLING RESOURCE";
    private CountingIdlingResource countingIdlingResource;
    private static MyIdlingResource myIdlingResource;

    private MyIdlingResource() {
        countingIdlingResource = new CountingIdlingResource(APP_IDLING_RESOURCE);
    }
    public static MyIdlingResource getInstance() {
        if (myIdlingResource == null) {
            myIdlingResource = new MyIdlingResource();
            return myIdlingResource;
        }
        return myIdlingResource;
    }

    public void countDown() {
        countingIdlingResource.decrement();
    }

    public void increment() {
        countingIdlingResource.increment();
    }

    @Override
    public String getName() {
        return APP_IDLING_RESOURCE;
    }

    @Override
    public boolean isIdleNow() {
        return countingIdlingResource.isIdleNow();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        countingIdlingResource.registerIdleTransitionCallback(callback);
    }
}
