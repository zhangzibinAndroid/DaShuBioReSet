package com.dashubio.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dashubio.R;
import com.dashubio.base.MyBaseAdapter;
import com.dashubio.bean.HelpMessageBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/14 0014
 * 时间： 下午 3:20
 * 描述： 帮助item的适配器
 */

public class HelpAdapter extends MyBaseAdapter<HelpMessageBean> {
    private List<HelpMessageBean.HelpDataBean> beanList;

    public HelpAdapter(Context context) {
        super(context);
    }

    public List getHelpDataBeanList(){
        return beanList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_help, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
            AutoUtils.autoSize(convertView);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        HelpMessageBean helpMessageBean = list.get(position);
        beanList = helpMessageBean.getData();
        String title = "";
        if (beanList!=null){
            for (int i = 0; i < beanList.size(); i++) {
                HelpMessageBean.HelpDataBean helpDataBean = beanList.get(i);
                title = helpDataBean.getTitle();
            }
        }
        viewHolder.tvItemHelpContent.setText(title);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_item_help_content)
        TextView tvItemHelpContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
