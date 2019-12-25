package com.mambure.newsassistant.utils;

public class DateParsingUtils {

    public static String parseDate(String date) {
        if (!date.isEmpty()) {
            return date.substring(0, 10);
        }else {
            return "";
        }
    }
}
