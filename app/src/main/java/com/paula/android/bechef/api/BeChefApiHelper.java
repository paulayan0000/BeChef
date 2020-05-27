package com.paula.android.bechef.api;

import com.paula.android.bechef.api.beans.GetSearchList;
import com.paula.android.bechef.api.exceptions.YoutubeException;

import org.json.JSONException;

import java.io.IOException;
import java.util.Map;

public class BeChefApiHelper {

    public static GetSearchList GetVideoListInChannel(Map<String, String> queryParameters) throws IOException, JSONException, YoutubeException {

        return BeChefParser.parseGetSearchList(new BeChefClient().get("search", queryParameters));
    }
}
