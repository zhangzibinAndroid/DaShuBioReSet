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
import com.linktop.infs.OnSPO2HResultListener;
import com.linktop.whealthService.MeasureType;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 作者： 张梓彬
 * 日期： 2017/9/15 0015
 * 时间： 下午 3:09
 * 描述： 血氧
 */
public class BloodOxygenFragment extends BaseFragment implements OnSPO2HResultListener {

    @BindView(R.id.progress_view_oxygen)
    ProgressView progressViewOxygen;
    @BindView(R.id.tv_oxygen_data)
    TextView tvOxygenData;
    @BindView(R.id.btn_start_measure_oxygen)
    Button btnStartMeasureOxygen;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.tv_oxygen_warning)
    TextView tvOxygenWarning;
    @BindView(R.id.tv_hr_warning)
    TextView tvHrWarning;
    private Unbinder unbinder;
    private boolean isMeasureBo = false;
    private int spo,hr;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_blood_oxygen, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        manager = MonitorDataTransmissionManager.getInstance();
        tvHrWarning.setVisibility(View.INVISIBLE);
        btnSave.setVisibility(View.GONE);
        isMeasureBo = false;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isMeasureBo){
            manager.stopMeasure(MeasureType.SPO2H);
            btnStartMeasureOxygen.setText("开始");
        }

        unbinder.unbind();
    }

    @OnClick({R.id.header_left_container, R.id.btn_start_measure_oxygen, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_left_container:
                if (isMeasureBo){
                    manager.stopMeasure(MeasureType.SPO2H);
                    btnStartMeasureOxygen.setText("开始");
                }
                EventBus.getDefault().post(new EventMessage("back"));

                break;
            case R.id.btn_start_measure_oxygen:
                btnSave.setVisibility(View.GONE);
                    if (!isMeasureBo) {
                        tvOxygenWarning.setText("正在测量...");
                        manager.startMeasure(MeasureType.SPO2H);
                        manager.setOnSPO2HResultListener(this);
                        tvHrWarning.setVisibility(View.INVISIBLE);
                        btnStartMeasureOxygen.setText("停止");
                        isMeasureBo = true;
                    } else {
                        manager.stopMeasure(MeasureType.SPO2H);
                        btnStartMeasureOxygen.setText("开始");
                        isMeasureBo = false;
                    }
                break;
            case R.id.btn_save:
                break;
        }
    }

    @Override
    public void onSPO2HResult(final int spo2h, final int heartRate) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spo = spo2h;
                hr = heartRate;
                manager.stopMeasure(MeasureType.SPO2H);
                isMeasureBo = false;
                btnSave.setVisibility(View.VISIBLE);
                tvHrWarning.setVisibility(View.VISIBLE);
                btnStartMeasureOxygen.setText("重新开始");
                tvOxygenData.setText(spo2h + "/" + heartRate);
                if (spo2h < 95) {
                    tvOxygenWarning.setText("血氧：" + spo2h + ",血氧偏低");
                    tvOxygenWarning.setTextColor(getResources().getColor(R.color.progress_orange));
                    progressViewOxygen.setColor(getResources().getColor(R.color.progress_orange));
                } else if (spo2h > 99) {
                    tvOxygenWarning.setText("血氧：" + spo2h + ",血氧偏高");
                    tvOxygenWarning.setTextColor(getResources().getColor(R.color.progress_red));
                    progressViewOxygen.setColor(getResources().getColor(R.color.progress_red));
                } else {
                    tvOxygenWarning.setText("血氧：" + spo2h + ",血氧正常");
                    tvOxygenWarning.setTextColor(getResources().getColor(R.color.progress_green));
                    progressViewOxygen.setColor(getResources().getColor(R.color.progress_green));
                }

                int angle = 96 * 320 / 110;
                progressViewOxygen.setAngleWithAnim(angle);

                if (heartRate < 60) {
                    tvHrWarning.setText("心率：" + heartRate + ",心率偏低");
                    tvHrWarning.setTextColor(getResources().getColor(R.color.progress_orange));
                } else if (heartRate > 100) {
                    tvHrWarning.setText("心率：" + heartRate + ",心率偏高");
                    tvHrWarning.setTextColor(getResources().getColor(R.color.progress_red));
                } else {
                    tvHrWarning.setText("心率：" + heartRate + ",心率正常");
                    tvHrWarning.setTextColor(getResources().getColor(R.color.progress_green));
                }

            }
        });


    }

    @Override
    public void onSPO2HWave(int i) {
        //血氧测量画图线数据
        runOnUiMothod(tvOxygenWarning, "正在测量：" + i);
    }
}
