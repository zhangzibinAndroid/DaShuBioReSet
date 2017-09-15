package com.dashubio.adapter.blueadapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dashubio.R;
import com.dashubio.base.MyBaseAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/3 0003
 * 时间： 下午 12:24
 * 描述： 蓝牙适配器
 */

public class BlueAdapter extends MyBaseAdapter<BluetoothDevice> {
    public BlueAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_text_bluetooth, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        BluetoothDevice bluetoothDevice = (BluetoothDevice) getItem(position);
        viewHolder.tvBluetoothName.setText("蓝牙名称："+bluetoothDevice.getName() + "   蓝牙地址：" + bluetoothDevice.getAddress());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_bluetooth_name)
        TextView tvBluetoothName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
