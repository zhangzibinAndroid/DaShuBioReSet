package com.dashubio.bean;

import java.util.List;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/21 0021
 * 时间： 下午 3:08
 * 描述： 健康档案实体类封装
 */

public class HealthArchivesBean {

    /**
     * status : success
     * data : {"id":"783","e_id":"3","phone":"13198968180","phone_contacts":"13771853364","card_img":"","name":"张梓彬","sex":"1","birth":"1992-09-18","nation":"汉族","card_id":"320125199209182318","address":"江苏省高淳县淳溪镇城东村咀头131号","province":"江苏省","city":"南京市","district":"玄武区","resident":"0","work":"新东方","blood":"5","blood_hr":"1","edu":"5","occ":"8","marr":"5","test":null,"status":"1","addtime":"1500602916","lasttime":null,"m_id":"759","pay_type":"1","allergor":"4","expose":"4","p_dis":[{"name":"高血压","id":"2"},{"name":"糖尿病","id":"3"},{"name":"冠心病","id":"4"},{"name":"慢性阻塞性肺疾病","id":"5"},{"name":"恶性肿瘤","id":"6"},{"name":"脑卒中","id":"7"},{"name":"重型精神疾病","id":"8"},{"name":"结核病","id":"9"},{"name":"肝炎","id":"10"},{"name":"其他法定遗传病","id":"11"},{"name":"职业病","id":"12"}],"p_opera":[{"dateTime":"2017-07-21","name":"尴尬"},{"dateTime":"2017-07-27","name":"幸福"}],"p_trau":[{"dateTime":"2017-07-29","name":"发给"},{"dateTime":"2017-07-21","name":"发个"}],"p_trans":[{"dateTime":"2017-07-26","name":"发疯"},{"dateTime":"2017-07-22","name":"嘿嘿"}],"f_p":[{"name":"高血压","id":"2"},{"name":"糖尿病","id":"3"},{"name":"冠心病","id":"4"},{"name":"慢性阻塞性肺疾病","id":"5"},{"name":"恶性肿瘤","id":"6"},{"name":"脑卒中","id":"7"},{"name":"重型精神疾病","id":"8"},{"name":"结核病","id":"9"},{"name":"肝炎","id":"10"},{"name":"其他法定遗传病","id":"11"}],"f_m":[{"name":"高血压","id":"2"},{"name":"糖尿病","id":"3"},{"name":"冠心病","id":"4"},{"name":"慢性阻塞性肺疾病","id":"5"},{"name":"恶性肿瘤","id":"6"},{"name":"脑卒中","id":"7"},{"name":"重型精神疾病","id":"8"},{"name":"结核病","id":"9"},{"name":"肝炎","id":"10"},{"name":"其他法定遗传病","id":"11"}],"f_chi":[{"name":"高血压","id":"2"},{"name":"糖尿病","id":"3"},{"name":"冠心病","id":"4"},{"name":"慢性阻塞性肺疾病","id":"5"},{"name":"恶性肿瘤","id":"6"},{"name":"脑卒中","id":"7"},{"name":"重型精神疾病","id":"8"},{"name":"结核病","id":"9"},{"name":"肝炎","id":"10"},{"name":"其他法定遗传病","id":"11"}],"inheri":"心脏病","deformity":"1","exha":"3","fuel":"6","water":"1","wc":"3","poultry":"1"}
     */

    private String status;
    private UserMessageDataBean data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserMessageDataBean getData() {
        return data;
    }

    public void setData(UserMessageDataBean data) {
        this.data = data;
    }

