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
import com.dashubio.view.EcgPathView;
import com.linktop.MonitorDataTransmissionManager;
import com.linktop.infs.OnEcgResultListener;
import com.linktop.whealthService.MeasureType;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 作者： 张梓彬
 * 日期： 2017/9/15 0015
 * 时间： 上午 11:38
 * 描述： 心电Fragment
 */
public class EcgFragment extends BaseFragment implements OnEcgResultListener {



    private Unbinder unbinder;
    @BindView(R.id.ecg_view)
    EcgPathView ecgView;
    @BindView(R.id.tv_rr_max)
    TextView tvRrMax;
    @BindView(R.id.tv_rr_min)
    TextView tvRrMin;
    @BindView(R.id.tv_mood)
    TextView tvMood;
    @BindView(R.id.tv_heart_rate)
    TextView tvHeartRate;
    @BindView(R.id.tv_heart_rate_variability)
    TextView tvHeartRateVariability;
    @BindView(R.id.tv_breathing_rate)
    TextView tvBreathingRate;
    @BindView(R.id.btn_start_save)
    Button btnStartSave;
    @BindView(R.id.btn_start_measure)
    Button btnStartMeasure;
    private boolean isMeasureEcg = false;
    private float width = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ecg, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        isMeasureEcg = false;
        manager = MonitorDataTransmissionManager.getInstance();
        btnStartSave.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        manager.stopMeasure(MeasureType.ECG);
        btnStartMeasure.setText("开始");
        isMeasureEcg = false;
        unbinder.unbind();
    }


    @OnClick({R.id.header_left_container, R.id.btn_start_measure, R.id.btn_start_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_left_container:
                EventBus.getDefault().post(new EventMessage("back"));
                manager.stopMeasure(MeasureType.ECG);
                btnStartMeasure.setText("开始");
                isMeasureEcg = false;
                break;
            case R.id.btn_start_measure:
                if (!isMeasureEcg) {
                    btnStartSave.setVisibility(View.GONE);
                    ecgView.getArrast().clear();
                    manager.startMeasure(MeasureType.ECG);
                    manager.setOnEcgResultListener(this);
                    btnStartMeasure.setText("停止");
                    tvHeartRate.setText("心率：");
                    tvRrMax.setText("RR最大值：");
                    tvRrMin.setText("RR最小值：");
                    tvHeartRateVariability.setText("心率变异性：");
                    tvMood.setText("心情：");
                    tvBreathingRate.setText("呼吸率：");
                    isMeasureEcg = true;
                } else {
                    manager.stopMeasure(MeasureType.ECG);
                    btnStartMeasure.setText("开始");
                    isMeasureEcg = false;
                }
                break;
            case R.id.btn_start_save:

                break;
        }
    }

    @Override
    public void onDrawWave(int i) {
        if (ecgView.getArrast().size() == 0) {
            width = 0;
        }
        width = (float) (width + 0.5);
        ecgView.setWidthes(width);
        ecgView.addDATA(i);
    }

    @Override
    public void onAvgHr(int i) {
        runOnUiMothod(tvHeartRate, "心率：" + i);
    }

    @Override
    public void onRRMax(int i) {
        runOnUiMothod(tvRrMax, "RR最大值：" + i);

    }

    @Override
    public void onRRMin(int i) {
        runOnUiMothod(tvRrMin, "RR最小值：" + i);

    }

    @Override
    public void onHrv(int i) {
        runOnUiMothod(tvHeartRateVariability, "心率变异性：" + i);

    }

    @Override
    public void onMood(int i) {
        runOnUiMothod(tvMood, "心情：" + i);

    }

    @Override
    public void onBr(int i) {
        runOnUiMothod(tvBreathingRate, "呼吸率：" + i);

    }

    @Override
    public void onEcgDuration(long l) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnStartSave.setVisibility(View.VISIBLE);
                manager.stopMeasure(MeasureType.ECG);
                btnStartMeasure.setText("重新开始");
                isMeasureEcg = false;
            }
        });


    }
}
