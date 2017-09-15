package com.dashubio.bean.viewbean;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/26 0026
 * 时间： 下午 4:55
 * 描述： 手术
 */

public class SurgeryBean {
    private String name;
    private String dateTime;
    public SurgeryBean() {
    }

    public SurgeryBean(String name, String dateTime) {
        this.name = name;
        this.dateTime = dateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return dateTime;
    }

    public void setTime(String time) {
        this.dateTime = time;
    }
}
