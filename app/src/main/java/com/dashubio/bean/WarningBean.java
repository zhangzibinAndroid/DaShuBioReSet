package com.dashubio.bean;

import java.util.List;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/14 0014
 * 时间： 下午 4:00
 * 描述： 预警列表实体类封装
 */

public class WarningBean {

    /**
     * status : success
     * data : [{"id":"1","c_id":"0","e_id":"0","project":"1-1","p_name":"高密度脂蛋白胆固醇","min_v":"1.00","min_warn":"高密度脂蛋白胆固醇较低","max_v":"1.90","max_warn":"高密度脂蛋白胆固醇较高","unit":"mmol/l","content":"抗动脉粥样硬化","level":"1","status":"1"},{"id":"2","c_id":"0","e_id":"0","project":"1-2","p_name":"葡萄糖","min_v":"3.90","min_warn":"葡萄糖含量较低","max_v":"6.10","max_warn":"葡萄糖含量较高","unit":"mmol/l","content":"人体血糖在3.9～7.8mmol/l之间","level":"1","status":"1"},{"id":"3","c_id":"0","e_id":"0","project":"1-3","p_name":"总胆固醇","min_v":"3.12","min_warn":"总胆固醇偏低","max_v":"5.18","max_warn":"总胆固醇偏高","unit":"mmol/l","content":"人体胆固醇在3.12～5.18mmol/l之间","level":"1","status":"1"},{"id":"4","c_id":"0","e_id":"0","project":"1-4","p_name":"甘油三脂","min_v":"0.44","min_warn":"甘油三脂偏低","max_v":"1.70","max_warn":"甘油三脂偏高","unit":"mmol/l","content":"甘油三脂在0.44~1.70mmol/l之间","level":"1","status":"1"},{"id":"5","c_id":"0","e_id":"0","project":"1-5","p_name":"极低密度脂蛋白胆固醇","min_v":"0.21","min_warn":"极低密度脂蛋白胆固醇偏高","max_v":"0.78","max_warn":"极低密度脂蛋白胆固醇偏高","unit":"mmol/l","content":"极低密度脂蛋白胆固醇0.21~0.78mmol/l之间","level":"1","status":"1"},{"id":"6","c_id":"0","e_id":"0","project":"1-6","p_name":"低密度脂蛋白胆固醇","min_v":"0.00","min_warn":"低密度脂蛋白胆固醇偏低","max_v":"3.10","max_warn":"低密度脂蛋白胆固醇偏高","unit":"mmol/l","content":"低密度脂蛋白胆固醇0.00~3.10mmol/l之间","level":"1","status":"1"},{"id":"7","c_id":"0","e_id":"0","project":"3","p_name":"谷丙转氨酶","min_v":"0.00","min_warn":"谷丙转氨酶偏低","max_v":"40.00","max_warn":"谷丙转氨酶偏高","unit":"u/l","content":"谷丙转氨酶在0.00~40.0u/l之间","level":"1","status":"1"},{"id":"8","c_id":"0","e_id":"0","project":"4","p_name":"血红蛋白","min_v":"110.00","min_warn":"血红蛋白偏低","max_v":"160.00","max_warn":"血红蛋白偏高","unit":"g/l","content":"血红蛋白在110.00~160.00g/l之间","level":"1","status":"1"},{"id":"9","c_id":"0","e_id":"0","project":"5-2","p_name":"收缩压","min_v":"110.00","min_warn":"收缩压偏低","max_v":"139.00","max_warn":"收缩压偏高","unit":"mmHg","content":"收缩压在90.00~139.00mmHg之间","level":"1","status":"1"},{"id":"10","c_id":"0","e_id":"0","project":"5-1","p_name":"舒张压","min_v":"70.00","min_warn":"舒张压偏低","max_v":"89.00","max_warn":"舒张压偏高","unit":"mmHg","content":"舒张压在90.00~139.00mmHg之间","level":"1","status":"1"},{"id":"11","c_id":"0","e_id":"0","project":"6","p_name":"体温","min_v":"36.00","min_warn":"体温偏低","max_v":"37.00","max_warn":"体温偏高","unit":"℃","content":"体温在36.00~37.00℃之间","level":"1","status":"1"},{"id":"12","c_id":"0","e_id":"0","project":"8","p_name":"血氧","min_v":"95.00","min_warn":"血氧偏低","max_v":"99.00","max_warn":"血氧偏高","unit":"-","content":"血氧在95%~99%之间","level":"1","status":"1"},{"id":"13","c_id":"0","e_id":"0","project":"9","p_name":"心率","min_v":"60.00","min_warn":"心率偏低","max_v":"100.00","max_warn":"心率偏高","unit":"次","content":"心率在60~100次之间","level":"1","status":"1"}]
     */

    private String status;
    private List<WarningDataBean> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<WarningDataBean> getData() {
        return data;
    }

    public void setData(List<WarningDataBean> data) {
        this.data = data;
    }

    public static class WarningDataBean {
        /**
         * id : 1
         * c_id : 0
         * e_id : 0
         * project : 1-1
         * p_name : 高密度脂蛋白胆固醇
         * min_v : 1.00
         * min_warn : 高密度脂蛋白胆固醇较低
         * max_v : 1.90
         * max_warn : 高密度脂蛋白胆固醇较高
         * unit : mmol/l
         * content : 抗动脉粥样硬化
         * level : 1
         * status : 1
         */

        private String id;
        private String c_id;
        private String e_id;
        private String project;
        private String p_name;
        private String min_v;
        private String min_warn;
        private String max_v;
        private String max_warn;
        private String unit;
        private String content;
        private String level;
        private String status;

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

        public String getE_id() {
            return e_id;
        }

        public void setE_id(String e_id) {
            this.e_id = e_id;
        }

        public String getProject() {
            return project;
        }

        public void setProject(String project) {
            this.project = project;
        }

        public String getP_name() {
            return p_name;
        }

        public void setP_name(String p_name) {
            this.p_name = p_name;
        }

        public String getMin_v() {
            return min_v;
        }

        public void setMin_v(String min_v) {
            this.min_v = min_v;
        }

        public String getMin_warn() {
            return min_warn;
        }

        public void setMin_warn(String min_warn) {
            this.min_warn = min_warn;
        }

        public String getMax_v() {
            return max_v;
        }

        public void setMax_v(String max_v) {
            this.max_v = max_v;
        }

        public String getMax_warn() {
            return max_warn;
        }

        public void setMax_warn(String max_warn) {
            this.max_warn = max_warn;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
