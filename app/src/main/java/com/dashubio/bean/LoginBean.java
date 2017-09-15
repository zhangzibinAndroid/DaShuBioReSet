package com.dashubio.bean;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/14 0014
 * 时间： 上午 10:34
 * 描述： 登录成功返回实体类
 */

public class LoginBean {

    /**
     * status : success
     * code : 3
     * t_session_3 : 663cd4f6bbbc8e19bbcae96203d40283
     * company : 第二人生科技
     */

    private String status;
    private String code;
    private String t_session_3;
    private String company;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getT_session_3() {
        return t_session_3;
    }

    public void setT_session_3(String t_session_3) {
        this.t_session_3 = t_session_3;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
