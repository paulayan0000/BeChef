package com.paula.android.bechef.api;

import android.util.Log;
import java.io.IOException;
import java.util.Map;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

class BeChefClient {
    private static final String LOG_TAG = BeChefClient.class.getSimpleName();
    private static final String KEY = "AIzaSyAajGSZR9eHL_IKeV34fd_nN58tYUVf5FQ";

    private HttpUrl.Builder mBasicHttpUrlBuilder;
    private OkHttpClient mOkHttpClient;

    BeChefClient() {
        mBasicHttpUrlBuilder = new HttpUrl.Builder().scheme("https")
                .host("www.googleapis.com")
                .addPathSegment("youtube")
                .addPathSegment("v3")
                .addQueryParameter("key", KEY);
        mOkHttpClient = new OkHttpClient();
    }

    String get(String apiTypeName, Map<String, String> queryParameters) throws IOException {
        HttpUrl.Builder httpUrl = mBasicHttpUrlBuilder
                .addPathSegment(apiTypeName);
        for (Map.Entry<String, String> entry : queryParameters.entrySet()) {
            httpUrl.addQueryParameter(entry.getKey(), entry.getValue());
        }
        Request request = new Request.Builder()
                .url(httpUrl.build())
                .build();
        Response response = mOkHttpClient.newCall(request).execute();

        return doResponse(response);
    }

    private String doResponse(Response response) throws IOException {
        Log.d(LOG_TAG, "Response Code: " + response.code());
        ResponseBody responseBody = response.body();
        if (responseBody == null) throw new NullPointerException("Null response body");
        if (response.isSuccessful()) {
            String responseData = responseBody.string();
//            Log.d(LOG_TAG, "Response Data: " + responseData);
            return responseData;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }
}
