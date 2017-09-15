package com.dashubio.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dashubio.R;
import com.dashubio.application.DashuApplication;
import com.dashubio.base.BaseActivity;
import com.dashubio.bean.ErrorCodeBean;
import com.dashubio.bean.EventLoginMessage;
import com.dashubio.bean.viewbean.TitleMessageBean;
import com.dashubio.constant.ErrorCode;
import com.dashubio.constant.InterfaceUrl;
import com.dashubio.fragment.main.HelpFragment;
import com.dashubio.fragment.main.MainFirstFragment;
import com.dashubio.fragment.main.UserLoginFragment;
import com.dashubio.fragment.main.UserManageFragment;
import com.dashubio.fragment.main.UserRegisterFragment;
import com.dashubio.fragment.main.WarningSettingFragment;
import com.dashubio.fragment.other.CameraFragment;
import com.dashubio.fragment.other.HelpDetailFragment;
import com.dashubio.gson.GsonParsing;
import com.dashubio.utils.NetUtil;
import com.dashubio.view.RoundImageView;
import com.linktop.DeviceType;
import com.linktop.MonitorDataTransmissionManager;
import com.linktop.infs.OnBleConnectListener;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;


/**
 * 作者： 张梓彬
 * 日期： 2017/7/12 0012
 * 时间： 下午 6:09
 * 描述： 主页面
 */
