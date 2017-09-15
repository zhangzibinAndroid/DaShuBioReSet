package com.dashubio.bean;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/13 0013
 * 时间： 下午 3:31
 * 描述： 短信错误返回码实体类
 */

public class ErrorCodeBean {

    /**
     * status : error
     * code : -11305
     */
    private String status;
    private int code;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
