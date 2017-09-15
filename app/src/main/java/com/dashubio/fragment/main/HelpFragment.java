package com.dashubio.fragment.main;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.dashubio.R;
import com.dashubio.adapter.HelpAdapter;
import com.dashubio.base.BaseFragment;
import com.dashubio.bean.ErrorCodeBean;
import com.dashubio.bean.EventLoginMessage;
import com.dashubio.bean.HelpMessageBean;
import com.dashubio.constant.ErrorCode;
import com.dashubio.constant.InterfaceUrl;
import com.dashubio.gson.GsonParsing;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/13 0013
 * 时间： 上午 10:13
 * 描述： 使用帮助
 */
public class HelpFragment extends BaseFragment {
    public static String title = "";
    public static String content = "";

    @BindView(R.id.lv_help)
    ListView lvHelp;
    private Unbinder unbinder;
    private HelpAdapter helpAdapter;

    public HelpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_help, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        helpAdapter = new HelpAdapter(getActivity());
        lvHelp.setAdapter(helpAdapter);
        lvHelp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                for (int i = 0; i < helpAdapter.getHelpDataBeanList().size(); i++) {
                    HelpMessageBean.HelpDataBean bean = (HelpMessageBean.HelpDataBean) helpAdapter.getHelpDataBeanList().get(position);
                    content = bean.getContent();
                    title = bean.getTitle();
                }
                EventBus.getDefault().post(new EventLoginMessage("HelpMessage"));
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                helpInterface();
            }
        }).start();
    }

    private void helpInterface() {
        OkHttpUtils.get().url(InterfaceUrl.HELP_URL + sessonWithCode).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
//                toastOnUi("获取信息失败，请检查网络");
            }

            @Override
            public void onResponse(String response, int id) {
                Message msg = new Message();
                msg.obj = response;
                helpHandler.sendMessage(msg);

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private Handler helpHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            if (result.indexOf(ErrorCode.SUCCESS) > 0) {
                HelpMessageBean helpMessageBean = null;
                try {
                    helpMessageBean = GsonParsing.getHelpMessage(result);
                    helpAdapter.addDATA(helpMessageBean);
                    helpAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_timeout_or_illegal_request), Toast.LENGTH_SHORT).show();
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
