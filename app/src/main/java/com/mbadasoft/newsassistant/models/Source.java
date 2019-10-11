package com.mbadasoft.newsassistant.models;

import androidx.annotation.Nullable;

import java.util.Objects;

public class Source {
    public String id;
    public String name;
    public String description;
    public String url;
    public String category;
    public String language;
    public String country;

    @Override
    public boolean equals(@Nullable Object obj) {
        return obj instanceof Source && this.id.equals(((Source) obj).id);
    }
}
