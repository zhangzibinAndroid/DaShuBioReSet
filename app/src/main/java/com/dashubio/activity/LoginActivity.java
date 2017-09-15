package com.dashubio.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.dashubio.R;
import com.dashubio.application.DashuApplication;
import com.dashubio.base.BaseActivity;
import com.dashubio.bean.dbmanagerbean.LoginUserBean;
import com.dashubio.constant.InterfaceUrl;
import com.dashubio.utils.NetUtil;
import com.dashubio.versionUpdate.UpdateInfo;
import com.dashubio.versionUpdate.UpdateInfoService;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/12 0012
 * 时间： 上午 10:04
 * 描述： 登录页面
 */
public class LoginActivity extends BaseActivity {
    private Unbinder unbinder;
    private PopupWindow login_window = null, register_window = null;
    // 更新版本要用到的一些信息
    private UpdateInfo info;
    private ProgressDialog pBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unbinder = ButterKnife.bind(this);
        DashuApplication.addActivity(this);
        Toast.makeText(getApplicationContext(), "正在检查版本更新..", Toast.LENGTH_SHORT).show();
        // 自动检查有没有新版本 如果有新版本就提示更新
        new Thread() {
            public void run() {
                try {
                    UpdateInfoService updateInfoService = new UpdateInfoService(
                            getApplicationContext());
                    info = updateInfoService.getUpDateInfo();
                    handler1.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
        }.start();
    }

    private void showLoginWindow() {
        //登录
        View loginView = LayoutInflater.from(this).inflate(R.layout.dialog_login, null);
        login_window = new PopupWindow(loginView,
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT);
        ColorDrawable cd = new ColorDrawable(0x000000);
        login_window.setBackgroundDrawable(cd);
        login_window.setOutsideTouchable(true);
        login_window.setFocusable(true);
        //设置弹出窗体需要软键盘，
        login_window.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        login_window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        login_window.showAtLocation(loginView, Gravity.CENTER, 0, 0);
        final EditText et_account = (EditText) loginView.findViewById(R.id.et_account);
        final EditText et_password = (EditText) loginView.findViewById(R.id.et_password);
        Button btn_login = (Button) loginView.findViewById(R.id.btn_login);
        Button btn_reg = (Button) loginView.findViewById(R.id.btn_reg);
        ImageView cancle = (ImageView) loginView.findViewById(R.id.cancle);
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        String phone = sharedPreferences.getString("phone", "");
        String password = sharedPreferences.getString("password", "");
        et_account.setText(phone);
        et_password.setText(password);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("正在登录");
                //手机号
                final String name = et_account.getText().toString();
                if (name.equals("")) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.please_input_username), Toast.LENGTH_SHORT).show();
                    return;
                }

                //密码
                final String mPassword = et_password.getText().toString();
                if (mPassword.equals("")) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.please_input_password), Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.show();
                //如果网络可用，则调用登录接口
                if (NetUtil.isNetworkConnectionActive(LoginActivity.this)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            equipmentLoginInterface(InterfaceUrl.LOGIN_URL, name, mPassword);
                        }
                    }).start();
                } else {
                    //如果网络不可用，则查询数据库，设备有数据则登录，无则不予通过
                    ArrayList<LoginUserBean> loginUserBeanList = new ArrayList<>();
                    loginUserBeanList = dbManager.searchLoginData(name,mPassword);
                    String result = "";
                    for (LoginUserBean loginUserBean : loginUserBeanList) {
                        result = result + String.valueOf(loginUserBean._id) + "|" + loginUserBean.name
                                + "|" + loginUserBean.pwds ;
                        result = result + "\n" + "------------------------------------------" + "\n";
                    }

                    if (result.indexOf(name)!=-1&&result.indexOf(mPassword)!=-1) {
                        //数据库有数据
                        JumpActivity(MainActivity.class);
                    }else {
                        toastOnUi("数据库无此设备，请先在线登录一次");
                    }
                    progressDialog.dismiss();
                }


            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_window.dismiss();
            }
        });

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_window.dismiss();
                showRegisterWindow();
            }
        });

    }


    private void showRegisterWindow() {
        //注册
        View registerView = LayoutInflater.from(this).inflate(R.layout.dialog_register, null);
        register_window = new PopupWindow(registerView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        ColorDrawable cd = new ColorDrawable(0x000000);
        register_window.setBackgroundDrawable(cd);
        register_window.setOutsideTouchable(true);
        register_window.setFocusable(true);
        register_window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        register_window.showAtLocation(registerView, Gravity.CENTER, 0, 0);
        ImageView cancle = (ImageView) registerView.findViewById(R.id.cancle);
        final EditText et_imei = (EditText) registerView.findViewById(R.id.et_imei);
        final EditText et_phone = (EditText) registerView.findViewById(R.id.et_phone);
        final EditText et_password = (EditText) registerView.findViewById(R.id.et_password);
        final EditText et_password_again = (EditText) registerView.findViewById(R.id.et_password_again);
        final EditText et_code = (EditText) registerView.findViewById(R.id.et_code);
        final Button btn_code = (Button) registerView.findViewById(R.id.btn_code);
        Button btn_reg = (Button) registerView.findViewById(R.id.btn_reg);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_window.dismiss();
            }
        });

        btn_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String telephone = et_phone.getText().toString();
                if (telephone.equals("")) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.input_reg_telphone), Toast.LENGTH_SHORT).show();
                    return;
                }
                startTime(TIME, btn_code);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        verificationCodeInterface(InterfaceUrl.VERIFICATION_CODE, telephone);
                    }
                }).start();


            }
        });


        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("正在注册");
                progressDialog.show();
                //设备串号
                final String serialNumber = et_imei.getText().toString();
                if (serialNumber.equals("")) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.please_enter_the_serial_number_of_equipment), Toast.LENGTH_SHORT).show();
                    return;
                }

                //手机号
                final String telephone = et_phone.getText().toString();
                if (telephone.equals("")) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.input_reg_telphone), Toast.LENGTH_SHORT).show();
                    return;
                }

                //密码
                final String password = et_password.getText().toString();
                if (password.equals("")) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.please_input_password), Toast.LENGTH_SHORT).show();
                    return;
                }

                //再次输入密码
                String confirmPassword = et_password_again.getText().toString();
                if (!confirmPassword.equals(password)) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.confirm_password_error), Toast.LENGTH_SHORT).show();
                    return;
                }


                //验证码
                final String verificationCode = et_code.getText().toString();
                if (verificationCode.equals("")) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.please_input_verification_code), Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        equipmentReisterInterface(InterfaceUrl.EQUIPMENT_REGISTER, serialNumber, telephone, password, verificationCode);
                    }
                }).start();


            }
        });

    }

    @OnClick({R.id.tv_login, R.id.tv_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                showLoginWindow();
                break;
            case R.id.tv_register:
                showRegisterWindow();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler1 = new Handler() {
        public void handleMessage(Message msg) {
            // 如果有更新就提示
            if (isNeedUpdate()) {
                showUpdateDialog();
            }
        };
    };


    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle("有新版本" + info.getVersion());
        builder.setMessage(info.getDescription());
        builder.setCancelable(false);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    downFile(info.getUrl());
                } else {
                    Toast.makeText(getApplicationContext(), "SD卡不可用，请插入SD卡",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }

        });
        builder.create().show();
    }

    private boolean isNeedUpdate() {

        String v = info.getVersion(); // 最新版本的版本号
        Toast.makeText(getApplicationContext(), v, Toast.LENGTH_SHORT).show();
        if (v.equals(getVersion())) {
            return false;
        } else {
            return true;
        }
    }

    // 获取当前版本的版本号
    private String getVersion() {
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    getPackageName(), 0);

            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "版本号未知";

        }
    }

    void downFile(final String url) {
        pBar = new ProgressDialog(this);    //进度条，在下载的时候实时更新进度，提高用户友好度
        pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pBar.setCanceledOnTouchOutside(false);
        pBar.setTitle("正在更新");
        pBar.setMessage("请稍候...");
        pBar.setProgress(0);
        pBar.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get().url(url).build().execute(new FileCallBack(Environment.getExternalStorageDirectory()+"/dashuhd" , "dashuhd.apk") {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        down();
                    }


                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);

                        int mtotal = (int) total;
                        pBar.setMax(mtotal);
                        pBar.setProgress((int)(mtotal* progress));
                    }
                });
            }
        }).start();
    }

    void down() {
        handler1.post(new Runnable() {
            public void run() {
                pBar.cancel();
                update();
            }
        });
    }

    void update() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory()+"/dashuhd", "dashuhd.apk")),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }

}
