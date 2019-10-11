package com.mbadasoft.newsassistant.utils;

import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mbadasoft.newsassistant.data.ApiInfo;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JsonObjectRequestUtils {

    private static final String TAG = JsonObjectRequestUtils.class.getSimpleName();

    public static JsonObjectRequest createJsonObjectRequest
            (String path, Map<String, ?> args, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        Uri.Builder builder =  Uri.parse("https://newsapi.org")
                .buildUpon()
                .appendPath("v2")
                .appendPath(path);

        if (ApiInfo.HEADLINES_API_ENDPOINT.equals(path)) {
            builder.appendQueryParameter("sources", buildArgsArray((Set<String>)args.get("Sources")) + "");

        } else if (ApiInfo.EVERYTHING_API_ENDPOINT.equals(path)) {
            builder.appendQueryParameter("qInTitle", "Arsenal");
        }

        Uri uri = builder.build();
        Log.d(TAG, uri.toString());

        return new JsonObjectRequest(Request.Method.GET,
                uri.toString(),
                null,
                listener,
                errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-Api-Key", "df66e036a5e14417b37c2cb31d5034f3");
                return headers;
            }
        };
    }

    static String buildArgsArray(Set<String> args) {
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
}
