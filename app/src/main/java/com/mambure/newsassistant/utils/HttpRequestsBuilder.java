package com.mambure.newsassistant.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.mambure.newsassistant.models.Source;

import org.apache.hc.core5.net.URIBuilder;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static com.mambure.newsassistant.data.Constants.*;


public class HttpRequestsBuilder {

    public static List<String> createRequests(@NonNull String path, @Nullable Map<String, Object> args) throws URISyntaxException {
        ArrayList<URI> uris = new ArrayList<>();
        URIBuilder builder1 = new URIBuilder();
        builder1.setScheme("https");
        builder1.setHost(BASE_URL);
        builder1.setPath(path);

        uris.add(builder1.build());

        if (args == null) {
            return Collections.singletonList(builder1.build().toString());
        }

        Set<String> queryParams = args.keySet();


        // TODO remove code specific to the news.org API's requirements to make class generally applicable
        if (queryParams.contains(SOURCES_QUERY_PARAM)) {
            List<Source> sources = (List<Source>) args.get(SOURCES_QUERY_PARAM);
            Set<String> sourcesSet = new HashSet<>();
            for (Source source: sources) {
                sourcesSet.add(source.id);
            }
            uris = createUrisFromQueryArgs(
                    SOURCES_QUERY_PARAM,
                    createQueryParamList(sourcesSet),
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
