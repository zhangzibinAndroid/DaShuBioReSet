package com.dashubio.fragment.main;


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
import com.dashubio.bean.UserListBean;
import com.dashubio.constant.ErrorCode;
import com.dashubio.constant.InterfaceUrl;
import com.dashubio.db.DBManager;
import com.dashubio.gson.GsonParsing;
import com.dashubio.view.ViewHeader;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;


/**
 * 作者： 张梓彬
 * 日期： 2017/7/13 0013
 * 时间： 上午 10:08
 * 描述： 第一个页面的首页
 */
public class MainFirstFragment extends BaseFragment {
    private static final String TAG = "MainFirstFragment";
    @BindView(R.id.lv_message)
    RecyclerView lv_message;
    @BindView(R.id.xrefresh_message)
    XRefreshView xrefresh_message;
    @BindView(R.id.tv_time)
    TextView tvTime;

    private MainPageAdapter mainPageAdapter;
    private List<MainPageBean.MessageDataBean> data;
    private Unbinder unbinder;
    private Handler downHandler;
    private Runnable downRunable;
    private boolean isdown = false;
    private final int USER_PAGE = -1;

    public MainFirstFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_first, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initXRefresh();
        getUserListInterface(USER_PAGE);
        return view;
    }

    private void initView() {
        dbManager = new DBManager(getActivity());
        isdown = false;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lv_message.setLayoutManager(linearLayoutManager);

        new Thread(new Runnable() {
            @Override
            public void run() {
                getMessageInterface();
            }
        }).start();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        tvTime.setText(df.format(calendar.getTime()) + " " + getString(R.string.check_list));
    }

    private void getMessageInterface() {
        Log.e(TAG, "mainUrl: "+ InterfaceUrl.MESSAGE_URL + sessonWithCode);
        OkHttpUtils.get().url(InterfaceUrl.MESSAGE_URL + sessonWithCode)
                .addParams("m_id", "").build().execute(new StringCallback() {
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
                    lv_message.setAdapter(mainPageAdapter);
                    mainPageAdapter.setOnLookingClickListener(new MainPageAdapter.OnLookingClickListener() {
                        @Override
                        public void lookingClick(View v, int position, AutoLinearLayout resultLayout) {

                            if (resultLayout.isSelected() == false) {
                                resultLayout.setVisibility(View.VISIBLE);
                                resultLayout.setSelected(true);
                            } else {
                                resultLayout.setVisibility(View.GONE);
                                resultLayout.setSelected(false);
                            }
                        }
                    });
                    mainPageAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.e(TAG, "Exception: " + e.getMessage());
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
        xrefresh_message.setCustomHeaderView(viewHeader);
        // 设置是否可以上拉刷新
        xrefresh_message.setPullLoadEnable(false);
        // 设置是否可以下拉刷新
        xrefresh_message.setPullRefreshEnable(true);
        xrefresh_message.setXRefreshViewListener(new XRefreshView.XRefreshViewListener() {
            @Override
            public void onRefresh() {
                isdown = true;
                downHandler = new Handler();
                downHandler.postDelayed(downRunable = new Runnable() {
                    @Override
                    public void run() {
                        xrefresh_message.stopRefresh();
                    }
                }, 1500);
            }

            @Override
            public void onRefresh(boolean isPullDown) {
                if (isPullDown) {
                    getMessageInterface();
                }
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        xrefresh_message.stopLoadMore();
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


    //获取用户列表接口
    private void getUserListInterface(int page) {
        OkHttpUtils.get().url(InterfaceUrl.USER_MESSAGE_URL + sessonWithCode)
                .addParams("p", String.valueOf(page))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                Message msg = new Message();
                msg.obj = response;
                allUserHnadler.sendMessage(msg);
            }
        });
    }


    private Handler allUserHnadler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            UserListBean userListBean = null;
            if (result.indexOf(ErrorCode.SUCCESS) > 0) {
                //遍历用户，存入数据库
                try {
                    userListBean = GsonParsing.getUserListMessage(result);
                    List<UserListBean.UserListDataBean> userList = userListBean.getData();
                    List<UserListBean.UserListDataBean> dbUserList = new ArrayList<>();
                    dbUserList = dbManager.searchUserList();
                    String mResult = "";
                    for (UserListBean.UserListDataBean userListDataBean : dbUserList) {
                        mResult = mResult + String.valueOf(userListDataBean.card_id);
                        mResult = mResult + "\n" + "------------------------------------------" + "\n";
                    }

                    for (int i = userList.size()-1; i >-1; i--) {
                        UserListBean.UserListDataBean users = userList.get(i);
                        String userName = users.getName();
                        String sex = users.getSex();
                        String userid = users.getId();
                        String card_id = users.getCard_id();
                        String phone = users.getPhone();
                        if (mResult.indexOf(card_id)!=-1){
                            //数据库有数据
                        }else {
                            dbManager.addUserData(userid,userName,sex,card_id,phone);
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "allUserHnadlerException: "+e.getMessage());
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
