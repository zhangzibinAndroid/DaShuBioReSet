package com.dashubio.fragment.main;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dashubio.R;
import com.dashubio.base.BaseFragment;
import com.dashubio.bean.EventLoginMessage;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/13 0013
 * 时间： 上午 10:10
 * 描述： 用户登录
 */
public class UserLoginFragment extends BaseFragment {
    private Unbinder unbinder;
    public static final String action = "idcard.scan";
    private final int TAKEPHOTO_CODE=2;
    public UserLoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_login, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_card, R.id.btn_search, R.id.btn_self})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_card:
                EventBus.getDefault().post(new EventLoginMessage("cardMessage"));
                break;
            case R.id.btn_search:
            case R.id.btn_self:
                EventBus.getDefault().post(new EventLoginMessage("message"));
                break;
        }
    }

}
