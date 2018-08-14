package com.liu.zhibao.angrypandaservice.proxy;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.liu.zhibao.angrypandaservice.aidl.IRemoteServiceInteface;

/**
 * Created by zhibao.Liu on 2018/8/13.
 *
 * @version :
 * @date : 2018/8/13
 * @des :
 * @see{@link}
 */

public class RemoteProxy implements ServiceConnection {

    private final static String TAG=RemoteProxy.class.getName();

    private static RemoteProxy instance;

    private IRemoteServiceInteface mService;
    private static Context mContext;

    public static RemoteProxy getInstance() {
        return instance;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mService=IRemoteServiceInteface.Stub.asInterface(service);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        if(mService!=null){
            mService=null;
        }
    }

    public static void init(Context context){
        mContext=context;
        instance=new RemoteProxy();
    }

    public void bindService(){
        Intent intent = new Intent().setClassName("com.liu.zhibao.angrypandaservice","com.liu.zhibao.angrypandaservice.service.RemoteService");
        if(!mContext.bindService(intent,instance, Service.BIND_AUTO_CREATE)){
            Log.e(TAG,"bindService can not be successfully !");
        }
        return;
    }

    public void unBindService(){
        mContext.unbindService(instance);
    }

    public void updateService(String data){

        if(mService!=null){
            if(!TextUtils.isEmpty(data)){
                try {
                    mService.updateService(data);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
