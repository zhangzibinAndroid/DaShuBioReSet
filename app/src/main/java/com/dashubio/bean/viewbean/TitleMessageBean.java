package com.dashubio.bean.viewbean;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/23 0023
 * 时间： 上午 11:27
 * 描述： 标题信息
 */

public class TitleMessageBean {

    /**
     * status : success
     * data : {"id":"4","c_id":"0","d_id":"0","con":"核心提示：血糖监测是维持良好血糖的必要工具，任何一种治疗计划都必须有监测的指标来获知成效，血糖监测是最直接的指标。 经常的血糖监测除了可以避免高血糖带来的问题外，还可以预防低血糖的发生。经常的血糖监测除了可以避免高血糖带来的问题外，还可以预防低血糖的发生。","status":"2","addtime":"1491805993"}
     */

    private String status;
    private MessageDataBean data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MessageDataBean getData() {
        return data;
    }

    public void setData(MessageDataBean data) {
        this.data = data;
    }

    public static class MessageDataBean {
        /**
         * id : 4
         * c_id : 0
         * d_id : 0
         * con : 核心提示：血糖监测是维持良好血糖的必要工具，任何一种治疗计划都必须有监测的指标来获知成效，血糖监测是最直接的指标。 经常的血糖监测除了可以避免高血糖带来的问题外，还可以预防低血糖的发生。经常的血糖监测除了可以避免高血糖带来的问题外，还可以预防低血糖的发生。
         * status : 2
         * addtime : 1491805993
         */

        private String id;
        private String c_id;
        private String d_id;
        private String con;
        private String status;
        private String addtime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getC_id() {
            return c_id;
        }

        public void setC_id(String c_id) {
            this.c_id = c_id;
        }

        public String getD_id() {
            return d_id;
        }

        public void setD_id(String d_id) {
            this.d_id = d_id;
        }

        public String getCon() {
            return con;
        }

        public void setCon(String con) {
            this.con = con;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }
    }
}
