package com.dashubio.fragment.other;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dashubio.R;
import com.dashubio.base.BaseFragment;
import com.dashubio.bean.EventLoginMessage;
import com.dashubio.fragment.main.HelpFragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/19 0019
 * 时间： 上午 10:46
 * 描述： 帮助详情页面
 */
public class HelpDetailFragment extends BaseFragment {


    @BindView(R.id.header_title)
    TextView headerTitle;
    @BindView(R.id.tv_helptitle)
    TextView tvHelptitle;
    @BindView(R.id.tv_helpcontext)
    TextView tvHelpcontext;
    private Unbinder unbinder;
    private static final String TAG = "HelpDetailFragment";
    public HelpDetailFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_help_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        headerTitle.setText(getResources().getString(R.string.use_help));
        tvHelptitle.setText(HelpFragment.title);
        tvHelpcontext.setText(HelpFragment.content);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick(R.id.header_left_container)
    public void onViewClicked() {
        EventBus.getDefault().post(new EventLoginMessage("HelpList"));
    }
}
