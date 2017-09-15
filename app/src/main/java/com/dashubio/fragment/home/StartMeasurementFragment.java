package com.dashubio.fragment.home;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.breathhome_ble_sdk.asynctask.AsyncResponse;
import com.breathhome_ble_sdk.asynctask.GetBleCurrentVersionTask;
import com.breathhome_ble_sdk.asynctask.SearchBleDeviceTask;
import com.breathhome_ble_sdk.bean.BleVersionMsgBean;
import com.breathhome_ble_sdk.bean.BluetoothDeviceBean;
import com.breathhome_ble_sdk.bean.HolderBean;
import com.breathhome_ble_sdk.bean.PefDataFromBleBean;
import com.breathhome_ble_sdk.bean.ReturnBean;
import com.breathhome_ble_sdk.broadreceiver.BroadcastResponse;
import com.breathhome_ble_sdk.controller.BluetoothController;
import com.breathhome_ble_sdk.message.MessageManager;
import com.breathhome_ble_sdk.utils.ConstantUtils;
import com.contec.jar.BC401.BC401_Data;
import com.contec.jar.BC401.BC401_Struct;
import com.contec.jar.BC401.DeviceCommand;
import com.contec.jar.BC401.DevicePackManager;
import com.dashubio.R;
import com.dashubio.activity.BluetoothDeviceListActivity;
import com.dashubio.activity.HomeActivity;
import com.dashubio.adapter.blueadapter.BlueAdapter;
import com.dashubio.adapter.blueadapter.BlueHuXiAdapter;
import com.dashubio.adapter.viewadapter.GanShiAdapter;
import com.dashubio.adapter.viewadapter.NiaoJiAdapter;
import com.dashubio.application.DashuApplication;
import com.dashubio.base.BaseFragment;
import com.dashubio.bean.DryDetectItem;
import com.dashubio.bean.ErrorCodeBean;
import com.dashubio.bean.eventmsg.EventMessage;
import com.dashubio.broadcast.MyBleStateBroadcast;
import com.dashubio.constant.Constants;
import com.dashubio.constant.ErrorCode;
import com.dashubio.constant.InterfaceUrl;
import com.dashubio.db.DBManager;
import com.dashubio.ecg_single.OnCallBack;
import com.dashubio.ecg_single.SingleLeadUtil;
import com.dashubio.ecg_single.ble.JPBleNormalData;
import com.dashubio.ecg_single.entity.Data;
import com.dashubio.fragment.other.BlueDialogListFragment;
import com.dashubio.gson.GsonParsing;
import com.dashubio.service.MyBLEService;
import com.dashubio.utils.DryDetectResult;
import com.dashubio.utils.NetUtil;
import com.dashubio.utils.Utils;
import com.dashubio.view.EcgPathOne;
import com.example.android.bluetoothlegatt.BleGattAttributes;
import com.example.android.bluetoothlegatt.BluetoothLeService;
import com.google.gson.Gson;
import com.linktop.MonitorDataTransmissionManager;
import com.linktop.infs.OnBatteryListener;
import com.linktop.infs.OnDeviceVersionListener;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.akexorcist.bluetotohspp.library.BluetoothConnection;
import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/13 0013
 * 时间： 上午 10:15
 * 描述： 开始测量
 */
public class StartMeasurementFragment extends BaseFragment implements OnDeviceVersionListener, OnBatteryListener {
    private static final String TAG = "StartMeasurementFragment";
    @BindView(R.id.lay_multi_parameter_monitor)
    AutoLinearLayout layMultiParameterMonitor;
    @BindView(R.id.lay_urine_detector)
    AutoLinearLayout layUrineDetector;
    @BindView(R.id.lay_dry_biochemical_analyzer)
    AutoLinearLayout layDryBiochemicalAnalyzer;
    @BindView(R.id.lay_respiratory_monitor)
    AutoLinearLayout layRespiratoryMonitor;
    private Unbinder unbinder;
    private AlertDialog dialog;
    private SingleLeadUtil singleLeadUtil;
    private List<BluetoothDevice> deviceList = new ArrayList<>();
    private BlueAdapter blueAdapter;
    private BlueHuXiAdapter blueHuXiAdapter;
    public DashuApplication myApplication;

    public StartMeasurementFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_start_measurement, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        manager = MonitorDataTransmissionManager.getInstance();
        if (manager.isConnected()){
            manager.disConnectBle();
        }
        dbManager = new DBManager(getActivity());
        myApplication = new DashuApplication();
        blueHuXiAdapter = new BlueHuXiAdapter(getActivity());
        blueAdapter = new BlueAdapter(getActivity());
        mGson = new Gson();
        mUrineBluetoothSPP = new BluetoothSPP(getActivity(), mSecure);
        if (!mUrineBluetoothSPP.isBluetoothAvailable()) {
            Toast.makeText(getActivity(), "蓝牙不可用", Toast.LENGTH_SHORT).show();
        }

