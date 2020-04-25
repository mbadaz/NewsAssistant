package com.mambure.newsAssistant.utils;

import com.mambure.newsAssistant.data.models.Source;

import java.util.List;

public class ParsingUtils {

    public static String createStringList(List<Source> sources) {
        final StringBuilder sb = new StringBuilder();
        int items = sources.size();

        for (Source source:sources) {
            items--;
            if (items == 0) {
                sb.append(source.id);
            }else {
                sb.append(source.id).append(",");
            }
        }
        return sb.toString();
    }
}
