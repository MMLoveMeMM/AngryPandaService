package com.liu.zhibao.angrypandaservice.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

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
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
