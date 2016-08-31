package com.example.feiqu.edmodo.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by feiqu on 8/28/16.
 */
public class AssignmentContract {
    private AssignmentContract() {

    }

    public static final String CONTENT_AUTHORITY = "com.example.feiqu.edmodo.assignment";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static class Assignment implements BaseColumns {
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.com.example.feiqu.edmodo.assignment";

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.com.example.feiqu.edmodo.assignment";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, "assignments");

        public static final String TABLE_NAME = "assignment";

        public static final String COLUMN_NAME_ASSIGNMENT_ID = "assignment_id";

        public static final String COLUMN_NAME_TITLE = "title";

        public static final String COLUMN_NAME_DESCRIPTION = "description";

        public static final String COLUMN_NAME_DUE_AT = "due_at";

        public static final String COLUMN_NAME_CREATOR_ID = "creator_id";
    }
}
