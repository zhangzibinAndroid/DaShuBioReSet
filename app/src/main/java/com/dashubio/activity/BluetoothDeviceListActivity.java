/*
 * Copyright 2014 Akexorcist
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.dashubio.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.dashubio.R;
import java.util.ArrayList;
import java.util.List;

import app.akexorcist.bluetotohspp.library.BluetoothState;

@SuppressLint("NewApi")
public class BluetoothDeviceListActivity extends Activity {
    // Debugging
    private static final String TAG = "BluetoothSPP";

    // Member fields
    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mDevicesArrayAdapter;
    private List<BluetoothDevice> mDeviceList = new ArrayList<>();

    public static final String REQUEST_CONNECT_DEVICE_TYPE_KEY = "RequestConnectDeviceTypeKey";//请求连接设备类型key
    public static final int REQUEST_CONNECT_DEVICE_HEALTH_DETECT = 1;//连接健康检测仪
    public static final int REQUEST_CONNECT_DEVICE_DRY_BIOCHEMICAL_ANALYZER = 2;//连接干式生化仪
    public static final int REQUEST_CONNECT_DEVICE_URINE_ANALYZER = 3;//连接尿液分析仪
    private int mRequestConnectDeviceType = -1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup the window
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.dialog_search_buletooth);

        Intent intent = getIntent();
        if (intent != null) {
            mRequestConnectDeviceType = intent.getIntExtra(REQUEST_CONNECT_DEVICE_TYPE_KEY, -1);
        }

        // Set result CANCELED in case the user backs out
        setResult(Activity.RESULT_CANCELED);

        // Initialize array adapters. One for already paired devices
        // and one for newly discovered devices
        mDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.textview_blueitem_layout);

        // Find and set up the ListView for paired devices
        ListView pairedListView = (ListView) findViewById(R.id.lv_search_bluetooth);
        pairedListView.setAdapter(mDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        // Get the local Bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        doDiscovery();

    }

    protected void onDestroy() {
        super.onDestroy();
        // Make sure we're not doing discovery anymore
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);
        this.finish();
    }

    // Start device discover with the BluetoothAdapter
    private void doDiscovery() {

        mDeviceList.clear();
        // Remove all element from the list
        mDevicesArrayAdapter.clear();

        // Indicate scanning in the title
        setProgressBarIndeterminateVisibility(true);
        setTitle("Scanning for devices...");

        // If we're already discovering, stop it
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        mBtAdapter.startDiscovery();
    }

    // The on-click listener for all devices in the ListViews
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            // Cancel discovery because it's costly and we're about to connect
            if (mBtAdapter.isDiscovering()) {
                mBtAdapter.cancelDiscovery();
            }

            BluetoothDevice bluetoothDevice = mDeviceList.get(position);

            // Create the result Intent and include the MAC address
            Intent intent = new Intent();
            intent.putExtra(BluetoothState.EXTRA_DEVICE_ADDRESS, bluetoothDevice.getAddress());
            intent.putExtra(BluetoothState.EXTRA_DEVICE, bluetoothDevice);

            // Set result and finish this Activity
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };

    // The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                boolean deviceContained = false;
                for (BluetoothDevice listDev : mDeviceList) {
                    if (listDev.getAddress().equals(device.getAddress())) {
                        deviceContained = true;
                        break;
                    }
                }

                //设备列表中不包含该设备
                if (!deviceContained) {
                    String deviceName = device.getName();
                    boolean isCorrespondingDevice = false;//是否是对应的设备
                    switch (mRequestConnectDeviceType) {
                        case REQUEST_CONNECT_DEVICE_HEALTH_DETECT://健康检测仪
                            if (!TextUtils.isEmpty(deviceName) && deviceName.startsWith("HC")) {
                                isCorrespondingDevice = true;
                            }
                            break;

                        case REQUEST_CONNECT_DEVICE_DRY_BIOCHEMICAL_ANALYZER://干式生化仪
                            int result = string2Int(deviceName);
                            if (result > 0) {
                                isCorrespondingDevice = true;
                            }
                            break;

                        case REQUEST_CONNECT_DEVICE_URINE_ANALYZER://尿液分析仪
                            if (!TextUtils.isEmpty(deviceName) && deviceName.startsWith("BC")) {
                                isCorrespondingDevice = true;
                            }
                            break;
                    }

                    if (isCorrespondingDevice) {
                        mDeviceList.add(device);
                        mDevicesArrayAdapter.add("蓝牙名称："+device.getName() + "   蓝牙地址：" + device.getAddress());
                    }
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                setTitle("Select a device to connect");
            }
        }
    };


    /**
     * String转int
     *
     * @param intstr
     * @return
     */
    public static int string2Int(String intstr) {
        int result = -1;
        try {
            result = Integer.valueOf(intstr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
