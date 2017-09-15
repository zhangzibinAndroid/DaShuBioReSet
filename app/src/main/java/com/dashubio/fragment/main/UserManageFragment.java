package com.dashubio.fragment.main;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.dashubio.R;
import com.dashubio.activity.HomeActivity;
import com.dashubio.adapter.UserListAdapter;
import com.dashubio.base.BaseFragment;
import com.dashubio.bean.ErrorCodeBean;
import com.dashubio.bean.UserListBean;
import com.dashubio.bean.UserLoginBean;
import com.dashubio.constant.Constants;
import com.dashubio.constant.ErrorCode;
import com.dashubio.constant.InterfaceUrl;
import com.dashubio.db.DBManager;
import com.dashubio.gson.GsonParsing;
import com.dashubio.utils.NetUtil;
import com.dashubio.view.ViewFooter;
import com.dashubio.view.ViewHeader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/13 0013
 * 时间： 上午 10:11
 * 描述： 用户管理
 */
public class UserManageFragment extends BaseFragment {



    @BindView(R.id.input_query_et)
    EditText inputQueryEt;
    @BindView(R.id.user_list_tv)
    TextView userListTv;
    @BindView(R.id.lv_user_list)
    ListView lvUserList;
    @BindView(R.id.xrefresh_user_list)
    XRefreshView xrefreshUserList;
    private Unbinder unbinder;
    private int page;
    private UserListAdapter userListAdapter;
    private Handler refreshUpHandler, refreshdownHandler;
    private Runnable upRunable, downRunable;
    private int upIsRefresh = 1;
    private int downIsRefresh = 1;
    private static final String TAG = "UserManageFragment";
    private boolean isSearch = false;


