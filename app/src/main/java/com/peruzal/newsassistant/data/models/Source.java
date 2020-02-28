package com.peruzal.newsassistant.data.models;

import androidx.annotation.Nullable;

import com.squareup.moshi.Json;

public class Source {
    @Json(name = "id") public String id;
    @Json(name = "name") public String name;
    @Json(name = "description") public String description;
    @Json(name = "url") public String url;
    @Json(name = "category") public String category;
    @Json(name = "language") public String language;
    @Json(name = "country") public String country;

//    private boolean checked = false;

    @Override
    public boolean equals(@Nullable Object obj) {
        return obj instanceof Source && this.id.equals(((Source) obj).id);
    }

//    public void setChecked(boolean checked) {
//        this.checked = checked;
//    }
//
//    public boolean isChecked() {
//        return checked;
//    }
}
