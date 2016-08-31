package com.example.feiqu.edmodo.ui;

import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.app.LoaderManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.feiqu.edmodo.R;
import com.example.feiqu.edmodo.Util;
import com.example.feiqu.edmodo.provider.SubmissionContract;
import com.example.feiqu.edmodo.syncadapter.AuthenticatorService;
import com.squareup.picasso.Picasso;

public class AssignmentActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_DES = "description";
    public static final String EXTRA_ID = "assignment_id";
    public static final String EXTRA_CREATOR_ID = "assignment_creator_id";
    private static final String[] PROJECTION = new String[] {
            SubmissionContract.Submission._ID,
            SubmissionContract.Submission.COLUMN_NAME_CONTENT,
            SubmissionContract.Submission.COLUMN_NAME_CREATOR_FIRST_NAME,
            SubmissionContract.Submission.COLUMN_NAME_CREATOR_LAST_NAME,
            SubmissionContract.Submission.COLUMN_NAME_SUBMITTED_AT,
            SubmissionContract.Submission.COLUMN_NAME_CREATOR_AVATARS_SMALL,
            SubmissionContract.Submission.COLUMN_NAME_CREATOR_AVATARS_LARGE
    };
    private static final String[] FROM_COLUMNS = new String[] {
            SubmissionContract.Submission.COLUMN_NAME_CREATOR_AVATARS_SMALL,
            SubmissionContract.Submission.COLUMN_NAME_CREATOR_FIRST_NAME,
            SubmissionContract.Submission.COLUMN_NAME_CREATOR_LAST_NAME,
            SubmissionContract.Submission.COLUMN_NAME_SUBMITTED_AT
    };
    private static final int[] TO_FIELDS = new int[] {
            R.id.profile_pic,
            R.id.first_name,
            R.id.last_name,
            R.id.submitted_at
    };
    private static final int COLUMN_CONTENT = 1;
    private static final int COLUMN_FIRST_NAME = 2;
    private static final int COLUMN_LAST_NAME = 3;
    private static final int COLUMN_SUBMITTED_AT = 4;
    private static final int COLUMN_CREATOR_AVATARS_SMALL = 5;
    private static final int COLUMN_CREATOR_AVATARS_LARGE = 6;
    private static final String SELECTION = SubmissionContract.Submission.COLUMN_NAME_ASSIGNMENT_ID + "=?";
    private String[] SELECTION_ARGS;

    private SimpleCursorAdapter mAdapter;
    private String mTitle;
    private int mAssignmentId;
    private int mAssignmentCreatorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        Intent intent = getIntent();
        mTitle = intent.getStringExtra(EXTRA_TITLE);
        String description = intent.getStringExtra(EXTRA_DES);
        mAssignmentId = intent.getIntExtra(EXTRA_ID, -1);
        mAssignmentCreatorId = intent.getIntExtra(EXTRA_CREATOR_ID, -1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle(mTitle);

        TextView descriptionView = (TextView) findViewById(R.id.description);
        descriptionView.setText(description);

        ListView submissionList = (ListView) findViewById(R.id.submission_list);
        mAdapter = new SimpleCursorAdapter(
                this,
                R.layout.item_submission,
                null,
                FROM_COLUMNS,
                TO_FIELDS,
                0
        );
        mAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int i) {
                if (i == COLUMN_SUBMITTED_AT) {
                    String dateStr = Util.convertDate(cursor.getString(i));
                    ((TextView) view).setText("turned in " + dateStr);
                    return true;
                } else if (i == COLUMN_CREATOR_AVATARS_SMALL) {
                    Picasso.with(AssignmentActivity.this)
                            .load(cursor.getString(i))
                            .into((ImageView) view);
                    return true;
                } else {
                    return false;
                }
            }
        });
        submissionList.setAdapter(mAdapter);

        submissionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = (Cursor) mAdapter.getItem(position);
                Intent intent = new Intent(AssignmentActivity.this, SubmissionActivity.class);
                intent.putExtra(EXTRA_TITLE, mTitle);
                intent.putExtra(SubmissionActivity.EXTRA_AVATAR, c.getString(COLUMN_CREATOR_AVATARS_LARGE));
                intent.putExtra(SubmissionActivity.EXTRA_NAME, c.getString(COLUMN_FIRST_NAME) + " " + c.getString(COLUMN_LAST_NAME));
                intent.putExtra(SubmissionActivity.EXTRA_SUBMITTED_AT, "turned in " + Util.convertDate(c.getString(COLUMN_SUBMITTED_AT)));
                intent.putExtra(SubmissionActivity.EXTRA_CONTENT, c.getString(COLUMN_CONTENT));
                startActivity(intent);
            }
        });

        SELECTION_ARGS = new String[1];
        SELECTION_ARGS[0] = String.valueOf(mAssignmentId);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this,
                SubmissionContract.Submission.CONTENT_URI,
                PROJECTION,
                SELECTION,
                SELECTION_ARGS,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.changeCursor(data);
        if (data.getCount() == 0) {
            Bundle b = new Bundle();
            b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
            b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
            b.putInt(EXTRA_ID, mAssignmentId);
            b.putInt(EXTRA_CREATOR_ID, mAssignmentCreatorId);
            ContentResolver.requestSync(AuthenticatorService.getAccount(), SubmissionContract.CONTENT_AUTHORITY, b);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.changeCursor(null);
    }
}
