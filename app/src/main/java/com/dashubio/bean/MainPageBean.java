package com.dashubio.bean;

import java.util.List;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/18 0018
 * 时间： 下午 4:01
 * 描述： 首页适配器
 */

public class MainPageBean {

    /**
     * status : success
     * data : [{"m_id":"282","addtime":"2017-07-18 15:38:02","id":"5011","project":"心电","val":"心率:0.00;心率变异性数据:0.00;心情数据:0.00;RRMAX 数据:0.00;RRMIX 数据:0.00;呼吸率:0.00;","unit":"-","warning":"","name":"离线一","age":0},{"m_id":"239","addtime":"2017-07-18 15:19:09","id":"5010","project":"体温","val":"35.86","unit":"℃","warning":"体温偏低","name":"离线测试","age":0},{"m_id":"282","addtime":"2017-07-18 15:18:45","id":"5009","project":"体温","val":"36.61","unit":"℃","warning":"","name":"离线一","age":0}]
     */

    private String status;
    private List<MessageDataBean> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<MessageDataBean> getData() {
        return data;
    }

    public void setData(List<MessageDataBean> data) {
        this.data = data;
    }

    public static class MessageDataBean {
        /**
         * m_id : 282
         * addtime : 2017-07-18 15:38:02
         * id : 5011
         * project : 心电
         * val : 心率:0.00;心率变异性数据:0.00;心情数据:0.00;RRMAX 数据:0.00;RRMIX 数据:0.00;呼吸率:0.00;
         * unit : -
         * warning :
         * name : 离线一
         * age : 0
         */

        private String m_id;
        private String addtime;
        private String id;
        private String project;
        private String val;
        private String unit;
        private String warning;
        private String name;
        private String age;

        public String getM_id() {
            return m_id;
        }

        public void setM_id(String m_id) {
            this.m_id = m_id;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProject() {
            return project;
        }

        public void setProject(String project) {
            this.project = project;
        }

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getWarning() {
            return warning;
        }

        public void setWarning(String warning) {
            this.warning = warning;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }
    }
}
