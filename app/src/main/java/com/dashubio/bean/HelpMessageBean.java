package com.dashubio.bean;

import java.util.List;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/14 0014
 * 时间： 上午 11:56
 * 描述： 帮助信息实体类封装
 */

public class HelpMessageBean {

    /**
     * status : success
     * count : 1
     * data : [{"id":"6","c_id":null,"d_id":null,"title":"APP使用帮助","content":"APP图标 \r\n \r\n 大 树 英 杰\r\n \r\n启动初始页面\r\n \r\nAPP管理用户注册页面\r\n \r\n注：一台干式生化的设备串号只能注册一次\r\n \r\nAPP管理用户登录页面\r\n \r\n\r\n一、管理用户登录后主页面\r\n例如：登录管理用户昵称为---\u201c第二人生科技\u201d\r\n主页面有6个页面，分为首页、用户注册、用户登录、用户管理、用户管理、预警设置、使用帮助。\r\n\r\n1、首页如下.显示内容为最近一次客户检查项目的数据。\r\n \r\n2、用户注册\r\na)、客户身份证扫描注册\r\n \r\nb）、客户手动输入注册\r\n \r\n3、用户登录\r\n分为身份证识别登录、搜索登录、手动登录。\r\n \r\na)、身份证识别登录\r\n身份证至于平板摄像头下方，拍摄识别自动登录\r\n \r\nb)搜索登录\r\n例如：\r\n搜索框输入 \u201c张\u201d\r\n跳出客户姓名数据--\u201c张梓彬\u201d，点击 登录\r\n \r\nc)手动登录，点击手动登录，跳入如下页面，点击 登录\r\n \r\n4、用户管理\r\n管理人\u201c第二人生科技\u201d检测的所有客户查看页面。\r\n \r\n5、预警设置\r\n高密度脂蛋白胆固醇、葡萄糖、总胆固醇、甘油三酯、血红蛋白、谷丙转氨酶、血压、血氧、体温、心电、心率等各项检测数据的正常范围指标参考。\r\n \r\n\r\n\r\n二、客户登陆主页面\r\n例如，检查的客户昵称为\u201c程\u201d。\r\n主页面分为6个。首页、开始测量、历史数据、健康报告、健康档案、健康指导\r\n1.首页\r\n内容为该客户最近一次检查数据\r\n \r\n\r\n\r\n2.开始测量\r\n分为3个板块。健康检查仪、干式生化仪、尿液分析仪。\r\n \r\na)多功能健康检查仪\r\n点击连接设备\r\n \r\n出现如下提示页面，点击\u201c扫描设备\u201d\r\n \r\n\r\n\r\n\r\n选择相应的蓝牙设备，并点击进行连接\r\n \r\n连接好设备后，先选择检查的项目，再点击开始测量\r\n \r\n血氧检测，检测结果出来后，点击保存\r\n \r\n例如：体温检测，检测结果出来后，点击保存\r\n \r\nb)干式生化分析仪\r\n在干式生化分析仪检测完成前，点击\u201c连接设备获取数据\u201d\r\n \r\n点击\u201c扫描设备\u201d，选择相应的蓝牙设备进行连接\r\n \r\n等待检测结果传送到App上\r\n \r\n等检测数据传送到App上，再点击保存，可在\u201c历史数据\u201d中查看\r\n \r\nC)便携式尿液分析仪\r\n在尿液分析仪进行检测前，点击\u201c连接设备获取数据\u201d\r\n \r\n\r\n\r\n\r\n\r\n\r\n点击\u201c扫描设备\u201d，选择相应的蓝牙设备进行连接\r\n \r\n等待检测结果传送到App上\r\n \r\n等检测数据传送到App上，再点击保存，可在\u201c历史数据\u201d中查看\r\n \r\n\r\n\r\n\r\n\r\n\r\n3.历史数据\r\n可查看客户\u201c程\u201d之前的健康检测数据，分为健康检测仪、干式生化仪、尿液分析仪3个板块。\r\n例如，干式生化仪检测的，血脂五项和葡萄糖的数据。\r\n \r\n体温的数据\r\n出现红色字体\u201c体温偏高\u201d，为系统实时监测预警，初步诊断客户健康情况。\r\n \r\n\r\n\r\n4.健康报告\r\n健康报告分为综合、单项两种报告，按年、月查询数据报告分析\r\n \r\n \r\n图为综合报告具体内容\r\n \r\n健康评估报告由后台系统填写完成，可在App上查看\r\n \r\n单项健康报告先选择月份，再选择项目，例如：\u201c血压\u201d报告选择血压\r\n \r\n显示血压完整报告\r\n \r\n健康评估报告由后台系统填写完成，可在App上查看\r\n \r\n\r\n5.健康档案\r\n可添加客户姓名、性别、手机电话、家庭地址、民族、身份证号码、工作单位、血型、职业、婚姻状态，以及个人基本健康信息、既往病史，个人生活环境等信息。\r\n \r\n \r\n \r\n \r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n6.健康指导\r\n例如，查看血压数据健康指导，点击\u201c体温\u201d下拉框\r\n \r\n选择\u201c血压\u201d\r\n \r\n\r\n\r\n三．用户后台管理系统\r\n通过网站进入后台管理系统，输入注册过的账号密码，进行登陆\r\n\r\n\r\n \r\n1.登陆后，信息统计显示本月测量的基本信息\r\n \r\n包括月测量总数，每日测量数，高血压、高血糖人数占比，不同年龄段人数统计\r\n \r\n\r\n\r\n\r\n2.点击\u201c用户管理\u201d，选择\u201c用户列表\u201d\r\n \r\n可对病种、年龄、性别、地区进行筛选\r\n \r\n点击\u201c测量详情\u201d，进行查看\r\n \r\n\r\n\r\n\r\n\r\n按月份显示健康检测报告\r\n \r\n点击其中的任意一项检测内容可生产单项健康报告，例如点击\u201c血压\u201d\r\n \r\n进入\u201c单项\u201d检测数据后，点击\u201c健康报告\u201d\r\n \r\n \r\n在输入栏输入信息后，点击\u201c提交\u201d，即可生产健康报告，点击\u201c打印\u201d，可打印血压的健康报告\r\n \r\n点击\u201c综合报告\u201d，可生产每月的综合健康报告，选择所需查看的月份\r\n \r\n可生产综合报告\r\n \r\n在输入栏输入信息后，点击\u201c提交\u201d，即可生产健康评估报告，点击\u201c打印\u201d，可打印完整的健康报告\r\n \r\n在App上也可查看到相应的健康评估报告\r\n \r\n生成的健康评估报告，可以点击健康评估左侧的按钮，发送到用户的微信上，方便用户查看健康报告\r\n \r\n\r\n","status":"1","addtime":"1491810216"}]
     */