public class MainActivity extends BaseActivity implements MonitorDataTransmissionManager.OnServiceBindListener, OnBleConnectListener {
    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.img_user)
    RoundImageView imgUser;
    @BindView(R.id.tv_company_name)
    TextView tvCompanyName;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindViews({R.id.tv_first, R.id.tv_user_register, R.id.tv_user_login, R.id.tv_user_manage, R.id.tv_warning_setting, R.id.tv_help})
    TextView[] tv_sel;
    @BindView(R.id.content)
    AutoFrameLayout content;
    private Unbinder unbinder;
    private MainFirstFragment mainFirstFragment;
    private UserRegisterFragment userRegisterFragment;
    private UserLoginFragment userLoginFragment;
    private UserManageFragment userManageFragment;
    private WarningSettingFragment warningSettingFragment;
    private HelpFragment helpFragment;
    private CameraFragment cameraFragment;
    private HelpDetailFragment helpDetailFragment;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        getWindow().getAttributes().softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN;
        initView();

    }

    private void initView() {
        manager = MonitorDataTransmissionManager.getInstance();
        manager.bind(DeviceType.HealthMonitor, this);

        if (NetUtil.isNetworkConnectionActive(this)){
            getTitleMessage();
        }
        mainFirstFragment = new MainFirstFragment();
        userRegisterFragment = new UserRegisterFragment();
        userLoginFragment = new UserLoginFragment();
        userManageFragment = new UserManageFragment();
        warningSettingFragment = new WarningSettingFragment();
        helpFragment = new HelpFragment();
        cameraFragment = new CameraFragment();
        helpDetailFragment = new HelpDetailFragment();
        Intent intent = getIntent();
        String companyName = intent.getStringExtra("companyName");
        tvCompanyName.setText(companyName);
        tv_sel[0].setSelected(true);
        setReplaceFragment(R.id.content, mainFirstFragment);
    }


    private long exitTime = 0;//点击2次返回，退出程序

    //点击两次退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {//两秒内再次点击返回则退出
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                dbManager.closeDB();//退出程序关闭数据库
                exitTime = System.currentTimeMillis();
            } else {
                DashuApplication.clearActivity();
                dbManager.closeDB();//退出程序关闭数据库
                System.exit(0);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.tv_first, R.id.tv_user_register, R.id.tv_user_login, R.id.tv_user_manage, R.id.tv_warning_setting, R.id.tv_help})
    public void onViewClicked(View view) {
        getWindow().getAttributes().softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN;
        for (int i = 0; i < tv_sel.length; i++) {
            tv_sel[i].setSelected(false);
        }

        switch (view.getId()) {
            case R.id.tv_first:
                tv_sel[0].setSelected(true);
                setReplaceFragment(R.id.content, mainFirstFragment);
                break;
            case R.id.tv_user_register:
                tv_sel[1].setSelected(true);
                setReplaceFragment(R.id.content, userRegisterFragment);

                break;
            case R.id.tv_user_login:
                tv_sel[2].setSelected(true);
                setReplaceFragment(R.id.content, userLoginFragment);

                break;
            case R.id.tv_user_manage:
                tv_sel[3].setSelected(true);
                setReplaceFragment(R.id.content, userManageFragment);

                break;
            case R.id.tv_warning_setting:
                tv_sel[4].setSelected(true);
                setReplaceFragment(R.id.content, warningSettingFragment);

                break;
            case R.id.tv_help:
                tv_sel[5].setSelected(true);
                setReplaceFragment(R.id.content, helpFragment);

                break;
        }
    }

    //为了避免影响选择器效果，和上面的监听分开
    @OnClick(R.id.tv_quit)
    public void onViewClicked() {
        getWindow().getAttributes().softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN;
        finish();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessage(EventLoginMessage event) {
        if (event.message.equals("message")) {
            setReplaceFragment(R.id.content, userManageFragment);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getCardMessage(EventLoginMessage event) {
        if (event.message.equals("cardMessage")) {
            setReplaceFragment(R.id.content, cameraFragment);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getHelpMessage(EventLoginMessage event) {
        if (event.message.equals("HelpMessage")) {
            setReplaceFragment(R.id.content, helpDetailFragment);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getHelpList(EventLoginMessage event) {
        if (event.message.equals("HelpList")) {
            setReplaceFragment(R.id.content, helpFragment);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
        manager.unBind();
    }


    //获取标题信息
    private void getTitleMessage() {
        Log.e(TAG, "getTitleMessageUlr: " + InterfaceUrl.TITLE_URL + sessonWithCode);
        OkHttpUtils.get().url(InterfaceUrl.TITLE_URL + sessonWithCode)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                Message msg = new Message();
                msg.obj = response;
                titleMessageHandler.sendMessage(msg);
            }
        });

    }


    private Handler titleMessageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            if (result.indexOf(ErrorCode.SUCCESS) > 0) {
                try {
                    TitleMessageBean titleMessageBean = GsonParsing.getTitleMessageJson(result);
                    TitleMessageBean.MessageDataBean messageDataBean = titleMessageBean.getData();
                    String titleMessage = messageDataBean.getCon();
                    tv_title.setText(titleMessage);

                } catch (Exception e) {
                    Log.e(TAG, "标题解析失败: "+e.getMessage() );
                }
            }else {
                //解析
                ErrorCodeBean errorCodeBean = null;
                try {
                    errorCodeBean = GsonParsing.sendCodeError(result);
                    judge(errorCodeBean.getCode() + "");
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.connection_timeout_or_illegal_request), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };


    @Override
    public void onServiceBind() {
        manager.setScanDevNamePrefixWhiteList(R.array.health_monitor_dev_name_prefixes);
        manager.setOnBleConnectListener(this);
    }

    @Override
    public void onServiceUnbind() {

    }

    @Override
    public void onBLENoSupported() {
        Toast.makeText(this, "蓝牙不支持", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onOpenBLE() {

    }

    @Override
    public void onBleState(int i) {
        switch (i) {
            case com.linktop.constant.BluetoothState.BLE_CLOSED:
                Toast.makeText(this, "蓝牙未开启", Toast.LENGTH_SHORT).show();
                break;
            case com.linktop.constant.BluetoothState.BLE_OPENED_AND_DISCONNECT:
//                Toast.makeText(this, "点此连接", Toast.LENGTH_SHORT).show();
                break;
            case com.linktop.constant.BluetoothState.BLE_CONNECTING_DEVICE:
                Toast.makeText(this, "正在连接", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onUpdateDialogBleList() {

    }
}
