package com.paula.android.bechef.objects;

public class SearchItem {
    private String mId;
    private Snippet mSnippet;

    public SearchItem() {
        mId = "";
        mSnippet = new Snippet();
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public Snippet getSnippet() {
        return mSnippet;
    }

    public void setSnippet(Snippet snippet) {
        mSnippet = snippet;
    }
}
