package com.dashubio.bean.eventmsg;

/**
 * 作者： 张梓彬
 * 日期： 2017/9/14 0014
 * 时间： 上午 10:54
 * 描述： EventBus传递消息
 */

public class EventMessage {
    public String msg;

    public EventMessage(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
