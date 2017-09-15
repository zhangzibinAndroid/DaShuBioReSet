package com.dashubio.utils;

import android.text.TextUtils;
import android.util.Log;

import com.dashubio.bean.DryDetectItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 干式生化仪检测结果
 */
public class DryDetectResult {
    private static final String TAG = "DryDetectResult";
    private String receivedData;//接收到的原生数据
    private int deviceNumber;//设备号
    private int flag;//标志位
    private int itemCnt;//检测项目数
    private List<DryDetectItem> itemList = new ArrayList<>();

    public String getReceivedData() {
        return receivedData;
    }

    public void setReceivedData(String receivedData) {
        this.receivedData = receivedData;
    }

    public int getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(int deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getItemCnt() {
        return itemCnt;
    }

    public void setItemCnt(int itemCnt) {
        this.itemCnt = itemCnt;
    }

    public List<DryDetectItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<DryDetectItem> itemList) {
        this.itemList = itemList;
    }

    public static DryDetectResult parseData(String receivedData){
        DryDetectResult result = new DryDetectResult();
        if(TextUtils.isEmpty(receivedData) || receivedData.length() < 72){
            return result;
        }

        result.setReceivedData(receivedData);

        int deviceNumber =  Utils.hexStr2Int(receivedData.substring(6, 14)); //设备号
        result.setDeviceNumber(deviceNumber);
        Log.e(TAG, "deviceNumber = " + deviceNumber);

        int flag = Utils.hexStr2Int(receivedData.substring(14, 16)); //标志位
        result.setFlag(flag);
        Log.e(TAG, "flag = " + flag);
        int itemCnt = Utils.hexStr2Int(receivedData.substring(28, 30)); //检测项目数
        result.setItemCnt(itemCnt);
        Log.e(TAG, "itemCnt = " + itemCnt );

        switch (flag){
            case 1: //六项：HDL-C  GLU  TC  TG  VLDL-C  LDL-C
                int HDL = Utils.hexStr2Int(receivedData.substring(32, 36));
                float hdlReal = (float) HDL / 100;
                DryDetectItem hdl_c_Item = new DryDetectItem();
                hdl_c_Item.setName("高密度脂蛋白胆固醇（HDL-C）");
                hdl_c_Item.setValue(hdlReal);
                hdl_c_Item.setReferenceRange("1.00-1.90mmol/L");
                hdl_c_Item.setUnit("mmol/L");
                hdl_c_Item.setShowMin(0.40f);
                hdl_c_Item.setShowMax(3.90f);
                result.getItemList().add(hdl_c_Item);
                Log.e(TAG, "HDL = " + HDL + " hdlReal = " + hdlReal );

                int GLU = Utils.hexStr2Int(receivedData.substring(38, 42));
                float gluReal = (float) GLU / 10;
                DryDetectItem glu_Item = new DryDetectItem();
                glu_Item.setName("葡萄糖（GLU）");
                glu_Item.setValue(gluReal);
                glu_Item.setUnit("mmol/L");
                glu_Item.setReferenceRange("3.9-6.1mmol/L");
                glu_Item.setShowMin(2.0f);
                glu_Item.setShowMax(20.0f);
                result.getItemList().add(glu_Item);
                Log.e(TAG, "GLU = " + GLU + " gluReal = " + gluReal);

                int TC = Utils.hexStr2Int(receivedData.substring(44, 48));
                float tcReal = (float) TC / 100;
                DryDetectItem tc_Item = new DryDetectItem();
                tc_Item.setName("总胆固醇（TC）");
                tc_Item.setValue(tcReal);
                tc_Item.setUnit("mmol/L");
                tc_Item.setReferenceRange("3.12-5.18mmol/L");
                tc_Item.setShowMin(2.00f);
                tc_Item.setShowMax(12.00f);
                result.getItemList().add(tc_Item);
                Log.e(TAG, "TC = " + TC + " tcReal = " + tcReal);

                int TG = Utils.hexStr2Int(receivedData.substring(50, 54));
                float tgReal = (float) TG / 100;
                DryDetectItem tg_Item = new DryDetectItem();
                tg_Item.setName("甘油三酯（TG）");
                tg_Item.setValue(tgReal);
                tg_Item.setUnit("mmol/L");
                tg_Item.setReferenceRange("0.44-1.70mmol/L");
                tg_Item.setShowMin(0.40f);
                tg_Item.setShowMax(6.00f);
                result.getItemList().add(tg_Item);
                Log.e(TAG, "TG = " + TG + " tgReal = " + tgReal );

                int VLDL = Utils.hexStr2Int(receivedData.substring(56, 60));
                float vldlReal = (float) VLDL / 100;
                DryDetectItem vldl_c_Item = new DryDetectItem();
                vldl_c_Item.setName("极低密码脂蛋白胆固醇（VLDL-C）");
                vldl_c_Item.setValue(vldlReal);
                vldl_c_Item.setUnit("mmol/L");
                vldl_c_Item.setReferenceRange("0.21-0.78mmol/L");
                result.getItemList().add(vldl_c_Item);
                Log.e(TAG, "VLDL = " + VLDL + " vldlReal = " + vldlReal);

                int LDL = Utils.hexStr2Int(receivedData.substring(62, 66));
                float ldlReal = (float) LDL / 100;
                DryDetectItem ldl_c_Item = new DryDetectItem();
                ldl_c_Item.setName("低密码脂蛋白胆固醇（LDL-C）");
                ldl_c_Item.setValue(ldlReal);
                ldl_c_Item.setUnit("mmol/L");
                ldl_c_Item.setReferenceRange("0.00-3.10 mmol/L");
                result.getItemList().add(ldl_c_Item);
                Log.e(TAG, "LDL = " + LDL + " ldlReal = " + ldlReal );

                break;

            case 2: //两项：ALT  Hb
                int ALT = Utils.hexStr2Int(receivedData.substring(32, 36));
                float altReal = (float) ALT / 10;
                DryDetectItem alt_Item = new DryDetectItem();
                alt_Item.setName("谷丙转氨酶（ALT）");
                alt_Item.setValue(altReal);
                alt_Item.setUnit("U/L");
                alt_Item.setReferenceRange("≤40.0U/L");
                alt_Item.setShowMin(5.0f);
                alt_Item.setShowMax(200.0f);
                result.getItemList().add(alt_Item);
                Log.e(TAG, "ALT = " + ALT + " altReal = " + altReal );

                int Hb = Utils.hexStr2Int(receivedData.substring(38, 42));
                DryDetectItem hb_Item = new DryDetectItem();
                hb_Item.setName("血红蛋白（Hb）");
                hb_Item.setValue(Hb);
                hb_Item.setUnit("g/L");
                hb_Item.setReferenceRange("110-160g/L");
                hb_Item.setShowMin(40f);
                hb_Item.setShowMax(256f);
                result.getItemList().add(hb_Item);
                Log.e(TAG, "Hb = " + Hb );
                break;

            case 3: //单项：ALT
                int individualALT = Utils.hexStr2Int(receivedData.substring(32, 36));
                float individualALTReal = (float) individualALT / 10;
                DryDetectItem individual_alt_Item = new DryDetectItem();
                individual_alt_Item.setName("谷丙转氨酶（ALT）");
                individual_alt_Item.setValue(individualALTReal);
                individual_alt_Item.setUnit("U/L");
                individual_alt_Item.setReferenceRange("≤40.0U/L");
                individual_alt_Item.setShowMin(5.0f);
                individual_alt_Item.setShowMax(200.0f);
                result.getItemList().add(individual_alt_Item);
                Log.e(TAG, "individualALT = " + individualALT + " individualALTReal = " + individualALTReal );
                break;

            case 4: //单项：Hb
                int individualHb = Utils.hexStr2Int(receivedData.substring(32, 36));
                DryDetectItem individual_hb_Item = new DryDetectItem();
                individual_hb_Item.setName("血红蛋白（Hb）");
                individual_hb_Item.setValue(individualHb);
                individual_hb_Item.setUnit("g/L");
                individual_hb_Item.setReferenceRange("110-160g/L");
                individual_hb_Item.setShowMin(40f);
                individual_hb_Item.setShowMax(256f);
                result.getItemList().add(individual_hb_Item);
                Log.e(TAG, "individualHb = " + individualHb );
                break;
        }

        return result;
    }
}
