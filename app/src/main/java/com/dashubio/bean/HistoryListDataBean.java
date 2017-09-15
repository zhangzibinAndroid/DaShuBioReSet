package com.dashubio.bean;

import java.util.List;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/24 0024
 * 时间： 下午 3:24
 * 描述： 历史数据列表实体类封装
 */
public class HistoryListDataBean {

    /**
     * status : success
     * data : [{"m_id":"1","addtime":"2016-12-14 11:54:19","id":"3","project":"体温","val":"40.00","unit":"℃","day":"20161214","warning":"体温偏高","name":"吴素清","age":20},{"m_id":"1","addtime":"2016-12-16 09:49:12","id":"51","project":"体温","val":"36.27","unit":"℃","day":"20161216","warning":"","name":"吴素清","age":20},{"m_id":"1","addtime":"2016-12-28 16:08:26","id":"118","project":"体温","val":"20.35","unit":"℃","day":"20161228","warning":"体温偏低","name":"吴素清","age":20}]
     */

    private String status;
    private List<HistoryDataBean> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<HistoryDataBean> getData() {
        return data;
    }

    public void setData(List<HistoryDataBean> data) {
        this.data = data;
    }

    public static class HistoryDataBean {
        /**
         * m_id : 1
         * addtime : 2016-12-14 11:54:19
         * id : 3
         * project : 体温
         * val : 40.00
         * unit : ℃
         * day : 20161214
         * warning : 体温偏高
         * name : 吴素清
         * age : 20
         */

        private String m_id;
        private String addtime;
        private String id;
        private String project;
        private String val;
        private String unit;
        private String day;
        private String warning;
        private String name;
        private int age;

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

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
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

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
