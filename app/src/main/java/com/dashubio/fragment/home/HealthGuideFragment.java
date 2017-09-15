package com.dashubio.fragment.home;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.dashubio.R;
import com.dashubio.activity.HomeActivity;
import com.dashubio.adapter.HealthyGuideListAdapter;
import com.dashubio.base.BaseFragment;
import com.dashubio.bean.ErrorCodeBean;
import com.dashubio.bean.HealthyGuideBean;
import com.dashubio.bean.HealthyGuideListBean;
import com.dashubio.constant.ErrorCode;
import com.dashubio.constant.InterfaceUrl;
import com.dashubio.gson.GsonParsing;
import com.dashubio.view.ViewHeader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/13 0013
 * 时间： 上午 10:18
 * 描述： 健康指导
 */
public class HealthGuideFragment extends BaseFragment {

    private static final String TAG = "HealthGuideFragment";
    @BindView(R.id.health_warning_spinner)
    Spinner healthWarningSpinner;
    @BindView(R.id.tv_nonedata)
    TextView tvNonedata;
    @BindView(R.id.lv_guide)
    ExpandableListView lvGuide;
    @BindView(R.id.xrefresh_guide)
    XRefreshView xrefreshGuide;
    private Unbinder unbinder;
    private int downIsRefresh = 1;
    private Handler downHandler;
    private Runnable downRunable;
    private List<String> projectDataBeanNameList = new ArrayList<>();
    private ArrayAdapter<String> projectDataBeanNameListAdapter = null;
    private static String so_id;
    private List<HealthyGuideBean.ProjectDataBean> healthyGuideProjectList;
    private HealthyGuideListAdapter healthyGuideListAdapter;
    public HealthGuideFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_health_guide, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initXRefreshWarning();
        return view;
    }

    private void initView() {
        downIsRefresh = 1;
        tvNonedata.setVisibility(View.GONE);
        healthWarningSpinner.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get().url(InterfaceUrl.HEALTH_GUIDE_URL + sessonWithCode)
                        .addParams("m_id", HomeActivity.mid)
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
//                        toastOnUi(getResources().getString(R.string.network_exception_please_try_again_later));
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Message msg = new Message();
                        msg.obj = response;
                        healthGuideHandler.sendMessage(msg);
                    }
                });
            }
        }).start();

        healthWarningSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HealthyGuideBean.ProjectDataBean projectDataBean = healthyGuideProjectList.get(position);
                so_id = projectDataBean.getId();
                getMessage(so_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e(TAG, "onNothingSelected: ");
            }
        });

        healthyGuideListAdapter = new HealthyGuideListAdapter(getActivity());
        lvGuide.setAdapter(healthyGuideListAdapter);
    }

    private void initXRefreshWarning() {
        ViewHeader viewHeader = new ViewHeader(getActivity());
        xrefreshGuide.setCustomHeaderView(viewHeader);
        // 设置是否可以上拉刷新
        xrefreshGuide.setPullLoadEnable(false);
        // 设置是否可以下拉刷新
        xrefreshGuide.setPullRefreshEnable(true);
        //设置是否可以自动刷新
        xrefreshGuide.setAutoRefresh(false);

        xrefreshGuide.setXRefreshViewListener(new XRefreshView.XRefreshViewListener() {
            @Override
            public void onRefresh() {
                downIsRefresh = 2;
                downHandler = new Handler();
                downHandler.postDelayed(downRunable = new Runnable() {
                    @Override
                    public void run() {
                        xrefreshGuide.stopRefresh();
                    }
                }, 1500);
            }

            @Override
            public void onRefresh(boolean isPullDown) {
                if (isPullDown) {

                }
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        xrefreshGuide.stopLoadMore();
                    }
                }, 1500);
            }

            @Override
            public void onRelease(float direction) {

            }

            @Override
            public void onHeaderMove(double headerMovePercent, int offsetY) {

            }
        });
    }


    private void getMessage(final String so_id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "url: "+InterfaceUrl.HEALTH_GUIDE_LIST_URL + sessonWithCode + "/m_id/" + HomeActivity.mid);
                OkHttpUtils.post().url(InterfaceUrl.HEALTH_GUIDE_LIST_URL + sessonWithCode + "/m_id/" + HomeActivity.mid)
                        .addParams("so_id", so_id)
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
//                        toastOnUi(getResources().getString(R.string.network_exception_please_try_again_later));

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, "onResponse: "+response );
                        Message msg = new Message();
                        msg.obj = response;
                        healthGuideListHandler.sendMessage(msg);
                    }
                });
            }
        }).start();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (downIsRefresh == 2) {
            downHandler.removeCallbacks(downRunable);
        }
    }

    private Handler healthGuideHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            if (result.indexOf(ErrorCode.SUCCESS) > 0) {
                try {
                    HealthyGuideBean healthyGuideBean = GsonParsing.getHealthProjectMessageJson(result);
                    healthyGuideProjectList = healthyGuideBean.getData();
                    if (healthyGuideProjectList.size() == 0) {
                        tvNonedata.setVisibility(View.VISIBLE);
                        healthWarningSpinner.setVisibility(View.GONE);
                        return;
                    }
                    projectDataBeanNameList.clear();
                    for (int i = 0; i < healthyGuideProjectList.size(); i++) {
                        HealthyGuideBean.ProjectDataBean projectDataBean = healthyGuideProjectList.get(i);
                        projectDataBeanNameList.add(projectDataBean.getName());
                    }
                    projectDataBeanNameListAdapter = new ArrayAdapter<String>(getActivity(), R.layout.textview_healtarchves, projectDataBeanNameList);
                    healthWarningSpinner.setAdapter(projectDataBeanNameListAdapter);
                    healthWarningSpinner.setSelection(0, true);
                } catch (Exception e) {
                    Log.e(TAG, "healthGuideHandlerException: " + e);
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


    private Handler healthGuideListHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            Log.e(TAG, "result: "+result );
            if (result.indexOf(ErrorCode.SUCCESS) > 0) {
                    try {
                        HealthyGuideListBean healthyGuideListBean = GsonParsing.getHealthProjectListMessageJson(result);
                        List<HealthyGuideListBean.ProjectListDataBean> healthyGuideList = healthyGuideListBean.getData();
                        healthyGuideListAdapter.addAllData(healthyGuideList);
                        healthyGuideListAdapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        Log.e(TAG, "healthGuideListHandlerException: "+e );
                    }

            }else {
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
}
