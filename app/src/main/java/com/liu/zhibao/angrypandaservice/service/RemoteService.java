package com.liu.zhibao.angrypandaservice.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
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

    private final static String PERMISSION_NAME = "com.liu.zhibao.ACCESS_REMOTE_SERVICE";
    private final static String CALLING_PKG_NAME="com.liu.zhibao.angrypandaservice";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        Log.e(TAG,"pkg name : "+intent.getComponent().getPackageName());
        String callingPkgName = intent.getComponent().getPackageName();
        if(checkPermission(getApplicationContext(),PERMISSION_NAME,callingPkgName/* 客户端(连接服务者)的包名 */)) {
            /*
            * 有指定权限的APP 才能够连接到这个服务,否则不准对方连接
            * */
            return stub;
        }else{
            /*
            * 无权限禁止连接
            * */
            return null;
        }
    }

    // 这个方法不管用,无法通过检查被调用者的权限来判断调用者是否有权限
    public boolean checkPermission(Context context, String permission) {
        //检查如果是当前应用则返回真
        permission="com.liu.zhibao.ACCESS_REMOTE_SERVICE";
        if (context.checkCallingPermission(permission) == PackageManager.PERMISSION_DENIED) {
            Log.e(TAG,"client has no permission : " + permission);
            return false;
        }
        return true;
    }

    /*
    * 还是需要通过检查对应连接者的包名来检查这个连接者是否有制定的权限
    * */
    private boolean checkPermission(Context context, String permName, String pkgName){
        PackageManager pm = context.getPackageManager();
        if(PackageManager.PERMISSION_GRANTED == pm.checkPermission(permName, pkgName)){
            Log.e(TAG,pkgName + " has permission : " + permName);
            return true;
        }else{
            Log.e(TAG,pkgName + "has no permission : " + permName);
            return false;
        }
    }

    private IRemoteServiceInteface.Stub stub=new IRemoteServiceInteface.Stub(){

        @Override
        public void updateService(String data) throws RemoteException {
            // 不是UI线程了,又和Messager+Service的一个区别
            // Toast.makeText(getApplicationContext(), "info : "+data, Toast.LENGTH_SHORT).show();
            Log.e(TAG,"000current thread 1: "+Thread.currentThread());
            Log.e(TAG,"info : "+data);
        }

        @Override
        public void makeCrash() throws RemoteException {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    /*
                    * 直接会将remote service搞挂掉
                    * */
                    Log.e(TAG,"000current thread 2: "+Thread.currentThread());
                    MediaPlayer mediaPlayer=null;
                    mediaPlayer.prepareAsync();
                }
            }).start();

        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Log.e(TAG,"过滤器filter code:" + code + "---flags:" + flags);
            String packageName = null;
            String[] packages = getPackageManager().
                    getPackagesForUid(getCallingUid());
            if (packages != null && packages.length > 0) {
                packageName = packages[0];
            }
            if (packageName == null) {
                return false;
            }
            boolean checkPermission = checkPermission(RemoteService.this, PERMISSION_NAME, packageName);
            if (!checkPermission) {
                Log.e(TAG,"onTransact check permission : no permission !");
                return false;
            }
            return super.onTransact(code, data, reply, flags);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
