package com.liu.zhibao.angrypandaservice.proxy;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.telecom.ConnectionService;
import android.util.Log;

import com.liu.zhibao.angrypandaservice.aidl.IDoubleConnInteface;

/**
 * Created by zhibao.Liu on 2018/8/16.
 *
 * @version :
 * @date : 2018/8/16
 * @des :
 * @see{@link}
 */

public class DoubleProxy implements ServiceConnection {

    private final static String TAG=DoubleProxy.class.getName();

    private static DoubleProxy instance ;
    private IDoubleConnInteface mService;

    private static Context mContext;
    public static DoubleProxy getInstance() {
        return instance;
    }

    public static void init(Context context){
        mContext=context;
        instance = new DoubleProxy();
    }

    public void bindService(){
        /*
        * 这里约定了用那个service去处理
        * 即使有两个service都被连接在同一个aidl
        * */
        Intent intent = new Intent().setClassName("com.liu.zhibao.angrypandaservice","com.liu.zhibao.angrypandaservice.service.mul.ConnOneService");
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
        mService = IDoubleConnInteface.Stub.asInterface(service);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        if(mService!=null){
            mService=null;
        }
    }

    public void connLine(String data){
        if(mService!=null){
            try {
                mService.connLine(data);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