    private String status;
    private String count;
    private List<HelpDataBean> data;

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

    public List<HelpDataBean> getData() {
        return data;
    }

    public void setData(List<HelpDataBean> data) {
        this.data = data;
    }

    public static class HelpDataBean {
        /**
         * id : 6
         * c_id : null
         * d_id : null
         * title : APP使用帮助
         * content : APP图标

         大 树 英 杰

         启动初始页面

         APP管理用户注册页面

         注：一台干式生化的设备串号只能注册一次

         APP管理用户登录页面


         一、管理用户登录后主页面
         例如：登录管理用户昵称为---“第二人生科技”
         主页面有6个页面，分为首页、用户注册、用户登录、用户管理、用户管理、预警设置、使用帮助。

         1、首页如下.显示内容为最近一次客户检查项目的数据。

         2、用户注册
         a)、客户身份证扫描注册

         b）、客户手动输入注册

         3、用户登录
         分为身份证识别登录、搜索登录、手动登录。

         a)、身份证识别登录
         身份证至于平板摄像头下方，拍摄识别自动登录

         b)搜索登录
         例如：
         搜索框输入 “张”
         跳出客户姓名数据--“张梓彬”，点击 登录

         c)手动登录，点击手动登录，跳入如下页面，点击 登录

         4、用户管理
         管理人“第二人生科技”检测的所有客户查看页面。

         5、预警设置
         高密度脂蛋白胆固醇、葡萄糖、总胆固醇、甘油三酯、血红蛋白、谷丙转氨酶、血压、血氧、体温、心电、心率等各项检测数据的正常范围指标参考。



         二、客户登陆主页面
         例如，检查的客户昵称为“程”。
         主页面分为6个。首页、开始测量、历史数据、健康报告、健康档案、健康指导
         1.首页
         内容为该客户最近一次检查数据



         2.开始测量
         分为3个板块。健康检查仪、干式生化仪、尿液分析仪。

         a)多功能健康检查仪
         点击连接设备

         出现如下提示页面，点击“扫描设备”




         选择相应的蓝牙设备，并点击进行连接

         连接好设备后，先选择检查的项目，再点击开始测量

         血氧检测，检测结果出来后，点击保存

         例如：体温检测，检测结果出来后，点击保存

         b)干式生化分析仪
         在干式生化分析仪检测完成前，点击“连接设备获取数据”

         点击“扫描设备”，选择相应的蓝牙设备进行连接

         等待检测结果传送到App上

         等检测数据传送到App上，再点击保存，可在“历史数据”中查看

         C)便携式尿液分析仪
         在尿液分析仪进行检测前，点击“连接设备获取数据”






         点击“扫描设备”，选择相应的蓝牙设备进行连接

         等待检测结果传送到App上

         等检测数据传送到App上，再点击保存，可在“历史数据”中查看






         3.历史数据
         可查看客户“程”之前的健康检测数据，分为健康检测仪、干式生化仪、尿液分析仪3个板块。
         例如，干式生化仪检测的，血脂五项和葡萄糖的数据。

         体温的数据
         出现红色字体“体温偏高”，为系统实时监测预警，初步诊断客户健康情况。



         4.健康报告
         健康报告分为综合、单项两种报告，按年、月查询数据报告分析


         图为综合报告具体内容

         健康评估报告由后台系统填写完成，可在App上查看

         单项健康报告先选择月份，再选择项目，例如：“血压”报告选择血压

         显示血压完整报告

         健康评估报告由后台系统填写完成，可在App上查看


         5.健康档案
         可添加客户姓名、性别、手机电话、家庭地址、民族、身份证号码、工作单位、血型、职业、婚姻状态，以及个人基本健康信息、既往病史，个人生活环境等信息。












         6.健康指导
         例如，查看血压数据健康指导，点击“体温”下拉框

         选择“血压”



         三．用户后台管理系统
         通过网站进入后台管理系统，输入注册过的账号密码，进行登陆



         1.登陆后，信息统计显示本月测量的基本信息

         包括月测量总数，每日测量数，高血压、高血糖人数占比，不同年龄段人数统计




         2.点击“用户管理”，选择“用户列表”

         可对病种、年龄、性别、地区进行筛选

         点击“测量详情”，进行查看





         按月份显示健康检测报告

         点击其中的任意一项检测内容可生产单项健康报告，例如点击“血压”

         进入“单项”检测数据后，点击“健康报告”


         在输入栏输入信息后，点击“提交”，即可生产健康报告，点击“打印”，可打印血压的健康报告

         点击“综合报告”，可生产每月的综合健康报告，选择所需查看的月份

         可生产综合报告

         在输入栏输入信息后，点击“提交”，即可生产健康评估报告，点击“打印”，可打印完整的健康报告

         在App上也可查看到相应的健康评估报告

         生成的健康评估报告，可以点击健康评估左侧的按钮，发送到用户的微信上，方便用户查看健康报告



         * status : 1
         * addtime : 1491810216
         */

        private String id;
        private Object c_id;
        private Object d_id;
        private String title;
        private String content;
        private String status;
        private String addtime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Object getC_id() {
            return c_id;
        }

        public void setC_id(Object c_id) {
            this.c_id = c_id;
        }

        public Object getD_id() {
            return d_id;
        }

        public void setD_id(Object d_id) {
            this.d_id = d_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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
    }
}
