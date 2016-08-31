package com.example.feiqu.edmodo.syncadapter;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.RemoteException;

import com.example.feiqu.edmodo.EdmodoApplication;
import com.example.feiqu.edmodo.network.AssignmentModel;
import com.example.feiqu.edmodo.network.Network;
import com.example.feiqu.edmodo.provider.AssignmentContract;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by feiqu on 8/28/16.
 */
public class AssignmentSyncAdapter extends AbstractThreadedSyncAdapter {
    private ContentResolver mContentResolver;

    public AssignmentSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    public AssignmentSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        try {
            Network network = ((EdmodoApplication)getContext()).getNetwork();
            Call<List<AssignmentModel>> call = network.getNetworkApi().getAssignments();
            List<AssignmentModel> assignmentModelList = call.execute().body();
            ArrayList<ContentProviderOperation> batch = new ArrayList<>();
            for (AssignmentModel assignmentModel : assignmentModelList) {
                batch.add(ContentProviderOperation.newInsert(AssignmentContract.Assignment.CONTENT_URI)
                        .withValue(AssignmentContract.Assignment.COLUMN_NAME_ASSIGNMENT_ID, assignmentModel.getId())
                        .withValue(AssignmentContract.Assignment.COLUMN_NAME_TITLE, assignmentModel.getTitle())
                        .withValue(AssignmentContract.Assignment.COLUMN_NAME_DESCRIPTION, assignmentModel.getDescription())
                        .withValue(AssignmentContract.Assignment.COLUMN_NAME_DUE_AT, assignmentModel.getDueAt())
                        .withValue(AssignmentContract.Assignment.COLUMN_NAME_CREATOR_ID, assignmentModel.getCreator().getId())
                        .build());
                syncResult.stats.numInserts++;
            }
            mContentResolver.applyBatch(AssignmentContract.CONTENT_AUTHORITY, batch);
        }
        catch (IOException e) {
            syncResult.stats.numIoExceptions++;
            return;
        }
        catch (RemoteException e) {
            syncResult.databaseError = true;
            return;
        }
        catch (OperationApplicationException e) {
            syncResult.databaseError = true;
            return;
        }
    }
}
