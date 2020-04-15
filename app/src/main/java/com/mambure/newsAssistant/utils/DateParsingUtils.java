package com.mambure.newsAssistant.utils;

public class DateParsingUtils {

    public static String parseDate(String date) {
        if (!date.isEmpty()) {
            return date.substring(0, 10);
        }else {
            return "";
        }
    }
}
