package com.dashubio.fragment.home;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.dashubio.R;
import com.dashubio.activity.HomeActivity;
import com.dashubio.adapter.HistoryDataAdapter;
import com.dashubio.base.BaseFragment;
import com.dashubio.bean.ErrorCodeBean;
import com.dashubio.bean.HistoryDataBean;
import com.dashubio.bean.HistoryListDataBean;
import com.dashubio.constant.ErrorCode;
import com.dashubio.constant.InterfaceUrl;
import com.dashubio.gson.GsonParsing;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/13 0013
 * 时间： 上午 10:16
 * 描述： 历史数据
 */
public class HistoryDataFragment extends BaseFragment {
    @BindView(R.id.tv_health_detector)
    TextView tvHealthDetector;
    @BindView(R.id.tv_dry_biochemical_apparatus)
    TextView tvDryBiochemicalApparatus;
    @BindView(R.id.tv_urinometer)
    TextView tvUrinometer;
    @BindView(R.id.tv_huxi)
    TextView tv_huxi;
    @BindView(R.id.lay_health_detector)
    TagFlowLayout layHealthDetector;
    @BindView(R.id.lay_dry_biochemical_apparatus)
    TagFlowLayout layDryBiochemicalApparatus;
    @BindView(R.id.lay_urinometer)
    TagFlowLayout layUrinometer;
    @BindView(R.id.lay_huxi)
    TagFlowLayout lay_huxi;
    @BindView(R.id.scrollView_history)
    ScrollView scrollViewHistory;
    @BindView(R.id.lv_history_data)
    ExpandableListView lvHistoryData;
    private Unbinder unbinder;
    private TagAdapter<HistoryDataBean.HistoryData.ProjectBean> healthDetectorAdapter;
    private TagAdapter<HistoryDataBean.HistoryData.ProjectBean> dryBiochemicalApparatusAdapter;
    private TagAdapter<HistoryDataBean.HistoryData.ProjectBean> urinometerAdapter;
    private TagAdapter<HistoryDataBean.HistoryData.ProjectBean> huxiAdapter;
    private List<HistoryDataBean.HistoryData.ProjectBean> healthDetectorChildrenList;
    private List<HistoryDataBean.HistoryData.ProjectBean> healthDetectorChildrenListSecond;
    private List<HistoryDataBean.HistoryData> healthDetectorFaherList;
    private static String FA_ID_ONE = "", FA_ID_TWO = "", FA_ID_THREE = "", FA_ID_FOUR = "";
    private static String SO_ID_ONE = "";
    private HistoryDataAdapter historyDataAdapter;
    private static final String TAG = "HistoryDataFragment";

