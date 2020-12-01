package com.paula.android.bechef.utils;

public class Constants {
    // Activity
    public static final int LOGIN_ACTIVITY = 0x01;

    // Status
    public static final int SIGN_IN_REQUSET_CODE = 0x10;
    public static final int LOGIN_SUCCESS = 0x11;
    public static final int LOGIN_EXIT = 0x12;

    // RecyclerView
    public static final int VIEW_TYPE_NORMAL = 0;
    public static final int VIEW_TYPE_LOADING = 1;

    public static final int VIEW_TYPE_IMAGE = -1;
    public static final int VIEW_TYPE_HEAD = 0;
    public static final int VIEW_TYPE_BODY = 1;
    public static final int VIEW_TYPE_FOOT = 2;
    public static final int IMAGE_ITEM_LIMIT = 3;
    public static final int DESCRIPTION_SIZE = 6;

    public static final int VIEW_TYPE_DISCOVER = 0;
    public static final int VIEW_TYPE_BOOKMARK = 1;
    public static final int VIEW_TYPE_RECEIPT = 2;

    // Table name
    public static final String BOOKMARK_ITEM_TABLE = "bookmark-item-table";
    public static final String BOOKMARK_TAB_TABLE = "bookmark-tab-table";
    public static final String DISCOVER_TAB_TABLE = "discover-tab-table";
    public static final String RECEIPT_ITEM_TABLE = "receipt-item-table";
    public static final String RECEIPT_TAB_TABLE = "receipt-tab-table";

    // Filter data type
    public static final int FILTER_WITH_TIME_DESC = 0;
    public static final int FILTER_WITH_TIME_ASC = 1;
    public static final int FILTER_WITH_RATING_DESC = 2;
    public static final int FILTER_WITH_RATING_ASC = 3;

    public static final String DATE_FORMAT = "yyyy年MM月dd日";;
}