        if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(getActivity(), "该设备不支持低功耗蓝牙", Toast.LENGTH_SHORT).show();
        }

        bindDryBluetoothService();
        initDatas();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    @OnClick({R.id.lay_multi_parameter_monitor, R.id.lay_urine_detector, R.id.lay_dry_biochemical_analyzer, R.id.lay_respiratory_monitor})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lay_multi_parameter_monitor:
                try {
                    showBlueDeceiveList();
                    manager.setOnDeviceVersionListener(this);
                    manager.setOnBatteryListener(this);
                }catch (Exception e){
                    Log.e(TAG, "健康检测仪设备异常: "+e.getMessage() );
                }

                break;
            case R.id.lay_urine_detector:
                findBluetoothDevices(BluetoothDeviceListActivity.REQUEST_CONNECT_DEVICE_URINE_ANALYZER);
                break;
            case R.id.lay_dry_biochemical_analyzer:
                findBluetoothDevices(BluetoothDeviceListActivity.REQUEST_CONNECT_DEVICE_DRY_BIOCHEMICAL_ANALYZER);
                break;
            case R.id.lay_respiratory_monitor:
                showBlueToathHuXiDialog();
                break;
        }
    }


    private boolean isSacn = true;

    private void showBlueToathDialog() {
        AlertDialog.Builder blueToathDialog = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.dialog_search_buletooth, null);
        ViewHolderBlueToath viewHolderBlueToath = new ViewHolderBlueToath(view);
        blueToathDialog.setView(view);
        viewHolderBlueToath.lvSearchBluetooth.setAdapter(blueAdapter);
        viewHolderBlueToath.lvSearchBluetooth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //匹配
                singleLeadUtil.pairDevice(deviceList.get(position));
                dialog.dismiss();
                showMultiParameterMonitorDialog();

            }
        });
        dialog = blueToathDialog.show();
        if (isSacn) {
            singleLeadUtil.scanLeDevice(true);
            isSacn = false;
        }
    }


    //多参数监测仪
    private void showMultiParameterMonitorDialog() {
        AlertDialog.Builder multiParameterMonitorDialog = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.dialog_xindian, null);
        final ViewHolderMultiParameterMonitor viewHolder = new ViewHolderMultiParameterMonitor(view);
        viewHolder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleLeadUtil.disconnect();//点击取消断开蓝夜连接
                dialog.dismiss();
            }
        });
        viewHolder.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final long time = System.currentTimeMillis() / 1000;
                //保存数据
                singleLeadUtil.disconnect();//点击取消断开蓝夜连接
                dialog.dismiss();

                if (NetUtil.isNetworkConnectionActive(getActivity())) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            saveMultiParameterMonitorData(time + "", viewHolder.tvHeartRateColon.getText().toString(),
                                    viewHolder.tvPulse.getText().toString(),
                                    viewHolder.tvSystolicBloodPressureDifference.getText().toString(),
                                    viewHolder.tvSystolicBloodPressure.getText().toString(),
                                    viewHolder.tvDiastolicBloodPressure.getText().toString(),
                                    viewHolder.tvTheAveragePressure.getText().toString(),
                                    viewHolder.tvOxygen.getText().toString(),
                                    viewHolder.tvBreathingRate.getText().toString(),
                                    viewHolder.tvStSegmentNumerical.getText().toString());
                        }
                    }).start();
                } else {
                    dbManager.addMultiData(Constants.id, time + "", viewHolder.tvHeartRateColon.getText().toString(),
                            viewHolder.tvPulse.getText().toString(),
                            viewHolder.tvSystolicBloodPressureDifference.getText().toString(),
                            viewHolder.tvSystolicBloodPressure.getText().toString(),
                            viewHolder.tvDiastolicBloodPressure.getText().toString(),
                            viewHolder.tvTheAveragePressure.getText().toString(),
                            viewHolder.tvOxygen.getText().toString(),
                            viewHolder.tvBreathingRate.getText().toString(),
                            viewHolder.tvStSegmentNumerical.getText().toString());
                    Toast.makeText(getActivity(), "本地保存成功", Toast.LENGTH_SHORT).show();
                }


            }
        });
        multiParameterMonitorDialog.setView(view);
        multiParameterMonitorDialog.setCancelable(false);
        dialog = multiParameterMonitorDialog.show();
        setOnDataCallBackListener(new OnDataCallBackListener() {
            @Override
            public void OnDataCallBack(ArrayList<Float> ecg_list_one) {
                viewHolder.ecg_path.addDATA(ecg_list_one);
            }
        });

        setOnTextDataCallBackListener(new OnTextDataCallBackListener() {
            @Override
            public void OnTextDataCallBack(String rhythm, String pulse, String sysdif, String sys, String dias, String mean, String oxygen, String resp, String st) {
                viewHolder.tvHeartRateColon.setText(rhythm);
                viewHolder.tvPulse.setText(pulse);
                viewHolder.tvSystolicBloodPressureDifference.setText(sysdif);
                viewHolder.tvSystolicBloodPressure.setText(sys);
                viewHolder.tvDiastolicBloodPressure.setText(dias);
                viewHolder.tvTheAveragePressure.setText(mean);
                viewHolder.tvOxygen.setText(oxygen);
                viewHolder.tvBreathingRate.setText(resp);
                viewHolder.tvStSegmentNumerical.setText(st);
            }
        });


    }


    //多参数检测仪数据保存接口
    private void saveMultiParameterMonitorData(String date, String rhythm, String pulse, String sysdif, String sys, String dias, String mean, String oxygen, String resp, String st) {
        OkHttpUtils.post().url(InterfaceUrl.MULTIPARAMETERMONITORDATA_URL + sessonWithCode + "/mid/" + HomeActivity.mid)
                .addParams("date", date)
                .addParams("rhythm", rhythm)
                .addParams("pulse", pulse)
                .addParams("sysdif", sysdif)
                .addParams("sys", sys)
                .addParams("dias", dias)
                .addParams("mean", mean)
                .addParams("oxygen", oxygen)
                .addParams("resp", resp)
                .addParams("st", st)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                toastOnUi("保存异常，请检查网络");
            }

            @Override
            public void onResponse(String response, int id) {
                toastOnUi("保存成功");

            }
        });
    }

    @Override
    public void onDeviceSoftwareVersion(String s) {

    }

    @Override
    public void onDeviceHardwareVersion(String s) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "连接成功", Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new EventMessage("health_deceive"));
            }
        });
    }

    @Override
    public void onBatteryCharging() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "设备充电中，请暂勿使用设备", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBatteryQuery(final int i) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (i<10){
                    Toast.makeText(getActivity(), "设备电量剩余"+i+"%,请及时充电！", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(), "设备电量剩余"+i+"%！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBatteryFull() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "设备电量充足", Toast.LENGTH_SHORT).show();
            }
        });
    }


    static class ViewHolderMultiParameterMonitor {
        @BindView(R.id.tv_heart_rate_colon)
        TextView tvHeartRateColon;
        @BindView(R.id.tv_pulse)
        TextView tvPulse;
        @BindView(R.id.tv_systolic_blood_pressure_difference)
        TextView tvSystolicBloodPressureDifference;
        @BindView(R.id.tv_systolic_blood_pressure)
        TextView tvSystolicBloodPressure;
        @BindView(R.id.tv_diastolic_blood_pressure)
        TextView tvDiastolicBloodPressure;
        @BindView(R.id.tv_the_average_pressure)
        TextView tvTheAveragePressure;
        @BindView(R.id.tv_oxygen)
        TextView tvOxygen;
        @BindView(R.id.tv_breathing_rate)
        TextView tvBreathingRate;
        @BindView(R.id.tv_st_segment_numerical)
        TextView tvStSegmentNumerical;
        @BindView(R.id.btn_cancel)
        Button btnCancel;
        @BindView(R.id.btn_save)
        Button btnSave;
        @BindView(R.id.ecg_path)
        EcgPathOne ecg_path;
        /*@BindView(R.id.ecg_path_second)
        EcgPathSecond ecg_path_second;*/

        ViewHolderMultiParameterMonitor(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolderBlueToath {
        @BindView(R.id.lv_search_bluetooth)
        ListView lvSearchBluetooth;

        ViewHolderBlueToath(View view) {
            ButterKnife.bind(this, view);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        singleLeadUtil = new SingleLeadUtil(getActivity(), new OnCallBack() {
            @Override
            public void onStatusCallBack(final int status) {
                String msg = "";
                //蓝牙状态返回
                switch (status) {
                    case OnCallBack.DEVICE_CONNECTED:
                        msg = "设备已连接";
                        break;
                    case OnCallBack.DEVICE_DISCONNECTED:
                        msg = "设备已断开";
                        break;
                    case OnCallBack.NO_BLE_ADAPTER:
                        msg = "没有适配器";
                        break;
                    case OnCallBack.NOT_SUPPORT_BLE:
                        msg = "不支持蓝牙";
                        break;
                    case OnCallBack.SEARCH_DEVICES_FAILED:
                        msg = "搜索蓝牙失败";
                        break;
                }

                final String finalMsg = msg;
//                Toast.makeText(getActivity(), finalMsg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDataCallBack(Data data) {
                //数据返回
                switch (data.getType()) {
                    case Data.TYPE_TEST_DATA:
                        JPBleNormalData mdata = data.getData();//普通测量数据
                        int rhythm = 0;
                        try {
                            rhythm = mdata.rhythm;
                        } catch (Exception e) {
                        }

                        int pulse = 0;
                        try {
                            pulse = mdata.pulse;
                        } catch (Exception e) {
                        }

                        int sysdif = 0;
                        try {
                            sysdif = mdata.sysdif;
                        } catch (Exception e) {
                        }

                        int sys = 0;
                        try {
                            sys = mdata.sys;
                        } catch (Exception e) {
                        }

                        int dias = 0;
                        try {
                            dias = mdata.dias;
                        } catch (Exception e) {

                        }


                        int mean = 0;
                        try {
                            mean = mdata.mean;
                        } catch (Exception e) {
                        }

                        int oxygen = 0;
                        try {
                            oxygen = mdata.oxygen;
                        } catch (Exception e) {
                        }

                        int resp = 0;
                        try {
                            resp = mdata.resp;
                        } catch (Exception e) {
                        }

                        float st = 0;
                        try {
                            st = mdata.st;
                        } catch (Exception e) {
                        }

                        onTextDataCallBackListener.OnTextDataCallBack(rhythm + "", pulse + "", sysdif + "", sys + "", dias + "", mean + "", oxygen + "", resp + "", st + "");


                        break;
                    case Data.TYPE_TEST_END:
                        /*JPBlePressureData datazzb = data.getPressureData();//测量结束，JPBleNormalData中取得体征值
                        Log.e("ZZZ", "心率: " + datazzb.rhythm);
                        Log.e("ZZZ", "脉搏: " + datazzb.pulse);
                        Log.e("ZZZ", "收缩压差: " + datazzb.sysdif);
                        Log.e("ZZZ", "收缩压: " + datazzb.sys);
                        Log.e("ZZZ", "舒张压: " + datazzb.dias);
                        Log.e("ZZZ", "平均压: " + datazzb.mean);
                        Log.e("ZZZ", "血氧: " + datazzb.oxygen);
                        Log.e("ZZZ", "呼吸: " + datazzb.resp);
                        Log.e("ZZZ", "ST段数值: " + datazzb.st);*/

                        JPBleNormalData datazzb = data.getData();//普通测量数据
                        Log.e(TAG, "onDataCallBack: ");
                        try {
                            Log.e(TAG, "心率: " + datazzb.rhythm);
                        } catch (Exception e) {
                            Log.e(TAG, "心率: " + 0);
                        }


                        try {
                            Log.e(TAG, "脉搏: " + datazzb.pulse);
                        } catch (Exception e) {
                            Log.e(TAG, "脉搏: " + 0);
                        }

                        try {
                            Log.e(TAG, "收缩压差: " + datazzb.sysdif);
                        } catch (Exception e) {
                            Log.e(TAG, "收缩压差: " + 0);
                        }

                        try {
                            Log.e(TAG, "收缩压: " + datazzb.sys);
                        } catch (Exception e) {
                            Log.e(TAG, "收缩压: " + 0);
                        }

                        try {
                            Log.e(TAG, "舒张压: " + datazzb.dias);
                        } catch (Exception e) {
                            Log.e(TAG, "舒张压: " + 0);
                        }

                        try {
                            Log.e(TAG, "平均压: " + datazzb.mean);
                        } catch (Exception e) {
                            Log.e(TAG, "平均压: " + 0);
                        }


                        try {
                            Log.e(TAG, "血氧: " + datazzb.oxygen);
                        } catch (Exception e) {
                            Log.e(TAG, "血氧: " + 0);
                        }

                        try {
                            Log.e(TAG, "呼吸: " + datazzb.resp);
                        } catch (Exception e) {
                            Log.e(TAG, "呼吸: " + 0);
                        }

                        try {
                            Log.e(TAG, "ST段数值: " + datazzb.st);
                        } catch (Exception e) {
                            Log.e(TAG, "ST段数值: " + 0);
                        }
                        break;
                    case Data.TYPE_WAVE_1_DATA:
                        onDataCallBackListener.OnDataCallBack(data.getWaveData());
//                        onDataCallBackListenerSecond.OnDataCallBack(data.getWaveData());
                        break;
                    case Data.TYPE_WAVE_2_DATA:
                        break;
                }
            }

            @Override
            public void onDeviceFound(BluetoothDevice device) {
//                Log.e(TAG, "onDeviceFound: >>>>>>>>>>>>>>");

                //设备发现
                if (deviceList.contains(device)) {
                    Log.e(TAG, "onDeviceFound: ");
                    return;
                }

                if (device.getName().contains("Monitor")) {
                    deviceList.add(device);
                }


                /*for (int i = 0; i < deviceList.size(); i++) {
                    Log.e(TAG, "deviceList: " + deviceList.get(i).getName());
                }*/
                blueAdapter.addAllDataToMyadapter(deviceList);
                blueAdapter.notifyDataSetChanged();

            }
        });
    }


    private OnDataCallBackListener onDataCallBackListener;
    private OnDataCallBackListenerSecond onDataCallBackListenerSecond;
    private OnTextDataCallBackListener onTextDataCallBackListener;

    public static interface OnDataCallBackListener {
        void OnDataCallBack(ArrayList<Float> ecg_list_one);
    }

    public void setOnDataCallBackListener(OnDataCallBackListener onDataCallBackListener) {
        this.onDataCallBackListener = onDataCallBackListener;
    }


    public static interface OnDataCallBackListenerSecond {
        void OnDataCallBack(ArrayList<Float> ecg_list_two);
    }

    public void setOnDataCallBackListenerSecond(OnDataCallBackListenerSecond onDataCallBackListenerSecond) {
        this.onDataCallBackListenerSecond = onDataCallBackListenerSecond;
    }


    public static interface OnTextDataCallBackListener {
        void OnTextDataCallBack(String rhythm, String pulse, String sysdif, String sys, String dias, String mean, String oxygen, String resp, String st);
    }

    public void setOnTextDataCallBackListener(OnTextDataCallBackListener onTextDataCallBackListener) {
        this.onTextDataCallBackListener = onTextDataCallBackListener;

    }


    private List<BluetoothDeviceBean> devicelistHuXi = new ArrayList<>();                            //存放搜索到的蓝牙设备
    private String IMEI = "";                                        //IMEI号,用户绑定的设备IMEI号
    private StringBuffer sb;                                                //用作存放蓝牙设备的指令
    private HolderBean holder;
    private GetBleCurrentVersionTask getBleCurrentVersionTask;
    private SearchBleDeviceTask mySearchBleDeviceTask;
    private MessageManager myMessageManager;            //蓝牙信息管理器
    private BluetoothController mBluetoothController;
    private BleVersionMsgBean bleVersionMsg;
    private MyBleStateBroadcast myBleStateBroadcast;                        //自定义蓝牙状态监听
    private SystemBleBroadcast systemBleBroadcast;
    public static BluetoothDeviceBean bluetoothDeviceBean;

    private void showBlueToathHuXiDialog() {
        AlertDialog.Builder blueToathDialog = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.dialog_search_buletooth, null);
        ViewHolderBlueToath viewHolderBlueToath = new ViewHolderBlueToath(view);
        blueToathDialog.setView(view);
        viewHolderBlueToath.lvSearchBluetooth.setAdapter(blueHuXiAdapter);
        dialog = blueToathDialog.show();
        setitemOnClickMeathed(viewHolderBlueToath.lvSearchBluetooth);
    }

    private void initDatas() {
        getTheNewsVersion();                                                //获取最新版本号
        sb = new StringBuffer();
        holder = new HolderBean();
        holder.setDetectedNo(33);
        holder.setSaleChannel(7200000);
        holder.setBirthdate("1988-3-15");
        holder.setPef(659);
        holder.setFev1((float) 4.56);
        holder.setFvc((float) 3.56);
        holder.setGender(1);
        holder.setHeight(183);
        holder.setWeight(78);
        holder.setDevieceNo("B652276135");
    }

    private void getTheNewsVersion() {
        getBleCurrentVersionTask = (GetBleCurrentVersionTask) new GetBleCurrentVersionTask(getActivity()).execute();
        getBleCurrentVersionTask.setAsyncResponse(new AsyncResponse<ReturnBean<BleVersionMsgBean>>() {
            @Override
            public void onDataReceivedFailed() {
                Log.e(TAG, "Error");
            }

            @Override
            public void onDataReceivedSuccess(ReturnBean<BleVersionMsgBean> returnBean) {
                Log.e(TAG, "onDataReceivedSuccess: ");
                bleVersionMsg = returnBean.getObject();
                initTools();                            //初始化工具
                initBroadcast();                        //定义广播接收
            }

            @Override
            public void onDataReceivedAndDefaultData(ReturnBean<BleVersionMsgBean> returnBean) {
                Log.e(TAG, "onDataReceivedAndDefaultData: ");
                bleVersionMsg = returnBean.getObject();
                initTools();                            //初始化工具
                initBroadcast();                        //定义广播接收
            }
        });
    }


    private void initBroadcast() {
        myBleStateBroadcast = new MyBleStateBroadcast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantUtils.ACTION_UPDATE_DEVICE_LIST);
        intentFilter.addAction(ConstantUtils.ACTION_CONNECTED_ONE_DEVICE);
        intentFilter.addAction(ConstantUtils.ACTION_RECEIVE_MESSAGE_FROM_DEVICE);
        intentFilter.addAction(ConstantUtils.ACTION_STOP_CONNECT);
        getActivity().registerReceiver(myBleStateBroadcast, intentFilter);
        myBleStateBroadcast.setMyBroadcastResponse(new BroadcastResponse() {

            @Override
            public void onSearchBleSuccess(final BluetoothDeviceBean bleDevice) {
                //搜索到蓝牙设备
                boolean found = false;//记录该条记录是否在list中
                for (BluetoothDeviceBean device : devicelistHuXi) {
                    if (device.getAddress().equals(bleDevice.getAddress())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    blueHuXiAdapter.getList().clear();
                    if (bleDevice.getName().length() == 10) {
                        devicelistHuXi.add(bleDevice);
                    }
                    for (int i = 0; i < devicelistHuXi.size(); i++) {
                        BluetoothDeviceBean bluetoothDeviceBean = devicelistHuXi.get(i);
                        blueHuXiAdapter.addDATA(bluetoothDeviceBean);
                    }
                    blueHuXiAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onReceiveBleMsg(String str) {
                //接收到蓝牙设备的指令
                sb.append(str);
                if (-1 != sb.lastIndexOf("\r\n")) {//判断结尾符
                    StringBuffer tmp = new StringBuffer(sb.toString());
                    myMessageManager.matchCommandCode(tmp);
                    sb.setLength(0);            //StringBuffer清零
                }
            }

            @Override
            public void onBleDisconnect() {
                //蓝牙设备断开连接
//                toastOnUi("蓝牙设备断开连接");
            }

            @Override
            public void onBleConnect() {
                //蓝牙设备已匹配上
                toastOnUi("蓝牙设备已匹配上");
            }
        });
        //系统蓝牙服务开启监听
        IntentFilter systemBleListenerFilter = new IntentFilter();
        systemBleListenerFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        getActivity().registerReceiver(systemBleBroadcast, systemBleListenerFilter);


    }

    private void setitemOnClickMeathed(ListView listview) {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mBluetoothController.stopScanBLE();
                mBluetoothController.connect(devicelistHuXi.get(i));
                dialog.dismiss();
                showHuXiMessageDialog();
            }
        });
    }

    //新版呼吸界面
    private void showHuXiMessageDialog() {
        AlertDialog.Builder huxiDialog = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.dialog_huxi_new, null);
        final ViewHolderHuXiMessage viewHolderHuXiMessage = new ViewHolderHuXiMessage(view);
        huxiDialog.setView(view);
        dialog = huxiDialog.show();
        viewHolderHuXiMessage.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBluetoothController.stopConnectBLe();
                dialog.dismiss();

            }
        });

        viewHolderHuXiMessage.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                final String date = viewHolderHuXiMessage.tvDate.getText().toString();
                final String tvPef = viewHolderHuXiMessage.tvPef.getText().toString();
                final String tvFvc = viewHolderHuXiMessage.tvFvc.getText().toString();
                final String tvFev1 = viewHolderHuXiMessage.tvFev1.getText().toString();
                //保存数据接口
                final long time = System.currentTimeMillis() / 1000;

                if (NetUtil.isNetworkConnectionActive(getActivity())) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            huxiInterface(time + "", tvPef, tvFvc, tvFev1);
                        }
                    }).start();
                } else {
                    mBluetoothController.stopConnectBLe();//保存失败或成功后断开蓝牙
                    dbManager.addBreathingData(time + "", Constants.id, tvPef, tvFvc, tvFev1);
                    Toast.makeText(getActivity(), "本地数据保存成功", Toast.LENGTH_SHORT).show();
                }

            }
        });

        setHuXiData(new GetDataListener() {
            @Override
            public void getData(String date, String pef, String fvc, String fev1) {
                viewHolderHuXiMessage.tvDate.setText("检测时间：" + date);
                viewHolderHuXiMessage.tvPef.setText(pef);
                viewHolderHuXiMessage.tvFvc.setText(fvc);
                viewHolderHuXiMessage.tvFev1.setText(fev1);

                int iPEF = (int) (Integer.parseInt(pef.trim()) * 100 / 441);
                String result = "";
                if (iPEF < 60) {
                    viewHolderHuXiMessage.layPef.setBackgroundColor(getResources().getColor(R.color.red));
                    result = "危险";
                } else if (iPEF >= 60 && iPEF < 80) {
                    viewHolderHuXiMessage.layPef.setBackgroundColor(getResources().getColor(R.color.orrange));
                    result = "警告";

                } else {
                    viewHolderHuXiMessage.layPef.setBackgroundColor(getResources().getColor(R.color.green_pro));
                    result = "正常";

                }

                int pro1 = (int) (Float.valueOf(fev1) * 100 / 3.37);

                int pro2 = (int) (Float.valueOf(fev1) * 100 / Float.valueOf(fvc));
                if (pro1 >= 80) {
                    viewHolderHuXiMessage.proFev1Fev1.setProgressDrawable(getResources().getDrawable(R.drawable.progress_style_green));
                } else if (pro1 >= 50 && pro1 < 80) {
                    viewHolderHuXiMessage.proFev1Fev1.setProgressDrawable(getResources().getDrawable(R.drawable.progress_style_yellow));

                } else if (pro1 >= 30 && pro1 < 50) {
                    viewHolderHuXiMessage.proFev1Fev1.setProgressDrawable(getResources().getDrawable(R.drawable.progress_style_orrange));

                } else {
                    viewHolderHuXiMessage.proFev1Fev1.setProgressDrawable(getResources().getDrawable(R.drawable.progress_style_red));

                }


                if (pro2 >= 80) {
                    viewHolderHuXiMessage.proFev1Fevc.setProgressDrawable(getResources().getDrawable(R.drawable.progress_style_green));
                } else if (pro2 >= 50 && pro2 < 80) {
                    viewHolderHuXiMessage.proFev1Fevc.setProgressDrawable(getResources().getDrawable(R.drawable.progress_style_yellow));
                } else if (pro2 >= 30 && pro2 < 50) {
                    viewHolderHuXiMessage.proFev1Fevc.setProgressDrawable(getResources().getDrawable(R.drawable.progress_style_orrange));
                } else {
                    viewHolderHuXiMessage.proFev1Fevc.setProgressDrawable(getResources().getDrawable(R.drawable.progress_style_red));
                }

                viewHolderHuXiMessage.proFev1Fev1.setProgress(pro1);
                viewHolderHuXiMessage.proFev1Fevc.setProgress(pro2);
                viewHolderHuXiMessage.tvPefContent.setText("PEF(最大呼气流量)为个人预计值的" + iPEF + "%，属于" + result + "范围；FEV1(一秒用力呼气容积)和FVC(用力肺活量)反应气流受限程度为正常");
                viewHolderHuXiMessage.tvFev1Fev1.setText(pro1 + "%");
                viewHolderHuXiMessage.tvFev1Fvc.setText(pro2 + "%");


            }
        });

    }

    //呼吸机上传数据
    private void huxiInterface(String date, String tvPef, String tvFvc, String tvFev1) {
        OkHttpUtils.post().url(InterfaceUrl.BREATHDATA_URL + sessonWithCode + "/mid/" + HomeActivity.mid)
                .addParams("date", date)
                .addParams("pef", tvPef)
                .addParams("fvc", tvFvc)
                .addParams("fev1", tvFev1)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                toastOnUi("保存失败，请检查网络");
                dialog.dismiss();
                mBluetoothController.stopConnectBLe();//保存失败或成功后断开蓝牙

            }

            @Override
            public void onResponse(String response, int id) {
                dialog.dismiss();
                toastOnUi("保存成功");
                mBluetoothController.stopConnectBLe();//保存失败或成功后断开蓝牙
            }
        });
    }

    static class ViewHolderHuXiMessage {
        @BindView(R.id.tv_pef)
        TextView tvPef;
        @BindView(R.id.lay_pef)
        AutoLinearLayout layPef;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_pef_content)
        TextView tvPefContent;
        @BindView(R.id.tv_fev1)
        TextView tvFev1;
        @BindView(R.id.pro_fev1_fev1)
        ProgressBar proFev1Fev1;
        @BindView(R.id.tv_fev1_fev1)
        TextView tvFev1Fev1;
        @BindView(R.id.tv_fvc)
        TextView tvFvc;
        @BindView(R.id.pro_fev1_fevc)
        ProgressBar proFev1Fevc;
        @BindView(R.id.tv_fev1_fvc)
        TextView tvFev1Fvc;
        @BindView(R.id.btn_cancel)
        Button btnCancel;
        @BindView(R.id.btn_save)
        Button btnSave;

        ViewHolderHuXiMessage(View view) {
            ButterKnife.bind(this, view);
        }
    }


    private void initTools() {
        myMessageManager = new MessageManager().getInstance(getActivity());
        mBluetoothController = BluetoothController.getInstance(myApplication.dashuhdApplication);
        mBluetoothController.setOnGattNoneListener(new BluetoothController.onGattNoneListener() {
            @Override
            public void onGattNone() {
                if (devicelistHuXi.size() != 0) {
                    for (BluetoothDeviceBean bleDevice : devicelistHuXi) {
                        if (bleDevice.getName().equals(IMEI)) {
                            mBluetoothController.stopScanBLE();
                            mBluetoothController.connect(bleDevice);
                            break;
                        }
                    }
                }
            }
        });
        myMessageManager.setmBluetoothController(mBluetoothController);
        myMessageManager.setHolder(holder);                                            //MessageManager需要设置检测人信息
        if (null == bleVersionMsg) {
        } else {
            myMessageManager.setBleVersionmsg(bleVersionMsg);
        }


        myMessageManager.setmListener(new MessageManager.ReceiveCommandCodeListener() {

            @Override
            public void matchSuccess() {
//                toastOnUi("成功连接");
            }

            @Override
            public void needUpdateBleProgram(Boolean isNeedUpdate) {
                if (isNeedUpdate) {//需要进行更新设备的程序
                    Toast.makeText(getActivity(), R.string.hint_updateprogram, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void sendDataFromBleDevice(final PefDataFromBleBean pefdata) {
                final PefDataFromBleBean mypefData = pefdata;
                getDataListener.getData(mypefData.getDate(), mypefData.getPefValue(), mypefData.getFvcValue(), mypefData.getFev1Value());
            }
        });
        mySearchBleDeviceTask = new SearchBleDeviceTask(mBluetoothController);

        startSearchBle();
    }


    private Intent searchIntent;

    private void startSearchBle() {
        searchIntent = new Intent(getActivity(), MyBLEService.class);
        getActivity().startService(searchIntent);
        try {
            if (!mBluetoothController.initBLE()) {//手机不支持蓝牙
                Toast.makeText(getActivity(), R.string.ble_judgement_notsupport, Toast.LENGTH_SHORT).show();
                return;//手机不支持蓝牙
            }

            if (!mBluetoothController.isBleOpen()) {// 如果蓝牙还没有打开
                Toast.makeText(getActivity(), R.string.ble_state_ble_open, Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            Log.e(TAG, "mBluetoothController: 空指针" + e.getMessage());
        }


        try {
            mySearchBleDeviceTask.execute();
        } catch (Exception e) {
            Log.e(TAG, "startSearchBleException: " + e.getMessage());
            return;
        }

    }

    public class SystemBleBroadcast extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                    switch (blueState) {
                        case BluetoothAdapter.STATE_TURNING_ON:
                            break;
                        case BluetoothAdapter.STATE_ON:
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            break;
                        case BluetoothAdapter.STATE_OFF:
                            break;
                        default:
                            break;
                    }
                    break;

                default:
                    break;
            }
        }
    }


    private GetDataListener getDataListener;

    public interface GetDataListener {
        void getData(String date, String pef, String fvc, String fev1);
    }

    public void setHuXiData(GetDataListener getDataListener) {
        this.getDataListener = getDataListener;
    }

    private static final boolean mSecure = BluetoothConnection.INSECURE;//是否安全连接蓝牙
    private BluetoothSPP mUrineBluetoothSPP;//尿液分析仪操作
    private BluetoothDevice mUrineBluetoothDevice;//尿液分析仪
    private BC401_Data mUrineData;//尿液分析仪接收的数据
    public final static int DETECT_TYPE_URINE_ANALYZER = 3;//尿液分析仪
    private Gson mGson;

    public void onStart() {
        super.onStart();
        if (!mUrineBluetoothSPP.isBluetoothEnabled()) {
            mUrineBluetoothSPP.enable();
        } else {
            if (!mUrineBluetoothSPP.isServiceAvailable()) {
                mUrineBluetoothSPP.setupService();
                mUrineBluetoothSPP.startService();
            }
        }
    }


    private void findBluetoothDevices(int requestCode) {
        //断开连接的设备
        disconectAllDevices();
        switch (requestCode) {
            case BluetoothDeviceListActivity.REQUEST_CONNECT_DEVICE_URINE_ANALYZER://连接尿液分析仪
                mUrineBluetoothDevice = null;
                break;
            case BluetoothDeviceListActivity.REQUEST_CONNECT_DEVICE_DRY_BIOCHEMICAL_ANALYZER://干式生化仪
                mDryBluetoothDevice = null;
                break;
        }
        //连接设备
        Intent intent = new Intent(getActivity(), BluetoothDeviceListActivity.class);
        intent.putExtra(BluetoothDeviceListActivity.REQUEST_CONNECT_DEVICE_TYPE_KEY, requestCode);
        startActivityForResult(intent, requestCode);

    }


    private void disconectAllDevices() {
        try {
            //断开尿液分析仪
            if (mUrineBluetoothSPP.getServiceState() == BluetoothState.STATE_CONNECTED) {
                mUrineBluetoothSPP.disconnect();
                unRegisterUrineAnalyzerListener();
            }

            //断开干式生化仪
            if (mDryBluetoothDevice != null) {
                getActivity().unregisterReceiver(mDryGattUpdateReceiver);
                mDryBluetoothLeService.disconnect();
                mDryBluetoothDevice = null;
            }
            mUrineBluetoothDevice = null;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消尿液分析仪监听
     */
    private void unRegisterUrineAnalyzerListener() {
        mUrineBluetoothSPP.setBluetoothStateListener(null);
        mUrineBluetoothSPP.setOnDataReceivedListener(null);
        mUrineBluetoothSPP.setBluetoothConnectionListener(null);
        mUrineBluetoothSPP.setAutoConnectionListener(null);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case BluetoothDeviceListActivity.REQUEST_CONNECT_DEVICE_URINE_ANALYZER://连接尿液分析仪
                toConnectBluetoothDevice(data, BluetoothDeviceListActivity.REQUEST_CONNECT_DEVICE_URINE_ANALYZER);
                break;
            case BluetoothDeviceListActivity.REQUEST_CONNECT_DEVICE_DRY_BIOCHEMICAL_ANALYZER://干式生化仪
                toConnectBluetoothDevice(data, BluetoothDeviceListActivity.REQUEST_CONNECT_DEVICE_DRY_BIOCHEMICAL_ANALYZER);
                break;
        }
    }


    private synchronized void toConnectBluetoothDevice(Intent data, int requestCode) {
        String address = data.getExtras().getString(BluetoothState.EXTRA_DEVICE_ADDRESS);
        BluetoothDevice bluetoothDevice = data.getExtras().getParcelable(BluetoothState.EXTRA_DEVICE);
        String deviceName = bluetoothDevice.getName();
        switch (requestCode) {
            case BluetoothDeviceListActivity.REQUEST_CONNECT_DEVICE_URINE_ANALYZER://尿液分析仪
                if (!TextUtils.isEmpty(deviceName) && deviceName.startsWith("BC")) {
                    registerUrineAnalyzerListener();//注册监听事件
                    mUrineBluetoothSPP.connect(data, mSecure);
                    mUrineBluetoothDevice = bluetoothDevice;
                    showStartMeasureDialog();
                } else {
                    Toast.makeText(getActivity(), "非尿机设备", Toast.LENGTH_SHORT).show();
                }
                break;
            case BluetoothDeviceListActivity.REQUEST_CONNECT_DEVICE_DRY_BIOCHEMICAL_ANALYZER://干式生化仪
                if (!TextUtils.isEmpty(deviceName) && (Utils.string2Int(deviceName) > 0)) {
                    getActivity().registerReceiver(mDryGattUpdateReceiver, makeGattUpdateIntentFilter());
                    final boolean result = mDryBluetoothLeService.connect(address);
                    Log.e(TAG, "Connect 干式生化仪 request result=" + result);
                    showStartMeasureDialog();
                    mDryBluetoothDevice = bluetoothDevice;
                } else {
                    Toast.makeText(getActivity(), "非干式生化设备", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //正在测量dialog
    private void showStartMeasureDialog() {
        AlertDialog.Builder proDialog = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.measure_dialog, null);
        ViewHolderStartMeasure viewHolderStartMeasure = new ViewHolderStartMeasure(view);
        proDialog.setView(view);
        proDialog.setCancelable(false);
        viewHolderStartMeasure.tvStopMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disconectAllDevices();
                dialog.dismiss();
            }
        });
        dialog = proDialog.show();
    }

    static class ViewHolderStartMeasure {
        @BindView(R.id.tv_stop_measure)
        TextView tvStopMeasure;

        ViewHolderStartMeasure(View view) {
            ButterKnife.bind(this, view);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopBlueMeathed();
    }


    private void stopBlueMeathed() {
        try {
            if (mUrineBluetoothSPP != null) {
                unRegisterUrineAnalyzerListener();
                mUrineBluetoothSPP.stopService();
            }

            if (null != searchIntent) {
                getActivity().stopService(searchIntent);
            }
            if (mySearchBleDeviceTask != null && mySearchBleDeviceTask.getStatus() != AsyncTask.Status.FINISHED) {
                mySearchBleDeviceTask.cancel(true);

            }
            if (getBleCurrentVersionTask != null && getBleCurrentVersionTask.getStatus() != AsyncTask.Status.FINISHED) {
                getBleCurrentVersionTask.cancel(true);
            }
            if (myMessageManager != null) {
                myMessageManager.destroyMessageManager();
            }

            getActivity().unbindService(mDryServiceConnection);
            mDryBluetoothLeService = null;

        } catch (Exception e) {
            Log.e(TAG, "stopBlueMeathed: >>>>>>>>>>>>>>>>>>>>" + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * 注册尿液分析仪监听
     */
    private void registerUrineAnalyzerListener() {
        mUrineBluetoothSPP.setBluetoothStateListener(mBluetoothStateListener);
        mUrineBluetoothSPP.setOnDataReceivedListener(mOnDataReceivedListener);
        mUrineBluetoothSPP.setBluetoothConnectionListener(mBluetoothConnectionListener);
        mUrineBluetoothSPP.setAutoConnectionListener(mAutoConnectionListener);
    }

    /**
     * 尿液分析仪 Listener when bluetooth connection has changed
     */
    BluetoothSPP.BluetoothStateListener mBluetoothStateListener = new BluetoothSPP.BluetoothStateListener() {
        public void onServiceStateChanged(int state) {
            if (state == BluetoothState.STATE_CONNECTED) {
                Log.d(TAG, "State : Connected: ");
            } else if (state == BluetoothState.STATE_CONNECTING) {
                Log.d(TAG, "State : Connecting: ");
            } else if (state == BluetoothState.STATE_LISTEN) {
                Log.d(TAG, "State : Listen: ");
            } else if (state == BluetoothState.STATE_NONE) {
                Log.d(TAG, "State : None: ");
            }
        }
    };

    /**
     * 尿液分析仪 Listener for data receiving
     */
    BluetoothSPP.OnDataReceivedListener mOnDataReceivedListener = new BluetoothSPP.OnDataReceivedListener() {
        @Override
        public void onDataReceived(byte[] data, String message) {
            Log.d(TAG, "Message : " + message);
            try {
                DevicePackManager devicePackManager = new DevicePackManager();
                byte _back = devicePackManager.arrangeMessage(data, data.length);
                switch (_back) {
                    case (byte) 0x02://校正成功
                        Log.d(TAG, "校正成功: ");
                        mUrineBluetoothSPP.send(DeviceCommand.Request_AllData(), false);
                        break;

                    case (byte) 0x05://返回数据
                        Log.d(TAG, "------返回数据------" + Utils.bytesToHexString(data));
                        mUrineData = devicePackManager.mBc401_Data;
                        //用户代码
                        if (mUrineData != null && mUrineData.Structs != null) {
                            String jsonData = mGson.toJson(mUrineData.Structs);
                            Log.e(TAG, "jsonData: " + jsonData);
                            dialog.dismiss();
                            showNiaoJiDataDialog(mUrineData.Structs);
                        }
                        if (devicePackManager.Percent == 100) {
                            //此时证明数据全部接收完毕
                            mUrineBluetoothSPP.send(DeviceCommand.Delete_AllData(), false);
                        }
                        break;

                    case (byte) 0x06://删除完毕
                        Log.d(TAG, "删除完毕: ");
                        break;

                    case (byte) 0x08://无新数据
                        Log.d(TAG, "无新数据: ");
                        break;

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private NiaoJiAdapter niaoJiAdapter;

    //尿液分析仪显示数据
    private void showNiaoJiDataDialog(final ArrayList<BC401_Struct> list) {
        AlertDialog.Builder ganshiDataDialog = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.dialog_dry_analyzer_data, null);
        ViewHolderNiaoJi viewHolderNiaoJi = new ViewHolderNiaoJi(view);
        viewHolderNiaoJi.title.setText(getResources().getString(R.string.urine_analyzer_measured_data));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        viewHolderNiaoJi.rcvGanshiData.setLayoutManager(linearLayoutManager);
        niaoJiAdapter = new NiaoJiAdapter(getActivity());
        viewHolderNiaoJi.rcvGanshiData.setAdapter(niaoJiAdapter);
        long time = System.currentTimeMillis() / 1000;
        String date = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date(Long.valueOf(time + "")));
        viewHolderNiaoJi.dateTimeTv.setText(date);
        niaoJiAdapter.addData(list);
        niaoJiAdapter.notifyDataSetChanged();
        ganshiDataDialog.setView(view);
        viewHolderNiaoJi.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disconectAllDevices();//点击取消，断开蓝牙
                dialog.dismiss();
            }
        });
        viewHolderNiaoJi.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetUtil.isNetworkConnectionActive(getActivity())) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            bcInterface(list);
                        }
                    }).start();
                } else {
                    dialog.dismiss();
                    final long time = System.currentTimeMillis() / 1000;
                    for (BC401_Struct bcBean : list) {
                        String URO = bcBean.URO + "";
                        String BIL = bcBean.BIL + "";
                        String GLU = bcBean.GLU + "";
                        String PH = bcBean.PH + "";
                        String LEU = bcBean.LEU + "";
                        String BLD = bcBean.BLD + "";
                        String KET = bcBean.KET + "";
                        String PRO = bcBean.PRO + "";
                        String NIT = bcBean.NIT + "";
                        String SG = bcBean.SG + "";
                        String VC = bcBean.VC + "";
                        dbManager.addBCData(Constants.id, time + "", URO, BLD, BIL, KET, GLU, PRO, PH, NIT, LEU, SG, VC);
                    }
                    Toast.makeText(getActivity(), "本地保存数据成功", Toast.LENGTH_SHORT).show();
                }


            }
        });
        dialog = ganshiDataDialog.show();
    }

    //尿液检测仪上传数据接口
    private void bcInterface(ArrayList<BC401_Struct> list) {
        String URO = "";
        String BLD = "";
        String BIL = "";
        String KET = "";
        String GLU = "";
        String PRO = "";
        String NIT = "";
        String LEU = "";
        String SG = "";
        String PH = "";
        String VC = "";
        final long time = System.currentTimeMillis() / 1000;

        for (int i = 0; i < list.size(); i++) {
            BC401_Struct mBC401Struct = list.get(i);
            URO = mBC401Struct.URO + "";
            BIL = mBC401Struct.BIL + "";
            GLU = mBC401Struct.GLU + "";
            PH = mBC401Struct.PH + "";
            LEU = mBC401Struct.LEU + "";
            BLD = mBC401Struct.BLD + "";
            KET = mBC401Struct.KET + "";
            PRO = mBC401Struct.PRO + "";
            NIT = mBC401Struct.NIT + "";
            SG = mBC401Struct.SG + "";
            VC = mBC401Struct.VC + "";


            Log.e(TAG, "bcInterfaceUrl: " + InterfaceUrl.BCDATA_URL + sessonWithCode + "/m_id/" + HomeActivity.mid);
            OkHttpUtils.post().url(InterfaceUrl.BCDATA_URL + sessonWithCode + "/m_id/" + HomeActivity.mid)
                    .addParams("addtime", time + "")
                    .addParams("URO", URO)
                    .addParams("BIL", BIL)
                    .addParams("GLU", GLU)
                    .addParams("PH", PH)
                    .addParams("LEU", LEU)
                    .addParams("BLD", BLD)
                    .addParams("KET", KET)
                    .addParams("PRO", PRO)
                    .addParams("NIT", NIT)
                    .addParams("SG", SG)
                    .addParams("VC", VC)
                    .build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    dialog.dismiss();
                    disconectAllDevices();
                    toastOnUi("保存数据失败，请检查网络");
                }

                @Override
                public void onResponse(String response, int id) {
                    dialog.dismiss();
                    disconectAllDevices();
                    Message msg = new Message();
                    msg.obj = response;
                    bcHandler.sendMessage(msg);
                }
            });
        }

    }

    static class ViewHolderNiaoJi {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.dateTimeTv)
        TextView dateTimeTv;
        @BindView(R.id.deviceNumberTv)
        TextView deviceNumberTv;
        @BindView(R.id.topContainer)
        AutoLinearLayout topContainer;
        @BindView(R.id.rcv_ganshi_data)
        RecyclerView rcvGanshiData;
        @BindView(R.id.saveBtn)
        Button saveBtn;
        @BindView(R.id.cancelBtn)
        Button cancelBtn;

        ViewHolderNiaoJi(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 尿液分析仪 Listener for connection atatus
     */
    BluetoothSPP.BluetoothConnectionListener mBluetoothConnectionListener = new BluetoothSPP.BluetoothConnectionListener() {
        public void onDeviceConnected(String name, String address) {
            Log.d(TAG, "onDeviceConnected: ");

            mUrineBluetoothSPP.send(DeviceCommand.Synchronous_Time(), false);
        }

        public void onDeviceDisconnected() {
            Log.d(TAG, "onDeviceDisconnected: ");
        }

        public void onDeviceConnectionFailed() {
            Log.d(TAG, "onDeviceConnectionFailed: ");
        }
    };

    /**
     * 尿液分析仪 Listener for auto connection
     */
    BluetoothSPP.AutoConnectionListener mAutoConnectionListener = new BluetoothSPP.AutoConnectionListener() {
        public void onNewConnection(String name, String address) {
            Log.d(TAG, "New Connection - " + name + " - " + address);
        }

        public void onAutoConnectionStarted() {
            Log.d(TAG, "onAutoConnectionStarted: ");
        }
    };


    private BluetoothLeService mDryBluetoothLeService;//干式生化仪操作
    private BluetoothDevice mDryBluetoothDevice;//干式生化仪
    private String mDryReceivedData = "";//干式生化仪接收的数据

    private final BroadcastReceiver mDryGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                Log.e(TAG, "干式生化仪----->" + getString(R.string.title_connected_to, mDryBluetoothDevice.getName()));
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                Log.e(TAG, "干式生化仪----->" + getString(R.string.title_not_connected));
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                List<BluetoothGattService> gattServices = mDryBluetoothLeService.getSupportedGattServices();
                for (BluetoothGattService gattService : gattServices) {
                    String serviceUUid = gattService.getUuid().toString();
                    if (BleGattAttributes.USR_SERVICE.equals(serviceUUid)) {
                        List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
                        for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                            String characteristicUUid = gattCharacteristic.getUuid().toString();
                            if (BleGattAttributes.USR_CHARACTERISTIC_NOTIFY.equals(characteristicUUid)) {
                                mDryBluetoothLeService.setCharacteristicNotification(gattCharacteristic, true);
                                break;
                            }
                        }
                    }
                }
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                try {
                    String result = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                    if (!TextUtils.isEmpty(result)) {
                        result = result.replaceAll("\\s*", "");//去掉空格
                        if (result.contains(Constants.DRY_BIOCHEMICAL_ANALYZER_PACK_HEAD)) {
                            int frontStart = result.indexOf(Constants.DRY_BIOCHEMICAL_ANALYZER_PACK_HEAD);
                            mDryReceivedData = result.substring(frontStart, frontStart + 40);
                        } else {
                            mDryReceivedData += result.substring(result.length() - 32, result.length());
                            DryDetectResult dryDetectResult = DryDetectResult.parseData(mDryReceivedData);
                            dialog.dismiss();
                            showGanshiDataDialog(dryDetectResult.getDeviceNumber(), dryDetectResult.getItemList(), mDryReceivedData);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };


    private GanShiAdapter ganShiAdapter;

    //干式生化仪数据显示页面
    private void showGanshiDataDialog(int dryDetectResult, List<DryDetectItem> itemList, final String mDryReceivedData) {
        AlertDialog.Builder ganshiDataDialog = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.dialog_dry_analyzer_data, null);
        final ViewHolderGanshiData viewHolderGanshiData = new ViewHolderGanshiData(view);
        GridLayoutManager gr = new GridLayoutManager(getActivity(), 2);
        viewHolderGanshiData.rcvGanshiData.setLayoutManager(gr);
        ganShiAdapter = new GanShiAdapter(getActivity());
        viewHolderGanshiData.rcvGanshiData.setAdapter(ganShiAdapter);
        viewHolderGanshiData.title.setText(getResources().getString(R.string.dry_analyzer_measured_data));
        ganshiDataDialog.setView(view);
        long time = System.currentTimeMillis() / 1000;
        String date = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date(Long.valueOf(time + "")));
        viewHolderGanshiData.dateTimeTv.setText(date);
        viewHolderGanshiData.deviceNumberTv.setText("机号：" + dryDetectResult);
        ganShiAdapter.addData(itemList);
        ganShiAdapter.notifyDataSetChanged();
        dialog = ganshiDataDialog.show();
        viewHolderGanshiData.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disconectAllDevices();//点击取消，断开蓝牙
                dialog.dismiss();
            }
        });
        viewHolderGanshiData.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetUtil.isNetworkConnectionActive(getActivity())) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            upGanShiDataInterface(mDryReceivedData);
                        }
                    }).start();
                } else {
                    dialog.dismiss();
                    dbManager.addBiochemicalData(Constants.id, mDryReceivedData);
                    Toast.makeText(getActivity(), "本地数据保存成功", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    //干式生化仪上传数据
    private void upGanShiDataInterface(String mDryReceivedData) {
        OkHttpUtils.post().url(InterfaceUrl.GANSHIDATA_URL + sessonWithCode + "/m_id/" + HomeActivity.mid)
                .addParams("val", mDryReceivedData)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                dialog.dismiss();
                disconectAllDevices();
                toastOnUi("保存数据失败，请检查网络");
            }

            @Override
            public void onResponse(String response, int id) {
                dialog.dismiss();
                disconectAllDevices();
                Message msg = new Message();
                msg.obj = response;
                ganshiHandler.sendMessage(msg);
            }
        });
    }


    static class ViewHolderGanshiData {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.dateTimeTv)
        TextView dateTimeTv;
        @BindView(R.id.deviceNumberTv)
        TextView deviceNumberTv;
        @BindView(R.id.topContainer)
        AutoLinearLayout topContainer;
        @BindView(R.id.rcv_ganshi_data)
        RecyclerView rcvGanshiData;
        @BindView(R.id.saveBtn)
        Button saveBtn;
        @BindView(R.id.cancelBtn)
        Button cancelBtn;

        ViewHolderGanshiData(View view) {
            ButterKnife.bind(this, view);
        }
    }


    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    private void bindDryBluetoothService() {
        Intent gattServiceIntent = new Intent(getActivity(), BluetoothLeService.class);
        getActivity().bindService(gattServiceIntent, mDryServiceConnection, Context.BIND_AUTO_CREATE);
    }

    // 干式生化仪 manage Service lifecycle.
    private final ServiceConnection mDryServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mDryBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mDryBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize 干式生化仪 Bluetooth");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mDryBluetoothLeService = null;
        }
    };

    private Handler ganshiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            if (result.indexOf(ErrorCode.SUCCESS) > 0) {
                toastOnUi("保存成功");
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


    private Handler bcHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            if (result.indexOf(ErrorCode.SUCCESS) > 0) {
                toastOnUi("保存成功");
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


    private BlueDialogListFragment blueDialogListFragment;
    private void showBlueDeceiveList() {
        blueDialogListFragment = new BlueDialogListFragment();
        blueDialogListFragment.show(getActivity().getFragmentManager(), "");
    }
}