    public UserManageFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_manage, null);
        unbinder = ButterKnife.bind(this, view);
        isSearch = false;
        initView();
        return view;
    }



    private void initView() {

        /*lay_edt.setFocusable(true);
        lay_edt.setFocusableInTouchMode(true);*/
        dbManager = new DBManager(getActivity());
        upIsRefresh = 1;
        downIsRefresh = 1;
        page = 1;
        userListAdapter = new UserListAdapter(getActivity());
        lvUserList.setAdapter(userListAdapter);
        //如果网络可用，则调用接口，否则从数据库查询数据
        if (NetUtil.isNetworkConnectionActive(getActivity())) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    getUserListInterface(1);
                }
            }).start();
        } else {
            ArrayList<UserListBean.UserListDataBean> userList = dbManager.searchUserList();
            for (int i = userList.size() - 1; i > -1; i--) {
                UserListBean.UserListDataBean bean = userList.get(i);
                userListAdapter.addDATA(bean);
                userListAdapter.notifyDataSetChanged();
            }

        }
        initXRefresh();
        userListAdapter.setOnButtonClickListener(new UserListAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(View view, final String id, int position) {
                inputQueryEt.setText("");

                if (NetUtil.isNetworkConnectionActive(getActivity())) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            loginInstance(id);
                        }
                    }).start();
                } else {
                    ArrayList<UserListBean.UserListDataBean> userList = dbManager.searchUserList();
                    UserListBean.UserListDataBean bean = userList.get(userList.size() - 1 - position);
                    JumpActivityWithUserData(HomeActivity.class, bean.getName(), bean.getId());
                    Constants.id = (userList.size() - position) + "";
                }


            }
        });
    }

    private void loginInstance(String id) {
        OkHttpUtils.get().url(InterfaceUrl.USER_LOGIN_URL + sessonWithCode)
                .addParams("id", id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        toastOnUi("登录失败，请检查网络");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Message msg = new Message();
                        msg.obj = response;
                        loginHandler.sendMessage(msg);
                    }
                });
    }

    protected void initXRefresh() {
        ViewHeader viewHeader = new ViewHeader(getActivity());
        xrefreshUserList.setCustomHeaderView(viewHeader);
        ViewFooter viewFooter = new ViewFooter(getActivity());
        xrefreshUserList.setCustomFooterView(viewFooter);
        // 设置是否可以上拉刷新
        xrefreshUserList.setPullLoadEnable(true);
        // 设置是否可以下拉刷新
        xrefreshUserList.setPullRefreshEnable(true);
        xrefreshUserList.setXRefreshViewListener(new XRefreshView.XRefreshViewListener() {
            @Override
            public void onRefresh() {
                downIsRefresh = 2;
                refreshdownHandler = new Handler();
                refreshdownHandler.postDelayed(downRunable = new Runnable() {
                    @Override
                    public void run() {
                        xrefreshUserList.stopRefresh();
                    }
                }, 1000);
            }

            @Override
            public void onRefresh(boolean isPullDown) {
                if (!isSearch) {
                    if (NetUtil.isNetworkConnectionActive(getActivity())) {
                        userListAdapter.removeAllDATA();
                        page = 1;
                        getUserListInterface(1);
                    }
                }


            }

            @Override
            public void onLoadMore(boolean isSilence) {
                upIsRefresh = 2;
                refreshUpHandler = new Handler();
                refreshUpHandler.postDelayed(upRunable = new Runnable() {
                    @Override
                    public void run() {
                        xrefreshUserList.stopLoadMore();
                    }
                }, 1000);

                if (!isSearch) {
                    if (NetUtil.isNetworkConnectionActive(getActivity())) {
                        page = page + 1;
                        getUserListInterface(page);
                    }
                }
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
//                toastOnUi("网络异常，请检查网络");
            }

            @Override
            public void onResponse(String response, int id) {
                Message msg = new Message();
                msg.obj = response;
                userListHandler.sendMessage(msg);
            }
        });
    }


    //用户列表查询接口
    private void getUserListSearchInterface(String search) {
        OkHttpUtils.get().url(InterfaceUrl.USER_MESSAGE_URL + sessonWithCode)
                .addParams("search", search)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                toastOnUi("网络异常，请检查网络");
            }

            @Override
            public void onResponse(String response, int id) {
                Message msg = new Message();
                msg.obj = response;
                searchUserHandler.sendMessage(msg);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (upIsRefresh == 2) {
            refreshUpHandler.removeCallbacks(upRunable);
        }

        if (downIsRefresh == 2) {
            refreshdownHandler.removeCallbacks(downRunable);
        }

        isSearch = false;
    }

    @OnClick(R.id.go_search)
    public void onViewClicked() {
        isSearch = true;
        final String search = inputQueryEt.getText().toString();
        inputQueryEt.setText("");
        userListTv.setText("搜索： " + search);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getUserListSearchInterface(search);
            }
        }).start();
    }


    private Handler userListHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            UserListBean userListBean = null;
            try {
                userListBean = GsonParsing.getUserListMessage(result);
                List<UserListBean.UserListDataBean> userList = userListBean.getData();
                userListAdapter.addAllDataToMyadapterWithoutClean(userList);
                userListAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                Log.e(TAG, "userListHandlerException: " + e);
            }

        }
    };

    private Handler searchUserHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            UserListBean userListBean = null;
            try {
                userListBean = GsonParsing.getUserListMessage(result);
                List<UserListBean.UserListDataBean> searchUserList = userListBean.getData();
                userListAdapter.addAllDataToMyadapter(searchUserList);
                userListAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                Toast.makeText(getActivity(), getResources().getString(R.string.connection_timeout_or_illegal_request), Toast.LENGTH_SHORT).show();
            }

        }
    };

    private Handler loginHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            if (result.indexOf(ErrorCode.SUCCESS) > 0) {
                try {
                    UserLoginBean bean = GsonParsing.getUserLoginMessageJson(result);
                    if (bean.getData() == null) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.user_not_exist_please_register_first), Toast.LENGTH_SHORT).show();
                    } else {
                        UserLoginBean.UserLoginDataBean dataBean = bean.getData();
                        JumpActivityWithUserData(HomeActivity.class, dataBean.getName(), dataBean.getId());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "loginHandlerException: " + e.getMessage());
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
