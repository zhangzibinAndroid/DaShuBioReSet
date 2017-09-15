package com.dashubio.fragment.other;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.dashubio.R;
import com.dashubio.bean.eventmsg.EventMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 作者： 张梓彬
 * 日期： 2017/9/15 0015
 * 时间： 上午 10:56
 * 描述： ProgressFragment自定义
 */

public class ProgressFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.progress_dialog_fragment, container);
        initView();
        return view;
    }

    private void initView() {
        EventBus.getDefault().register(this);
        setCancelable(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessage(EventMessage eventMessage) {
        if (eventMessage.msg.equals("health_deceive")) {
            dismiss();
        }
    }

}
