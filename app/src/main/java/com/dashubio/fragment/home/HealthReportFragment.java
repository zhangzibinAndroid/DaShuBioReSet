package com.dashubio.fragment.home;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.dashubio.R;
import com.dashubio.activity.HomeActivity;
import com.dashubio.adapter.HealthReportAdapter;
import com.dashubio.base.BaseFragment;
import com.dashubio.bean.ErrorCodeBean;
import com.dashubio.bean.HealthReportBean;
import com.dashubio.bean.HealthReportSecondBean;
import com.dashubio.constant.ErrorCode;
import com.dashubio.constant.InterfaceUrl;
import com.dashubio.gson.GsonParsing;
import com.dashubio.utils.NetUtil;
import com.dashubio.view.ViewHeader;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/13 0013
 * 时间： 上午 10:17
 * 描述： 健康报告
 */
public class HealthReportFragment extends BaseFragment {


    @BindView(R.id.tv_single)
    TextView tvSingle;
    @BindView(R.id.tv_comprehensive)
    TextView tvComprehensive;
    @BindView(R.id.lv_health_report)
    ExpandableListView lvHealthReport;
    @BindView(R.id.xrefresh_health_report)
    XRefreshView xrefreshHealthReport;
    @BindView(R.id.lay_list)
    AutoLinearLayout layList;
    @BindView(R.id.myProgressBar)
    ProgressBar myProgressBar;
    @BindView(R.id.myWebView)
    WebView myWebView;
    @BindView(R.id.lay_web)
    AutoLinearLayout layWeb;
    private Unbinder unbinder;
    private boolean isdown = false;
    private Handler downHandler;
    private Runnable downRunable;
    private static final String TAG = "HealthReportFragment";
    private HealthReportAdapter healthReportAdapter;
    private List<HealthReportBean.HealthDataBean.FatherDetBean.ChildrenProBean> childrenProBeanList;
    private Map<String, List<HealthReportBean.HealthDataBean.FatherDetBean.ChildrenProBean>> dataset;
    private List<HealthReportBean.HealthDataBean.FatherDetBean> childrenBean;
    private Map<String, ArrayList<String>> childernDataset;
    private boolean isComprehensive = false;

    public HealthReportFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_health_report, container, false);
        unbinder = ButterKnife.bind(this, view);
        isComprehensive = false;
        initRefresh();
        initView();
        initWebView();
        return view;
    }

    private void initView() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                healthReportInterface(InterfaceUrl.HEALTH_REPORT_URL + sessonWithCode);
            }
        }).start();
    }

    private void initComprehensive() {
        Log.e(TAG, "initComprehensive: "+InterfaceUrl.HEALTH_REPORT_SECOND_LIST_URL + sessonWithCode );
        new Thread(new Runnable() {
            @Override
            public void run() {
                healthReportSecondInterface(InterfaceUrl.HEALTH_REPORT_SECOND_LIST_URL + sessonWithCode);
            }
        }).start();
    }

    private void healthReportSecondInterface(String url) {
        OkHttpUtils.get().url(url)
                .addParams("m_id", HomeActivity.mid)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "获取数据异常onError: "+e.getMessage() );
//                toastOnUi(getResources().getString(R.string.network_exception_please_try_again_later));
            }

            @Override
            public void onResponse(String response, int id) {
                Message msg = new Message();
                msg.obj = response;
                healthReportComprehensiveHandler.sendMessage(msg);

            }
        });
    }


    private void healthReportInterface(String url) {
        Log.e(TAG, "url: "+url);
        OkHttpUtils.get().url(url)
                .addParams("m_id", HomeActivity.mid)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "onError: "+e.getMessage() );
