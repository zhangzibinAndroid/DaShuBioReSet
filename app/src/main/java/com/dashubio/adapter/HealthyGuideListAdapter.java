package com.dashubio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.dashubio.R;
import com.dashubio.bean.HealthyGuideListBean;

import java.util.ArrayList;
import java.util.List;


/**
 * 作者： 张梓彬
 * 日期： 2017/7/25 0025
 * 时间： 上午 10:47
 * 描述： 健康指导适配器
 */

public class HealthyGuideListAdapter extends BaseExpandableListAdapter {
    public List<HealthyGuideListBean.ProjectListDataBean> projectList = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;

    public HealthyGuideListAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addAllData(List<HealthyGuideListBean.ProjectListDataBean> list){
        projectList.clear();
        projectList.addAll(list);
    }

    @Override
    public int getGroupCount() {
        return projectList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return projectList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return projectList.get(groupPosition);
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
            convertView = inflater.inflate(R.layout.item_history_guide_father,null);
        }
        HealthyGuideListBean.ProjectListDataBean bean= projectList.get(groupPosition);
        TextView text = (TextView) convertView.findViewById(R.id.tv_health_guide_father);
        text.setText(bean.getAddtime());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView = inflater.inflate(R.layout.item_health_guide_children,null);
        }
        HealthyGuideListBean.ProjectListDataBean bean= projectList.get(groupPosition);
        TextView tv_age = (TextView) convertView.findViewById(R.id.tv_age);
        TextView tv_project_name = (TextView) convertView.findViewById(R.id.tv_project_name);
        TextView tv_data = (TextView) convertView.findViewById(R.id.tv_data);
        TextView tv_warning = (TextView) convertView.findViewById(R.id.tv_warning);
        tv_age.setText(bean.getAge()+"");
        tv_project_name.setText(bean.getProject());
        tv_data.setText(bean.getVal()+bean.getUnit());
        tv_warning.setText(bean.getWarning());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
