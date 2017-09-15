package com.dashubio.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dashubio.R;
import com.dashubio.bean.WarningBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;


/**
 * 作者： 张梓彬
 * 日期： 2017/7/14 0014
 * 时间： 下午 3:25
 * 描述： 预警列表适配器
 */

public class WarningAdapter extends RecyclerView.Adapter<WarningAdapter.ViewHolder> {
    private static final String TAG = "WarningAdapter";
    private List<WarningBean.WarningDataBean> dataList;

    public WarningAdapter(List<WarningBean.WarningDataBean> dataList) {
        this.dataList = dataList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_early_warning, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        AutoUtils.autoSize(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WarningBean.WarningDataBean bean = dataList.get(position);
        holder.tvFirstNbItem.setText(bean.getP_name());
        holder.tvSecondNbItem.setText(bean.getMin_v() + "-" + bean.getMax_v()+" "+bean.getUnit());
        holder.tvThirdNbItem.setText(bean.getContent());
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        if (dataList!=null){
            return dataList.size();
        }else {
            return 0;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFirstNbItem;
        TextView tvSecondNbItem;
        TextView tvThirdNbItem;

        public ViewHolder(View view) {
            super(view);
            tvFirstNbItem = (TextView) view.findViewById(R.id.tv_first_nb_item);
            tvSecondNbItem = (TextView) view.findViewById(R.id.tv_second_nb_item);
            tvThirdNbItem = (TextView) view.findViewById(R.id.tv_third_nb_item);
        }
    }


}
