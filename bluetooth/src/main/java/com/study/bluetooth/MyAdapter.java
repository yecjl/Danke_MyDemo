package com.study.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

class MyAdapter extends SimpleAdapter<BluetoothDevice> {

    public MyAdapter(Context context) {
        super(context, R.layout.set_print_item);
    }

    @Override
    public ViewHolder<BluetoothDevice> getViewHolder() {
        return new MyViewHolder();
    }

    public class MyViewHolder extends ViewHolder<BluetoothDevice> {

        @Bind(R.id.tvListItemPrinterName)
        TextView tvListItemPrinterName;
        @Bind(R.id.tvListItemPrinterMac)
        TextView tvListItemPrinterMac;
        @Bind(R.id.lianjie)
        TextView lianjie;

        @Override
        public void onFindView(View root) {
            ButterKnife.bind(this, root);
        }

        @Override
        public void onBindData(BluetoothDevice data) {
            tvListItemPrinterMac.setText(data.getAddress());
            tvListItemPrinterName.setText(data.getName());
//            if ((Integer) map.get("is") == 1) {
//                vh.lianjie.setText("已配对");
//            } else {
//                vh.lianjie.setText("未连接");
//            }
        }
    }
}