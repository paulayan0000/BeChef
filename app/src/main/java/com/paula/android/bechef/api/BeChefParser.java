package com.paula.android.bechef.api;

import com.paula.android.bechef.api.beans.GetSearchList;
import com.paula.android.bechef.api.exceptions.YoutubeException;
import com.paula.android.bechef.objects.SearchItem;
import com.paula.android.bechef.objects.Snippet;
import com.paula.android.bechef.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class BeChefParser {

    static GetSearchList parseGetSearchList(String jsonString) throws JSONException, YoutubeException {
        GetSearchList beanGetSearchList = new GetSearchList();

        beanGetSearchList.setViewType(Constants.VIEWTYPE_DISCOVER);
        JSONObject obj = new JSONObject(jsonString);
        beanGetSearchList.setNextPageToken(obj.has("nextPageToken") ? obj.getString("nextPageToken") : "");
        beanGetSearchList.setPrevPageToken(obj.has("prevPageToken") ? obj.getString("prevPageToken") : "");
        JSONArray data = obj.getJSONArray("items");
        for (int i = 0; i < data.length(); i++) {
            beanGetSearchList.getSearchItems().add(parseSearchItem(data.getJSONObject(i)));
        }
        if (obj.has("error")) {
            throw new YoutubeException(obj.getJSONObject("error").getString("message"));
        }

        return beanGetSearchList;
    }

    private static SearchItem parseSearchItem(JSONObject jsonObject) {
        SearchItem searchItem = new SearchItem();
        try {
            JSONObject jsonId = jsonObject.getJSONObject("id");
            searchItem.setId((jsonId.has("videoId") ? jsonId.getString("videoId")
                    : jsonId.getString("channelId")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            searchItem.setSnippet(parseSnippet(jsonObject.getJSONObject("snippet")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return searchItem;
    }

    private static Snippet parseSnippet(JSONObject jsonObject) {
        Snippet snippet = new Snippet();
        try {
            snippet.setPublishedAt(jsonObject.getString("publishedAt"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            snippet.setTitle(jsonObject.getString("title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            snippet.setDescription(jsonObject.getString("description"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            snippet.setThumbnailMediumUrl(jsonObject.getJSONObject("thumbnails").getJSONObject("medium").getString("url"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return snippet;
    }
}
