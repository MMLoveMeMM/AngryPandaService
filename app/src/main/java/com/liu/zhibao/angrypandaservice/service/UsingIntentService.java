package com.liu.zhibao.angrypandaservice.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.jetbrains.annotations.Nullable;

/**
 * Created by zhibao.Liu on 2018/8/10.
 *
 * @version :
 * @date : 2018/8/10
 * @des :
 * @see{@link}
 */

public class UsingIntentService extends IntentService {

    private final static String TAG=UsingIntentService.class.getName();

    public UsingIntentService() {
        super("UsingIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"onCreate ...");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        // 这里面已经是从消息队列中一个一个被拿出来处理的,
        // 即使按键再快,也是间隔3s,一个一个执行
        Bundle bundle=intent.getBundleExtra("bundle");
        String key=bundle.getString("key");
        Log.e(TAG,"key : "+key);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"onDestroy");
    }
}
