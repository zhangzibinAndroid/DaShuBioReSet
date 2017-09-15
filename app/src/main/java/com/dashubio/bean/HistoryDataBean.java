package com.dashubio.bean;

import java.util.List;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/24 0024
 * 时间： 上午 10:41
 * 描述： 历史数据实体类分装
 */

public class HistoryDataBean {

    /**
     * status : success
     * data : [{"adevice":{"id":"1","name":"健康检测仪"},"project":[{"id":5,"name":"血压"},{"id":6,"name":"体温"},{"id":7,"name":"心电"},{"id":8,"name":"血氧"},{"id":9,"name":"心率"}]},{"adevice":{"id":"2","name":"干式生化仪"},"project":[{"id":"1","name":"六项检测"},{"id":"2","name":"两项检测"}]},{"adevice":{"id":"3","name":"尿液分析仪"},"project":[{"id":"1","name":"尿液分析仪检测数据"}]}]
     */

    private String status;
    private List<HistoryData> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<HistoryData> getData() {
        return data;
    }

    public void setData(List<HistoryData> data) {
        this.data = data;
    }

    public static class HistoryData {
        /**
         * adevice : {"id":"1","name":"健康检测仪"}
         * project : [{"id":5,"name":"血压"},{"id":6,"name":"体温"},{"id":7,"name":"心电"},{"id":8,"name":"血氧"},{"id":9,"name":"心率"}]
         */

        private AdeviceBean adevice;
        private List<ProjectBean> project;

        public AdeviceBean getAdevice() {
            return adevice;
        }

        public void setAdevice(AdeviceBean adevice) {
            this.adevice = adevice;
        }

        public List<ProjectBean> getProject() {
            return project;
        }

        public void setProject(List<ProjectBean> project) {
            this.project = project;
        }

        public static class AdeviceBean {
            /**
             * id : 1
             * name : 健康检测仪
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

        public static class ProjectBean {
            /**
             * id : 5
             * name : 血压
             */

            private int id;
            private String name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
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
}
