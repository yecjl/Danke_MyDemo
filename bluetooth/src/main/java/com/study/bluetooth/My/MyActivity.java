package com.study.bluetooth.My;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.study.bluetooth.R;

/**
 * 功能：
 * Created by danke on 2016/9/22.
 */
public class MyActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client);
    }

    @Override
    protected void onStart() {
        //开启后台service
        Intent startService = new Intent(this, MyService.class);
        startService(startService);
        super.onStart();
    }

    @Override
    protected void onStop() {
        //关闭后台Service
        Intent startService = new Intent(com.study.bluetooth.bluetoothUtil.BluetoothTools.ACTION_STOP_SERVICE);
        sendBroadcast(startService);

        super.onStop();
    }
}
