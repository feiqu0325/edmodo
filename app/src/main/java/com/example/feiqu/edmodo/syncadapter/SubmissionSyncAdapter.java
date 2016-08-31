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

import com.example.feiqu.edmodo.ui.AssignmentActivity;
import com.example.feiqu.edmodo.EdmodoApplication;
import com.example.feiqu.edmodo.network.Network;
import com.example.feiqu.edmodo.network.SubmissionModel;
import com.example.feiqu.edmodo.provider.SubmissionContract;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by feiqu on 8/30/16.
 */
public class SubmissionSyncAdapter extends AbstractThreadedSyncAdapter {
    private ContentResolver mContentResolver;

    public SubmissionSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    public SubmissionSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        try {
            int assignmentId = extras.getInt(AssignmentActivity.EXTRA_ID);
            int assignmentCreatorId = extras.getInt(AssignmentActivity.EXTRA_CREATOR_ID);
            if (assignmentId != -1 && assignmentCreatorId != -1) {
                Network network = ((EdmodoApplication)getContext()).getNetwork();
                Call<List<SubmissionModel>> call = network.getNetworkApi().getSubmissions(assignmentId, assignmentCreatorId);
                List<SubmissionModel> submissionModelList = call.execute().body();
                ArrayList<ContentProviderOperation> batch = new ArrayList<>();
                for (SubmissionModel submissionModel : submissionModelList) {
                    batch.add(ContentProviderOperation.newInsert(SubmissionContract.Submission.CONTENT_URI)
                            .withValue(SubmissionContract.Submission.COLUMN_NAME_ASSIGNMENT_ID, submissionModel.getAssignmentId())
                            .withValue(SubmissionContract.Submission.COLUMN_NAME_CONTENT, submissionModel.getContent())
                            .withValue(SubmissionContract.Submission.COLUMN_NAME_CREATOR_FIRST_NAME, submissionModel.getCreator().getFirstName())
                            .withValue(SubmissionContract.Submission.COLUMN_NAME_CREATOR_LAST_NAME, submissionModel.getCreator().getLastName())
                            .withValue(SubmissionContract.Submission.COLUMN_NAME_SUBMITTED_AT, submissionModel.getSubmittedAt())
                            .withValue(SubmissionContract.Submission.COLUMN_NAME_CREATOR_AVATARS_SMALL, submissionModel.getCreator().getSmallAvatar())
                            .withValue(SubmissionContract.Submission.COLUMN_NAME_CREATOR_AVATARS_LARGE, submissionModel.getCreator().getLargeAvatar())
                            .build());
                    syncResult.stats.numInserts++;
                }
                mContentResolver.applyBatch(SubmissionContract.CONTENT_AUTHORITY, batch);
            }
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
