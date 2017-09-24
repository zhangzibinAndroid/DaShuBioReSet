package com.dashubio.fragment.health_deceive;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dashubio.R;
import com.dashubio.activity.HomeActivity;
import com.dashubio.base.BaseFragment;
import com.dashubio.bean.eventmsg.EventMessage;
import com.dashubio.constant.Constants;
import com.dashubio.constant.ErrorCode;
import com.dashubio.constant.InterfaceUrl;
import com.dashubio.db.DBManager;
import com.dashubio.fragment.health_deceive.bp.BloodPressureMeasuredData;
import com.dashubio.fragment.health_deceive.ecg_bean.DetectItem;
import com.dashubio.utils.NetUtil;
import com.dashubio.view.ProgressView;
import com.google.gson.Gson;
import com.linktop.MonitorDataTransmissionManager;
import com.linktop.infs.OnBpResultListener;
import com.linktop.whealthService.MeasureType;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * 作者： 张梓彬
 * 日期： 2017/9/15 0015
 * 时间： 下午 2:54
 * 描述： 血压
 */
public class BloodPressureMeasureFragment extends BaseFragment implements OnBpResultListener {

    @BindView(R.id.progress_view_blood_pressure)
    ProgressView progressViewBloodPressure;
    @BindView(R.id.tv_blood_pressure_data)
    TextView tvBloodPressureData;
    @BindView(R.id.btn_start_measure_bp)
    Button btnStartMeasureBp;
    @BindView(R.id.btn_start_measure_save)
    Button btnStartMeasureSave;
    @BindView(R.id.tv_systolic_warning)
    TextView tvSystolicWarning;
    @BindView(R.id.tv_diastolic_warning)
    TextView tvDiastolicWarning;
    private Unbinder unbinder;
    private boolean isMeasureBp = false;
    private int sys,dia;
    private BloodPressureMeasuredData bloodPressureMeasuredData;
    private Gson gson;

    private static final String TAG = "BloodPressureMeasureFra";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_blood_pressure_measure, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }


    private void initView() {
        gson = new Gson();
        dbManager = new DBManager(getActivity());
        bloodPressureMeasuredData = new BloodPressureMeasuredData();
        tvDiastolicWarning.setVisibility(View.INVISIBLE);
        btnStartMeasureSave.setVisibility(View.GONE);
        manager = MonitorDataTransmissionManager.getInstance();
        isMeasureBp = false;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isMeasureBp){
            manager.stopMeasure(MeasureType.BP);
            btnStartMeasureBp.setText("开始");
            isMeasureBp = false;
        }

        unbinder.unbind();
    }

    @OnClick({R.id.header_left_container, R.id.btn_start_measure_bp, R.id.btn_start_measure_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_left_container:

                if (isMeasureBp){
                    manager.stopMeasure(MeasureType.BP);
                    btnStartMeasureBp.setText("开始");
                    isMeasureBp = false;
                }
                EventBus.getDefault().post(new EventMessage("back"));
                break;
            case R.id.btn_start_measure_bp:


                btnStartMeasureSave.setVisibility(View.GONE);
                if (!isMeasureBp) {
                    MonitorDataTransmissionManager.getInstance().startMeasure(MeasureType.BP);
                    MonitorDataTransmissionManager.getInstance().setOnBpResultListener(this);
                    btnStartMeasureBp.setText("停止");
                    tvDiastolicWarning.setVisibility(View.INVISIBLE);
                    isMeasureBp = true;
                } else {
                    manager.stopMeasure(MeasureType.BP);
                    btnStartMeasureBp.setText("开始");
                    isMeasureBp = false;
                }
                break;
            case R.id.btn_start_measure_save:
                final String temUrl = InterfaceUrl.HEALTH_URL+sessonWithCode+"/m_id/"+ HomeActivity.mid;
                Log.e(TAG, "url: "+temUrl);
                if (NetUtil.isNetworkConnectionActive(getActivity())){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ecgInterface(temUrl);
                        }
                    }).start();
                }else {
                    dbManager.addBpData(Constants.id,sys+"",dia+"");
                    Toast.makeText(getActivity(), "本地保存成功", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void ecgInterface(String url) {
        OkHttpUtils.post().url(url)
                .addParams("data",gson.toJson(bloodPressureMeasuredData))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        toastOnUi("保存异常，请检查网络"+e.getMessage());

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response.indexOf(ErrorCode.SUCCESS) > 0) {
                            toastOnUi("保存成功");
                        }
                    }
                });

    }

    @Override
    public void onBpResult(final int systolicPressure, final int diastolicPressure, int heartRate) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sys = systolicPressure;
                dia = diastolicPressure;
                //高压
                DetectItem highPressureItem = new DetectItem();
                highPressureItem.setValue(Float.valueOf(sys));
                highPressureItem.setUnit("mmHg");
                bloodPressureMeasuredData.setHighPressure(highPressureItem);

                //低压
                DetectItem lowPressureItem = new DetectItem();
                lowPressureItem.setValue(Float.valueOf(dia));
                lowPressureItem.setUnit("mmHg");
                bloodPressureMeasuredData.setLowPressure(lowPressureItem);

                btnStartMeasureSave.setVisibility(View.VISIBLE);
                tvDiastolicWarning.setVisibility(View.VISIBLE);
                manager.stopMeasure(MeasureType.BP);
                isMeasureBp = false;
                btnStartMeasureBp.setText("开始");
                tvBloodPressureData.setText(systolicPressure + "/" + diastolicPressure);
                if (systolicPressure < 90) {
                    tvSystolicWarning.setText("收缩压：" + systolicPressure + ",收缩压偏低");
                    tvSystolicWarning.setTextColor(getResources().getColor(R.color.progress_orange));
                    progressViewBloodPressure.setColor(getResources().getColor(R.color.progress_orange));
                } else if (systolicPressure > 139) {
                    tvSystolicWarning.setText("收缩压：" + systolicPressure + ",收缩压偏高");
                    tvSystolicWarning.setTextColor(getResources().getColor(R.color.progress_red));
                    progressViewBloodPressure.setColor(getResources().getColor(R.color.progress_red));
                } else {
                    tvSystolicWarning.setText("收缩压：" + systolicPressure + ",收缩压正常");
                    tvSystolicWarning.setTextColor(getResources().getColor(R.color.progress_green));
                    progressViewBloodPressure.setColor(getResources().getColor(R.color.progress_green));
                }

                int angle = 100 * 320 / 150;
                progressViewBloodPressure.setAngleWithAnim(angle);
                if (diastolicPressure < 90) {
                    tvDiastolicWarning.setText("舒张压：" + diastolicPressure + ",舒张压偏低");
                    tvDiastolicWarning.setTextColor(getResources().getColor(R.color.progress_orange));
                } else if (diastolicPressure > 139) {
                    tvDiastolicWarning.setText("舒张压：" + diastolicPressure + ",舒张压偏高");
                    tvDiastolicWarning.setTextColor(getResources().getColor(R.color.progress_red));
                } else {
                    tvDiastolicWarning.setText("舒张压：" + diastolicPressure + ",舒张压正常");
                    tvDiastolicWarning.setTextColor(getResources().getColor(R.color.progress_green));
                }

            }
        });

    }

    @Override
    public void onLeakError(int i) {
        final int text = i == 0 ? R.string.leak_and_check : R.string.measurement_void;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                manager.stopMeasure(MeasureType.BP);
                btnStartMeasureBp.setText("开始");
                tvSystolicWarning.setText(getString(text));
                isMeasureBp = false;

            }
        });
    }
}
