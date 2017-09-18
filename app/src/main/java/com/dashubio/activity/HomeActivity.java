package com.dashubio.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dashubio.R;
import com.dashubio.base.BaseActivity;
import com.dashubio.bean.eventmsg.EventMessage;
import com.dashubio.fragment.health_deceive.BloodOxygenFragment;
import com.dashubio.fragment.health_deceive.BloodPressureMeasureFragment;
import com.dashubio.fragment.health_deceive.BodyTemperatureFragment;
import com.dashubio.fragment.health_deceive.EcgFragment;
import com.dashubio.fragment.home.HealthArchivesFragment;
import com.dashubio.fragment.home.HealthGuideFragment;
import com.dashubio.fragment.home.HealthReportFragment;
import com.dashubio.fragment.home.HistoryDataFragment;
import com.dashubio.fragment.home.HomeFristFragment;
import com.dashubio.fragment.home.StartMeasurementFragment;
import com.dashubio.fragment.other.HealthDeceiveFragment;
import com.linktop.MonitorDataTransmissionManager;
import com.zhy.autolayout.AutoFrameLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HomeActivity extends BaseActivity {
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_return)
    TextView tvReturn;

    @BindViews({R.id.tv_first_home, R.id.tv_start_measurement, R.id.tv_history_data, R.id.tv_health_report, R.id.tv_health_archives, R.id.tv_health_guide})
    TextView[] tv_sel;

    @BindView(R.id.home_content)
    AutoFrameLayout content;
    private Unbinder unbinder;
    public static String userName = "";
    public static String mid = "";
    private static final String TAG = "HomeActivity";
    private HomeFristFragment homeFristFragment;
    private StartMeasurementFragment startMeasurementFragment;
    private HistoryDataFragment historyDataFragment;
    private HealthReportFragment healthReportFragment;
    private HealthArchivesFragment healthArchivesFragment;
    private HealthGuideFragment healthGuideFragment;
    private HealthDeceiveFragment healthDeceiveFragment;
    private EcgFragment ecgFragment;
    private BodyTemperatureFragment bodyTemperatureFragment;
    private BloodPressureMeasureFragment bloodPressureMeasureFragment;
    private BloodOxygenFragment bloodOxygenFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        unbinder = ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        manager = MonitorDataTransmissionManager.getInstance();
        EventBus.getDefault().register(this);
        homeFristFragment = new HomeFristFragment();
        startMeasurementFragment = new StartMeasurementFragment();
        historyDataFragment = new HistoryDataFragment();
        healthReportFragment = new HealthReportFragment();
        healthArchivesFragment = new HealthArchivesFragment();
        healthGuideFragment = new HealthGuideFragment();
        healthDeceiveFragment = new HealthDeceiveFragment();
        ecgFragment = new EcgFragment();
        bodyTemperatureFragment = new BodyTemperatureFragment();
        bloodPressureMeasureFragment = new BloodPressureMeasureFragment();
        bloodOxygenFragment = new BloodOxygenFragment();
        tv_sel[0].setSelected(true);
        setReplaceFragment(R.id.home_content, homeFristFragment);
        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        mid = intent.getStringExtra("mid");
        tvUserName.setText(userName);

    }

    @OnClick({R.id.tv_first_home, R.id.tv_start_measurement, R.id.tv_history_data, R.id.tv_health_report, R.id.tv_health_archives, R.id.tv_health_guide})
    public void onViewClicked(View view) {
        for (int i = 0; i < tv_sel.length; i++) {
            tv_sel[i].setSelected(false);
        }

        switch (view.getId()) {
            case R.id.tv_first_home:
                tv_sel[0].setSelected(true);
                setReplaceFragment(R.id.home_content, homeFristFragment);
                break;
            case R.id.tv_start_measurement:
                tv_sel[1].setSelected(true);
                setReplaceFragment(R.id.home_content, startMeasurementFragment);
                break;
            case R.id.tv_history_data:
                tv_sel[2].setSelected(true);
                setReplaceFragment(R.id.home_content, historyDataFragment);
                break;
            case R.id.tv_health_report:
                tv_sel[3].setSelected(true);
                setReplaceFragment(R.id.home_content, healthReportFragment);
                break;
            case R.id.tv_health_archives:
                tv_sel[4].setSelected(true);
                setReplaceFragment(R.id.home_content, healthArchivesFragment);
                break;
            case R.id.tv_health_guide:
                tv_sel[5].setSelected(true);
                setReplaceFragment(R.id.home_content, healthGuideFragment);
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.disConnectBle();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);

    }

    @OnClick(R.id.tv_return)
    public void onViewClicked() {
        finish();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getHealthDeceiveMessage(EventMessage eventMessage) {
        switch (eventMessage.msg) {
            case "back":
                setReplaceFragment(R.id.home_content, healthDeceiveFragment);
                break;
            case "health_deceive":
                setReplaceFragment(R.id.home_content, healthDeceiveFragment);
                break;
            case "health_disconnect":
                setReplaceFragment(R.id.home_content, startMeasurementFragment);
                break;
            case "health_ecg":
                setReplaceFragment(R.id.home_content, ecgFragment);
                break;
            case "health_tem":
                setReplaceFragment(R.id.home_content, bodyTemperatureFragment);
                break;
            case "health_bp":
                setReplaceFragment(R.id.home_content, bloodPressureMeasureFragment);
                break;
            case "health_bo":
                setReplaceFragment(R.id.home_content, bloodOxygenFragment);
                break;
        }

    }
}
