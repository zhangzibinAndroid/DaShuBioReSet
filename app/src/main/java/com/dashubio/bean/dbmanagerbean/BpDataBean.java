package com.dashubio.bean.dbmanagerbean;

/**
 * 作者： 张梓彬
 * 日期： 2017/9/18 0018
 * 时间： 上午 11:44
 * 描述： 血压
 */

public class BpDataBean {
    public String id;
    public String sys;
    public String dia;

    public BpDataBean() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSys() {
        return sys;
    }

    public void setSys(String sys) {
        this.sys = sys;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }
}