    public static class UserMessageDataBean {
        /**
         * id : 783
         * e_id : 3
         * phone : 13198968180
         * phone_contacts : 13771853364
         * card_img :
         * name : 张梓彬
         * sex : 1
         * birth : 1992-09-18
         * nation : 汉族
         * card_id : 320125199209182318
         * address : 江苏省高淳县淳溪镇城东村咀头131号
         * province : 江苏省
         * city : 南京市
         * district : 玄武区
         * resident : 0
         * work : 新东方
         * blood : 5
         * blood_hr : 1
         * edu : 5
         * occ : 8
         * marr : 5
         * test : null
         * status : 1
         * addtime : 1500602916
         * lasttime : null
         * m_id : 759
         * pay_type : 1
         * allergor : 4
         * expose : 4
         * p_dis : [{"name":"高血压","id":"2"},{"name":"糖尿病","id":"3"},{"name":"冠心病","id":"4"},{"name":"慢性阻塞性肺疾病","id":"5"},{"name":"恶性肿瘤","id":"6"},{"name":"脑卒中","id":"7"},{"name":"重型精神疾病","id":"8"},{"name":"结核病","id":"9"},{"name":"肝炎","id":"10"},{"name":"其他法定遗传病","id":"11"},{"name":"职业病","id":"12"}]
         * p_opera : [{"dateTime":"2017-07-21","name":"尴尬"},{"dateTime":"2017-07-27","name":"幸福"}]
         * p_trau : [{"dateTime":"2017-07-29","name":"发给"},{"dateTime":"2017-07-21","name":"发个"}]
         * p_trans : [{"dateTime":"2017-07-26","name":"发疯"},{"dateTime":"2017-07-22","name":"嘿嘿"}]
         * f_p : [{"name":"高血压","id":"2"},{"name":"糖尿病","id":"3"},{"name":"冠心病","id":"4"},{"name":"慢性阻塞性肺疾病","id":"5"},{"name":"恶性肿瘤","id":"6"},{"name":"脑卒中","id":"7"},{"name":"重型精神疾病","id":"8"},{"name":"结核病","id":"9"},{"name":"肝炎","id":"10"},{"name":"其他法定遗传病","id":"11"}]
         * f_m : [{"name":"高血压","id":"2"},{"name":"糖尿病","id":"3"},{"name":"冠心病","id":"4"},{"name":"慢性阻塞性肺疾病","id":"5"},{"name":"恶性肿瘤","id":"6"},{"name":"脑卒中","id":"7"},{"name":"重型精神疾病","id":"8"},{"name":"结核病","id":"9"},{"name":"肝炎","id":"10"},{"name":"其他法定遗传病","id":"11"}]
         * f_chi : [{"name":"高血压","id":"2"},{"name":"糖尿病","id":"3"},{"name":"冠心病","id":"4"},{"name":"慢性阻塞性肺疾病","id":"5"},{"name":"恶性肿瘤","id":"6"},{"name":"脑卒中","id":"7"},{"name":"重型精神疾病","id":"8"},{"name":"结核病","id":"9"},{"name":"肝炎","id":"10"},{"name":"其他法定遗传病","id":"11"}]
         * inheri : 心脏病
         * deformity : 1
         * exha : 3
         * fuel : 6
         * water : 1
         * wc : 3
         * poultry : 1
         */

        private String id;
        private String e_id;
        private String phone;
        private String phone_contacts;
        private String card_img;
        private String name;
        private String sex;
        private String birth;
        private String nation;
        private String card_id;
        private String address;
        private String province;
        private String city;
        private String district;
        private String resident;
        private String work;
        private String blood;
        private String blood_hr;
        private String edu;
        private String occ;
        private String marr;
        private Object test;
        private String status;
        private String addtime;
        private Object lasttime;
        private String m_id;
        private String pay_type;
        private String allergor;
        private String expose;
        private String inheri;
        private String deformity;
        private String exha;
        private String fuel;
        private String water;
        private String wc;
        private String poultry;
        private List<PDisBean> p_dis;
        private List<POperaBean> p_opera;
        private List<PTrauBean> p_trau;
        private List<PTransBean> p_trans;
        private List<FPBean> f_p;
        private List<FMBean> f_m;
        private List<FChiBean> f_chi;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getE_id() {
            return e_id;
        }

