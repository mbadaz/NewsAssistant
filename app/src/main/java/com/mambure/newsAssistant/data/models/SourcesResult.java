package com.mambure.newsAssistant.data.models;

import androidx.annotation.NonNull;

import com.mambure.newsAssistant.Constants;

import java.util.List;

public class SourcesResult {
    public String status;
    public List<Source> sources;
    public Constants.Result result;

    @NonNull
    @Override
    public String toString() {
        return sources.toString();

    }
}
