package com.liu.zhibao.angrypandaservice.service.mul;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.liu.zhibao.angrypandaservice.aidl.IDoubleConnInteface;

public class ConnOneService extends Service {

    private final static String TAG=ConnOneService.class.getName();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return stub;
    }

    private IDoubleConnInteface.Stub stub=new IDoubleConnInteface.Stub(){

        @Override
        public void connLine(String data) throws RemoteException {
            Log.e(TAG,"connLine data : "+data);
        }
    };

}
