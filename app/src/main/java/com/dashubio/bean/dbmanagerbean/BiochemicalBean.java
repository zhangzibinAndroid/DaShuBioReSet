package com.dashubio.bean.dbmanagerbean;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/22 0022
 * 时间： 下午 4:44
 * 描述： 干式生化仪数据库保存类封装
 */

public class BiochemicalBean {
    public String id;
    public String message;

    public BiochemicalBean() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
