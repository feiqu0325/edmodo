package com.example.feiqu.edmodo.syncadapter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by feiqu on 8/29/16.
 */
public class AssignmentSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static AssignmentSyncAdapter sAssignmentSyncAdapter = null;

    @Override
    public void onCreate() {
        super.onCreate();
        synchronized (sSyncAdapterLock) {
            if (sAssignmentSyncAdapter == null) {
                sAssignmentSyncAdapter = new AssignmentSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sAssignmentSyncAdapter.getSyncAdapterBinder();
    }
}
