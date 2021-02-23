package com.paula.android.bechef.api;

import com.paula.android.bechef.api.beans.GetSearchList;
import com.paula.android.bechef.api.exceptions.NoResourceException;
import com.paula.android.bechef.api.exceptions.YoutubeException;

import org.json.JSONException;

import java.io.IOException;
import java.util.Map;

public class BeChefApiHelper {

    public static GetSearchList SearchYoutubeData(Map<String, String> queryParameters) throws IOException, JSONException, YoutubeException, NoResourceException {
        return BeChefParser.parseGetSearchListFromSearch(new BeChefClient().get("search", queryParameters));
    }

    public static GetSearchList GetYoutubeVideos(Map<String, String> queryParameters) throws IOException, JSONException, YoutubeException, NoResourceException {
        return BeChefParser.parseGetSearchListFromSearch(new BeChefClient().get("videos", queryParameters));
    }

    public static GetSearchList GetYoutubeData(Map<String, String> queryParameters, String apiTypeName) throws IOException, JSONException, YoutubeException, NoResourceException {
        return BeChefParser.parseGetSearchListFromSearch(new BeChefClient().get(apiTypeName, queryParameters));
    }
}
