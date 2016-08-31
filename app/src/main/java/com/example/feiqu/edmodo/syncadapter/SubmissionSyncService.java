package com.example.feiqu.edmodo.syncadapter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by feiqu on 8/30/16.
 */
public class SubmissionSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static SubmissionSyncAdapter sSubmissionSyncAdapter = null;

    @Override
    public void onCreate() {
        super.onCreate();
        synchronized (sSyncAdapterLock) {
            if (sSubmissionSyncAdapter == null) {
                sSubmissionSyncAdapter = new SubmissionSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSubmissionSyncAdapter.getSyncAdapterBinder();
    }
}
