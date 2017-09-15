package com.dashubio.base;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dashubio.R;
import com.dashubio.constant.ErrorCode;
import com.dashubio.db.DBManager;
import com.linktop.MonitorDataTransmissionManager;

import static com.dashubio.constant.InterfaceUrl.code;
import static com.dashubio.constant.InterfaceUrl.t_session_code;
import static com.dashubio.constant.InterfaceUrl.zSesson;


/**
 * 作者： 张梓彬
 * 日期： 2017/7/14 0014
 * 时间： 上午 11:35
 * 描述： 基础的Fragment封装
 */

public class BaseFragment extends Fragment {
    protected View view;
    protected String sessonWithCode = zSesson + code + "/" + t_session_code + "/uid/" + code;
    private static final String TAG = "BaseFragment";
    protected DBManager dbManager;
    protected MonitorDataTransmissionManager manager;


    protected void toastOnUi(final String text) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void JumpActivity(Class<?> cls) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }

    protected void JumpActivityWithUserData(Class<?> cls, String name,String mid) {
        Intent intent = new Intent(getActivity(), cls);
        intent.putExtra("userName", name);
        intent.putExtra("mid", mid);
        startActivity(intent);
    }


    protected void judge(String result) {
        switch (result) {
            case ErrorCode.CONNECTION_TIMEOUT_OR_ILLEGAL_REQUEST://连接超时，或非法请求
                toastOnUi(getResources().getString(R.string.connection_timeout_or_illegal_request));
                return;

            case ErrorCode.PARAMETER_ERROE://参数错误
                toastOnUi(getResources().getString(R.string.parameter_error));
                return;

            case ErrorCode.PARAMETER_COMMIT_FAILED://参数提交失败
                toastOnUi(getResources().getString(R.string.parameter_commit_failed));
                return;

            case ErrorCode.DATA_EMPTY://数据为空
                toastOnUi(getResources().getString(R.string.data_empty));
                return;

            case ErrorCode.USERID_CANNOT_EMPTY://用户id不可为空
                toastOnUi(getResources().getString(R.string.userid_cannot_empty));
                return;

            case ErrorCode.COMPANY_NAME_CANNOT_EMPYT://公司名称不可为空
                toastOnUi(getResources().getString(R.string.company_name_cannot_empty));
                return;

            case ErrorCode.USERNAME_CANNOT_EMPYT://用户姓名不可为空
                toastOnUi(getResources().getString(R.string.username_cannot_empty));
                return;

            case ErrorCode.CONTACT_PHONE_CANNOT_EMPYT://联系电话不可为空
                toastOnUi(getResources().getString(R.string.contact_phone_cannot_empty));
                return;

            case ErrorCode.PROVINCE_CANNOT_EMPYT://省份不可为空
                toastOnUi(getResources().getString(R.string.province_cannot_empty));
                return;

            case ErrorCode.CITY_CANNOT_EMPYT://城市不可为空
                toastOnUi(getResources().getString(R.string.city_cannot_empty));
                return;

            case ErrorCode.DETAIL_ADDRESS_CANNOT_EMPYT://详细地址不可为空
                toastOnUi(getResources().getString(R.string.detail_address_cannot_empty));
                return;

            case ErrorCode.FORMAT_ERROR://格式错误
                toastOnUi(getResources().getString(R.string.format_error));
                return;

            case ErrorCode.DEVICE_HAS_BEEN_REGISTERED://设备已注册
                toastOnUi(getResources().getString(R.string.device_has_been_registered));
                return;

            case ErrorCode.DEVICE_NAME_NOT_VALID://设备名不合法
                toastOnUi(getResources().getString(R.string.device_name_not_valid));
                return;

            case ErrorCode.PHONE_HAS_BEEN_OCCUPIED://手机号已被占用
                toastOnUi(getResources().getString(R.string.phone_has_been_occupied));
                return;

            case ErrorCode.PASSWORD_LENGTH_NOT_VALID://密码长度不合法
                toastOnUi(getResources().getString(R.string.password_length_not_valid));
                return;

            case ErrorCode.PHONE_NOT_LEGAL://手机号不合法
                toastOnUi(getResources().getString(R.string.phone_not_legal));
                return;

            case ErrorCode.SMS_VERIFICATION_CODE_ERROR://短信验证码错误
                toastOnUi(getResources().getString(R.string.sms_verification_code_error));
                return;

            case ErrorCode.USER_NOT_EXIST://用户不存在
                toastOnUi(getResources().getString(R.string.user_not_exist));
                return;

            case ErrorCode.PASSWORD_ERROR://密码错误
                toastOnUi(getResources().getString(R.string.password_error));
                return;

            case ErrorCode.USER_ID_CARD_HAS_BEEN_REGISTERED://用户身份证已注册
                toastOnUi(getResources().getString(R.string.user_id_card_has_been_registered));
                return;

            case ErrorCode.USER_ID_CARD_CANNOT_EMPTY://用户身份证id不可为空
                toastOnUi(getResources().getString(R.string.user_id_card_cannot_empty));
                return;

            case ErrorCode.USER_NAME_CANNOT_EMPTY://用户姓名不可为空
                toastOnUi(getResources().getString(R.string.username_cannot_empty));
                return;

            case ErrorCode.USER_GENDER_CANNOT_EMPTY://用户性别不可为空
                toastOnUi(getResources().getString(R.string.user_gender_cannot_empty));
                return;

            case ErrorCode.PHONE_CANNOT_EMPTY://电话不可为空
                toastOnUi(getResources().getString(R.string.phone_cannot_empty));
                return;
        }
    }


    protected void runOnUiMothod(final TextView textView, final String data){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(data);
            }
        });
    }


}
