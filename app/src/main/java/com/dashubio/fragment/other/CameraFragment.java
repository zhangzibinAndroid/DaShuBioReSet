package com.dashubio.fragment.other;


import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dashubio.R;
import com.dashubio.activity.HomeActivity;
import com.dashubio.base.BaseFragment;
import com.dashubio.bean.CameraCardBean;
import com.dashubio.bean.ErrorCodeBean;
import com.dashubio.bean.UserLoginBean;
import com.dashubio.constant.ErrorCode;
import com.dashubio.constant.InterfaceUrl;
import com.dashubio.fragment.main.UserLoginFragment;
import com.dashubio.gson.GsonParsing;
import com.dashubio.utils.CameraManager;
import com.dashubio.utils.FileUtil;
import com.dashubio.utils.HttpUtil;
import com.dashubio.utils.NetUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/17 0017
 * 时间： 下午 5:40
 * 描述： 身份证拍照页面
 */
public class CameraFragment extends BaseFragment implements Callback {
    protected static final String TAG = "TAG";
    private CameraManager mCameraManager;
    private SurfaceView mSurfaceView;
    private ProgressBar pb;
    private ImageButton mShutter;
    private SurfaceHolder mSurfaceHolder;
    private String flashModel = Parameters.FLASH_MODE_OFF;
    private byte[] jpegData = null;

    public CameraFragment() {
        // Required empty public constructor
    }

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(getActivity(), "拍照失败", Toast.LENGTH_SHORT).show();
                    mCameraManager.initPreView();
                    break;
                case 1:
                    jpegData = (byte[]) msg.obj;
                    if (jpegData != null && jpegData.length > 0) {
                        pb.setVisibility(View.VISIBLE);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if ((jpegData.length > (1000 * 1024 * 5))) {
                                    mHandler.sendMessage(mHandler.obtainMessage(3, getResources().getString(R.string.photo_too_lage)));
                                    return;
                                }
                                String result = null;
                                boolean isavilable = NetUtil.isNetworkConnectionActive(getActivity());
                                if (isavilable) {
                                    result = Scan(UserLoginFragment.action, jpegData, "jpg");
                                    Log.d(TAG, "" + result);

                                    if (result.equals("-2")) {
                                        result = "连接超时！";
                                        mHandler.sendMessage(mHandler.obtainMessage(3, result));
                                    } else if (HttpUtil.connFail.equals(result)) {
                                        mHandler.sendMessage(mHandler.obtainMessage(3, result));
                                    } else {
                                        mHandler.sendMessage(mHandler.obtainMessage(4, result));
                                    }
                                } else {
                                    mHandler.sendMessage(mHandler.obtainMessage(3, "无网络，请确定网络是否连接!"));
                                }
                            }
                        }).start();
                    }
                    break;
                case 3:
                    pb.setVisibility(View.GONE);
                    String str = msg.obj + "";
                    Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
                    mCameraManager.initPreView();
                    mShutter.setEnabled(true);
                    break;
                case 4:
                    mShutter.setEnabled(true);
                    pb.setVisibility(View.GONE);
                    String result = msg.obj + "";
                    CameraCardBean cameraCardBean = null;
                    try {
                        cameraCardBean = GsonParsing.getCardMessageJson(result);
                        CameraCardBean.DataBean dataBean = cameraCardBean.getData();
                        final CameraCardBean.DataBean.ItemBean itemBean = dataBean.getItem();
                        Log.e(TAG, "handleMessage: " + itemBean.getCardno());
                        loginInstance(itemBean.getCardno());
                        Toast.makeText(getActivity(), "操作成功", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "身份证识别失败", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case 5:
                    String filePath = msg.obj + "";
                    byte[] data = FileUtil.getByteFromPath(filePath);
                    if (data != null && data.length > 0) {
                        mHandler.sendMessage(mHandler.obtainMessage(1, data));
                    } else {
                        mHandler.sendMessage(mHandler.obtainMessage(0));
                    }
                    break;
                case 6:
                    Toast.makeText(getActivity(), "请插入存储卡！", Toast.LENGTH_SHORT).show();
                    mCameraManager.initPreView();
                    break;
            }
        }

        ;
    };

    private void loginInstance(String cardID) {
        OkHttpUtils.get().url(InterfaceUrl.USER_LOGIN_URL + sessonWithCode)
                .addParams("card_id", cardID)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        toastOnUi("登录失败，请检查网络");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, "onResponse: "+response );
                        Message msg = new Message();
                        msg.obj = response;
                        loginHandler.sendMessage(msg);
                    }
                });
    }

    private Handler loginHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            if (result.indexOf(ErrorCode.SUCCESS) > 0) {
                try {
                    UserLoginBean bean = GsonParsing.getUserLoginMessageJson(result);
                    if (bean.getData() ==null){
                        Toast.makeText(getActivity(), getResources().getString(R.string.user_not_exist_please_register_first), Toast.LENGTH_SHORT).show();
                    }else {
                        UserLoginBean.UserLoginDataBean dataBean = bean.getData();
                        JumpActivityWithUserData(HomeActivity.class,dataBean.getName(),dataBean.getId());
                    }
                    Log.e(TAG, "data: "+bean.getData() );
                } catch (Exception e) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_timeout_or_illegal_request), Toast.LENGTH_SHORT).show();
                }

            } else {
                //解析
                ErrorCodeBean errorCodeBean = null;
                try {
                    errorCodeBean = GsonParsing.sendCodeError(result);
                    judge(errorCodeBean.getCode() + "");
                } catch (Exception e) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_timeout_or_illegal_request), Toast.LENGTH_SHORT).show();
                }
            }


        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_camera, container, false);
        mCameraManager = new CameraManager(getActivity(), mHandler);
        initViews(view);

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(getActivity(), "请插入存储卡", Toast.LENGTH_LONG).show();
        }

        File dir = new File(CameraManager.strDir);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return view;
    }


    private void initViews(View view) {
        pb = (ProgressBar) view.findViewById(R.id.reco_recognize_bar);
        mSurfaceView = (SurfaceView) view.findViewById(R.id.camera_preview);
        mShutter = (ImageButton) view.findViewById(R.id.camera_shutter);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mShutter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCameraManager.requestFocuse();
                mShutter.setEnabled(false);
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCameraManager.openCamera(mSurfaceHolder);
            if (flashModel == null || !mCameraManager.isSupportFlash(flashModel)) {
                flashModel = mCameraManager.getDefaultFlashMode();
            }
            mCameraManager.setCameraFlashMode(flashModel);
        } catch (RuntimeException e) {
            Toast.makeText(getActivity(), R.string.camera_open_error, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getActivity(), R.string.camera_open_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (width > height) {
            mCameraManager.setPreviewSize(width, height);
        } else {
            mCameraManager.setPreviewSize(height, width);
        }
        mCameraManager.initPreView();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCameraManager.closeCamera();
    }

    public static String Scan(String type, byte[] file, String ext) {
        String xml = HttpUtil.getSendXML(type, ext);
        return HttpUtil.send(xml, file);
    }

}
