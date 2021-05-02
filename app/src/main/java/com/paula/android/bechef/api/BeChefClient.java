package com.paula.android.bechef.api;

import com.paula.android.bechef.ApiKey;
import com.paula.android.bechef.utils.Constants;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

class BeChefClient {
    String get(String apiType, Map<String, String> queryParameters) throws IOException {
        StringBuilder url = new StringBuilder(String.format(Constants.URL,
                apiType,
                ApiKey.DEVELOPER_KEY));
        for (Map.Entry<String, String> entry : queryParameters.entrySet()) {
            url.append(String.format(Constants.QUERY_PARAMS, entry.getKey(), entry.getValue()));
        }
        Request request = new Request.Builder()
                .url(url.toString())
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .callTimeout(Constants.CALL_TIMEOUT, TimeUnit.SECONDS)
                .build();
        Response response = client.newCall(request).execute();
        if (apiType.equals(Constants.API_SEARCH)) {
            return doResponse(response).replaceAll("&amp;", "&");
        } else {
            return doResponse(response);
        }
    }

    private String doResponse(Response response) throws IOException {
        ResponseBody responseBody = response.body();
        if (responseBody != null && response.isSuccessful()) {
            return responseBody.string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }
}
