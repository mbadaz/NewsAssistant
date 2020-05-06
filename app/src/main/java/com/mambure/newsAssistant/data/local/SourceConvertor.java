package com.mambure.newsAssistant.data.local;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.mambure.newsAssistant.data.models.Source;

public class SourceConvertor {

    @TypeConverter
    public static String convert(Source source){
        return source.name;
    }

    @TypeConverter
    public static Source convertToSource(String name) {
        Source source = new Source();
        source.name = name;
        return source;
    }
}
