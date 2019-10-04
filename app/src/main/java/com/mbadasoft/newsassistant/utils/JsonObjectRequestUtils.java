package com.mbadasoft.newsassistant.utils;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JsonObjectRequestUtils {

    public static JsonObjectRequest createJsonObjectRequest(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){
        return new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                listener,
                errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-Api-Key", "df66e036a5e14417b37c2cb31d5034f3");
                return headers;
            }
        };
    }
}
