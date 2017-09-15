package com.dashubio.ecg_single;

import android.bluetooth.BluetoothDevice;

import com.dashubio.ecg_single.entity.Data;


/**
 * Created by altria on 17-7-7.
 */

public interface OnCallBack {
    int SEARCH_DEVICES_FAILED = 0;//搜索设备失败
    int NOT_SUPPORT_BLE = 1;//不支持BLE
    int NO_BLE_ADAPTER = 2;//没有适配器
    int DEVICE_CONNECTED = 3;//设备连接
    int DEVICE_DISCONNECTED = 4;//设备断开连接

    void onStatusCallBack(int status);
    void onDataCallBack(Data data);
    void onDeviceFound(BluetoothDevice device);
}
