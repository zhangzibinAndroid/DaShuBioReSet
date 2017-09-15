package com.dashubio.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dashubio.R;
import com.dashubio.bean.MainPageBean;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/18 0018
 * 时间： 下午 3:48
 * 描述： 第一个页面的首页适配器
 */

public class MainPageAdapter extends RecyclerView.Adapter<MainPageAdapter.ViewHolder>{
    private List<MainPageBean.MessageDataBean> dataList;
    private OnLookingClickListener onLookingClickListener= null;
    public MainPageAdapter(List<MainPageBean.MessageDataBean> dataList) {
        this.dataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_first_message, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        AutoUtils.autoSize(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        MainPageBean.MessageDataBean bean = dataList.get(position);
        holder.tvItemIndexName.setText(bean.getName());
        holder.tvItemIndexAge.setText(bean.getAge());
        holder.tvItemIndexSubject.setText(bean.getProject());
        String dateStr = "";
        String addTime = bean.getAddtime();
        holder.viewTv.setTag(position);
        holder.resultStatusContainer.setTag(holder.resultStatusContainer);
        if (!TextUtils.isEmpty(addTime)) {
            dateStr = addTime.substring(0, 10);
        }
        holder.tvItemIndexTime.setText(dateStr);


        String statusStr = "";
        String valueStr = bean.getVal();
        if (!TextUtils.isEmpty(valueStr)) {
            statusStr = valueStr.replaceAll(";", "\n");
        }
        statusStr += " " + bean.getUnit();
        holder.resultStatusTv.setText(statusStr);
        holder.warningTv.setText(bean.getWarning());
        holder.viewTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLookingClickListener!=null){
                    onLookingClickListener.lookingClick(v, (int) v.getTag(), (AutoLinearLayout) holder.resultStatusContainer.getTag());
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }




    public static interface OnLookingClickListener{
        void lookingClick(View v, int position, AutoLinearLayout resultLayout);
    }

    public void setOnLookingClickListener(OnLookingClickListener listener){
        this.onLookingClickListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_item_index_name)
        TextView tvItemIndexName;
        @BindView(R.id.tv_item_index_age)
        TextView tvItemIndexAge;
        @BindView(R.id.tv_item_index_subject)
        TextView tvItemIndexSubject;
        @BindView(R.id.tv_item_index_time)
        TextView tvItemIndexTime;
        @BindView(R.id.view_tv)
        TextView viewTv;
        @BindView(R.id.result_status_tv)
        TextView resultStatusTv;
        @BindView(R.id.warning_tv)
        TextView warningTv;
        @BindView(R.id.result_status_container)
        AutoLinearLayout resultStatusContainer;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
