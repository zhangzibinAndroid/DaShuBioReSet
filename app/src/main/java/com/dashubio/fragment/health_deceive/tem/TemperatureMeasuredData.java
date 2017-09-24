package com.dashubio.fragment.health_deceive.tem;

import com.dashubio.fragment.health_deceive.ecg_bean.DetectItem;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 体温
 */
public class TemperatureMeasuredData implements Serializable {

    @SerializedName("6")
    private DetectItem temperature;//体温

    public DetectItem getTemperature() {
        return temperature;
    }

    public void setTemperature(DetectItem temperature) {
        this.temperature = temperature;
    }
}
