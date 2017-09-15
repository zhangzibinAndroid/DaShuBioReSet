package com.dashubio.fragment.health_deceive;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dashubio.R;
import com.dashubio.base.BaseFragment;
import com.dashubio.bean.eventmsg.EventMessage;
import com.dashubio.view.ProgressView;
import com.linktop.MonitorDataTransmissionManager;
import com.linktop.infs.OnBpResultListener;
import com.linktop.whealthService.MeasureType;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_blood_pressure_measure, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }


    private void initView() {
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
                break;
        }
    }

    @Override
    public void onBpResult(final int systolicPressure, final int diastolicPressure, int heartRate) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sys = systolicPressure;
                dia = diastolicPressure;
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
