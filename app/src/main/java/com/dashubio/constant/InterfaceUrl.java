package com.dashubio.constant;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/21 0021
 * 时间： 上午 10:15
 * 描述： 数据接口
 */

public class InterfaceUrl {
    public static String zSesson = "t_session_";
    public static String code = "";
    public static String t_session_code = "";
    public static final String BASE_URL = "http://dashubio.cn/Mobile/";
    public static final String WEBVIEW_BASE_URL = "http://dashubio.cn/";
    public static final String EQUIPMENT_REGISTER = BASE_URL+"Login/register";//设备注册
    public static final String VERIFICATION_CODE= BASE_URL+"Login/sendCode";//短信验证码
    public static final String LOGIN_URL= BASE_URL+"Login/login";//登录接口
    public static final String HELP_URL= BASE_URL+"Index/help/";//帮助接口需要添加sesson和code
    public static final String WARNING_URL= BASE_URL+"Index/hwarning/";//预警设置接口需要添加sesson和code
    public static final String USER_MESSAGE_URL= BASE_URL+"Ulogin/userlist/";//用户列表接口需要添加sesson和code
    public static final String USER_LOGIN_URL= BASE_URL+"Ulogin/userlogin/";//用户登录接口需要添加sesson和code
    public static final String MESSAGE_URL= BASE_URL+"Testing/tests/";//获取检测数据（首页）接口需要添加sesson和code
    public static final String USER_REGISTER_URL= BASE_URL+"Ulogin/register/";//用户登录接口需要添加sesson和code
    public static final String HISTORY_DATA_URL= BASE_URL+"Testing/history/";//历史数据列表接口需要添加sesson和code
    public static final String HISTORY_KIND_URL= BASE_URL+"Testing/index/";//历史数据接口需要添加sesson和code
    public static final String HEALTH_REPORT_URL= BASE_URL+"Testing/reportlist/";//健康报告接口需要添加sesson和code
    public static final String HEALTH_REPORT_SECOND_LIST_URL= BASE_URL+"Testing/compiled/";//健康报告接口需要添加sesson和code
    public static final String HEALTH_ARCHIVES_URL= BASE_URL+"Ulogin/perinfo/";//健康档案接口需要添加sesson和code
    public static final String HEALTH_GUIDE_URL= BASE_URL+"Testing/warnitem/";//健康指导接口需要添加sesson和code
    public static final String HEALTH_GUIDE_LIST_URL= BASE_URL+"Testing/warninfo/";//健康指导接口需要添加sesson和code
    public static final String SINGLE_WEBVIEW_URL= WEBVIEW_BASE_URL+"Manage/Equid/reportzer";//单项WEBVIEW接口
    public static final String COMPREHENSIVE_WEBVIEW_URL= WEBVIEW_BASE_URL+"Manage/Equid/compiledzer";//综合WEBVIEW接口
    public static final String MULTIPARAMETERMONITORDATA_URL= BASE_URL+"Testing/multiple/";//多参数检测仪上传数据接口
    public static final String BREATHDATA_URL= BASE_URL+"Testing/breath/";//呼吸机上传数据接口
    public static final String GANSHIDATA_URL= BASE_URL+"Testing/dry/";//干式生化仪上传数据接口需要添加sesson和code
    public static final String BCDATA_URL= BASE_URL+"Testing/urine/";//干式生化仪上传数据接口需要添加sesson和code
    public static final String TITLE_URL= BASE_URL+"Index/getad/";//干式生化仪上传数据接口需要添加sesson和code
    public static final String BITMAP_URL= WEBVIEW_BASE_URL+"Uploads/";//加上图片名称
    public static final String HEALTH_URL= BASE_URL+"Testing/check/";//健康检测仪上传数据接口需要添加sesson和code

}
