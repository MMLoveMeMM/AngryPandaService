package com.liu.zhibao.angrypandaservice.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.liu.zhibao.angrypandaservice.aidl.IRemoteServiceInteface;

import org.jetbrains.annotations.Nullable;

/**
 * Created by zhibao.Liu on 2018/8/10.
 *
 * @version :
 * @date : 2018/8/10
 * @des :
 * @see{@link}
 */

public class RemoteService extends Service {

    private final static String TAG = RemoteService.class.getName();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    private IRemoteServiceInteface.Stub stub=new IRemoteServiceInteface.Stub(){

        @Override
        public void updateService(String data) throws RemoteException {
            // 不是UI线程了,又和Messager+Service的一个区别
            // Toast.makeText(getApplicationContext(), "info : "+data, Toast.LENGTH_SHORT).show();
            Log.e(TAG,"info : "+data);
        }



    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
