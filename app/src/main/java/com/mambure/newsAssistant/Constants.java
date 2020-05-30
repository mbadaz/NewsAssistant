package com.mambure.newsAssistant;

public class Constants {
    public static final String BASE_URL = "https://newsapi.org/v2/";
    public static final String SOURCES_API_ENDPOINT = "sources";
    public static final String HEADLINES_API_ENDPOINT = "top-headlines";
    public static final String EVERYTHING_API_ENDPOINT = "everything";
    public static final String LOCAL = "local";
    public static final String REMOTE = "remote";
    public static final String SHARED_PREFERENCES = "shared preferences";
    public static final String SOURCES = "sources";
    public static final String RESULT_OK = "ok";
    public static final String RESULT_ERROR = "error";

    public static class SharedPrefsKeys{
        public static final String IS_FIRST_RUN = "is first run";
    }

    public enum FragmentId {
        NEW_ARTICLES,
        SAVED_ARTICLES
    }

    public enum Result {
        OK,
        NO_DATA,
        ERROR
    }

}
