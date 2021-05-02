package com.paula.android.bechef.api;

import com.paula.android.bechef.api.beans.YouTubeData;
import com.paula.android.bechef.api.exceptions.NoResourceException;
import com.paula.android.bechef.api.exceptions.YoutubeException;
import com.paula.android.bechef.data.entity.DiscoverItem;
import com.paula.android.bechef.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class BeChefParser {
    static YouTubeData parseGetSearchListFromSearch(String jsonString) throws JSONException, YoutubeException, NoResourceException {
        YouTubeData beanYouTubeData = new YouTubeData();
        JSONObject obj = new JSONObject(jsonString);

        // Set errorMsg
        if (obj.has("error")) {
            throw new YoutubeException("YoutubeException: " +
                    getDataString(obj.getJSONObject("error"), "message"));
        } else {
            beanYouTubeData.setErrorMsg("");
        }

        // Set pageTokens
        beanYouTubeData.setNextPageToken(obj.has("nextPageToken") ?
                getDataString(obj, "nextPageToken") : "");
        beanYouTubeData.setPrevPageToken(obj.has("prevPageToken") ?
                getDataString(obj, "prevPageToken") : "");

        // Set items
        JSONArray data = obj.getJSONArray("items");
        if (data.isNull(0)) {
            throw new NoResourceException("NoResourceException: No result, response: " + jsonString);
        }
        DiscoverItem parsedItem;
        for (int i = 0; i < data.length(); i++) {
            parsedItem = parseSearchItem(data.getJSONObject(i));
            if (parsedItem != null) beanYouTubeData.getDiscoverItems().add(parsedItem);
        }
        return beanYouTubeData;
    }

    private static DiscoverItem parseSearchItem(JSONObject jsonObject) {
        DiscoverItem discoverItem = new DiscoverItem();
        // Set id according to kind data
        switch (getDataString(jsonObject, "kind")) {
            case "youtube#video": // api type: video
                discoverItem.setVideoId(getDataString(jsonObject, Constants.JSON_KEY_ID));
                break;
            case "youtube#searchResult": // api type: search (video or channel)
                try {
                    String idKind = jsonObject.getJSONObject(Constants.JSON_KEY_ID).getString("kind");
                    if ("youtube#video".equals(idKind)) {
                        discoverItem.setVideoId(jsonObject.getJSONObject(Constants.JSON_KEY_ID)
                                .getString("videoId"));
                    }
                } catch (JSONException e) {
                    return null;
                }
                break;
            case "youtube#channel":
                discoverItem.setChannelId(getDataString(jsonObject, Constants.JSON_KEY_ID));
                break;
            default:
                return discoverItem;
        }

        // Set snippet data
        JSONObject jsonSnippet;
        try {
            jsonSnippet = jsonObject.getJSONObject("snippet");
        } catch (JSONException e) {
            return null;
        }
        discoverItem.setPublishedAt(getDataString(jsonSnippet, "publishedAt"));
        discoverItem.setChannelId(getDataString(jsonSnippet, "channelId"));
        discoverItem.setTitle(getDataString(jsonSnippet, "title"));
        discoverItem.setDescription(getDataString(jsonSnippet, "description"));
        try {
            discoverItem.setImageUrl(jsonSnippet.getJSONObject("thumbnails")
                    .getJSONObject("medium").getString("url"));
        } catch (JSONException e) {
//            e.printStackTrace();
        }
        if (jsonSnippet.has("tags")) {
            JSONArray jsonTags;
            StringBuilder tags = new StringBuilder();
            try {
                jsonTags = jsonSnippet.getJSONArray("tags");
                int jsonTagsLength = jsonTags.length();
                for (int i = 0; i < jsonTagsLength - 1; i++) tags.append(jsonTags.get(i)).append(";");
                tags.append(jsonTags.get(jsonTagsLength - 1));
            } catch (JSONException e) {
//                e.printStackTrace();
            }
            discoverItem.setTags(tags.toString());
        }

        // Set contentDetails data
        JSONObject jsonContentDetails;
        try {
            jsonContentDetails = jsonObject.getJSONObject("contentDetails");
        } catch (JSONException e) {
            return discoverItem;
        }
        discoverItem.setDuration(getDataString(jsonContentDetails, "duration"));

        // Set statistics data
        JSONObject jsonStatistics;
        try {
            jsonStatistics = jsonObject.getJSONObject("statistics");
        } catch (JSONException e) {
            return discoverItem;
        }
        discoverItem.setViewCount(getDataString(jsonStatistics, "viewCount"));
        discoverItem.setSubscribeCount(getDataString(jsonStatistics, "subscriberCount"));
        return discoverItem;
    }

    private static String getDataString(JSONObject jsonObject, String key) {
        try {
            return jsonObject.getString(key);
        } catch (JSONException e) {
            return "";
        }
    }
}
