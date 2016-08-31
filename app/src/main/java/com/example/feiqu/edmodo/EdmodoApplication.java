package com.example.feiqu.edmodo;

import android.app.Application;

import com.example.feiqu.edmodo.network.Network;

/**
 * Created by feiqu on 8/29/16.
 */
public class EdmodoApplication extends Application {
    private Network mNetwork;

    @Override
    public void onCreate() {
        super.onCreate();
        mNetwork = new Network();
    }

    public Network getNetwork() {
        return mNetwork;
    }
}
