package com.dashubio.fragment.home;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.dashubio.R;
import com.dashubio.adapter.MainPageAdapter;
import com.dashubio.base.BaseFragment;
import com.dashubio.bean.ErrorCodeBean;
import com.dashubio.bean.MainPageBean;
import com.dashubio.constant.ErrorCode;
import com.dashubio.constant.InterfaceUrl;
import com.dashubio.gson.GsonParsing;
import com.dashubio.view.ViewHeader;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

import static com.dashubio.activity.HomeActivity.mid;


/**
 * 作者： 张梓彬
 * 日期： 2017/7/13 0013
 * 时间： 上午 10:14
 * 描述： 第二个页面的首页
 */
public class HomeFristFragment extends BaseFragment {

    private static final String TAG = "HomeFristFragment";
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.lv_message_home)
    RecyclerView lvMessageHome;
    @BindView(R.id.xrefresh_message_home)
    XRefreshView xrefreshMessageHome;
    private MainPageAdapter mainPageAdapter;
    private List<MainPageBean.MessageDataBean> data;
    private Unbinder unbinder;
    private Handler downHandler;
    private Runnable downRunable;
    private boolean isdown = false;
    public HomeFristFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_frist, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initXRefresh();
        return view;
    }

    private void initView() {
        isdown = false;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lvMessageHome.setLayoutManager(linearLayoutManager);

        new Thread(new Runnable() {
            @Override
            public void run() {
                getMessageInterface(mid);
            }
        }).start();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        tvTime.setText(df.format(calendar.getTime()) + " " + getString(R.string.check_list));
    }


    private void getMessageInterface(String mid) {
        OkHttpUtils.get().url(InterfaceUrl.MESSAGE_URL + sessonWithCode)
                .addParams("m_id", mid).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
//                toastOnUi("获取列表异常，请检查网络");

            }

            @Override
            public void onResponse(String response, int id) {
                Log.e(TAG, "首页数据: "+response );
                Message msg = new Message();
                msg.obj = response;
                mainPageMessageHandler.sendMessage(msg);
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


    private Handler mainPageMessageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            if (result.indexOf(ErrorCode.SUCCESS) > 0) {
                try {
                    MainPageBean mainPageBean = GsonParsing.getMainPageMessageJson(result);
                    data = mainPageBean.getData();
                    mainPageAdapter = new MainPageAdapter(data);
                    lvMessageHome.setAdapter(mainPageAdapter);
                    mainPageAdapter.setOnLookingClickListener(new MainPageAdapter.OnLookingClickListener() {
                        @Override
                        public void lookingClick(View v, int position, AutoLinearLayout resultLayout) {

                            if (resultLayout.isSelected()==false){
                                resultLayout.setVisibility(View.VISIBLE);
                                resultLayout.setSelected(true);
                            }else {
                                resultLayout.setVisibility(View.GONE);
                                resultLayout.setSelected(false);
                            }
                        }
                    });
                    mainPageAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.e(TAG, "mainPageMessageHandlerException: "+e.getMessage() );
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


    private void initXRefresh() {
        ViewHeader viewHeader = new ViewHeader(getActivity());
        xrefreshMessageHome.setCustomHeaderView(viewHeader);
        // 设置是否可以上拉刷新
        xrefreshMessageHome.setPullLoadEnable(false);
        // 设置是否可以下拉刷新
        xrefreshMessageHome.setPullRefreshEnable(true);
        xrefreshMessageHome.setXRefreshViewListener(new XRefreshView.XRefreshViewListener() {
            @Override
            public void onRefresh() {
                isdown = true;
                downHandler = new Handler();
                downHandler.postDelayed(downRunable = new Runnable() {
                    @Override
                    public void run() {
                        xrefreshMessageHome.stopRefresh();
                    }
                }, 1500);
            }

            @Override
            public void onRefresh(boolean isPullDown) {
                if (isPullDown) {
                    getMessageInterface(mid);
                }
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        xrefreshMessageHome.stopLoadMore();
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
}
