package com.dashubio.bean.dbmanagerbean;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/21 0021
 * 时间： 上午 10:41
 * 描述： 用来接收离线版用户名和密码
 */

public class LoginUserBean {
    public int    _id;
    public String name;
    public String pwds;

    public LoginUserBean() {
    }

    public LoginUserBean(int _id, String name, String pwds) {
        this._id = _id;
        this.name = name;
        this.pwds = pwds;
    }

    public int get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getPwds() {
        return pwds;
    }


}
