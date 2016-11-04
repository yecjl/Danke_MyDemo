package com.study.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    @Bind(R.id.addPrinter)
    RelativeLayout addPrinter;
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.pb)
    ProgressBar pb;
    private MainActivity context;
    private BluetoothAdapter mBluetoothAdapter;
    private MyAdapter mAdapter;
    private int REQUEST_ENABLE_BT = 0x0011;
    private List<BluetoothDevice> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context = this;
        mList = new ArrayList<>();
        mAdapter = new MyAdapter(this);
        listview.setAdapter(mAdapter);
    }

    @OnClick(R.id.addPrinter)
    public void setPrinter(View view) {
        openEnableBluetooth();
    }

    /**
     * 启动蓝牙
     */
    public void openEnableBluetooth() {
        // 判断当前设备是否支持蓝牙功能
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(context, "你的设备不支持蓝牙", Toast.LENGTH_SHORT).show();
            return;
        }

        // 判断蓝牙是否启动
        if (!mBluetoothAdapter.isEnabled()) {
            // 强制打开蓝牙
//            BluetoothAdapter.getDefaultAdapter().enable();
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

    }

    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                mList.add(device);
                mAdapter.setList(mList);
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            // 开启蓝牙成功
            Toast.makeText(context, "开启蓝牙成功", Toast.LENGTH_SHORT).show();

            mList.clear();
            // 查询配对的蓝牙设备
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            // If there are paired devices
            if (pairedDevices.size() > 0) {
                // Loop through paired devices
                for (BluetoothDevice device : pairedDevices) {
                    // Add the name and address to an array adapter to show in a ListView
                    mList.add(device);
                }
            }
            mAdapter.setList(mList);

            // 搜索蓝牙设备
            searchDevice();

        } else if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_CANCELED) {
            // 开启蓝牙失败
            Toast.makeText(context, "开启蓝牙失败", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 搜索蓝牙设备
     */
    private void searchDevice() {
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }
}
