package com.dashubio.ecg_single;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.dashubio.application.DashuApplication;
import com.dashubio.ecg_single.ble.JPBleDataHandler;
import com.dashubio.ecg_single.ble.JPBleUtils;
import com.dashubio.ecg_single.ble.JPBleWaveCallback;
import com.dashubio.ecg_single.ble.JPWaveDecoder;
import com.dashubio.ecg_single.entity.Data;
import com.xtremeprog.sdk.ble.BleGattCharacteristic;
import com.xtremeprog.sdk.ble.BleGattService;
import com.xtremeprog.sdk.ble.BleService;
import com.xtremeprog.sdk.ble.IBle;

import java.util.ArrayList;
import java.util.List;

public class SingleLeadUtil {
    private static final int DATA_MIN_LENGTH = 20;
    private final Handler mRawHandler;

    private BleGattService mGetDataService = null;
    private BleGattService mPressureService = null;
    private BleGattCharacteristic mCharacterEnable = null;
    private BleGattCharacteristic mCharacterData = null;
    private BleGattCharacteristic mCharacterWave1 = null;
    private BleGattCharacteristic mCharacterWave2 = null;
    private BleGattCharacteristic mCharacterPressure = null;

    private JPWaveDecoder waveDecoder = new JPWaveDecoder(new JPBleWaveCallback() {
        @Override
        public void uiDrawWavePoints(float[] points) {
            ArrayList<Float> res = new ArrayList<>();

            for (int i = 0; i < points.length; i++) {
                res.add(points[i]);
            }

            callBackData = new Data();
            callBackData.setWaveData(res);
            callBackData.setType(Data.TYPE_WAVE_1_DATA);
            callBackData.setUuid(uuid);
            callBackData.setVal(val);
            onCallBack.onDataCallBack(callBackData);
        }
    });

    private JPWaveDecoder wave2Decoder = new JPWaveDecoder(new JPBleWaveCallback() {
        @Override
        public void uiDrawWavePoints(float[] points) {
            ArrayList<Float> res = new ArrayList<>();

            for (int i = 0; i < points.length; i++) {
                res.add(points[i]);
            }

            callBackData = new Data();
            callBackData.setWaveData(res);
            callBackData.setType(Data.TYPE_WAVE_2_DATA);
            callBackData.setUuid(uuid);
            callBackData.setVal(val);
            onCallBack.onDataCallBack(callBackData);
        }
    });

    private final Handler mHandler;
    private Context context;
    private OnCallBack onCallBack;
    private IBle mBle = null;
    private String mDeviceAddress = null;
    private boolean mScanning = false;
    private boolean mConnected = false;
    private final int REQUESAT_PERMISSION_CODE = 1024;
    private static final long SCAN_PERIOD = 120000;
    private Data callBackData;//数据
    private String uuid;
    private byte[] val;

