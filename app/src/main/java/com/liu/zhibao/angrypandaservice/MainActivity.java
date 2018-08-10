package com.liu.zhibao.angrypandaservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.liu.zhibao.angrypandaservice.service.UsingIntentService;

public class MainActivity extends Activity {

    private Button mIntentServiceBtn;
    private Button mServiceBtn;
    private Button mRemoteServiceBtn;

    private Intent intent;
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

    }
}