        public void setE_id(String e_id) {
            this.e_id = e_id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPhone_contacts() {
            return phone_contacts;
        }

        public void setPhone_contacts(String phone_contacts) {
            this.phone_contacts = phone_contacts;
        }

        public String getCard_img() {
            return card_img;
        }

        public void setCard_img(String card_img) {
            this.card_img = card_img;
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

        public String getBirth() {
            return birth;
        }

        public void setBirth(String birth) {
            this.birth = birth;
        }

        public String getNation() {
            return nation;
        }

        public void setNation(String nation) {
            this.nation = nation;
        }

        public String getCard_id() {
            return card_id;
        }

        public void setCard_id(String card_id) {
            this.card_id = card_id;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getResident() {
            return resident;
        }

        public void setResident(String resident) {
            this.resident = resident;
        }

        public String getWork() {
            return work;
        }

        public void setWork(String work) {
            this.work = work;
        }

        public String getBlood() {
            return blood;
        }

        public void setBlood(String blood) {
            this.blood = blood;
        }

        public String getBlood_hr() {
            return blood_hr;
        }

        public void setBlood_hr(String blood_hr) {
            this.blood_hr = blood_hr;
        }

        public String getEdu() {
            return edu;
        }

        public void setEdu(String edu) {
            this.edu = edu;
        }

        public String getOcc() {
            return occ;
        }

        public void setOcc(String occ) {
            this.occ = occ;
        }

        public String getMarr() {
            return marr;
        }

        public void setMarr(String marr) {
            this.marr = marr;
        }

        public Object getTest() {
            return test;
        }

        public void setTest(Object test) {
            this.test = test;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public Object getLasttime() {
            return lasttime;
        }

        public void setLasttime(Object lasttime) {
            this.lasttime = lasttime;
        }

        public String getM_id() {
            return m_id;
        }

        public void setM_id(String m_id) {
            this.m_id = m_id;
        }

        public String getPay_type() {
            return pay_type;
        }

        public void setPay_type(String pay_type) {
            this.pay_type = pay_type;
        }

        public String getAllergor() {
            return allergor;
        }

        public void setAllergor(String allergor) {
            this.allergor = allergor;
        }

        public String getExpose() {
            return expose;
        }

        public void setExpose(String expose) {
            this.expose = expose;
        }

        public String getInheri() {
            return inheri;
        }

        public void setInheri(String inheri) {
            this.inheri = inheri;
        }

        public String getDeformity() {
            return deformity;
        }

        public void setDeformity(String deformity) {
            this.deformity = deformity;
        }

        public String getExha() {
            return exha;
        }

        public void setExha(String exha) {
            this.exha = exha;
        }

        public String getFuel() {
            return fuel;
        }

        public void setFuel(String fuel) {
            this.fuel = fuel;
        }

        public String getWater() {
            return water;
        }

        public void setWater(String water) {
            this.water = water;
        }

        public String getWc() {
            return wc;
        }

        public void setWc(String wc) {
            this.wc = wc;
        }

        public String getPoultry() {
            return poultry;
        }

        public void setPoultry(String poultry) {
            this.poultry = poultry;
        }

        public List<PDisBean> getP_dis() {
            return p_dis;
        }

        public void setP_dis(List<PDisBean> p_dis) {
            this.p_dis = p_dis;
        }

        public List<POperaBean> getP_opera() {
            return p_opera;
        }

        public void setP_opera(List<POperaBean> p_opera) {
            this.p_opera = p_opera;
        }

        public List<PTrauBean> getP_trau() {
            return p_trau;
        }

        public void setP_trau(List<PTrauBean> p_trau) {
            this.p_trau = p_trau;
        }

        public List<PTransBean> getP_trans() {
            return p_trans;
        }

        public void setP_trans(List<PTransBean> p_trans) {
            this.p_trans = p_trans;
        }

        public List<FPBean> getF_p() {
            return f_p;
        }

        public void setF_p(List<FPBean> f_p) {
            this.f_p = f_p;
        }

        public List<FMBean> getF_m() {
            return f_m;
        }

        public void setF_m(List<FMBean> f_m) {
            this.f_m = f_m;
        }

        public List<FChiBean> getF_chi() {
            return f_chi;
        }

        public void setF_chi(List<FChiBean> f_chi) {
            this.f_chi = f_chi;
        }

        public static class PDisBean {
            /**
             * name : 高血压
             * id : 2
             */

            private String name;
            private String id;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }

        public static class POperaBean {
            /**
             * dateTime : 2017-07-21
             * name : 尴尬
             */

            private String dateTime;
            private String name;

            public String getDateTime() {
                return dateTime;
            }

            public void setDateTime(String dateTime) {
                this.dateTime = dateTime;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class PTrauBean {
            /**
             * dateTime : 2017-07-29
             * name : 发给
             */

            private String dateTime;
            private String name;

            public String getDateTime() {
                return dateTime;
            }

            public void setDateTime(String dateTime) {
                this.dateTime = dateTime;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class PTransBean {
            /**
             * dateTime : 2017-07-26
             * name : 发疯
             */

            private String dateTime;
            private String name;

            public String getDateTime() {
                return dateTime;
            }

            public void setDateTime(String dateTime) {
                this.dateTime = dateTime;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class FPBean {
            /**
             * name : 高血压
             * id : 2
             */

            private String name;
            private String id;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }

        public static class FMBean {
            /**
             * name : 高血压
             * id : 2
             */

            private String name;
            private String id;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }

        public static class FChiBean {
            /**
             * name : 高血压
             * id : 2
             */

            private String name;
            private String id;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }
    }
}