    public HistoryDataFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history_data, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        tvHealthDetector.setVisibility(View.GONE);
        tvDryBiochemicalApparatus.setVisibility(View.GONE);
        tvUrinometer.setVisibility(View.GONE);
        layHealthDetector.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                if (selectPosSet == null || selectPosSet.size() == 0) {

                } else {
                    Iterator<Integer> it = selectPosSet.iterator();
                    while (it.hasNext()) {
                        int pos = it.next();
                        HistoryDataBean.HistoryData historyDataBean = healthDetectorFaherList.get(0);
                        healthDetectorChildrenListSecond = historyDataBean.getProject();
                        HistoryDataBean.HistoryData.ProjectBean bean = healthDetectorChildrenListSecond.get(pos);
                        SO_ID_ONE = bean.getId() + "";
                        scrollViewHistory.setVisibility(View.GONE);
                        lvHistoryData.setVisibility(View.VISIBLE);
                        showListData(FA_ID_ONE, SO_ID_ONE);
                    }
                }
            }
        });

        layDryBiochemicalApparatus.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                if (selectPosSet == null || selectPosSet.size() == 0) {

                } else {
                    Iterator<Integer> it = selectPosSet.iterator();
                    while (it.hasNext()) {
                        int pos = it.next();
                        HistoryDataBean.HistoryData historyDataBean = healthDetectorFaherList.get(1);
                        healthDetectorChildrenListSecond = historyDataBean.getProject();
                        HistoryDataBean.HistoryData.ProjectBean bean = healthDetectorChildrenListSecond.get(pos);
                        SO_ID_ONE = bean.getId() + "";
                        scrollViewHistory.setVisibility(View.GONE);
                        lvHistoryData.setVisibility(View.VISIBLE);
                        showListData(FA_ID_TWO, SO_ID_ONE);
                    }
                }
            }
        });


        layUrinometer.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                if (selectPosSet == null || selectPosSet.size() == 0) {

                } else {
                    Iterator<Integer> it = selectPosSet.iterator();
                    while (it.hasNext()) {
                        int pos = it.next();
                        HistoryDataBean.HistoryData historyDataBean = healthDetectorFaherList.get(2);
                        healthDetectorChildrenListSecond = historyDataBean.getProject();
                        HistoryDataBean.HistoryData.ProjectBean bean = healthDetectorChildrenListSecond.get(pos);
                        SO_ID_ONE = bean.getId() + "";
                        scrollViewHistory.setVisibility(View.GONE);
                        lvHistoryData.setVisibility(View.VISIBLE);
                        showListData(FA_ID_THREE, SO_ID_ONE);
                    }
                }
            }
        });

        //呼吸
        lay_huxi.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                if (selectPosSet == null || selectPosSet.size() == 0) {

                } else {
                    Iterator<Integer> it = selectPosSet.iterator();
                    while (it.hasNext()) {
                        int pos = it.next();
                        HistoryDataBean.HistoryData historyDataBean = healthDetectorFaherList.get(4);
                        healthDetectorChildrenListSecond = historyDataBean.getProject();
                        HistoryDataBean.HistoryData.ProjectBean bean = healthDetectorChildrenListSecond.get(pos);
                        SO_ID_ONE = bean.getId() + "";
                        scrollViewHistory.setVisibility(View.GONE);
                        lvHistoryData.setVisibility(View.VISIBLE);
                        showListData(FA_ID_FOUR, SO_ID_ONE);
                    }
                }
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                historyDataInterface();
            }
        }).start();

    }

    private void showListData(final String fa_id, final String so_id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "fa_id: " + fa_id + "so_id==" + so_id);
                OkHttpUtils.post().url(InterfaceUrl.HISTORY_DATA_URL + sessonWithCode + "/m_id/" + HomeActivity.mid)
                        .addParams("fa_id", fa_id)
                        .addParams("so_id", so_id)
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        toastOnUi(getResources().getString(R.string.network_exception_please_try_again_later));
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, "子onResponse: " + response);
                        Message msg = new Message();
                        msg.obj = response;
                        historyListHandler.sendMessage(msg);
                    }
                });
            }
        }).start();
    }

    private void historyDataInterface() {
        OkHttpUtils.get().url(InterfaceUrl.HISTORY_KIND_URL + sessonWithCode)
                .addParams("m_id", HomeActivity.mid)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
//                toastOnUi(getResources().getString(R.string.network_exception_please_try_again_later));
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e(TAG, "历史数据1: " + response);
                Message msg = new Message();
                msg.obj = response;
                historyDataHandler.sendMessage(msg);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private Handler historyDataHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            if (result.indexOf(ErrorCode.SUCCESS) > 0) {
                try {
                    HistoryDataBean historyDataBean = GsonParsing.getHistoryDataMessageJson(result);
                    healthDetectorFaherList = historyDataBean.getData();
                    if (healthDetectorFaherList == null || healthDetectorFaherList.size() < 0) {
                        return;
                    }
                    //设置健康检测仪
                    if (healthDetectorFaherList.size() >= 1) {
                        tvHealthDetector.setVisibility(View.VISIBLE);

                        for (int i = 0; i < healthDetectorFaherList.size(); i++) {
                            HistoryDataBean.HistoryData historyData = healthDetectorFaherList.get(0);
                            healthDetectorChildrenList = historyData.getProject();
                            HistoryDataBean.HistoryData.AdeviceBean adevice = historyData.getAdevice();
                            tvHealthDetector.setText(adevice.getName());
                            FA_ID_ONE = adevice.getId();
                        }

                        healthDetectorAdapter = new TagAdapter<HistoryDataBean.HistoryData.ProjectBean>(healthDetectorChildrenList) {
                            @Override
                            public View getView(FlowLayout parent, int position, HistoryDataBean.HistoryData.ProjectBean projectBean) {
                                TextView propertySingleNameTv = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.item_textview_children, parent, false);
                                propertySingleNameTv.setText(projectBean.getName());
                                return propertySingleNameTv;
                            }
                        };
                        layHealthDetector.setAdapter(healthDetectorAdapter);
                    }

                    //设置干式生化仪
                    if (healthDetectorFaherList.size() >= 2) {
                        tvDryBiochemicalApparatus.setVisibility(View.VISIBLE);
                        for (int i = 0; i < healthDetectorFaherList.size(); i++) {
                            HistoryDataBean.HistoryData historyData = healthDetectorFaherList.get(1);
                            healthDetectorChildrenList = historyData.getProject();
                            HistoryDataBean.HistoryData.AdeviceBean adevice = historyData.getAdevice();
                            tvDryBiochemicalApparatus.setText(adevice.getName());
                            FA_ID_TWO = adevice.getId();
                        }
                        dryBiochemicalApparatusAdapter = new TagAdapter<HistoryDataBean.HistoryData.ProjectBean>(healthDetectorChildrenList) {
                            @Override
                            public View getView(FlowLayout parent, int position, HistoryDataBean.HistoryData.ProjectBean projectBean) {
                                TextView propertySingleNameTv = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.item_textview_children, parent, false);
                                propertySingleNameTv.setText(projectBean.getName());
                                return propertySingleNameTv;
                            }
                        };
                        layDryBiochemicalApparatus.setAdapter(dryBiochemicalApparatusAdapter);
                    }

                    //设置尿液分析仪
                    if (healthDetectorFaherList.size() >= 3) {
                        tvUrinometer.setVisibility(View.VISIBLE);
                        for (int i = 0; i < healthDetectorFaherList.size(); i++) {
                            HistoryDataBean.HistoryData historyData = healthDetectorFaherList.get(2);
                            healthDetectorChildrenList = historyData.getProject();
                            HistoryDataBean.HistoryData.AdeviceBean adevice = historyData.getAdevice();
                            tvUrinometer.setText(adevice.getName());
                            FA_ID_THREE = adevice.getId();

                        }

                        urinometerAdapter = new TagAdapter<HistoryDataBean.HistoryData.ProjectBean>(healthDetectorChildrenList) {
                            @Override
                            public View getView(FlowLayout parent, int position, HistoryDataBean.HistoryData.ProjectBean projectBean) {
                                TextView propertySingleNameTv = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.item_textview_children, parent, false);
                                propertySingleNameTv.setText(projectBean.getName());
                                return propertySingleNameTv;
                            }
                        };
                        layUrinometer.setAdapter(urinometerAdapter);
                    }

                    //设置呼吸
                    if (healthDetectorFaherList.size() >= 5) {
                        tv_huxi.setVisibility(View.VISIBLE);
                        for (int i = 0; i < healthDetectorFaherList.size(); i++) {
                            HistoryDataBean.HistoryData historyData = healthDetectorFaherList.get(4);
                            healthDetectorChildrenList = historyData.getProject();
                            HistoryDataBean.HistoryData.AdeviceBean adevice = historyData.getAdevice();
                            tv_huxi.setText(adevice.getName());
                            FA_ID_FOUR = adevice.getId();

                        }

                        huxiAdapter = new TagAdapter<HistoryDataBean.HistoryData.ProjectBean>(healthDetectorChildrenList) {
                            @Override
                            public View getView(FlowLayout parent, int position, HistoryDataBean.HistoryData.ProjectBean projectBean) {
                                TextView propertySingleNameTv = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.item_textview_children, parent, false);
                                propertySingleNameTv.setText(projectBean.getName());
                                return propertySingleNameTv;
                            }
                        };
                        lay_huxi.setAdapter(huxiAdapter);
                    }


                } catch (Exception e) {
                    Log.e(TAG, "historyDataHandlerException: " + e.getMessage());
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

    private List<HistoryListDataBean.HistoryDataBean> list = new ArrayList<>();
    private Handler historyListHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            Log.e(TAG, "result: " + result);
            if (result.indexOf(ErrorCode.SUCCESS) > 0) {
                try {
                    HistoryListDataBean historyListDataBean = GsonParsing.getHistoryListDataMessageJson(result);
                    for (int i = historyListDataBean.getData().size() - 1; i > -1; i--) {
                        list.add(historyListDataBean.getData().get(i));
                    }
                    historyDataAdapter = new HistoryDataAdapter(getActivity(), list);
                    lvHistoryData.setAdapter(historyDataAdapter);
                    historyDataAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.e(TAG, "handleMessage: " + e.getMessage());
//                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_timeout_or_illegal_request), Toast.LENGTH_SHORT).show();
                }
            } else {
                //解析
                ErrorCodeBean errorCodeBean = null;
                try {
                    errorCodeBean = GsonParsing.sendCodeError(result);
                    judge(errorCodeBean.getCode() + "");
                } catch (Exception e) {
                    Log.e(TAG, "Exception: " + e.getMessage());
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_timeout_or_illegal_request), Toast.LENGTH_SHORT).show();
                }
            }

        }
    };
}
