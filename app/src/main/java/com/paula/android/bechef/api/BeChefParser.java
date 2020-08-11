package com.paula.android.bechef.api;

import com.paula.android.bechef.api.beans.GetSearchList;
import com.paula.android.bechef.api.exceptions.NoResourceException;
import com.paula.android.bechef.api.exceptions.YoutubeException;
import com.paula.android.bechef.data.SearchItem;
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
        for (int i = 0; i < data.length(); i++) {
            beanGetSearchList.getSearchItems().add(parseSearchItem(data.getJSONObject(i)));
        }
        return beanGetSearchList;
    }

    private static SearchItem parseSearchItem(JSONObject jsonObject) {
        SearchItem searchItem = new SearchItem();
        switch (getDataString(jsonObject, "kind")) {
            case "youtube#video": // api type: video
                searchItem.setId(getDataString(jsonObject, "id"));
                break;
            case "youtube#searchResult": // api type: search
                try {
                    searchItem.setId(getDataString(jsonObject.getJSONObject("id"), "videoId"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                return searchItem;
        }
        // Get snippet data
        JSONObject jsonSnippet;
        try {
            jsonSnippet = jsonObject.getJSONObject("snippet");
        } catch (JSONException e) {
            e.printStackTrace();
            return searchItem;
        }
        searchItem.setPublishedAt(getDataString(jsonSnippet, "publishedAt"));
        searchItem.setTitle(getDataString(jsonSnippet, "title"));
        searchItem.setDescription(getDataString(jsonSnippet, "description"));
        try {
            searchItem.setThumbnailMediumUrl(jsonSnippet.getJSONObject("thumbnails")
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
            searchItem.setTags(tags.toString());
        }

        // Get contentDetails data
        JSONObject jsonContentDetails;
        try {
            jsonContentDetails = jsonObject.getJSONObject("contentDetails");
        } catch (JSONException e) {
            e.printStackTrace();
            return searchItem;
        }
        searchItem.setDuration(getDataString(jsonContentDetails, "duration"));

        // Get statistics data
        JSONObject jsonStatistics;
        try {
            jsonStatistics = jsonObject.getJSONObject("statistics");
        } catch (JSONException e) {
            e.printStackTrace();
            return searchItem;
        }
        searchItem.setViewCount(getDataString(jsonStatistics, "viewCount"));
        return searchItem;
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
