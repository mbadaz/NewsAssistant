package com.mambure.newsAssistant.data;

public interface DataSource<T> {
    T getDataStream();
}
