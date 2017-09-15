package com.dashubio.bean;

import java.util.List;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/25 0025
 * 时间： 下午 7:04
 * 描述： 健康报告综合列表实体类封装
 */
public class HealthReportSecondBean {

    /**
     * status : success
     * data : [{"year":"2017","det":[{"month":"06"},{"month":"04"},{"month":"02"},{"month":"01"}]},{"year":"2016","det":[{"month":"12"}]}]
     */

    private String status;
    private List<HealthReportTimeDataBean> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<HealthReportTimeDataBean> getData() {
        return data;
    }

    public void setData(List<HealthReportTimeDataBean> data) {
        this.data = data;
    }

    public static class HealthReportTimeDataBean {
        /**
         * year : 2017
         * det : [{"month":"06"},{"month":"04"},{"month":"02"},{"month":"01"}]
         */

        private String year;
        private List<HealthReportDetBean> det;

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public List<HealthReportDetBean> getDet() {
            return det;
        }

        public void setDet(List<HealthReportDetBean> det) {
            this.det = det;
        }

        public static class HealthReportDetBean {
            /**
             * month : 06
             */

            private String month;

            public String getMonth() {
                return month;
            }

            public void setMonth(String month) {
                this.month = month;
            }
        }
    }
}
