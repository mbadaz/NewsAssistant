package com.peruzal.newsassistant.data;

import com.peruzal.newsassistant.R;

public interface DataSource<T> {
    T getDataStream();
}
