package com.mambure.newsassistant.models;

import androidx.annotation.Nullable;

import io.realm.RealmObject;

public class Article extends RealmObject {


    public Source source;
    public String author;
    public String title;
    public String description;
    public String url;
    public String urlToImage;
    public String publishedAt;

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return obj instanceof Article && description.equals(((Article) obj).description)
                && title.equals(((Article) obj).title);
    }
}
