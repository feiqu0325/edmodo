package com.example.feiqu.edmodo.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by feiqu on 8/28/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "assignment.db";
    private static final String TYPE_TEXT = " TEXT";
    private static final String TYPE_INTEGER = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ASSIGNMENTS =
            "CREATE TABLE " + AssignmentContract.Assignment.TABLE_NAME + " (" +
            AssignmentContract.Assignment._ID + " INTEGER PRIMARY KEY," +
            AssignmentContract.Assignment.COLUMN_NAME_ASSIGNMENT_ID + TYPE_INTEGER + COMMA_SEP +
            AssignmentContract.Assignment.COLUMN_NAME_TITLE + TYPE_TEXT + COMMA_SEP +
            AssignmentContract.Assignment.COLUMN_NAME_DESCRIPTION + TYPE_TEXT + COMMA_SEP +
            AssignmentContract.Assignment.COLUMN_NAME_DUE_AT + TYPE_TEXT + COMMA_SEP +
            AssignmentContract.Assignment.COLUMN_NAME_CREATOR_ID + TYPE_INTEGER + ")";
    private static final String SQL_CREATE_SUBMISSIONS =
            "CREATE TABLE " + SubmissionContract.Submission.TABLE_NAME + " (" +
                    SubmissionContract.Submission._ID + " INTEGER PRIMARY KEY," +
                    SubmissionContract.Submission.COLUMN_NAME_ASSIGNMENT_ID + TYPE_INTEGER + COMMA_SEP +
                    SubmissionContract.Submission.COLUMN_NAME_CREATOR_FIRST_NAME + TYPE_TEXT + COMMA_SEP +
                    SubmissionContract.Submission.COLUMN_NAME_CREATOR_LAST_NAME + TYPE_TEXT + COMMA_SEP +
                    SubmissionContract.Submission.COLUMN_NAME_CREATOR_AVATARS_SMALL + TYPE_TEXT + COMMA_SEP +
                    SubmissionContract.Submission.COLUMN_NAME_CREATOR_AVATARS_LARGE + TYPE_TEXT + COMMA_SEP +
                    SubmissionContract.Submission.COLUMN_NAME_CONTENT + TYPE_TEXT + COMMA_SEP +
                    SubmissionContract.Submission.COLUMN_NAME_SUBMITTED_AT + TYPE_TEXT + ")";
    private static final String SQL_DELETE_ASSIGNMENTS =
            "DROP TABLE IF EXISTS " + AssignmentContract.Assignment.TABLE_NAME;
    private static final String SQL_DELETE_SUBMISSIONS =
            "DROP TABLE IF EXISTS " + SubmissionContract.Submission.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ASSIGNMENTS);
        db.execSQL(SQL_CREATE_SUBMISSIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ASSIGNMENTS);
        db.execSQL(SQL_DELETE_SUBMISSIONS);
        onCreate(db);
    }
}
