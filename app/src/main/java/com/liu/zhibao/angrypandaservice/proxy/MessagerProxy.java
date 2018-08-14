package com.liu.zhibao.angrypandaservice.proxy;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.liu.zhibao.angrypandaservice.service.MessageService;

/**
 * Created by zhibao.Liu on 2018/8/10.
 *
 * @version :
 * @date : 2018/8/10
 * @des :
 * @see{@link}
 */

public class MessagerProxy implements ServiceConnection {

    private final static String TAG=MessagerProxy.class.getName();
    private static MessagerProxy instance;

    public static MessagerProxy getInstance() {
        return instance;
    }

    private Messenger mService = null;
    private boolean isBind=false;

    private static Context mContext; // 这不是个好办法

    public static void init(Context context,String classname){
        mContext = context;
        instance = new MessagerProxy();
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

        Log.d(TAG,"onServiceConnected");
        isBind = true;
        mService = new Messenger(service);

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mService=null;
    }

    @Override
    public void onBindingDied(ComponentName name) {
        Log.e(TAG,"onBindingDied ComponentName : "+name);
    }

    public void bindService(){
        Intent intent = new Intent().setClassName("com.liu.zhibao.angrypandaservice","com.liu.zhibao.angrypandaservice.service.MessageService");
        if(!mContext.bindService(intent,instance, Service.BIND_AUTO_CREATE)){
            Log.e(TAG,"bindService can not be successfully !");
        }
        return;
    }

    public void unBindService(){
        isBind = false;
        mContext.unbindService(instance);
    }

    public void sendMessage(Message msg){
        if(mService != null){
            try {
                mService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


}
