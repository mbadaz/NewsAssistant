package com.mambure.newsassistant;

import com.mambure.newsassistant.utils.HttpRequestsBuilder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.mambure.newsassistant.data.Constants.CATEGORY_QUERY_PARAM;
import static com.mambure.newsassistant.data.Constants.HEADLINES_API_ENDPOINT;
import static com.mambure.newsassistant.data.Constants.KEYWORD_QUERY_PARAM;
import static com.mambure.newsassistant.data.Constants.PAGE_SIZE_QUERY_PARAM;
import static com.mambure.newsassistant.data.Constants.SOURCES_QUERY_PARAM;

public class HttpRequestUtilTest {
    private List<String> uris = new ArrayList<>();

    private static Map<String, Object> PARAMS = new HashMap<>();

    private static Set<String> CATEGORY_PARAM_VALUES = new HashSet<>();
    private static final String CATEGORY_PARAM_VALUE_1 = "business";
    private static final String CATEGORY_PARAM_VALUE_2 = "technology";
    private static final String CATEGORY_PARAM_VALUE_3 = "sport";

    private static Set<String> KEYWORDS_PARAM_VALUES = new HashSet<>();
    private static final String KEYWORDS_PARAM_VALUE_1 = "Donald Trump";
    private static final String KEYWORDS_PARAM_VALUE_2 = "Candy pies";
    private static final String KEYWORDS_PARAM_VALUE_3 = "Aliens from mars";

    private static Set<String> SOURCES_PARAM_VALUES = new HashSet<>();
    private static final String SOURCES_PARAM_VALUE_1 = "cnn";
    private static final String SOURCES_PARAM_VALUE_2 = "al-jazeera";
    private static final String SOURCES_PARAM_VALUE_3 = "bbc";

    private static final int PAGE_SIZE = 10;






    @Before
    public void initialize() {

        CATEGORY_PARAM_VALUES.add(CATEGORY_PARAM_VALUE_1);
        CATEGORY_PARAM_VALUES.add(CATEGORY_PARAM_VALUE_2);
        CATEGORY_PARAM_VALUES.add(CATEGORY_PARAM_VALUE_3);

        KEYWORDS_PARAM_VALUES.add(KEYWORDS_PARAM_VALUE_1);
        KEYWORDS_PARAM_VALUES.add(KEYWORDS_PARAM_VALUE_2);
        KEYWORDS_PARAM_VALUES.add(KEYWORDS_PARAM_VALUE_3);

        SOURCES_PARAM_VALUES.add(SOURCES_PARAM_VALUE_1);
        SOURCES_PARAM_VALUES.add(SOURCES_PARAM_VALUE_2);
        SOURCES_PARAM_VALUES.add(SOURCES_PARAM_VALUE_3);

        PARAMS.put(SOURCES_QUERY_PARAM, SOURCES_PARAM_VALUES);
        PARAMS.put(CATEGORY_QUERY_PARAM, CATEGORY_PARAM_VALUES);
        PARAMS.put(KEYWORD_QUERY_PARAM, KEYWORDS_PARAM_VALUES);
        PARAMS.put(PAGE_SIZE_QUERY_PARAM, PAGE_SIZE);


    }

    @Test
    public void createUrisFromQueryArgsTest() {
        try {
            uris = HttpRequestsBuilder.createRequests(
                    HEADLINES_API_ENDPOINT,
                    PARAMS
            );
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(12, uris.size());

    }
}
