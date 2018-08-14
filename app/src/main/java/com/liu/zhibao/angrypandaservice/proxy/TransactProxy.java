package com.liu.zhibao.angrypandaservice.proxy;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by zhibao.Liu on 2018/8/14.
 *
 * @version :
 * @date : 2018/8/14
 * @des :
 * @see{@link}
 */

public class TransactProxy implements ServiceConnection {

    private final static String TAG = TransactProxy.class.getName();

    private static TransactProxy instance;
    private IBinder mIBinder;
    private static Context mContext;

    public static TransactProxy getInstance(){
        return instance;
    }

    public static void init(Context context){

        if(instance!=null){
            return;
        }
        mContext=context;
        instance=new TransactProxy();

    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mIBinder=service;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        if(mIBinder!=null){
            mIBinder=null;
        }
    }

    public void bindService(){
        Intent intent = new Intent().setClassName("com.liu.zhibao.angrypandaservice","com.liu.zhibao.angrypandaservice.service.TransactService");
        if(!mContext.bindService(intent,instance, Service.BIND_AUTO_CREATE)){
            Log.e(TAG,"bindService can not be successfully !");
        }
        return;
    }

    public void unBindService(){
        mContext.unbindService(instance);
    }

    public synchronized void sendMessage(){

        if (mIBinder == null)
        {
            Toast.makeText(mContext, "未连接服务端或服务端被异常杀死", Toast.LENGTH_SHORT).show();
        } else {
            android.os.Parcel _data = android.os.Parcel.obtain();
            android.os.Parcel _reply = android.os.Parcel.obtain();
            String _result = null;
            try{
                _data.writeInterfaceToken("TransactService");
                Log.e(TAG,"get name id : "+2);
                _data.writeInt(2);
                mIBinder.transact(0x001, _data, _reply, 0); // 阻塞运行的,如果在服务端做耗时,这个地方会block
                _reply.readException();
                _result = _reply.readString();
                Log.e(TAG,"_result : "+_result);
            }catch (RemoteException e)
            {
                e.printStackTrace();
            } finally
            {
                _reply.recycle();
                _data.recycle();
            }
        }

    }

}
