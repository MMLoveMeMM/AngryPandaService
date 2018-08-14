package com.liu.zhibao.angrypandaservice.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.liu.zhibao.angrypandaservice.aidl.IDeathCheckInteface;

import org.jetbrains.annotations.Nullable;

/**
 * Created by zhibao.Liu on 2018/8/13.
 *
 * @version :
 * @date : 2018/8/13
 * @des : 这个远程服务用语监听客户端是否还存在,通过监听客户端的Binder对象是否销毁.
 * @see{@link}
 */

public class DeathCheckService extends Service {

    private final static String TAG=DeathCheckService.class.getName();
    private MediaPlayer mediaPlayer;

    private IBinder mClient = null;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mDeathCheck;
    }

    /*
    * 检查客户端是否crash
    * */
    private class CheckDeathRecipient implements IBinder.DeathRecipient {

        @Override
        public void binderDied() {
            Log.e(TAG, "client has died");
        }

    }

    private IDeathCheck mDeathCheck=new IDeathCheck();
    private class IDeathCheck extends IDeathCheckInteface.Stub{
        @Override
        public void checkDeath(String data) throws RemoteException {
            Log.e(TAG, "client send : "+data);
            // 在这个地方故意产生crash
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.prepareAsync();
                }
            }).start();
        }
        @Override
        public void setBinder(IBinder client) throws RemoteException {
            mClient = client;
            mClient.linkToDeath(new CheckDeathRecipient(), 0);
        }
    }

}
