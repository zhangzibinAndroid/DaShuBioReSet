package com.dashubio.fragment.health_deceive.ecg_bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 心电
 */
public class EcgMeasuredData implements Serializable {
    public String user_id;

    @SerializedName("7-1")
    private DetectItem heartRate = new DetectItem();//心率

    @SerializedName("7-2")
    private DetectItem prmax = new DetectItem();//RRMAX 数据

    @SerializedName("7-3")
    private DetectItem prmin = new DetectItem();//RRMIX 数据

    @SerializedName("7-4")
    private DetectItem heartRateVariability = new DetectItem();//心率变异性数据

    @SerializedName("7-5")
    private DetectItem mood = new DetectItem();//心情数据

    @SerializedName("7-6")
    private DetectItem respirationRate = new DetectItem();//呼吸率

    public DetectItem getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(DetectItem heartRate) {
        this.heartRate = heartRate;
    }

    public DetectItem getPrmax() {
        return prmax;
    }

    public void setPrmax(DetectItem prmax) {
        this.prmax = prmax;
    }

    public DetectItem getPrmin() {
        return prmin;
    }

    public void setPrmin(DetectItem prmin) {
        this.prmin = prmin;
    }

    public DetectItem getHeartRateVariability() {
        return heartRateVariability;
    }

    public void setHeartRateVariability(DetectItem heartRateVariability) {
        this.heartRateVariability = heartRateVariability;
    }

    public DetectItem getMood() {
        return mood;
    }

    public void setMood(DetectItem mood) {
        this.mood = mood;
    }

    public DetectItem getRespirationRate() {
        return respirationRate;
    }

    public void setRespirationRate(DetectItem respirationRate) {
        this.respirationRate = respirationRate;
    }
}
