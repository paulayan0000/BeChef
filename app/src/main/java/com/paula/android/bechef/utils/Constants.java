package com.paula.android.bechef.utils;

public class Constants {
    // Activity
    public static final int LOGIN_ACTIVITY = 0x01;

    // Status
    public static final int SIGN_IN_REQUSET_CODE = 0x10;
    public static final int LOGIN_SUCCESS = 0x11;
    public static final int LOGIN_EXIT = 0x12;

    // RecyclerView
    public static final float NORMAL_PADDING = 8;
    public static final int IMAGE_ITEM_LIMIT = 3;
    public static final int INFO_SIZE = 6;

    public static final int VIEW_TYPE_IMAGE = -1;   // Need to be smaller than zero
    public static final int VIEW_TYPE_INFO = 0;
    public static final int VIEW_TYPE_MATERIALS = 1;
    public static final int VIEW_TYPE_STEPS = 2;

    public static final int VIEW_TYPE_NORMAL = 10;
    public static final int VIEW_TYPE_LOADING = -10;
    public static final int VIEW_TYPE_NO_RESULT = -11;

    public static final int STEP_POSITION_DETAIL = -20;  // Need to be smaller than zero

    public static final int TAB_TYPE_DISCOVER = 20;
    public static final int TAB_TYPE_BOOKMARK = 21;
    public static final int TAB_TYPE_RECIPE = 22;

    // Database
    public static final String TAB_TABLE = "tab-table";
    public static final String ITEM_TABLE = "item-table";
    public static final String VARIABLE_NAME_TITLE = "title";
    public static final String VARIABLE_NAME_TAGS = "tags";
    public static final String VARIABLE_NAME_DESCRIPTION = "description";

    // Filter data type
    public static final int FILTER_WITH_TIME_DESC = 0;
    public static final int FILTER_WITH_TIME_ASC = 1;
    public static final int FILTER_WITH_RATING_DESC = 2;
    public static final int FILTER_WITH_RATING_ASC = 3;


    // Google Sign In status codes
    // https://developers.google.com/android/reference/com/google/android/gms/auth/api/signin/GoogleSignInStatusCodes#SIGN_IN_CANCELLED
//    public static final int SIGN_IN_CANCELLED = 12501;
//    public static final int SIGN_IN_CURRENTLY_IN_PROGRESS = 12502;
//    public static final int NETWORK_ERROR = 7;
    public static final int SIGN_IN_FAILED = 12500;

    // YouTube data API
    public static final String URL = "https://www.googleapis.com/youtube/v3/%s?key=%s";
    public static final String QUERY_PARAMS = "&%s=%s";
    public static final String API_SEARCH = "search";
    public static final String API_VIDEOS = "videos";
    public static final String API_CHANNELS = "channels";
    public static final String JSON_KEY_ID = "id";
    public static final String API_PART_ALL = "snippet,contentDetails,statistics";
    public static final String API_PART_SNIPPET = "snippet";
    public static final String API_MAX_RESULTS = "10";
    public static final String API_DEFAULT_ORDER = "date";
    public static final String API_TYPE_VIDEO = "video";
    public static final String API_TYPE_CHANNEL = "channel";
    public static final String API_TIME_REGEX = "[-T]";

    // Dialog
    public static final String DIALOG_TAG_NEW = "new";
    public static final long CALL_TIMEOUT = 35;
    public static final int ADD_NEW_TAB_INDEX = 0;
    public static final float DEFAULT_RATING = 0;

    // Permission and camera codes
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    public static final String FILE_PROVIDER_AUTHORITY = "com.paula.android.fileprovider";
    public static final String IMAGE_FILE_SUFFIX = ".jpg";

    // Utils
    public final static long ONE_MINUTE = 60 * 1000; // 1分鐘
    public final static long ONE_HOUR = 60 * ONE_MINUTE; // 1小時
    public final static long ONE_DAY = 24 * ONE_HOUR; // 1天
    public final static long ONE_WEEK = 7 * ONE_DAY; // 1週
    public final static long ONE_MONTH = 31 * ONE_DAY; // 1月
    public final static long ONE_YEAR = 12 * ONE_MONTH; // 1年
    public final static String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String TIME_ZONE = "UTC";
}