    public SingleLeadUtil(Context context, final OnCallBack onCallBack) {
        this.context = context;
        this.onCallBack = onCallBack;
        mHandler = new Handler();
        context.registerReceiver(mBleReceiver, BleService.getIntentFilter());

        mRawHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                uuid = bundle.getString("uuid");
                val = bundle.getByteArray("raw");

                parseRawData(uuid, val);
            }
        };
    }

    private final BroadcastReceiver mBleReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BleService.BLE_NOT_SUPPORTED.equals(action)) {
                processNotSupportBle();
            } else if (BleService.BLE_DEVICE_FOUND.equals(action)) {
                processDeviceFound(intent);
            } else if (BleService.BLE_NO_BT_ADAPTER.equals(action)) {
                processNoBleAdapter();
            } else if (BleService.BLE_GATT_CONNECTED.equals(action)) {
                processDeviceConnected();
            } else if (BleService.BLE_REQUEST_FAILED.equals(action)) {
                scanLeDevice(true);
            } else if (BleService.BLE_GATT_DISCONNECTED.equals(action)) {
                processDeviceDisconnected();
            } else if (BleService.BLE_SERVICE_DISCOVERED.equals(action)) {
                processGetGattServices(mBle.getServices(mDeviceAddress));
            } else if (BleService.BLE_CHARACTERISTIC_WRITE.equals(action)) {
            } else if (BleService.BLE_CHARACTERISTIC_READ.equals(action)) {
            } else if (BleService.BLE_CHARACTERISTIC_CHANGED.equals(action)) {
                Bundle extras = intent.getExtras();
                String uuid = extras.getString(BleService.EXTRA_UUID);
                byte[] val = extras.getByteArray(BleService.EXTRA_VALUE);

                Bundle bundle = new Bundle();
                bundle.putString("uuid", uuid);
                bundle.putByteArray("raw", val);
                Message msg = new Message();
                msg.setData(bundle);
                mRawHandler.sendMessage(msg);
            }
        }
    };

    /**
     * 解析流数据
     *
     * @param uuid
     * @param rawData
     */
    private synchronized void parseRawData(String uuid, byte[] rawData) {
        String chUuid = uuid.toUpperCase();
        if (0 == chUuid.compareTo("0000FFA3-0000-1000-8000-00805F9B34FB")) {
            if (DATA_MIN_LENGTH > rawData.length) {
                return;
            }

            callBackData = new Data();

            int flag0 = rawData[0] & 0xFF;
            int flag1 = rawData[1] & 0xFF;

            if (flag0 == 0xaa && flag1 == 0x52) {
                //普通测量数据
                callBackData.setData(JPBleDataHandler.rawDataToNormalData(rawData));
                callBackData.setType(Data.TYPE_TEST_DATA);
                callBackData.setUuid(uuid);
                callBackData.setVal(val);
                onCallBack.onDataCallBack(callBackData);
            } else if (flag0 == 0xaa && flag1 == 0x53) {
                callBackData.setType(Data.TYPE_TEST_END);
                callBackData.setPressureData(JPBleDataHandler.rawDataToPressureData(rawData));
                callBackData.setUuid(uuid);
                callBackData.setVal(val);
                onCallBack.onDataCallBack(callBackData);
            }
        } else if (0 == chUuid.compareTo("0000FFA4-0000-1000-8000-00805F9B34FB")) {
            //波形1数据
            waveDecoder.decodeWaveFrame(rawData);
        } else if (0 == chUuid.compareTo("0000FFA5-0000-1000-8000-00805F9B34FB")) {
            //波形2数据
            wave2Decoder.decodeWaveFrame(rawData);
        }
    }

    /**
     * 开始搜索设备
     *
     * @param enable
     */
    public void scanLeDevice(final boolean enable) {
        if (mBle == null)
            mBle = DashuApplication.getInstance().getIBle();

        if (mBle != null && !mBle.adapterEnabled()) {
            // 如果蓝牙未打开
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            context.startActivity(enableBtIntent);
            return;
        }

        if (enable) {
            bleStartWork();
        } else {//停止扫描
            mScanning = false;
            if (mBle != null) {
                mBle.stopScan();
            }
        }
    }



    public void pairDevice(BluetoothDevice bluetoothDevice) {
        mDeviceAddress = bluetoothDevice.getAddress();
        mBle.requestConnect(bluetoothDevice.getAddress());
        scanLeDevice(false);
    }

    /**
     * 蓝牙开始工作
     */
    private void bleStartWork() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mScanning) {
                    mScanning = false;
                    if (mBle != null) {
                        mBle.stopScan();

                        onCallBack.onStatusCallBack(OnCallBack.SEARCH_DEVICES_FAILED);
                    }
                }
            }
        }, SCAN_PERIOD);

        mScanning = true;
        if (mBle != null) {
            mBle.startScan();
        }
    }

    /**
     * 处理不支持BLE
     */
    private void processNotSupportBle() {
        mScanning = false;
        if (mBle != null) {
            mBle.stopScan();
        }

        onCallBack.onStatusCallBack(OnCallBack.NOT_SUPPORT_BLE);
    }

    /**
     * 处理找到指定设备
     *
     * @param intent
     */
    private void processDeviceFound(Intent intent) {
        Bundle extras = intent.getExtras();
        BluetoothDevice device = extras.getParcelable(BleService.EXTRA_DEVICE);
        onCallBack.onDeviceFound(device);
    }

    /**
     * 处理没有适配器
     */
    private void processNoBleAdapter() {
        mScanning = false;
        if (mBle != null) {
            mBle.stopScan();
        }

        onCallBack.onStatusCallBack(OnCallBack.NO_BLE_ADAPTER);
    }

    /**
     * 处理设备已连接
     */
    private void processDeviceConnected() {
        mConnected = true;
        scanLeDevice(false);

        onCallBack.onStatusCallBack(OnCallBack.DEVICE_CONNECTED);
    }

    /**
     * 处理设备已断开
     */
    private void processDeviceDisconnected() {
        mConnected = false;
        onCallBack.onStatusCallBack(OnCallBack.DEVICE_DISCONNECTED);
    }

    /**
     * 断开连接
     */
    public void disconnect() {
        if (mScanning) {
            scanLeDevice(false);
        }
        mBle.disconnect(mDeviceAddress);
//        context.unregisterReceiver(mBleReceiver);
    }

    /**
     * 处理获取到服务
     */
    private void processGetGattServices(List<BleGattService> gattServices) {
        Log.e("TAG", "gattServices:" + gattServices);
        if (gattServices == null) {
            return;
        }

        String uuid = null;

        for (BleGattService gattService : gattServices) {
            uuid = gattService.getUuid().toString().toUpperCase();
            if (JPBleUtils.BLE_SERVICES.containsKey(uuid)) {
                if (0 == uuid.compareTo("0000FFA0-0000-1000-8000-00805F9B34FB")) {
                    //get data service
                    mGetDataService = gattService;
                } else if (0 == uuid.compareTo("00001802-0000-1000-8000-00805F9B34FB")) {
                    //get pressure service
                    mPressureService = gattService;
                } else {
                    continue;
                }
            }
        }

        List<BleGattCharacteristic> gattCharacteristics = null;
        if (null != mGetDataService) {
            gattCharacteristics = mGetDataService.getCharacteristics();

            for (BleGattCharacteristic gattCharacteristic : gattCharacteristics) {
                uuid = gattCharacteristic.getUuid().toString().toUpperCase();
                if (JPBleUtils.BLE_CHARACTERISTICS.containsKey(uuid)) {
                    if (0 == uuid.compareTo("0000FFA1-0000-1000-8000-00805F9B34FB")) {
                        mCharacterEnable = gattCharacteristic;

                        mCharacterEnable.setValue(new byte[]{0x00});
                        mBle.requestWriteCharacteristic(mDeviceAddress, mCharacterEnable, "");
                    } else if (0 == uuid.compareTo("0000FFA3-0000-1000-8000-00805F9B34FB")) {
                        mCharacterData = gattCharacteristic;

                        mBle.requestCharacteristicNotification(mDeviceAddress, mCharacterData);
                    } else if (0 == uuid.compareTo("0000FFA4-0000-1000-8000-00805F9B34FB")) {
                        mCharacterWave1 = gattCharacteristic;

                        mBle.requestCharacteristicNotification(mDeviceAddress, mCharacterWave1);
                    } else if (0 == uuid.compareTo("0000FFA5-0000-1000-8000-00805F9B34FB")) {
                        mCharacterWave2 = gattCharacteristic;

                        mBle.requestCharacteristicNotification(mDeviceAddress, mCharacterWave2);
                    } else {
                        continue;
                    }
                }
            }
        }

        if (null != mPressureService) {
            gattCharacteristics = mPressureService.getCharacteristics();

            for (BleGattCharacteristic gattCharacteristic : gattCharacteristics) {
                uuid = gattCharacteristic.getUuid().toString().toUpperCase();
                if (JPBleUtils.BLE_CHARACTERISTICS.containsKey(uuid)) {
                    if (0 == uuid.compareTo("00002A06-0000-1000-8000-00805F9B34FB")) {
                        mCharacterPressure = gattCharacteristic;
                    } else {
                        continue;
                    }
                }
            }
        }
    }
}