//                toastOnUi(getResources().getString(R.string.network_exception_please_try_again_later));
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e(TAG, "response: " +response);
                Message msg = new Message();
                msg.obj = response;
                healthReportHandler.sendMessage(msg);
            }
        });
    }

    private void initRefresh() {
        isdown = false;
        ViewHeader viewHeader = new ViewHeader(getActivity());
        xrefreshHealthReport.setCustomHeaderView(viewHeader);
        // 设置是否可以上拉刷新
        xrefreshHealthReport.setPullLoadEnable(false);
        // 设置是否可以下拉刷新
        xrefreshHealthReport.setPullRefreshEnable(true);
        xrefreshHealthReport.setXRefreshViewListener(new XRefreshView.XRefreshViewListener() {
            @Override
            public void onRefresh() {
                isdown = true;
                downHandler = new Handler();
                downHandler.postDelayed(downRunable = new Runnable() {
                    @Override
                    public void run() {
                        xrefreshHealthReport.stopRefresh();
                    }
                }, 1500);
            }

            @Override
            public void onRefresh(boolean isPullDown) {
                if (NetUtil.isNetworkConnectionActive(getActivity())) {
                    if (isPullDown) {
                        //加载
                        if (!isComprehensive) {
                            initView();
                        } else {
                            initComprehensive();
                        }
                    }
                }

            }

            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        xrefreshHealthReport.stopLoadMore();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (isdown) {
            downHandler.removeCallbacks(downRunable);
        }
    }

    @OnClick({R.id.tv_single, R.id.tv_comprehensive})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_single:
                isComprehensive = false;
                tvSingle.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_left_all));
                tvComprehensive.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_right));
                tvSingle.setTextColor(getResources().getColor(R.color.white));
                tvComprehensive.setTextColor(getResources().getColor(R.color.black));
                initView();
                break;
            case R.id.tv_comprehensive:
                isComprehensive = true;
                tvSingle.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_left));
                tvComprehensive.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_right_all));
                tvSingle.setTextColor(getResources().getColor(R.color.black));
                tvComprehensive.setTextColor(getResources().getColor(R.color.white));
                initComprehensive();
                break;
        }
    }


    private Handler healthReportHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            if (result.indexOf(ErrorCode.SUCCESS) > 0) {
                try {
                    HealthReportBean healthReportBean = GsonParsing.getHealthReportMessageJson(result);
                    List<HealthReportBean.HealthDataBean> groupList = healthReportBean.getData();
                    final String[] parentList = new String[groupList.size()];
                    childernDataset = new HashMap<>();
                    dataset = new HashMap<>();
                    final List<String> childenList = new ArrayList<>();

                    for (int i = 0; i < groupList.size(); i++) {
                        HealthReportBean.HealthDataBean bean = groupList.get(i);
                        parentList[i] = bean.getYear();
                        childrenBean = bean.getDet();
                        for (int j = 0; j < childrenBean.size(); j++) {
                            HealthReportBean.HealthDataBean.FatherDetBean monthBean = childrenBean.get(j);
                            childenList.add(monthBean.getMonth());
                            childrenProBeanList = monthBean.getPro();
                            dataset.put(i + "and" + j, new ArrayList<HealthReportBean.HealthDataBean.FatherDetBean.ChildrenProBean>(childrenProBeanList));
                            childrenProBeanList.clear();
                        }
                        childernDataset.put("children" + i, new ArrayList<String>(childenList));
                        childenList.clear();

                    }

                    healthReportAdapter = new HealthReportAdapter(getActivity(), parentList, childernDataset);
                    lvHealthReport.setAdapter(healthReportAdapter);
                    healthReportAdapter.notifyDataSetChanged();
                    healthReportAdapter.setOnTagFlowLayoutItemClickListener(new HealthReportAdapter.OnTagFlowLayoutItemClickListener() {
                        @Override
                        public void onTagFlowLayoutItemClick(int groupPosition, int position) {
                            showDialog(parentList[groupPosition] + "年" + childernDataset.get("children" + groupPosition).get(position) + "月", dataset.get(groupPosition + "and" + position), parentList[groupPosition], childernDataset.get("children" + groupPosition).get(position));
                        }
                    });

                } catch (Exception e) {
                    Log.e(TAG, "healthReportHandlerException: " + e);
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

    private AlertDialog dialog;

    private void showDialog(String title, final List<HealthReportBean.HealthDataBean.FatherDetBean.ChildrenProBean> list, final String year, final String month) {
        AlertDialog.Builder messageDialog = new AlertDialog.Builder(getActivity());
        messageDialog.setTitle(title);
        View view = View.inflate(getActivity(), R.layout.dialog_tagflowlayout, null);
        TagFlowLayout dialog_tagflowlayout = (TagFlowLayout) view.findViewById(R.id.dialog_tagflowlayout);
        TagAdapter tagAdapter = new TagAdapter(list) {
            @Override
            public View getView(FlowLayout parent, int position, Object o) {
                TextView tvDialog = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.item_textview_children, parent, false);
                HealthReportBean.HealthDataBean.FatherDetBean.ChildrenProBean bean = list.get(position);
                tvDialog.setText(bean.getName());
                return tvDialog;
            }
        };
        dialog_tagflowlayout.setAdapter(tagAdapter);
        dialog_tagflowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                layList.setVisibility(View.GONE);
                layWeb.setVisibility(View.VISIBLE);
                HealthReportBean.HealthDataBean.FatherDetBean.ChildrenProBean bean = list.get(position);
                myWebView.loadUrl(InterfaceUrl.SINGLE_WEBVIEW_URL + "/mid/" + HomeActivity.mid + "/pro/" + bean.getId() + "/year/" + year + "/month/" + month + ".html");

                dialog.dismiss();
                return true;
            }
        });

        messageDialog.setView(view);
        dialog = messageDialog.show();
    }

    private void initWebView() {
        //声明WebSettings子类
        WebSettings webSettings = myWebView.getSettings();
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
//        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式


        //步骤3. 复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        myWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    myProgressBar.setMax(100);
                    myProgressBar.setProgress(newProgress);
                } else {
                    myProgressBar.setVisibility(View.GONE);
                }
            }
        });

    }

    private List<HealthReportSecondBean.HealthReportTimeDataBean> healthReportTimeDataList;
    private List<HealthReportSecondBean.HealthReportTimeDataBean.HealthReportDetBean> healthReportDetList;
    private List<String> moonthStringList = new ArrayList<>();
    private List<String> moonthStringListSecond = new ArrayList<>();
    private Map<String, ArrayList<String>> monthListString = new HashMap<>();
    private Map<String, ArrayList<String>> monthList = new HashMap<>();
    private Handler healthReportComprehensiveHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            if (result.indexOf(ErrorCode.SUCCESS) > 0) {
                try {
                    HealthReportSecondBean healthReportSecondBean = GsonParsing.getHealthReportSecondMessageJson(result);
                    healthReportTimeDataList = healthReportSecondBean.getData();
                    final String[] yearData = new String[healthReportTimeDataList.size()];
                    for (int i = 0; i < healthReportTimeDataList.size(); i++) {
                        HealthReportSecondBean.HealthReportTimeDataBean healthReportTimeDataBean = healthReportTimeDataList.get(i);
                        yearData[i] = healthReportTimeDataBean.getYear();
                        healthReportDetList = healthReportTimeDataBean.getDet();
                        for (int j = 0; j < healthReportDetList.size(); j++) {
                            HealthReportSecondBean.HealthReportTimeDataBean.HealthReportDetBean healthReportDetBean = healthReportDetList.get(j);
                            moonthStringList.add(healthReportDetBean.getMonth());
                            moonthStringListSecond.add(healthReportDetBean.getMonth());
                            monthListString.put(i + "and" + j, new ArrayList<String>(moonthStringListSecond));
                            moonthStringListSecond.clear();
                        }
                        monthList.put("children" + i, new ArrayList<String>(moonthStringList));
                        moonthStringList.clear();

                    }
                    healthReportAdapter = new HealthReportAdapter(getActivity(), yearData, monthList);
                    lvHealthReport.setAdapter(healthReportAdapter);
                    healthReportAdapter.notifyDataSetChanged();
                    healthReportAdapter.setOnTagFlowLayoutItemClickListener(new HealthReportAdapter.OnTagFlowLayoutItemClickListener() {
                        @Override
                        public void onTagFlowLayoutItemClick(int groupPosition, int position) {
                            layList.setVisibility(View.GONE);
                            layWeb.setVisibility(View.VISIBLE);
                            myWebView.loadUrl(InterfaceUrl.COMPREHENSIVE_WEBVIEW_URL + "/mid/" + HomeActivity.mid + "/year/" + yearData[groupPosition] + "/month/" + monthListString.get(groupPosition + "and" + position).get(0) + ".html");
                            Log.e(TAG, "url: =="+InterfaceUrl.COMPREHENSIVE_WEBVIEW_URL + "/mid/" + HomeActivity.mid + "/year/" + yearData[groupPosition] + "/month/" + monthListString.get(groupPosition + "and" + position).get(0) + ".html" );
                        }
                    });


                } catch (Exception e) {
                    Log.e(TAG, "healthReportComprehensiveHandlerException: " + e.getMessage());
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


}
