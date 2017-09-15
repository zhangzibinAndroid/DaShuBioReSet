package com.dashubio.adapter.viewadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dashubio.R;
import com.dashubio.bean.DryDetectItem;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/18 0018
 * 时间： 下午 2:24
 * 描述： 干式生化仪数据Adapter
 */

public class GanShiAdapter extends RecyclerView.Adapter<GanShiAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflat;
    private List<DryDetectItem> itemList = new ArrayList<>();

    public GanShiAdapter(Context context) {
        this.context = context;
    }

    public void addData(List<DryDetectItem> list){
        this.itemList.clear();
        this.itemList.addAll(list);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ganshi_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        AutoUtils.autoSize(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DryDetectItem item = itemList.get(position);
        String showStr = item.getName() + "\n";
        String showValue = "测量值" + " " + item.getValue() + " " + item.getUnit();
        if (DryDetectItem.REAL_VALUE != item.getShowMin()) {
            if (item.getValue() < item.getShowMin()) {
                showValue = "测量值" + " <" + " " + item.getShowMin() + " " + item.getUnit();
            } else if (item.getValue() > item.getShowMax()) {
                showValue = "测量值"+ " >" + " " + item.getShowMax() + " " + item.getUnit();
            }
        }
        showStr += showValue;
        showStr += "\n";
        showStr += "（ " + item.getReferenceRange() + " ）";
        holder.tvGanshiData.setText(showStr);
    }



    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_ganshi_data)
        TextView tvGanshiData;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
