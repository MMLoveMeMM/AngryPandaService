package com.liu.zhibao.angrypandaservice.proxy;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.telecom.ConnectionService;
import android.util.Log;

import com.liu.zhibao.angrypandaservice.aidl.IDeathCheckInteface;

/**
 * Created by zhibao.Liu on 2018/8/13.
 *
 * @version :
 * @date : 2018/8/13
 * @des :
 * @see{@link}
 */

public class DeathRemoteProxy implements ServiceConnection {

    private final static String TAG=DeathRemoteProxy.class.getName();

    private static DeathRemoteProxy instance;

    private static Context mContext;

    private IDeathCheckInteface mService;

    private IBinder mBinder = new Binder();

    private IBinder mRemoteBinder;
    public static DeathRemoteProxy getInstance() {
        return instance;
    }

    public static void init(Context context){
        mContext=context;
        instance = new DeathRemoteProxy();
    }

    /*
    * 检查远程服务端是否crash
    * */
    private CheckDeathRecipient mCheckDeathRecipient=new CheckDeathRecipient();
    private class CheckDeathRecipient implements IBinder.DeathRecipient {

        @Override
        public void binderDied() {
            Log.e(TAG, "remote service has died");
            // 反注册
            if(mRemoteBinder!=null){
                mRemoteBinder.unlinkToDeath(mCheckDeathRecipient,0);
            }

            // 重新绑定
            bindService();
        }

    }

    public void bindService(){
        Intent intent = new Intent().setClassName("com.liu.zhibao.angrypandaservice","com.liu.zhibao.angrypandaservice.service.DeathCheckService");
        if(!mContext.bindService(intent,instance, Service.BIND_AUTO_CREATE)){
            Log.e(TAG,"bindService can not be successfully !");
        }
        return;
    }

    public void unBindService(){
        mContext.unbindService(instance);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mService = IDeathCheckInteface.Stub.asInterface(service);
        try {
            // 将binder传给远程service去监听
            mService.setBinder(mBinder);

            // 监听远程服务service是否crash
            service.linkToDeath(mCheckDeathRecipient,0);
            mRemoteBinder = service;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        if(mService!=null){
            mService=null;
        }
    }

    public void checkDeath(String data){
        if(mService!=null){
            try {
                mService.checkDeath(data);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
