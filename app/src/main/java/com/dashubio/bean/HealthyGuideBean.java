package com.dashubio.bean;

import java.util.List;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/25 0025
 * 时间： 上午 10:11
 * 描述： 健康指导项目实体类封装
 */
public class HealthyGuideBean {

    /**
     * status : success
     * data : [{"id":8,"name":"血氧"},{"id":"7-1","name":"心率"},{"id":6,"name":"体温"},{"id":5,"name":"血压"}]
     */

    private String status;
    private List<ProjectDataBean> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ProjectDataBean> getData() {
        return data;
    }

    public void setData(List<ProjectDataBean> data) {
        this.data = data;
    }

    public static class ProjectDataBean {
        /**
         * id : 8
         * name : 血氧
         */

        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
