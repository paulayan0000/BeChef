package com.paula.android.bechef.api;

import com.paula.android.bechef.api.beans.YouTubeData;
import com.paula.android.bechef.api.exceptions.NoResourceException;
import com.paula.android.bechef.api.exceptions.YoutubeException;

import java.io.IOException;
import java.util.Map;

import org.json.JSONException;

public class BeChefApiHelper {
    public static YouTubeData GetYoutubeData(Map<String, String> queryParameters, String apiType)
            throws IOException, JSONException, YoutubeException, NoResourceException {
        return BeChefParser.parseGetSearchListFromSearch(
                new BeChefClient().get(apiType, queryParameters));
    }
}
