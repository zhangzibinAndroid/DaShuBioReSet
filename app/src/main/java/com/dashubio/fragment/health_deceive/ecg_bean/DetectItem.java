package com.dashubio.fragment.health_deceive.ecg_bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 测量Item
 */
public class DetectItem implements Serializable{

    @SerializedName("val")
    private float value;//检测值

    private String unit;//单位

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
