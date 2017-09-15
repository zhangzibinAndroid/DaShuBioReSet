package com.dashubio.bean;

import java.util.List;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/17 0017
 * 时间： 上午 11:04
 * 描述： 用户列表实体类封装
 */

public class UserListBean {

    /**
     * status : success
     * count : 9
     * data : [{"id":"524","card_id":"320125199709162316","phone":"18795934321","name":"掌柜","sex":"0"},{"id":"281","card_id":"320125199209183411","phone":"13771873361","name":"电饭锅","sex":"0"},{"id":"280","card_id":"320125199867453218","phone":"null","name":"离线注册","sex":"0"},{"id":"279","card_id":"320125199867453218","phone":"13771853308","name":"离线注册","sex":"0"},{"id":"276","card_id":"320125199345672345","phone":"13771854476","name":"体温离线","sex":"1"},{"id":"240","card_id":"320125188208123456","phone":"13771854465","name":"离线1","sex":"2"},{"id":"238","card_id":"320125199209886523","phone":"13771853398","name":"张啧啧啧","sex":"1"},{"id":"237","card_id":"320125199209232314","phone":"13771853360","name":"离线1","sex":"1"},{"id":"113","card_id":"320125199209181235","phone":"18952088164","name":"测试2","sex":"1"}]
     */

    private String status;
    private String count;
    private List<UserListDataBean> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<UserListDataBean> getData() {
        return data;
    }

    public void setData(List<UserListDataBean> data) {
        this.data = data;
    }

    public static class UserListDataBean {
        /**
         * id : 524
         * card_id : 320125199709162316
         * phone : 18795934321
         * name : 掌柜
         * sex : 0
         */

        public String id;
        public String card_id;
        public String phone;
        public String name;
        public String sex;

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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
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
