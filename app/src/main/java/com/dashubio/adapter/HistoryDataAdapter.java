package com.dashubio.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.dashubio.R;
import com.dashubio.bean.HistoryListDataBean;

import java.util.List;


/**
 * 作者： 张梓彬
 * 日期： 2017/7/24 0024
 * 时间： 下午 2:19
 * 描述： 历史数据适配器
 */

public class HistoryDataAdapter extends BaseExpandableListAdapter {
    public List<HistoryListDataBean.HistoryDataBean> childrenList;
    private Context context;
    private LayoutInflater inflater;
    public HistoryDataAdapter(Context context,List<HistoryListDataBean.HistoryDataBean> childrenList) {
        this.context = context;
        this.childrenList = childrenList;


        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return childrenList.size();
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return childrenList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childrenList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView = inflater.inflate(R.layout.item_history_data_group,null);
        }
        HistoryListDataBean.HistoryDataBean bean= childrenList.get(groupPosition);
        TextView text = (TextView) convertView.findViewById(R.id.tv_history_data_group);
        text.setText(bean.getAddtime());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView = inflater.inflate(R.layout.item_history_data,null);
        }
        HistoryListDataBean.HistoryDataBean bean= childrenList.get(groupPosition);
        TextView tv_history_data_name = (TextView) convertView.findViewById(R.id.tv_history_data_name);
        String valueStr = bean.getVal()+bean.getUnit();
        if (!TextUtils.isEmpty(valueStr)) {
            valueStr = valueStr.replaceAll(";", "\n");
        }
        tv_history_data_name.setText(valueStr);
        TextView tv_history_data_warning = (TextView) convertView.findViewById(R.id.tv_history_data_warning);
        tv_history_data_warning.setText(bean.getWarning());


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
