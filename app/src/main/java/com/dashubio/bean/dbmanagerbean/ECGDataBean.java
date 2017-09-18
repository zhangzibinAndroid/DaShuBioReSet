package com.dashubio.bean.dbmanagerbean;

/**
 * 作者： 张梓彬
 * 日期： 2017/9/18 0018
 * 时间： 上午 11:53
 * 描述： 心电
 */

public class ECGDataBean {
    public String id;
    public String rr_max;
    public String rr_min;
    public String mood;
    public String hr;
    public String hrv;
    public String breath;

    public ECGDataBean() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRr_max() {
        return rr_max;
    }

    public void setRr_max(String rr_max) {
        this.rr_max = rr_max;
    }

    public String getRr_min() {
        return rr_min;
    }

    public void setRr_min(String rr_min) {
        this.rr_min = rr_min;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getHr() {
        return hr;
    }

    public void setHr(String hr) {
        this.hr = hr;
    }

    public String getHrv() {
        return hrv;
    }

    public void setHrv(String hrv) {
        this.hrv = hrv;
    }

    public String getBreath() {
        return breath;
    }

    public void setBreath(String breath) {
        this.breath = breath;
    }
}
