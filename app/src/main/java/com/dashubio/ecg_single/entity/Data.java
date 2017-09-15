package com.dashubio.ecg_single.entity;


import com.dashubio.ecg_single.ble.JPBleNormalData;
import com.dashubio.ecg_single.ble.JPBlePressureData;

import java.util.ArrayList;

/**
 * Created by altria on 17-7-7.
 */

public class Data {
    public static final int TYPE_TEST_DATA = 0;//普通测量数据
    public static final int TYPE_TEST_END = 1;//测量结束
    public static final int TYPE_WAVE_1_DATA= 2;//波形1
    public static final int TYPE_WAVE_2_DATA = 3;//波形2
    private int type;//数据类型
    private String uuid;
    private byte[] val;
    private JPBleNormalData data;
    private JPBlePressureData pressureData;
    private ArrayList<Float> waveData;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public byte[] getVal() {
        return val;
    }

    public void setVal(byte[] val) {
        this.val = val;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public JPBleNormalData getData() {
        return data;
    }

    public void setData(JPBleNormalData data) {
        this.data = data;
    }

    public JPBlePressureData getPressureData() {
        return pressureData;
    }

    public void setPressureData(JPBlePressureData pressureData) {
        this.pressureData = pressureData;
    }

    public ArrayList<Float> getWaveData() {
        return waveData;
    }

    public void setWaveData(ArrayList<Float> waveData) {
        this.waveData = waveData;
    }
}
