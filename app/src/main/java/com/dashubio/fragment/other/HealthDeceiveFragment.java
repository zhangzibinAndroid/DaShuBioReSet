package com.dashubio.fragment.other;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dashubio.R;
import com.dashubio.base.BaseFragment;
import com.dashubio.bean.eventmsg.EventMessage;
import com.linktop.MonitorDataTransmissionManager;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 作者： 张梓彬
 * 日期： 2017/9/14 0014
 * 时间： 上午 11:23
 * 描述： 健康检测仪
 */
public class HealthDeceiveFragment extends BaseFragment {

    @BindView(R.id.header_title)
    TextView headerTitle;
    private Unbinder unbinder;
    private static final String TAG = "HealthDeceiveFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_health_deceive, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        headerTitle.setText("健康检测仪");
        manager = MonitorDataTransmissionManager.getInstance();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.header_left_container, R.id.lay_ecg, R.id.lay_blood_oxygen, R.id.lay_blood_pressure, R.id.lay_body_temperature})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_left_container:
                EventBus.getDefault().post(new EventMessage("health_disconnect"));
                manager.disConnectBle();
                break;
            case R.id.lay_ecg:
                EventBus.getDefault().post(new EventMessage("health_ecg"));
                break;
            case R.id.lay_blood_oxygen:
                EventBus.getDefault().post(new EventMessage("health_bo"));
                break;
            case R.id.lay_blood_pressure:
                EventBus.getDefault().post(new EventMessage("health_bp"));
                break;
            case R.id.lay_body_temperature:
                EventBus.getDefault().post(new EventMessage("health_tem"));
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
