package com.dashubio.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 干式生化仪检测项
 */
public class DryDetectItem implements Serializable{

    public final static float REAL_VALUE = -1f;//实际值

    private String name;//项目名

    @SerializedName("val")
    private float value;//检测值

    private String unit;//单位
    private String referenceRange;//参考范围
    private float showMin = REAL_VALUE;//最小值
    private float showMax = REAL_VALUE;//最大值

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getReferenceRange() {
        return referenceRange;
    }

    public void setReferenceRange(String referenceRange) {
        this.referenceRange = referenceRange;
    }

    public float getShowMin() {
        return showMin;
    }

    public void setShowMin(float showMin) {
        this.showMin = showMin;
    }

    public float getShowMax() {
        return showMax;
    }

    public void setShowMax(float showMax) {
        this.showMax = showMax;
    }
}
