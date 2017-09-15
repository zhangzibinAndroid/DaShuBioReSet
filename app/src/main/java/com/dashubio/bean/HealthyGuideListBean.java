package com.dashubio.bean;

import java.util.List;

/**
 * Created by 张梓彬 on 2017/7/25 0025.
 */

public class HealthyGuideListBean {

    /**
     * status : success
     * data : [{"m_id":"1","addtime":"2016-12-30 16:22:03","id":"129","project":"血氧","val":"94.42","unit":"","day":"20161230","warning":"血氧偏低","name":"吴素清","age":20},{"m_id":"1","addtime":"2017-01-11 16:48:54","id":"177","project":"血氧","val":"0.50","unit":"","day":"20170111","warning":"血氧偏低","name":"吴素清","age":20}]
     */

    private String status;
    private List<ProjectListDataBean> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ProjectListDataBean> getData() {
        return data;
    }

    public void setData(List<ProjectListDataBean> data) {
        this.data = data;
    }

    public static class ProjectListDataBean {
        /**
         * m_id : 1
         * addtime : 2016-12-30 16:22:03
         * id : 129
         * project : 血氧
         * val : 94.42
         * unit :
         * day : 20161230
         * warning : 血氧偏低
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
