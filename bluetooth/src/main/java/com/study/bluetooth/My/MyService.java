package com.study.bluetooth.My;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.study.bluetooth.bluetoothUtil.BluetoothTools;

/**
 * 功能：
 * Created by danke on 2016/9/22.
 */
public class MyService extends Service {
    //蓝牙适配器
    private final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("onReceive", "onReceive-----------");
            switch (intent.getAction()) {
                case BluetoothAdapter.ACTION_STATE_CHANGED: // 蓝牙状态的监听
                    int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                    switch (blueState) {
                        case BluetoothAdapter.STATE_TURNING_ON:
                            Log.d("onReceive", "STATE_TURNING_ON 手机蓝牙正在开启");
                            break;
                        case BluetoothAdapter.STATE_ON:
                            Log.d("onReceive", "STATE_ON 手机蓝牙开启");

                            if (bluetoothAdapter.isEnabled()) {
                                // 开始搜索
                                bluetoothAdapter.startDiscovery();
                            }

                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            Log.d("onReceive", "STATE_TURNING_OFF 手机蓝牙正在关闭");
                            break;
                        case BluetoothAdapter.STATE_OFF:
                            Log.d("onReceive", "STATE_OFF 手机蓝牙关闭");
                            break;
                    }
                    break;
                case BluetoothDevice.ACTION_FOUND: // 发现远程蓝牙设备
                    //获取设备
                    BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Log.d("onReceive", bluetoothDevice.getName() + "--" + bluetoothDevice.getAddress());
                    //发送发现设备广播
//                    Intent deviceListIntent = new Intent(BluetoothTools.ACTION_FOUND_DEVICE);
//                    deviceListIntent.putExtra(BluetoothTools.DEVICE, bluetoothDevice);
//                    sendBroadcast(deviceListIntent);
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED: // 开始搜索
                    Log.d("onReceive", "started");
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED: // 搜索结束
                    Log.d("onReceive", "finished");
                    break;
                case BluetoothTools.ACTION_STOP_SERVICE: // 关闭蓝牙服务
                    Log.d("onReceive", "stopSelf");
                    stopSelf();
                    break;
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        // 注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);

        // 判断当前蓝牙是否开启
        if (bluetoothAdapter.isEnabled()) {
            // 开始搜索
            bluetoothAdapter.startDiscovery();
        }
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}
