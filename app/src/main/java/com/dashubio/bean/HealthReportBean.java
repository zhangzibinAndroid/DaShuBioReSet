package com.dashubio.bean;

import java.util.List;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/20 0020
 * 时间： 上午 11:04
 * 描述： 健康报告实体类分装
 */

public class HealthReportBean {

    /**
     * status : success
     * data : [{"year":"2017","det":[{"month":"07","pro":[{"id":6,"name":"体温"},{"id":8,"name":"血氧"}]}]}]
     */

    private String status;
    private List<HealthDataBean> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<HealthDataBean> getData() {
        return data;
    }

    public void setData(List<HealthDataBean> data) {
        this.data = data;
    }

    public static class HealthDataBean {
        /**
         * year : 2017
         * det : [{"month":"07","pro":[{"id":6,"name":"体温"},{"id":8,"name":"血氧"}]}]
         */

        private String year;
        private List<FatherDetBean> det;

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public List<FatherDetBean> getDet() {
            return det;
        }

        public void setDet(List<FatherDetBean> det) {
            this.det = det;
        }

        public static class FatherDetBean {
            /**
             * month : 07
             * pro : [{"id":6,"name":"体温"},{"id":8,"name":"血氧"}]
             */

            private String month;
            private List<ChildrenProBean> pro;

            public String getMonth() {
                return month;
            }

            public void setMonth(String month) {
                this.month = month;
            }

            public List<ChildrenProBean> getPro() {
                return pro;
            }

            public void setPro(List<ChildrenProBean> pro) {
                this.pro = pro;
            }

            public static class ChildrenProBean {
                /**
                 * id : 6
                 * name : 体温
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
}
