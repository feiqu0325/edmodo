package com.example.feiqu.edmodo.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.feiqu.edmodo.R;
import com.example.feiqu.edmodo.Util;
import com.example.feiqu.edmodo.provider.AssignmentContract;
import com.example.feiqu.edmodo.syncadapter.AuthenticatorService;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String[] PROJECTION = new String[] {
            AssignmentContract.Assignment._ID,
            AssignmentContract.Assignment.COLUMN_NAME_ASSIGNMENT_ID,
            AssignmentContract.Assignment.COLUMN_NAME_TITLE,
            AssignmentContract.Assignment.COLUMN_NAME_DESCRIPTION,
            AssignmentContract.Assignment.COLUMN_NAME_DUE_AT,
            AssignmentContract.Assignment.COLUMN_NAME_CREATOR_ID
    };
    private static final String[] FROM_COLUMNS = new String[] {
            AssignmentContract.Assignment.COLUMN_NAME_TITLE,
            AssignmentContract.Assignment.COLUMN_NAME_DUE_AT
    };
    private static final int[] TO_FIELDS = new int[] {
            R.id.assignment_title,
            R.id.assignment_due
    };
    private static final int COLUMN_ASSIGNMENT_ID = 1;
    private static final int COLUMN_TITLE = 2;
    private static final int COLUMN_DESCRIPTION = 3;
    private static final int COLUMN_DUE_AT = 4;
    private static final int COLUMN_CREATOR_ID = 5;

    private SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Assignments");

        ListView assignmentList = (ListView) findViewById(R.id.assignment_list);
        mAdapter = new SimpleCursorAdapter(
                this,
                R.layout.item_assignment,
                null,
                FROM_COLUMNS,
                TO_FIELDS,
                0
        );
        mAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int i) {
                if (i == COLUMN_DUE_AT) {
                    String dateStr = Util.convertDate(cursor.getString(i));
                    ((TextView) view).setText("due " + dateStr);
                    return true;
                } else {
                    return false;
                }
            }
        });
        assignmentList.setAdapter(mAdapter);

        assignmentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = (Cursor) mAdapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, AssignmentActivity.class);
                intent.putExtra(AssignmentActivity.EXTRA_ID, c.getInt(COLUMN_ASSIGNMENT_ID));
                intent.putExtra(AssignmentActivity.EXTRA_TITLE, c.getString(COLUMN_TITLE));
                intent.putExtra(AssignmentActivity.EXTRA_DES, c.getString(COLUMN_DESCRIPTION));
                intent.putExtra(AssignmentActivity.EXTRA_CREATOR_ID, c.getInt(COLUMN_CREATOR_ID));
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(0, null, this);

        Account account = AuthenticatorService.getAccount();
        AccountManager accountManager = (AccountManager)getSystemService(Context.ACCOUNT_SERVICE);
        if (accountManager.addAccountExplicitly(account, null, null)) {
            ContentResolver.setIsSyncable(account, AssignmentContract.CONTENT_AUTHORITY, 1);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this,
                AssignmentContract.Assignment.CONTENT_URI,
                PROJECTION,
                null,
                null,
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
            ContentResolver.requestSync(AuthenticatorService.getAccount(), AssignmentContract.CONTENT_AUTHORITY, b);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.changeCursor(null);
    }
}
