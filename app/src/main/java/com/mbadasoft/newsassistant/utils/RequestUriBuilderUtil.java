package com.mbadasoft.newsassistant.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.apache.http.client.utils.URIBuilder;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static com.mbadasoft.newsassistant.data.Constants.*;


public class RequestUriBuilderUtil{

    public static List<String> buildUris(@NonNull String path, @Nullable Map<String, Object> args) throws URISyntaxException {
        ArrayList<URI> uris = new ArrayList<>();

        URIBuilder builder1 = new URIBuilder();
        builder1.setPath(BASE_URL + path);
        uris.add(builder1.build());

        if (args == null) {
            return Collections.singletonList(builder1.build().toString());
        }

        Set<String> queryParams = args.keySet();


        // TODO remove code specific to the news.org API's requirements to make class generally applicable
        if (queryParams.contains(SOURCES_QUERY_PARAM)) {
            uris = createUrisFromQueryArgs(
                    SOURCES_QUERY_PARAM,
                    createQueryParamList((Set<String>) args.get(SOURCES_QUERY_PARAM)),
                    uris
            );
            queryParams.remove(SOURCES_QUERY_PARAM);

            for (String param : queryParams) {
                if (!param.equals(CATEGORY_QUERY_PARAM) && !param.equals(COUNTRY_QUERY_PARAM)) {
                    uris = createUrisFromQueryArgs(param, args.get(param), uris);
                }
            }
        }

        ArrayList<URI> tempUrisArray = new ArrayList<>();
        tempUrisArray.add(builder1.build());

        for (String param : queryParams) {
            tempUrisArray = createUrisFromQueryArgs(param, args.get(param), tempUrisArray);
        }

        uris.addAll(tempUrisArray);


        ArrayList<String> uriStrings = new ArrayList<>();
        for (URI uri: uris) {
            uriStrings.add(uri.toString());
        }

        return uriStrings;
    }

    private static String createQueryParamList(Set<String> args) {
        StringBuilder builder = new StringBuilder();
        int size = args.size();
        int counter = 0;
        for (String source : args) {
            counter++;
            if (counter == size){
                builder.append(source);
            } else {
                builder.append(source).append(",");
            }
        }
        return builder.toString();
    }

    private static ArrayList<URI> createUrisFromQueryArgs(String queryParam, Object paramValue, ArrayList<URI> uris) throws URISyntaxException {
        ArrayList<URI> newUris = new ArrayList<>();
        for (URI uri : uris) {
            if (paramValue instanceof Set) {
                for (String value : (Set<String>) paramValue) {
                    URIBuilder builder = new URIBuilder(uri);
                    builder.addParameter(queryParam, value);
                    newUris.add(builder.build());
                }
            } else {
                URIBuilder builder = new URIBuilder(uri);
                builder.addParameter(queryParam, paramValue.toString());
                newUris.add(builder.build());
            }

        }
        return newUris;
    }
}
