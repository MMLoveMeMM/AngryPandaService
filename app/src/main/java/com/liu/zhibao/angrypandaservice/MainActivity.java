package com.liu.zhibao.angrypandaservice;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.liu.zhibao.angrypandaservice.proxy.DeathRemoteProxy;
import com.liu.zhibao.angrypandaservice.proxy.MessagerProxy;
import com.liu.zhibao.angrypandaservice.proxy.RemoteProxy;
import com.liu.zhibao.angrypandaservice.service.UsingIntentService;

public class MainActivity extends Activity {

    private final static String TAG=MainActivity.class.getName();

    private Button mIntentServiceBtn;
    private Button mServiceBtn;
    private Button mRemoteServiceBtn;
    private Button mDeathBtn;

    private Button mSendBtn;

    private Button mCrashBtn;

    private Intent intent;
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIntentServiceBtn=(Button)findViewById(R.id.intentservice);
        mIntentServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 不断快速的点击按钮看看效果
                intent=new Intent(MainActivity.this, UsingIntentService.class);
                // 传个数过去吧
                Bundle bundle=new Bundle();
                bundle.putString("key","hello,intent service");
                bundle.putInt("number",14564);
                intent.putExtra("bundle",bundle);
                startService(intent);
            }
        });

        mServiceBtn=(Button)findViewById(R.id.service);
        mServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = Message.obtain(null, 1001, 0, 0);
                msg.replyTo=mRecReplyMsg;
                MessagerProxy.getInstance().sendMessage(msg);
            }
        });

        mSendBtn=(Button)findViewById(R.id.sendmessage);
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = Message.obtain(null, 1002, 0, 0);
                MessagerProxy.getInstance().sendMessage(msg);

                try {
                    PackageManager pm = getPackageManager();
                    @SuppressLint("WrongConstant") ApplicationInfo ai = pm.getApplicationInfo("com.liu.zhibao.angrypandaservice", PackageManager.GET_ACTIVITIES);
                    Log.e(TAG, "!!" + ai.uid);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });

        mRemoteServiceBtn=(Button)findViewById(R.id.remoteservice);
        mRemoteServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoteProxy.getInstance().updateService("hello,liuzhibao!");
            }
        });

        mDeathBtn=(Button)findViewById(R.id.death);
        mDeathBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeathRemoteProxy.getInstance().checkDeath("make client crash!");
            }
        });

        mCrashBtn=(Button)findViewById(R.id.crash);
        mCrashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 强制奔溃APP
                mediaPlayer.prepareAsync();
            }
        });

        MessagerProxy.init(getApplicationContext(),"");

        RemoteProxy.init(getApplicationContext());

        DeathRemoteProxy.init(getApplicationContext());

    }

    @Override
    protected void onResume() {
        super.onResume();
        MessagerProxy.getInstance().bindService();
        RemoteProxy.getInstance().bindService();
        DeathRemoteProxy.getInstance().bindService();
    }

    /*
    * 接受从service返回的消息
    * */
    private Messenger mRecReplyMsg= new Messenger(new RecReplyMsgHandler());
    private static class RecReplyMsgHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //接收服务端回复
                case 1001:
                    Log.e(TAG, "receiver message from service:"+msg.getData().getString("reply"));
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

}
