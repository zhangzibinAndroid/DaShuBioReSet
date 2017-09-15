package com.dashubio.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.dashubio.R;
import com.dashubio.activity.MainActivity;
import com.dashubio.bean.ErrorCodeBean;
import com.dashubio.bean.LoginBean;
import com.dashubio.bean.dbmanagerbean.LoginUserBean;
import com.dashubio.constant.ErrorCode;
import com.dashubio.db.DBManager;
import com.dashubio.gson.GsonParsing;
import com.linktop.MonitorDataTransmissionManager;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;

import java.util.ArrayList;

import okhttp3.Call;

import static com.dashubio.constant.InterfaceUrl.code;
import static com.dashubio.constant.InterfaceUrl.t_session_code;
import static com.dashubio.constant.InterfaceUrl.zSesson;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/12 0012
 * 时间： 上午 10:24
 * 描述： 基础activity封装
 */

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    protected static final long TIME = 60;
    protected long mTimeRemaining;
    protected SharedPreferences sharedPreferences;
    protected ProgressDialog progressDialog;
    protected CountDownTimer mCountDownTimer;
    protected DBManager dbManager;
    protected static String mPhone;
    protected static String mPwds;
    protected MonitorDataTransmissionManager manager;


    protected String sessonWithCode = zSesson + code + "/" + t_session_code + "/uid/" + code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
        dbManager = new DBManager(this);
        initSystemBar(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }


    protected void startTime(long time, final Button btn_time) {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        mCountDownTimer = new CountDownTimer(time * 1000, 50) {
            @Override
            public void onTick(long millisUnitFinished) {
                mTimeRemaining = ((millisUnitFinished / 1000) + 1);
                btn_time.setText("倒计时：" + mTimeRemaining);
                btn_time.setClickable(false);
                btn_time.setBackgroundColor(getResources().getColor(R.color.gray));

            }

            @Override
            public void onFinish() {
                btn_time.setText("重新开始");
                btn_time.setClickable(true);
                btn_time.setBackgroundColor(getResources().getColor(R.color.blue_dep));

            }
        };
        mCountDownTimer.start();
    }

    public static void initSystemBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(activity, true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        // 使用颜色资源
        tintManager.setStatusBarTintResource(R.color.colorPrimary);
    }


    private static void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    protected void JumpActivity(Class<?> cls) {
        Intent intent = new Intent(getApplicationContext(), cls);
        startActivity(intent);
    }

    protected void toastOnUi(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void toastOnUiWithProgress(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, text, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
    }

    protected void setReplaceFragment(@IdRes int containerViewId, Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(containerViewId, fragment).commit();
    }


    //短信验证码接口
    protected void verificationCodeInterface(String url, String phone) {
        OkHttpUtils.post().url(url).addParams("phone", phone).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                toastOnUi("获取短信异常，请检查网络");
            }

            @Override
            public void onResponse(String response, int id) {
                Message msg = new Message();
                msg.obj = response;
                verificationCodeHandler.sendMessage(msg);

            }
        });
    }

    //设备注册接口
    protected void equipmentReisterInterface(String url, String nid, String phone, String password, String msgcode) {

        OkHttpUtils.post().url(url)
                .addParams("nid", nid)
                .addParams("phone", phone)
                .addParams("password", password)
                .addParams("msgcode", msgcode)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                toastOnUiWithProgress("注册异常，请检查网络");
            }

            @Override
            public void onResponse(String response, int id) {
                Message msg = new Message();
                msg.obj = response;
                equipmentReisterHadler.sendMessage(msg);

            }
        });
    }

    //设备登录接口
    protected void equipmentLoginInterface(String url, final String phone, final String password) {
        OkHttpUtils.post().url(url)
                .addParams("phone", phone)
                .addParams("password", password)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "onError: "+e.getMessage() );
                toastOnUiWithProgress("登陆失败，请检查网络");
            }

            @Override
            public void onResponse(String response, int id) {
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString("phone", phone);
                edit.putString("password", password);
                edit.commit();
                mPhone = phone;
                mPwds = password;

                Message msg = new Message();
                msg.obj = response;
                equipmentLoginHandler.sendMessage(msg);
            }
        });
    }


    //短信验证码接口Handler处理
    protected Handler verificationCodeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            if (result.indexOf(ErrorCode.SUCCESS) > 0) {
                toastOnUi("短信已成功发送");
            } else {
                //解析
                ErrorCodeBean errorCodeBean = null;
                try {
                    errorCodeBean = GsonParsing.sendCodeError(result);
                    judge(errorCodeBean.getCode() + "");

                } catch (Exception e) {
                    Toast.makeText(BaseActivity.this, getResources().getString(R.string.connection_timeout_or_illegal_request), Toast.LENGTH_SHORT).show();

                }
            }

        }
    };

    //设备注册接口Handler处理
    protected Handler equipmentReisterHadler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            if (result.indexOf(ErrorCode.SUCCESS) > 0) {
                Toast.makeText(BaseActivity.this, "设备注册成功", Toast.LENGTH_SHORT).show();
            } else {
                ErrorCodeBean errorCodeBean = null;
                try {
                    errorCodeBean = GsonParsing.sendCodeError(result);
                    judge(errorCodeBean.getCode() + "");
                    progressDialog.dismiss();
                } catch (Exception e) {
                    Toast.makeText(BaseActivity.this, getResources().getString(R.string.connection_timeout_or_illegal_request), Toast.LENGTH_SHORT).show();
                }

            }
        }
    };

    //设备登录接口Handler处理
    protected Handler equipmentLoginHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            if (result.indexOf(ErrorCode.SUCCESS) > 0) {
                toastOnUi("登录成功");
                LoginBean loginBean = null;
                String companyName = "";
                //如果数据库中有该用户，那么不用在将该用户添加到数据库中
                ArrayList<LoginUserBean> loginUserBeanList = new ArrayList<>();
                loginUserBeanList = dbManager.searchLoginData(mPhone,mPwds);
                String mResult = "";
                for (LoginUserBean loginUserBean : loginUserBeanList) {
                    mResult = mResult + String.valueOf(loginUserBean._id) + "|" + loginUserBean.name
                            + "|" + loginUserBean.pwds ;
                    mResult = mResult + "\n" + "------------------------------------------" + "\n";
                }

                if (mResult.indexOf(mPhone)!=-1&&mResult.indexOf(mPwds)!=-1) {
                    //数据库有数据
                }else {
                    dbManager.clearLoginTable();//清空设备登录表
                    dbManager.clearUserTable();//清空小用户列表
                    dbManager.addLoginUser(mPhone, mPwds);
                    Toast.makeText(BaseActivity.this, "成功存入本地数据库", Toast.LENGTH_SHORT).show();
                }


                try {
                    loginBean = GsonParsing.getMessage(result);
                    code = loginBean.getCode();
                    companyName = loginBean.getCompany();
                } catch (Exception e) {
                    Toast.makeText(BaseActivity.this, getResources().getString(R.string.connection_timeout_or_illegal_request), Toast.LENGTH_SHORT).show();
                }

                try {
                    t_session_code = GsonParsing.gsonLoginSesson(result);
                } catch (JSONException e) {
                    Toast.makeText(BaseActivity.this, "t_session_code解析失败", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("companyName",companyName);
                startActivity(intent);


                progressDialog.dismiss();
            } else {
                ErrorCodeBean errorCodeBean = null;
                try {
                    errorCodeBean = GsonParsing.sendCodeError(result);
                    judge(errorCodeBean.getCode() + "");
                    progressDialog.dismiss();
                } catch (Exception e) {
                    Toast.makeText(BaseActivity.this, getResources().getString(R.string.connection_timeout_or_illegal_request), Toast.LENGTH_SHORT).show();
                }

            }
        }
    };


    public void judge(String result) {
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


}
