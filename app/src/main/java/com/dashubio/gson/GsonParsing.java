package com.dashubio.gson;

import com.dashubio.bean.CameraCardBean;
import com.dashubio.bean.ErrorCodeBean;
import com.dashubio.bean.HealthArchivesBean;
import com.dashubio.bean.HealthReportBean;
import com.dashubio.bean.HealthReportSecondBean;
import com.dashubio.bean.HealthyGuideBean;
import com.dashubio.bean.HealthyGuideListBean;
import com.dashubio.bean.HelpMessageBean;
import com.dashubio.bean.HistoryDataBean;
import com.dashubio.bean.HistoryListDataBean;
import com.dashubio.bean.LoginBean;
import com.dashubio.bean.MainPageBean;
import com.dashubio.bean.UserListBean;
import com.dashubio.bean.UserLoginBean;
import com.dashubio.bean.WarningBean;
import com.dashubio.bean.viewbean.TitleMessageBean;
import com.dashubio.constant.InterfaceUrl;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/13 0013
 * 时间： 下午 4:06
 * 描述： gson解析json
 */

public class GsonParsing {
    //短信请求错误码解析
    public static ErrorCodeBean sendCodeError(String json) throws Exception{
        ErrorCodeBean result = GsonUtils.parseJsonWithGson(json, ErrorCodeBean.class);
        return result;
    }

    public static LoginBean getMessage(String json)throws Exception{
        LoginBean result = GsonUtils.parseJsonWithGson(json, LoginBean.class);
        return result;
    }

    public static String gsonLoginSesson(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        LoginBean user = new LoginBean();
        user.setT_session_3(jsonObject.getString(InterfaceUrl.zSesson+InterfaceUrl.code));
        return user.getT_session_3();
    }

    public static HelpMessageBean getHelpMessage(String json)throws Exception{
        HelpMessageBean result = GsonUtils.parseJsonWithGson(json, HelpMessageBean.class);
        return result;
    }

    public static WarningBean getWarningMessage(String json)throws Exception{
        WarningBean result = GsonUtils.parseJsonWithGson(json, WarningBean.class);
        return result;
    }

    public static UserListBean getUserListMessage(String json)throws Exception{
        UserListBean result = GsonUtils.parseJsonWithGson(json, UserListBean.class);
        return result;
    }

    public static CameraCardBean getCardMessageJson(String json) throws Exception{
        CameraCardBean result = GsonUtils.parseJsonWithGson(json, CameraCardBean.class);
        return result;
    }

    public static MainPageBean getMainPageMessageJson(String json) throws Exception{
        MainPageBean result = GsonUtils.parseJsonWithGson(json, MainPageBean.class);
        return result;
    }

    public static UserLoginBean getUserLoginMessageJson(String json) throws Exception{
        UserLoginBean result = GsonUtils.parseJsonWithGson(json, UserLoginBean.class);
        return result;
    }


    public static HealthReportBean getHealthReportMessageJson(String json) throws Exception{
        HealthReportBean result = GsonUtils.parseJsonWithGson(json, HealthReportBean.class);
        return result;
    }

    public static HealthArchivesBean getHealthArchivesMessageJson(String json) throws Exception{
        HealthArchivesBean result = GsonUtils.parseJsonWithGson(json, HealthArchivesBean.class);
        return result;
    }

    public static HistoryDataBean getHistoryDataMessageJson(String json) throws Exception{
        HistoryDataBean result = GsonUtils.parseJsonWithGson(json, HistoryDataBean.class);
        return result;
    }

    public static HistoryListDataBean getHistoryListDataMessageJson(String json) throws Exception{
        HistoryListDataBean result = GsonUtils.parseJsonWithGson(json, HistoryListDataBean.class);
        return result;
    }

    public static HealthyGuideBean getHealthProjectMessageJson(String json) throws Exception{
        HealthyGuideBean result = GsonUtils.parseJsonWithGson(json, HealthyGuideBean.class);
        return result;
    }

    public static HealthyGuideListBean getHealthProjectListMessageJson(String json) throws Exception{
        HealthyGuideListBean result = GsonUtils.parseJsonWithGson(json, HealthyGuideListBean.class);
        return result;
    }

    public static HealthReportSecondBean getHealthReportSecondMessageJson(String json) throws Exception{
        HealthReportSecondBean result = GsonUtils.parseJsonWithGson(json, HealthReportSecondBean.class);
        return result;
    }


    public static TitleMessageBean getTitleMessageJson(String json) throws Exception{
        TitleMessageBean result = GsonUtils.parseJsonWithGson(json, TitleMessageBean.class);
        return result;
    }

}


class GsonUtils {
    //将Json数据解析成相应的映射对象
    public static <T> T parseJsonWithGson(String jsonData, Class<T> type) {
        Gson gson = new Gson();
        T result = gson.fromJson(jsonData, type);
        return result;
    }


}