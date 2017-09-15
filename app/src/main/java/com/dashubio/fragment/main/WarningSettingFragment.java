package com.dashubio.fragment.main;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.dashubio.R;
import com.dashubio.adapter.WarningAdapter;
import com.dashubio.base.BaseFragment;
import com.dashubio.bean.ErrorCodeBean;
import com.dashubio.bean.WarningBean;
import com.dashubio.constant.ErrorCode;
import com.dashubio.constant.InterfaceUrl;
import com.dashubio.gson.GsonParsing;
import com.dashubio.view.ViewHeader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/13 0013
 * 时间： 上午 10:12
 * 描述： 预警设置
 */
public class WarningSettingFragment extends BaseFragment {

    @BindView(R.id.lv_warning)
    RecyclerView lvWarning;
    @BindView(R.id.xrefresh_warning)
    XRefreshView xrefreshWarning;
    Unbinder unbinder;
    private WarningAdapter warningAdapter;
    private List<WarningBean.WarningDataBean> dataList;
    private int downIsRefresh = 1;
    private Handler downHandler;
    private Runnable downRunable;
    public WarningSettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_warning_setting, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        downIsRefresh = 1;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lvWarning.setLayoutManager(linearLayoutManager);
        initXRefreshWarning();
        new Thread(new Runnable() {
            @Override
            public void run() {
                warningInterface();
            }
        }).start();


    }

    private void warningInterface() {
        OkHttpUtils.get().url(InterfaceUrl.WARNING_URL + sessonWithCode).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
//                toastOnUi("获取预警列表异常，请检查网络");
            }

            @Override
            public void onResponse(String response, int id) {
                Message msg = new Message();
                msg.obj = response;
                warningHandler.sendMessage(msg);
            }
        });
    }

    private void initXRefreshWarning() {
        ViewHeader viewHeader = new ViewHeader(getActivity());
        xrefreshWarning.setCustomHeaderView(viewHeader);
        // 设置是否可以上拉刷新
        xrefreshWarning.setPullLoadEnable(false);
        // 设置是否可以下拉刷新
        xrefreshWarning.setPullRefreshEnable(true);
        //设置是否可以自动刷新
        xrefreshWarning.setAutoRefresh(false);

        xrefreshWarning.setXRefreshViewListener(new XRefreshView.XRefreshViewListener() {
            @Override
            public void onRefresh() {
                downIsRefresh = 2;
                downHandler = new Handler();
                downHandler.postDelayed(downRunable = new Runnable() {
                    @Override
                    public void run() {
                        xrefreshWarning.stopRefresh();
                    }
                }, 1500);
            }

            @Override
            public void onRefresh(boolean isPullDown) {
                if (isPullDown){
                    warningInterface();
                }
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        xrefreshWarning.stopLoadMore();
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
        if (downIsRefresh==2){
            downHandler.removeCallbacks(downRunable);
        }
    }


    private Handler warningHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            if (result.indexOf(ErrorCode.SUCCESS) > 0) {
                WarningBean bean = null;
                try {
                    bean = GsonParsing.getWarningMessage(result);
                    dataList = bean.getData();
                    warningAdapter = new WarningAdapter(dataList);
                    lvWarning.setAdapter(warningAdapter);
                    warningAdapter.notifyDataSetChanged();
                } catch (Exception e) {
//                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_timeout_or_illegal_request), Toast.LENGTH_SHORT).show();
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
