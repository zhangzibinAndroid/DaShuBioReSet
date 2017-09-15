package com.dashubio.bean;

/**
 * Created by 张梓彬 on 2017/7/18 0018.
 */

public class UserLoginBean {

    /**
     * status : success
     * data : {"id":"282","card_id":"320125199209183412","name":"离线一","sex":"0"}
     */

    private String status;
    private UserLoginDataBean data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserLoginDataBean getData() {
        return data;
    }

    public void setData(UserLoginDataBean data) {
        this.data = data;
    }

    public static class UserLoginDataBean {
        /**
         * id : 282
         * card_id : 320125199209183412
         * name : 离线一
         * sex : 0
         */

        private String id;
        private String card_id;
        private String name;
        private String sex;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCard_id() {
            return card_id;
        }

        public void setCard_id(String card_id) {
            this.card_id = card_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }
    }
}
