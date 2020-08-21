package com.paula.android.bechef.api;

import com.paula.android.bechef.api.beans.GetSearchList;
import com.paula.android.bechef.api.exceptions.NoResourceException;
import com.paula.android.bechef.api.exceptions.YoutubeException;
import com.paula.android.bechef.data.DiscoverItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class BeChefParser {

    static GetSearchList parseGetSearchListFromSearch(String jsonString) throws JSONException, YoutubeException, NoResourceException {
        GetSearchList beanGetSearchList = new GetSearchList();
        JSONObject obj = new JSONObject(jsonString);
        if (obj.has("error")) {
            throw new YoutubeException("YoutubeException: "
                    + getDataString(obj.getJSONObject("error"), "message"));
        }

        beanGetSearchList.setNextPageToken(obj
                .has("nextPageToken") ? getDataString(obj, "nextPageToken") : "");
        beanGetSearchList.setPrevPageToken(obj
                .has("prevPageToken") ? getDataString(obj, "prevPageToken") : "");

        JSONArray data = obj.getJSONArray("items");
        if (data.isNull(0)) {
            throw new NoResourceException("NoResourceException: No result, response: " + jsonString);
        }
        DiscoverItem parsedItem;
        for (int i = 0; i < data.length(); i++) {
            parsedItem = parseSearchItem(data.getJSONObject(i));
            if (parsedItem != null) beanGetSearchList.getDiscoverItems().add(parsedItem);
        }
        return beanGetSearchList;
    }

    private static DiscoverItem parseSearchItem(JSONObject jsonObject) {
        DiscoverItem discoverItem = new DiscoverItem();
        switch (getDataString(jsonObject, "kind")) {
            case "youtube#video": // api type: video
                discoverItem.setId(getDataString(jsonObject, "id"));
                break;
            case "youtube#searchResult": // api type: search
                try {
                    discoverItem.setId(jsonObject.getJSONObject("id").getString("videoId"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
                break;
            default:
                return discoverItem;
        }
        // Get snippet data
        JSONObject jsonSnippet;
        try {
            jsonSnippet = jsonObject.getJSONObject("snippet");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        discoverItem.setPublishedAt(getDataString(jsonSnippet, "publishedAt"));
        discoverItem.setTitle(getDataString(jsonSnippet, "title"));
        discoverItem.setDescription(getDataString(jsonSnippet, "description"));
        try {
            discoverItem.setImageUrl(jsonSnippet.getJSONObject("thumbnails")
                    .getJSONObject("medium").getString("url"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonSnippet.has("tags")) {
            JSONArray jsonTags;
            StringBuilder tags = new StringBuilder();
            try {
                jsonTags = jsonSnippet.getJSONArray("tags");
                int jsonTagsLength = jsonTags.length();
                for (int i = 0; i < jsonTagsLength - 1; i++) {
                    tags.append(jsonTags.get(i)).append(";");
                }
                tags.append(jsonTags.get(jsonTagsLength - 1));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            discoverItem.setTags(tags.toString());
        }

        // Get contentDetails data
        JSONObject jsonContentDetails;
        try {
            jsonContentDetails = jsonObject.getJSONObject("contentDetails");
        } catch (JSONException e) {
            e.printStackTrace();
            return discoverItem;
        }
        discoverItem.setDuration(getDataString(jsonContentDetails, "duration"));

        // Get statistics data
        JSONObject jsonStatistics;
        try {
            jsonStatistics = jsonObject.getJSONObject("statistics");
        } catch (JSONException e) {
            e.printStackTrace();
            return discoverItem;
        }
        discoverItem.setViewCount(getDataString(jsonStatistics, "viewCount"));
        return discoverItem;
    }

    private static String getDataString(JSONObject jsonObject, String key) {
        String result = "";
        try {
            result = jsonObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
