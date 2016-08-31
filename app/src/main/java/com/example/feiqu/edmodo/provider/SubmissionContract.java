package com.example.feiqu.edmodo.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by feiqu on 8/30/16.
 */
public class SubmissionContract {
    private SubmissionContract() {

    }

    public static final String CONTENT_AUTHORITY = "com.example.feiqu.edmodo.submission";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static class Submission implements BaseColumns {
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.com.example.feiqu.edmodo.submission";

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.com.example.feiqu.edmodo.submission";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, "submissions");

        public static final String TABLE_NAME = "submission";

        public static final String COLUMN_NAME_ASSIGNMENT_ID = "assignment_id";

        public static final String COLUMN_NAME_CREATOR_FIRST_NAME = "creator_first_name";

        public static final String COLUMN_NAME_CREATOR_LAST_NAME = "creator_last_name";

        public static final String COLUMN_NAME_CREATOR_AVATARS_SMALL = "creator_avatars_small";

        public static final String COLUMN_NAME_CREATOR_AVATARS_LARGE = "creator_avatars_large";

        public static final String COLUMN_NAME_CONTENT = "content";

        public static final String COLUMN_NAME_SUBMITTED_AT = "submitted_at";
    }
}
