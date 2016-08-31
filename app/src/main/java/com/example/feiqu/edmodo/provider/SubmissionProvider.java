package com.example.feiqu.edmodo.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by feiqu on 8/30/16.
 */
public class SubmissionProvider extends ContentProvider {
    DatabaseHelper mDatabaseHelper;

    private static final String AUTHORITY = SubmissionContract.CONTENT_AUTHORITY;

    public static final int ROUTE_SUBMISSIONS = 1;
    public static final int ROUTE_SUBMISSION = 2;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(AUTHORITY, "submissions", ROUTE_SUBMISSIONS);
        sUriMatcher.addURI(AUTHORITY, "submissions/#", ROUTE_SUBMISSION);
    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        int uriMatch = sUriMatcher.match(uri);
        switch (uriMatch) {
            case ROUTE_SUBMISSIONS:
                builder.setTables(SubmissionContract.Submission.TABLE_NAME);
                break;
            case ROUTE_SUBMISSION:
                builder.setTables(SubmissionContract.Submission.TABLE_NAME);
                builder.appendWhere(SubmissionContract.Submission._ID + " = " + uri.getLastPathSegment());
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri" + uri);
        }
        Cursor cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ROUTE_SUBMISSIONS:
                return SubmissionContract.Submission.CONTENT_TYPE;
            case ROUTE_SUBMISSION:
                return SubmissionContract.Submission.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri" + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        long id;
        switch (match) {
            case ROUTE_SUBMISSIONS:
                id = db.insertOrThrow(SubmissionContract.Submission.TABLE_NAME, null, values);
                break;
            case ROUTE_SUBMISSION:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            default:
                throw new UnsupportedOperationException("Unknown uri" + uri);
        }
        Uri result = ContentUris.withAppendedId(uri, id);
        getContext().getContentResolver().notifyChange(uri, null, false);
        return result;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
