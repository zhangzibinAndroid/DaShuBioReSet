package com.dashubio.bean.dbmanagerbean;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/22 0022
 * 时间： 下午 5:07
 * 描述： 呼吸本地数据接收类
 */

public class BreathingBean {
    public String id;
    public String date;
    public String tvPef;
    public String tvFvc;
    public String tvFev1;

    public BreathingBean() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTvPef() {
        return tvPef;
    }

    public void setTvPef(String tvPef) {
        this.tvPef = tvPef;
    }

    public String getTvFvc() {
        return tvFvc;
    }

    public void setTvFvc(String tvFvc) {
        this.tvFvc = tvFvc;
    }

    public String getTvFev1() {
        return tvFev1;
    }

    public void setTvFev1(String tvFev1) {
        this.tvFev1 = tvFev1;
    }
}
