package com.mambure.newsAssistant.data.models;

import androidx.annotation.NonNull;

import java.util.List;

public class SourcesResult {
    public String status;
    public List<Source> sources;

    @NonNull
    @Override
    public String toString() {
        return sources.toString();

    }
}
